package cn.edu.gdut.zaoying.excel.legacy;

import cn.edu.gdut.zaoying.excel.legacy.stream.XLSHelper;
import org.junit.Test;

import java.io.InputStream;

public class XLStreamTest {
    @Test
    public void testStream(){
        String fileName = "/20131025.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        XLSHelper.readStream(inputStream)
                .handleRecord(new StdOutputHandler())
                .execute();
    }
}
