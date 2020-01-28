package cn.edu.gdut.zaoying.excel.legacy.stream.handlers;

import org.apache.poi.hssf.record.Record;

public interface Handler<R extends Record> {
    void handle(R record);
}
