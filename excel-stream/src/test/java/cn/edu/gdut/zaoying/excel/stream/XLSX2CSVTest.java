package cn.edu.gdut.zaoying.excel.stream;

import cn.edu.gdut.zaoying.excel.stream.elements.Element;
import cn.edu.gdut.zaoying.excel.stream.handlers.CellHandler;
import cn.edu.gdut.zaoying.excel.stream.handlers.ElementHandler;
import cn.edu.gdut.zaoying.excel.stream.handlers.WorkbookHandler;
import org.junit.Test;

import java.io.InputStream;
import java.util.Stack;

public class XLSX2CSVTest {
    @Test
    public void test(){
        String fileName = "/20131025.xlsx";
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);

        XLSXHelper.readStream(inputStream)
                .workbookHandler(xlsxPackage -> WorkbookHandler.build(xlsxPackage)
                        .setElementHandler(ElementHandler.onEnd(element -> {
                            switch (element.getType()){
                                case ROW:
                                    System.out.println();
                                    break;
                                case Cell:
                                    System.out.print(',');
                            }
                        }))
                        .setCellHandlerBuilder(tuple3 -> new CellHandler(tuple3.getA(), tuple3.getB(), tuple3.getC()){
                            @Override
                            public String handle(Stack<Element> elementStack) {
                                String result = super.handle(elementStack);
                                System.out.print(result);
                                return result;
                            }
                        })
                )
                .handleError(Throwable::printStackTrace)
                .execute();
    }
}
