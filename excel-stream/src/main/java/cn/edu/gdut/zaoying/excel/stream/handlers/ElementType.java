package cn.edu.gdut.zaoying.excel.stream.handlers;

public enum ElementType {
    WORKSHEET("worksheet"),
    DIMENSION("dimension"),
    SHEET_VIEWS("sheetViews"),
    SHEET_VIEW("sheetView"),
    PANE("pane"),
    SELECTION("selection"),
    SHEET_FORMAT_PR("sheetFormatPr"),
    COLS("cols"),
    COL("col"),
    SHEET_DATA("sheetData"),

    ROW("row"),
    Cell("c"),
    VALUE("v"),
    TEXT("t"),
    INLINE_STRING_OUTER("is"),
    INLINE_STRING("inlineStr"),
    SST_STRING("s"),
    FORMULA("f"),
    OTHER("");

    private String localName;

    ElementType(String localName) {
        this.localName = localName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }
}
