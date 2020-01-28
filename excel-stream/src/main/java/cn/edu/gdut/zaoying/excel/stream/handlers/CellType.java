package cn.edu.gdut.zaoying.excel.stream.handlers;

public enum CellType {

    BOOLEAN("b"),
    ERROR("e"),
    INLINE_STRING("inlineStr"),
    SST_STRING("s"),
    FORMULA("str"),
    NUMBER("");

    private String type;

    CellType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
