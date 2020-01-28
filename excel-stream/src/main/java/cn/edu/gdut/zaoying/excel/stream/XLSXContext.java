package cn.edu.gdut.zaoying.excel.stream;

import cn.edu.gdut.zaoying.excel.common.handlers.ErrorHandler;
import cn.edu.gdut.zaoying.excel.common.Readable;
import cn.edu.gdut.zaoying.excel.stream.Streamable;
import cn.edu.gdut.zaoying.excel.stream.handlers.Handlable;
import cn.edu.gdut.zaoying.excel.stream.handlers.WorkbookHandler;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class XLSXContext implements Readable, Streamable {

    private String filePath;

    private InputStream inputStream;

    private ErrorHandler errorHandler;

    private ContentHandler contentHandler;

    private Handlable<OPCPackage,ContentHandler,Throwable> workbookHandler;

    public XLSXContext(String filePath) {
        this.filePath = filePath;
    }

    public XLSXContext(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static XLSXContext readFromFile(String filePath) {
        return new XLSXContext(filePath);
    }

    public static XLSXContext readFromStream(InputStream inputStream) {
        return new XLSXContext(inputStream);
    }

    @Override
    public boolean handleWorksheet(XMLReader sheetParser, InputStream sheetInputStream) throws IOException, SAXException {
        InputSource sheetSource = new InputSource(sheetInputStream);
        sheetParser.parse(sheetSource);
        return false;
    }

    @Override
    public XLSXContext workbookHandler(Handlable<OPCPackage,ContentHandler,Throwable> workbookHandler) {
        this.workbookHandler = workbookHandler;
        return this;
    }

    @Override
    public ContentHandler workbookHandler() {
        return contentHandler;
    }

    @Override
    public XLSXContext handleError(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ErrorHandler errorHandler() {
        return errorHandler;
    }

    @Override
    public void execute(){
        try{

            checkStreamAndPath();

            handleWorkbook();

        }
        catch (Throwable throwable){
            handleError(throwable);
        }
    }

    public void handleWorkbook() throws Throwable {

        try (OPCPackage xlsxPackage = OPCPackage.open(inputStream)) {

            XSSFReader xssfReader = new XSSFReader(xlsxPackage);

            if(contentHandler == null){
                if(workbookHandler == null){
                    contentHandler = WorkbookHandler.build(xlsxPackage);
                }
                else contentHandler = workbookHandler.handle(xlsxPackage);
            }

            try {

                handleWorkSheet(xssfReader, contentHandler);

            } catch (ParserConfigurationException e) {
                throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
            }

        }
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *

     * @exception IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     * @throws SAXException if parsing the XML data fails.
     */
    public void handleWorkSheet(XSSFReader xssfReader, ContentHandler contentHandler) throws IOException, SAXException, InvalidFormatException, ParserConfigurationException {

        XMLReader sheetParser = SAXHelper.newXMLReader();
        sheetParser.setContentHandler(contentHandler);

        XSSFReader.SheetIterator iterator = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (iterator.hasNext()){

            try (InputStream sheetInputStream = iterator.next()) {

                if(handleWorksheet(sheetParser, sheetInputStream)){
                    break;
                }

            }
        }
    }

    protected void handleError(Throwable throwable) {
        if(errorHandler == null){
            throwable.printStackTrace();
        }
        else {
            errorHandler.handleError(throwable);
        }
    }

    public void checkStreamAndPath() throws IllegalArgumentException, FileNotFoundException {

        if(inputStream == null && filePath == null){
            throw new IllegalArgumentException("args \"inputStream\" or \"filePath\" can not be both null !");
        }
        if(inputStream == null){
            inputStream = new FileInputStream(filePath);
        }

    }

}
