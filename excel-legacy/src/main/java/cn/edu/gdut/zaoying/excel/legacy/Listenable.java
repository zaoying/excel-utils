package cn.edu.gdut.zaoying.excel.legacy;

import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.Handler;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.record.Record;

public interface Listenable extends HSSFListener {

    Listenable handleRecord(Handler<Record> recordHandler);

    Handler<? extends Record> recordHandler();

    Listenable handleFormat(FormatTrackingHSSFListener formatListener);

    FormatTrackingHSSFListener formatListener();
}
