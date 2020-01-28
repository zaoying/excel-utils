package cn.edu.gdut.zaoying.excel.stream;

import cn.edu.gdut.zaoying.excel.stream.elements.Element;
import cn.edu.gdut.zaoying.excel.stream.elements.FormulaElement;
import cn.edu.gdut.zaoying.excel.stream.elements.InlineStringElement;
import cn.edu.gdut.zaoying.excel.stream.elements.NumberElement;
import cn.edu.gdut.zaoying.excel.stream.handlers.CellType;
import cn.edu.gdut.zaoying.excel.stream.handlers.Handler;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.model.SharedStrings;

public class Handlers {

    public static Handler<Element, Boolean> booleanHandler(){
        return element -> "0".equals(element.getContent().toString());
    }

    public static Handler<Element, String> errorHandler(){
        return element -> "ERROR:" + element.getContent().toString();
    }

    public static Handler<FormulaElement, String> formulaHandler(DataFormatter dataFormatter){
        return formulaElement -> {

            StringBuilder value = formulaElement.getValue();

            short formatIndex = formulaElement.getFormatIndex();
            String formatString = formulaElement.getFormatString();

            String number = value.toString();
            String thisStr;
            if (formatString != null) {
                try {
                    // Try to use the value as a formattable number
                    double d = Double.parseDouble(number);
                    thisStr = dataFormatter.formatRawCellContents(d, formatIndex, formatString);
                } catch(NumberFormatException e) {
                    // Formula is a String result not a Numeric one
                    thisStr = number;
                }
            } else {
                // No formatting applied, just do raw value in all cases
                thisStr = number;
            }
            return thisStr;
        };
    }

    public static Handler<InlineStringElement, String> inlineStringHandler(){
        return inlineStringElement -> {
            Element inlineString = inlineStringElement.getInlineString();
            return inlineString.getContent().toString();
        };
    }

    public static Handler<Element, String> sharedStringHandler(SharedStrings sharedStringsTable){
        return sharedStringElement -> {
                String sstIndex = sharedStringElement.getContent().toString();

                int idx = Integer.parseInt(sstIndex);
                RichTextString richTextString = sharedStringsTable.getItemAt(idx);
                return richTextString.toString();
        };
    }

    public static Handler<NumberElement, String> numberHandler(DataFormatter dataFormatter){
        return numberElement -> {

            StringBuilder value = numberElement.getValue();

            short formatIndex = numberElement.getFormatIndex();
            String formatString = numberElement.getFormatString();

            String number = value.toString();

            if (formatString != null && !number.isEmpty())
                return dataFormatter.formatRawCellContents(Double.parseDouble(number), formatIndex, formatString);
            else
                return value.toString();
        };
    }

    public static Handler<CellType,String> defaultHandler(){
        return nextDataType -> "(TODO: Unexpected type: " + nextDataType + ")";
    }
}
