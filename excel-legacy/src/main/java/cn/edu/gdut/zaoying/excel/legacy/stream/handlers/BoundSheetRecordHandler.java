package cn.edu.gdut.zaoying.excel.legacy.stream.handlers;

import org.apache.poi.hssf.record.BoundSheetRecord;

import java.util.List;

public class BoundSheetRecordHandler implements Handler<BoundSheetRecord> {

    private List<BoundSheetRecord> boundSheetRecords;

    public BoundSheetRecordHandler(List<BoundSheetRecord> boundSheetRecords) {
        this.boundSheetRecords = boundSheetRecords;
    }

    @Override
    public void handle(BoundSheetRecord boundSheetRecord) {
        boundSheetRecords.add(boundSheetRecord);
    }
}
