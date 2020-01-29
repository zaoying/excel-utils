package cn.edu.gdut.zaoying.excel.common;

import cn.edu.gdut.zaoying.excel.common.fop.Function;
import cn.edu.gdut.zaoying.excel.common.fop.Promise;
import cn.edu.gdut.zaoying.excel.common.utils.ThreadUtil;
import org.junit.Test;

public class PromiseTest {

    public static void main(String[] args) {
        new PromiseTest().callTest();
    }

    @Test
    public void justTest(){
        Promise.just(12345678)
        .then(input -> ThreadUtil.await(args -> args * 10, input))
        .then(input -> input * 8)
        .then(input -> input + 9)
        .then(Function.avoid(System.out::println));
    }

    @Test
    public void callTest(){
        Promise.call(resolver -> ThreadUtil.await(Function.avoid(input -> resolver.resolve(12345678)),null))
        .then(Function.avoid(System.out::println));
    }
}
