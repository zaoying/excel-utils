package cn.edu.gdut.zaoying.excel.stream.handlers;

import cn.edu.gdut.zaoying.excel.stream.Handlers;
import cn.edu.gdut.zaoying.excel.stream.StyleUtil;
import cn.edu.gdut.zaoying.excel.stream.elements.Element;
import cn.edu.gdut.zaoying.excel.stream.elements.FormulaElement;
import cn.edu.gdut.zaoying.excel.stream.elements.InlineStringElement;
import cn.edu.gdut.zaoying.excel.stream.elements.NumberElement;
import cn.edu.gdut.zaoying.excel.common.utils.Dictionary;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.util.Stack;

public class CellHandler implements Handler<Stack<Element>, String> {
    private static final POILogger logger = POILogFactory.getLogger(CellHandler.class);

    public static final Dictionary<String, CellType> cellTypeDict = Dictionary.stringDict(CellType.class)
            .keyGenerator(CellType::getType)
            .whenNotFound(key -> CellType.NUMBER);

    private Handler<Element, Boolean> booleanHandler;
    private Handler<Element, String> errorHandler;
    private Handler<FormulaElement, String> formulaHandler;
    private Handler<InlineStringElement, String> inlineStringHandler;
    private Handler<Element, String> sharedStringHandler;
    private Handler<NumberElement, String> numberHandler;
    private Handler<CellType,String> defaultHandler;

    private boolean hasInitialized = false;

    private Styles stylesTable;
    private SharedStrings sharedStringsTable;
    private DataFormatter dataFormatter;

    private InlineStringElement inlineStringElement;
    private FormulaElement formulaElement;
    private NumberElement numberElement;

    public CellHandler(Styles stylesTable, SharedStrings sharedStringsTable, DataFormatter dataFormatter) {
        this.stylesTable = stylesTable;
        this.sharedStringsTable = sharedStringsTable;
        this.dataFormatter = dataFormatter;

        inlineStringElement = new InlineStringElement();

        formulaElement = new FormulaElement();

        numberElement = new NumberElement();

    }

    public void initHandlers(){

        if(booleanHandler == null){
            booleanHandler = Handlers.booleanHandler();
        }

        if(errorHandler == null){
            errorHandler = Handlers.errorHandler();
        }

        if(formulaHandler == null){
            formulaHandler = Handlers.formulaHandler(dataFormatter);
        }

        if(inlineStringHandler == null){
            inlineStringHandler = Handlers.inlineStringHandler();
        }

        if(sharedStringHandler == null){
            sharedStringHandler = Handlers.sharedStringHandler(sharedStringsTable);
        }

        if(numberHandler == null){
            numberHandler = Handlers.numberHandler(dataFormatter);
        }

        if(defaultHandler == null){
            defaultHandler = Handlers.defaultHandler();
        }

        hasInitialized = true;

    }

    @Override
    public String handle(Stack<Element> elementStack) {

        if(!hasInitialized){
            initHandlers();
        }

        Element cell = elementStack.elementAt(0);
        String cellTypeStr = cell.getAttributes("t");
        CellType cellType = cellTypeDict.translate(cellTypeStr);

        String thisStr;

        // Process the value contents as required, now we have it all
        switch (cellType) {
            case BOOLEAN:
                thisStr = booleanHandler.handle(elementStack.pop()).toString();
                break;

            case ERROR:
                thisStr = errorHandler.handle(elementStack.pop());
                break;

            case FORMULA:
                Element value = elementStack.pop();
                Element formula = elementStack.pop();
                if(formulaHandler == null) {
                    thisStr = formula.getContent().toString();
                } else {
                    formulaElement.setFormula(formula.getContent());
                    formulaElement.setValue(value.getContent());
                    thisStr = formulaHandler.handle(formulaElement);
                }
                formulaElement.reset();
                break;

            case INLINE_STRING:
                Element text = elementStack.pop();
                Element inlineString = elementStack.pop();
                inlineStringElement.setInlineString(inlineString);
                inlineStringElement.setText(text);

                thisStr = inlineStringHandler.handle(inlineStringElement);
                inlineStringElement.reset();
                break;

            case SST_STRING:
                Element element = elementStack.pop();
                try {
                    thisStr = sharedStringHandler.handle(element);
                }
                catch (NumberFormatException ex) {
                    thisStr = "";
                    logger.log(POILogger.ERROR, "Failed to parse SST index '" + element.getContent(), ex);
                }
                break;

            case NUMBER:
                value = elementStack.pop();
                numberElement.setValue(value.getContent());

                XSSFCellStyle xssfCellStyle = StyleUtil.getStyle(stylesTable, cell);
                numberElement.format(xssfCellStyle);

                thisStr = numberHandler.handle(numberElement);
                numberElement.reset();
                break;

            default:
                thisStr = defaultHandler.handle(cellType);
                break;
        }
        return thisStr;
    }

    public Handler<Element, Boolean> getBooleanHandler() {
        return booleanHandler;
    }

    public void setBooleanHandler(Handler<Element, Boolean> booleanHandler) {
        this.booleanHandler = booleanHandler;
    }

    public Handler<Element, String> getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(Handler<Element, String> errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Handler<FormulaElement, String> getFormulaHandler() {
        return formulaHandler;
    }

    public void setFormulaHandler(Handler<FormulaElement, String> formulaHandler) {
        this.formulaHandler = formulaHandler;
    }

    public Handler<InlineStringElement, String> getInlineStringHandler() {
        return inlineStringHandler;
    }

    public CellHandler setInlineStringHandler(Handler<InlineStringElement, String> inlineStringHandler) {
        this.inlineStringHandler = inlineStringHandler;
        return this;
    }

    public Handler<Element, String> getSharedStringHandler() {
        return sharedStringHandler;
    }

    public void setSharedStringHandler(Handler<Element, String> sharedStringHandler) {
        this.sharedStringHandler = sharedStringHandler;
    }

    public Handler<NumberElement, String> getNumberHandler() {
        return numberHandler;
    }

    public void setNumberHandler(Handler<NumberElement, String> numberHandler) {
        this.numberHandler = numberHandler;
    }

    public Handler<CellType, String> getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(Handler<CellType, String> defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public boolean isHasInitialized() {
        return hasInitialized;
    }

    public void setHasInitialized(boolean hasInitialized) {
        this.hasInitialized = hasInitialized;
    }

    public SharedStrings getSharedStringsTable() {
        return sharedStringsTable;
    }

    public void setSharedStringsTable(SharedStrings sharedStringsTable) {
        this.sharedStringsTable = sharedStringsTable;
    }

    public DataFormatter getDataFormatter() {
        return dataFormatter;
    }

    public void setDataFormatter(DataFormatter dataFormatter) {
        this.dataFormatter = dataFormatter;
    }
}
