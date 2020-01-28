package cn.edu.gdut.zaoying.excel.stream;

import cn.edu.gdut.zaoying.excel.stream.elements.Element;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class StyleUtil {

    public static XSSFCellStyle getStyle(Styles stylesTable, Element element){


        String cellStyleStr = element.getAttributes("s");

        return getStyle(stylesTable, cellStyleStr);
    }

    public static XSSFCellStyle getStyle(Styles stylesTable, String cellStyleStr){

        XSSFCellStyle style = null;

        if (stylesTable != null) {
            if (cellStyleStr != null) {
                int styleIndex = Integer.parseInt(cellStyleStr);
                style = stylesTable.getStyleAt(styleIndex);
            } else if (stylesTable.getNumCellStyles() > 0) {
                style = stylesTable.getStyleAt(0);
            }
        }
        return style;
    }
}
