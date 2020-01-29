package cn.edu.gdut.zaoying.excel.stream;

import cn.edu.gdut.zaoying.excel.stream.elements.Element;
import cn.edu.gdut.zaoying.excel.stream.handlers.ElementType;
import org.junit.Test;

public class ElementTest {

    @Test
    public void testTranslate(){
        for (ElementType elementType : ElementType.values()) {
            Element element = Element.forLocalName(elementType.getLocalName());
            assert elementType == element.getType();
        }
    }
}
