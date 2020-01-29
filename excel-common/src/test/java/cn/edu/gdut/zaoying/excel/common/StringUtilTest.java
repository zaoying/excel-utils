package cn.edu.gdut.zaoying.excel.common;

import cn.edu.gdut.zaoying.excel.common.utils.StringUtil;
import org.junit.Test;

public class StringUtilTest {
    @Test
    public void firstLetter2LowerCase(){
        String simpleName = StringUtil.firstLetter2LowerCase(this.getClass().getSimpleName());
        assert "stringUtilTest".equals(simpleName);
    }
}
