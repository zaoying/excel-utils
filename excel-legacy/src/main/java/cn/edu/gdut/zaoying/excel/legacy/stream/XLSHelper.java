package cn.edu.gdut.zaoying.excel.legacy.stream;

import java.io.InputStream;

public class XLSHelper {

    public static XLSContext readFile(String filePath){
        return XLSContext.readFromFile(filePath);
    }

    public static XLSContext readStream(InputStream inputStream){
        return XLSContext.readFromStream(inputStream);
    }
}
