package cn.edu.gdut.zaoying.excel.common;

import cn.edu.gdut.zaoying.excel.common.utils.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

public class ThreadUtilTest {
    @Test
    public void testAsync(){
        String output;
        output = ThreadUtil.await(input -> "sync",null);
        System.out.println(output);

        AtomicReference<String> result = new AtomicReference<>();
        ThreadUtil.start(input -> "async", null).call(result::set);
        System.out.println(result.get());
    }
}
