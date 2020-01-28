package cn.edu.gdut.zaoying.excel.stream;

import cn.edu.gdut.zaoying.excel.stream.handlers.Handlable;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;

public interface Streamable {
    /**
     *
     * @param sheetParser XMLParse
     * @param sheetInputStream InputStream
     * @return should break
     * @throws IOException IO
     * @throws SAXException SAX
     */
    boolean handleWorksheet(XMLReader sheetParser, InputStream sheetInputStream) throws IOException, SAXException;

    Streamable workbookHandler(Handlable<OPCPackage, ContentHandler, Throwable> workbookHandler);

    ContentHandler workbookHandler();
}
