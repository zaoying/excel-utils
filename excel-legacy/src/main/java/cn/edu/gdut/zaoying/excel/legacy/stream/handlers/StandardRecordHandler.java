package cn.edu.gdut.zaoying.excel.legacy.stream.handlers;

import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.StandardRecord;

public class StandardRecordHandler implements Handler<StandardRecord> {
    private Handler<BlankRecord> blankRecordHandler;
    private Handler<NoteRecord> noteRecordHandler;

    public StandardRecordHandler() {
        blankRecordHandler = record -> {};
        noteRecordHandler = record -> {};
    }

    @Override
    public void handle(StandardRecord standardRecord) {

        switch (standardRecord.getSid()){
            case BlankRecord.sid:
                blankRecordHandler.handle((BlankRecord) standardRecord);
                break;
            case NoteRecord.sid:
                noteRecordHandler.handle((NoteRecord) standardRecord);
                break;
        }
    }

    public StandardRecordHandler setBlankRecordHandler(Handler<BlankRecord> blankRecordHandler) {
        this.blankRecordHandler = blankRecordHandler;
        return this;
    }

    public StandardRecordHandler setNoteRecordHandler(Handler<NoteRecord> noteRecordHandler) {
        this.noteRecordHandler = noteRecordHandler;
        return this;
    }
}
