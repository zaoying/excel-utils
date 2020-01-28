package cn.edu.gdut.zaoying.excel.stream;

import java.io.InputStream;

public class XLSXHelper {

    public static XLSXContext readFile(String filePath){
        return new XLSXContext(filePath);
    }

    public static XLSXContext readStream(InputStream inputStream){
        return new XLSXContext(inputStream);
    }
}
