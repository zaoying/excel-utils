package cn.edu.gdut.zaoying.excel.legacy;

import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.CellRecordHandler;
import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.ContinuableRecordHandler;
import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.RecordHandler;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.record.CellRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.StringRecord;

/**
 * 标准输出处理器
 */
public class StdOutputHandler extends RecordHandler{

    private String cellSeparator = "\t";

    public StdOutputHandler() {

        //处理常规单元格
        setCellRecordHandler(
                new CellRecordHandler()
                .setAfterHandler(this::separateCell)
                .setNumberRecordHandler(this::handleNumber)
                .setLabelSSTRecordHandler(this::handleSSTRecord)
        );

        //处理字符串单元格
        setContinuableRecordHandler(
                new ContinuableRecordHandler()
                .setStringRecordHandler(this::handleString)
        );

        //处理每行最后一个虚拟单元格
        setLastCellOfRowDummyRecordHandler(this::nextRow);
    }

    public StdOutputHandler setCellSeparator(String cellSeparator) {
        this.cellSeparator = cellSeparator;
        return this;
    }

    void separateCell(CellRecord cellRecord){
        System.out.print(cellSeparator);
    }

    void handleString(StringRecord stringRecord){
        System.out.print(stringRecord.getString());
    }

    void handleSSTRecord(LabelSSTRecord labelSSTRecord) {
        String string = translate(labelSSTRecord);
        System.out.print(string);
    }

    void handleNumber(NumberRecord numberRecord) {
        String number = formatNumberDateCell(numberRecord);
        System.out.print(number);
    }

    void nextRow(LastCellOfRowDummyRecord lastCellOfRowDummyRecord){
        System.out.println();
    }
}
