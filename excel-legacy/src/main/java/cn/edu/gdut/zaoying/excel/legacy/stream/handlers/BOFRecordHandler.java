package cn.edu.gdut.zaoying.excel.legacy.stream.handlers;

import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;

import java.util.List;


public class BOFRecordHandler implements Handler<BOFRecord> {

    /** So we known which sheet we're on */
    private int sheetIndex = -1;
    private BoundSheetRecord[] orderedBSRs;
    private List<BoundSheetRecord> boundSheetRecords;

    public BOFRecordHandler(List<BoundSheetRecord> boundSheetRecords) {
        this.boundSheetRecords = boundSheetRecords;
    }

    @Override
    public void handle(BOFRecord bofRecord) {
        if (bofRecord.getType() == BOFRecord.TYPE_WORKSHEET) {

            // Output the worksheet name
            // Works by ordering the BSRs by the location of
            //  their BOFRecords, and then knowing that we
            //  process BOFRecords in byte offset order
            sheetIndex++;
            if (orderedBSRs == null) {
                orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
            }
        }
    }
}
