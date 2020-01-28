package cn.edu.gdut.zaoying.excel.stream.elements;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class NumberElement {
    private short formatIndex = -1;

    private String formatString = null;

    private StringBuilder value = null;

    public NumberElement() {
    }

    public void format(XSSFCellStyle xssfCellStyle) {

        if (xssfCellStyle != null) {
            formatIndex = xssfCellStyle.getDataFormat();
            formatString = xssfCellStyle.getDataFormatString();
            if (formatString == null)
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
        }
    }

    public void reset(){
        formatIndex = -1;
        formatString = null;
        value = null;
    }

    public short getFormatIndex() {
        return formatIndex;
    }

    public String getFormatString() {
        return formatString;
    }

    public StringBuilder getValue() {
        return value;
    }

    public NumberElement setValue(StringBuilder value) {
        this.value = value;
        return this;
    }
}
