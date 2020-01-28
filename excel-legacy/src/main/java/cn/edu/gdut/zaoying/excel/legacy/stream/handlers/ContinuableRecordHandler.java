package cn.edu.gdut.zaoying.excel.legacy.stream.handlers;

import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.record.cont.ContinuableRecord;

import java.util.concurrent.atomic.AtomicReference;

public class ContinuableRecordHandler implements Handler<ContinuableRecord> {

    private AtomicReference<SSTRecord> sstRecord = new AtomicReference<>();

    private Handler<StringRecord> stringRecordHandler;

    private Handler<SSTRecord> sstRecordHandler;

    public ContinuableRecordHandler() {
        stringRecordHandler = record -> {};
        sstRecordHandler = sstRecord::set;
    }

    @Override
    public void handle(ContinuableRecord continuableRecord) {
        switch (continuableRecord.getSid()){
            case StringRecord.sid:
                stringRecordHandler.handle((StringRecord) continuableRecord);
                break;
            case SSTRecord.sid:
                sstRecordHandler.handle((SSTRecord) continuableRecord);
                break;
        }
    }

    public SSTRecord getSstRecord() {
        return sstRecord.get();
    }

    public void setSstRecord(SSTRecord sstRecord) {
        this.sstRecord.set(sstRecord);
    }

    public ContinuableRecordHandler setStringRecordHandler(Handler<StringRecord> stringRecordHandler) {
        this.stringRecordHandler = stringRecordHandler;
        return this;
    }

    public ContinuableRecordHandler setSstRecordHandler(Handler<SSTRecord> sstRecordHandler) {
        this.sstRecordHandler = sstRecordHandler;
        return this;
    }
}
