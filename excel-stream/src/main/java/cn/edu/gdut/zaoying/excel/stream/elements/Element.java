package cn.edu.gdut.zaoying.excel.stream.elements;

import cn.edu.gdut.zaoying.excel.stream.handlers.ElementType;
import cn.edu.gdut.zaoying.excel.common.utils.Dictionary;
import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

public class Element {

    private ElementType type;
    private Map<String,String> attributes;
    private StringBuilder content;

    public Element(ElementType type) {
        this.type = type;
        attributes = new HashMap<>();
        content = new StringBuilder();
    }

    public final static Dictionary<String, ElementType> elementTypeDictionary;

    static {
       elementTypeDictionary = Dictionary.stringDict(ElementType.class)
                .keyGenerator(ElementType::getLocalName)
                .whenNotFound(key -> ElementType.OTHER);
    }

    public static Element forLocalName(String localName){
        ElementType elementType = elementTypeDictionary.translate(localName);
        return Element.forType(elementType);
    }

    public static Element forType(ElementType type){
        return new Element(type);
    }

    public void resetAttributes(Attributes attributes){

        int length = attributes.getLength();

        for (int index = 0; index < length; index++) {

            this.attributes.put(attributes.getLocalName(index), attributes.getValue(index));

        }
    }

    public void reset(){
        content.delete(0, content.length());
        attributes.clear();
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public String getAttributes(String key) {
        return attributes.get(key);
    }

    public void setAttributes(Attributes attributes) {
        resetAttributes(attributes);
    }

    public StringBuilder getContent() {
        return content;
    }

    public void setContent(StringBuilder content) {
        this.content = content;
    }
}
