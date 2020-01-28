package cn.edu.gdut.zaoying.excel.legacy.stream.handlers;

import org.apache.poi.hssf.record.CellRecord;

public class Handlers {

    public static RecordHandler handleRecord(){
        return new RecordHandler();
    }
    public static RecordHandler handleRecord(Handler<CellRecord> cellRecordHandler){
        return new RecordHandler().setCellRecordHandler(cellRecordHandler);
    }

    public static CellRecordHandler handleCell(){
        return new CellRecordHandler();
    }

}
