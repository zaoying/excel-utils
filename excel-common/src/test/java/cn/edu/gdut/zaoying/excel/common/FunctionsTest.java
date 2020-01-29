package cn.edu.gdut.zaoying.excel.common;

import cn.edu.gdut.zaoying.excel.common.fop.Function;
import cn.edu.gdut.zaoying.excel.common.fop.Functions;
import org.junit.Test;

public class FunctionsTest {
    @Test
    public void test() {

        Function<Integer,Integer> timesTen = num -> num * 10;

        Function<Integer,Integer> plusNine = num -> num + 9;

        Function<Integer, Void> last = Functions.first(timesTen)
                .next(plusNine)
                .next(String::valueOf)
                .last(Function.avoid(System.out::println));

        last.call(8);
    }
}
