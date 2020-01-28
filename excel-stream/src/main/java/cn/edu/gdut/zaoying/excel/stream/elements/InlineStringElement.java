package cn.edu.gdut.zaoying.excel.stream.elements;

public class InlineStringElement {

    private Element inlineString;
    private Element text;

    public InlineStringElement() {
    }

    public InlineStringElement(Element inlineString, Element text) {
        this.inlineString = inlineString;
        this.text = text;
    }

    public void setInlineString(Element inlineString) {
        this.inlineString = inlineString;
    }

    public void setText(Element text) {
        this.text = text;
    }

    public Element getInlineString() {
        return inlineString;
    }

    public Element getText() {
        return text;
    }

    public void reset(){
        text = null;
        inlineString = null;
    }
}
