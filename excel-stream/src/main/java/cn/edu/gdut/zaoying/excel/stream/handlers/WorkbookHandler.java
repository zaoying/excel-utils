package cn.edu.gdut.zaoying.excel.stream.handlers;

import cn.edu.gdut.zaoying.excel.stream.elements.Element;
import cn.edu.gdut.zaoying.excel.stream.tuples.TupleThree;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.Comments;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import static org.apache.poi.xssf.usermodel.XSSFRelation.NS_SPREADSHEETML;

public class WorkbookHandler extends DefaultHandler {

    /**
     * Table with the styles used for formatting
     */
    private Styles stylesTable;

    /**
     * Table with cell comments
     */
    private Comments comments;

    /**
     * Read only access to the shared strings table, for looking
     *  up (most) string cell's contents
     */
    private SharedStrings sharedStringsTable;

    private DataFormatter dataFormatter;

    private Queue<CellAddress> commentCellRefs;

    private Stack<Element> primaryStack;

    private Stack<Element> secondaryStack;

    private Map<String,Element> sharedElement;

    private ElementHandler elementHandler;

    private Handler<Stack<Element>, String> cellHandler;

    private Handler<TupleThree<Styles,SharedStrings,DataFormatter>, Handler<Stack<Element>, String>> cellHandlerBuilder;

    public static WorkbookHandler build(OPCPackage xlsxPackage) {

        try {
            ReadOnlySharedStringsTable sharedStringsTable = new ReadOnlySharedStringsTable(xlsxPackage);
            XSSFReader xssfReader = new XSSFReader(xlsxPackage);
            StylesTable stylesTable = xssfReader.getStylesTable();

            return new WorkbookHandler(stylesTable, sharedStringsTable);
        } catch (IOException | OpenXML4JException | SAXException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public WorkbookHandler(StylesTable stylesTable, ReadOnlySharedStringsTable sharedStringsTable){

        this.dataFormatter = new DataFormatter();

        this.stylesTable = stylesTable;

        this.sharedStringsTable = sharedStringsTable;

        primaryStack = new Stack<>();
        secondaryStack = new Stack<>();
        sharedElement = new HashMap<>();
    }

    private ElementHandler handleElement(){
        if (elementHandler == null){
            elementHandler = ElementHandler.emptyHandler();
        }
        return elementHandler;
    }

    private Handler<Stack<Element>, String> handleCell(){
        if(cellHandler == null){
            if (cellHandlerBuilder == null){
                cellHandler = ElementHandler.cellElementHandler(stylesTable, sharedStringsTable, dataFormatter);
            }
            else cellHandler = cellHandlerBuilder.handle(new TupleThree<>(stylesTable, sharedStringsTable, dataFormatter));
        }
        return cellHandler;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (uri != null && ! uri.equals(NS_SPREADSHEETML)) {
            return;
        }

        Element element = retrieveElement(localName);

        element.setAttributes(attributes);

        //put CellElement itself and it's child element into the secondaryStack
        if(element.getType() == ElementType.Cell || !secondaryStack.empty()) {
            secondaryStack.push(element);
        }

        primaryStack.push(element);
        handleElement().onStart(element);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        Element peek = primaryStack.pop();

        //start CellElement procedure then clear the secondaryStack
        if(peek.getType() == ElementType.Cell) {
            handleCell().handle(secondaryStack);
            while (!secondaryStack.empty()){
                Element element = secondaryStack.pop();
                //reset the element
                element.reset();
                //recycle the disposed element
                sharedElement.put(element.getType().getLocalName(), element);
            }
        }

        handleElement().onEnd(peek);

        if(secondaryStack.empty()){
            //reset the element
            peek.reset();
            //recycle the disposed element
            sharedElement.put(peek.getType().getLocalName(), peek);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if(!secondaryStack.isEmpty()){
            Element element = secondaryStack.peek();
            element.getContent().append(ch, start, length);
        }
        else throw new RuntimeException("secondaryStack is empty !");
    }

    public WorkbookHandler setElementHandler(ElementHandler elementHandler) {
        this.elementHandler = elementHandler;
        return this;
    }

    public WorkbookHandler setCellHandlerBuilder(Handler<TupleThree<Styles,SharedStrings,DataFormatter>, Handler<Stack<Element>, String>> cellHandlerBuilder) {
        this.cellHandlerBuilder = cellHandlerBuilder;
        return this;
    }

    private Element retrieveElement(String localName){

        Element element;

        boolean existed = sharedElement.containsKey(localName);

        //if the same type element exists, then reuse it.
        if(existed){

            element =sharedElement.get(localName);

            //remove the reused element
            sharedElement.remove(localName);
        }
        else {
            //create new element
            element = Element.forLocalName(localName);
        }

        return element;
    }
}
