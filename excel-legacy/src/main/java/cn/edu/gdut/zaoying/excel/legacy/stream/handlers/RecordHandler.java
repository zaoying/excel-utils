package cn.edu.gdut.zaoying.excel.legacy.stream.handlers;

import cn.edu.gdut.zaoying.excel.legacy.stream.XLSContext;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.record.cont.ContinuableRecord;

import java.util.ArrayList;
import java.util.List;

public class RecordHandler implements Handler<Record> {

    private XLSContext xlsContext;

    private List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();

    private Handler<BOFRecord> bofRecordHandler;

    private Handler<BoundSheetRecord> sheetRecordHandler;

    private Handler<CellRecord> cellRecordHandler;

    private Handler<StandardRecord> standardRecordHandler;

    private ContinuableRecordHandler continuableRecordHandler;

    private Handler<MissingCellDummyRecord> missingCellDummyRecordHandler;

    private Handler<LastCellOfRowDummyRecord> lastCellOfRowDummyRecordHandler;

    public RecordHandler(){

        bofRecordHandler = new BOFRecordHandler(boundSheetRecords);

        sheetRecordHandler = new BoundSheetRecordHandler(boundSheetRecords);

        cellRecordHandler = new CellRecordHandler();

        standardRecordHandler = new StandardRecordHandler();

        continuableRecordHandler = new ContinuableRecordHandler();

        missingCellDummyRecordHandler = cell ->{};

        lastCellOfRowDummyRecordHandler = cell ->{};
    }

    @Override
    public void handle(Record record) {
        switch (record.getSid()) {
            case BOFRecord.sid:
                bofRecordHandler.handle((BOFRecord) record);
                break;
            case BoundSheetRecord.sid:
                sheetRecordHandler.handle((BoundSheetRecord) record);
                break;
            case BoolErrRecord.sid:
            case RKRecord.sid:
            case NumberRecord.sid:
            case LabelSSTRecord.sid:
            case FormulaRecord.sid:
                cellRecordHandler.handle((CellRecord) record);
                break;
            case BlankRecord.sid:
            case NoteRecord.sid:
                standardRecordHandler.handle((StandardRecord) record);
                break;
            case StringRecord.sid:
            case SSTRecord.sid:
                continuableRecordHandler.handle((ContinuableRecord) record);
                break;
            default:
                if(record instanceof MissingCellDummyRecord){
                    missingCellDummyRecordHandler.handle((MissingCellDummyRecord) record);
                }
                else if(record instanceof LastCellOfRowDummyRecord){
                    lastCellOfRowDummyRecordHandler.handle((LastCellOfRowDummyRecord) record);
                }
        }
    }

    public String formatNumberDateCell(NumberRecord numberRecord){
        return xlsContext.formatListener().formatNumberDateCell(numberRecord);
    }

    public String translate(LabelSSTRecord labelSSTRecord){
        SSTRecord sstRecord = getContinuableRecordHandler().getSstRecord();
        if(sstRecord == null){
            throw new RuntimeException("SSTRecord of ContinuableRecordHandler can not be null");
        }
        return sstRecord.getString(labelSSTRecord.getSSTIndex()).toString();
    }

    public XLSContext getXlsContext() {
        return xlsContext;
    }

    public RecordHandler setXlsContext(XLSContext xlsContext) {
        this.xlsContext = xlsContext;
        return this;
    }

    public RecordHandler setBoundSheetRecords(List<BoundSheetRecord> boundSheetRecords) {
        this.boundSheetRecords = boundSheetRecords;
        return this;
    }

    public RecordHandler setBofRecordHandler(Handler<BOFRecord> bofRecordHandler) {
        this.bofRecordHandler = bofRecordHandler;
        return this;
    }

    public RecordHandler setSheetRecordHandler(Handler<BoundSheetRecord> sheetRecordHandler) {
        this.sheetRecordHandler = sheetRecordHandler;
        return this;
    }

    public RecordHandler setCellRecordHandler(Handler<CellRecord> cellRecordHandler) {
        this.cellRecordHandler = cellRecordHandler;
        return this;
    }

    public RecordHandler setStandardRecordHandler(Handler<StandardRecord> standardRecordHandler) {
        this.standardRecordHandler = standardRecordHandler;
        return this;
    }

    public ContinuableRecordHandler getContinuableRecordHandler() {
        return continuableRecordHandler;
    }

    public RecordHandler setContinuableRecordHandler(ContinuableRecordHandler continuableRecordHandler) {
        this.continuableRecordHandler = continuableRecordHandler;
        return this;
    }

    public RecordHandler setMissingCellDummyRecordHandler(Handler<MissingCellDummyRecord> missingCellDummyRecordHandler) {
        this.missingCellDummyRecordHandler = missingCellDummyRecordHandler;
        return this;
    }

    public RecordHandler setLastCellOfRowDummyRecordHandler(Handler<LastCellOfRowDummyRecord> lastCellOfRowDummyRecordHandler) {
        this.lastCellOfRowDummyRecordHandler = lastCellOfRowDummyRecordHandler;
        return this;
    }
}
