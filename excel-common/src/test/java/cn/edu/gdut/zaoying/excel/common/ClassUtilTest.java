package cn.edu.gdut.zaoying.excel.common;

import cn.edu.gdut.zaoying.excel.common.fop.Function;
import cn.edu.gdut.zaoying.excel.common.utils.ClassUtil;
import org.junit.Test;

import java.lang.reflect.Type;

public class ClassUtilTest {
    @Test
    public void getActualType(){
        String test = "test";
        Class<String> clazz = ClassUtil.getActualType(test);
        assert clazz.isInstance(test);
    }

    @Test
    public void getTypeParameters() {
        Function<Object,String> onFulfilled = String::valueOf;

        Type[] types = ClassUtil.getActualTypeArguments(onFulfilled, Function.class);
        for (Type type : types) {
            System.out.println(type.getTypeName());
        }
    }
}
