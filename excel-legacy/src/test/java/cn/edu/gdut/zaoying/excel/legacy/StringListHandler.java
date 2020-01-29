package cn.edu.gdut.zaoying.excel.legacy;

import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.CellRecordHandler;
import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.ContinuableRecordHandler;
import cn.edu.gdut.zaoying.excel.legacy.stream.handlers.RecordHandler;

import java.util.ArrayList;
import java.util.List;

public class StringListHandler extends RecordHandler {

    private List<String> fieldList = new ArrayList<>();

    public StringListHandler() {

        //处理常规单元格
        setCellRecordHandler(
                new CellRecordHandler()
                        .setNumberRecordHandler(cell -> fieldList.add(formatNumberDateCell(cell)))
                        .setLabelSSTRecordHandler(cell -> fieldList.add('"' + translate(cell) + '"'))
        );

        //处理字符串单元格
        setContinuableRecordHandler(
                new ContinuableRecordHandler()
                        .setStringRecordHandler(cell -> fieldList.add(cell.getString()))
        );

        //处理每行最后一个虚拟单元格
        setLastCellOfRowDummyRecordHandler(cell -> fieldList.clear());
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }
}
