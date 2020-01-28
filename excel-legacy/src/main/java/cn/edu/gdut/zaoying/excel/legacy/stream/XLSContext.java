package cn.edu.gdut.zaoying.excel.legacy.stream;

import cn.edu.gdut.zaoying.excel.common.handlers.ErrorHandler;
import cn.edu.gdut.zaoying.excel.legacy.Listenable;
import cn.edu.gdut.zaoying.excel.common.Readable;
import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.Handler;
import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.RecordHandler;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XLSContext implements Readable, Listenable {

    private String filePath;
    private InputStream inputStream;
    private FormatTrackingHSSFListener formatListener;
    private ErrorHandler errorHandler;
    private Handler<Record> recordHandler;

    public XLSContext(String filePath) {
        this.filePath = filePath;
    }

    public XLSContext(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static XLSContext readFromFile(String filePath) {
        return new XLSContext(filePath);
    }

    public static XLSContext readFromStream(InputStream inputStream) {
        return new XLSContext(inputStream);
    }

    @Override
    public XLSContext handleRecord(Handler<Record> recordHandler) {
        this.recordHandler = recordHandler;
        return this;
    }

    @Override
    public Handler<? extends Record> recordHandler() {
        return recordHandler;
    }

    @Override
    public XLSContext handleFormat(FormatTrackingHSSFListener formatListener) {
        this.formatListener = formatListener;
        return this;
    }

    @Override
    public FormatTrackingHSSFListener formatListener() {
        return formatListener;
    }

    @Override
    public XLSContext handleError(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ErrorHandler errorHandler() {
        return errorHandler;
    }

    @Override
    public void execute(){
        try {
            HSSFEventFactory factory = new HSSFEventFactory();
            HSSFRequest request = new HSSFRequest();

            checkStreamAndPath();

            request.addListenerForAllRecords(generateFormatListener());

            try (POIFSFileSystem fs = new POIFSFileSystem(inputStream)) {
                factory.processWorkbookEvents(request, fs);
            }

        }catch (Throwable throwable){
            handleError(throwable);
        }
    }

    @Override
    public void processRecord(Record record) {
        if(recordHandler == null) {
            handleRecord(new RecordHandler());
        }
        recordHandler.handle(record);
    }

    protected void handleError(Throwable throwable) {
        if(errorHandler == null){
            throwable.printStackTrace();
        }
        else {
            errorHandler.handleError(throwable);
        }
    }

    protected HSSFListener generateFormatListener(){
        if (formatListener == null) {

            /* For parsing Formulas */
            EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;

            MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);

            formatListener = new FormatTrackingHSSFListener(listener);

            workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(formatListener);

            return workbookBuildingListener;
        } else {
            return formatListener;
        }
    }

    protected void checkStreamAndPath() throws IllegalArgumentException, FileNotFoundException {
        if(inputStream == null && filePath == null){
            throw new IllegalArgumentException("args \"inputStream\" or \"filePath\" can not be both null !");
        }
        if(inputStream == null){
            inputStream = new FileInputStream(filePath);
        }
    }
}
