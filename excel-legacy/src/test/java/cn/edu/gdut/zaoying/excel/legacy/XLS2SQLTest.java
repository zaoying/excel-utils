package cn.edu.gdut.zaoying.excel.legacy;

import cn.edu.gdut.zaoying.excel.legacy.stream.XLSHelper;
import org.apache.poi.util.StringUtil;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class XLS2SQLTest {
    @Test
    public void test(){
        String fileName = "/20131025.xls";
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);

        AtomicLong rowNumber = new AtomicLong(1);

        StringBuilder sql = new StringBuilder(1024*1024);
        sql.append("insert into student(no, stu_no, stu_name, major, grade, zone, origin, remark) values \n");

        StringListHandler stringListHandler = new StringListHandler();

        //处理每一行最后的虚拟单元格
        stringListHandler.setLastCellOfRowDummyRecordHandler(cell -> {
            List<String> fieldList = stringListHandler.getFieldList();
            if(fieldList.isEmpty()){
                //遇到空白行且rowNumber大于1时输出
                if(rowNumber.get() > 1){
                    int length = sql.length();
                    sql.delete(length-2, length);
                    System.out.println(sql);
                    //重置
                    sql.delete(0, sql.length());
                    rowNumber.set(1);
                    sql.append("insert into student(no, stu_no, stu_name, major, grade, zone, origin, remark) values \n");
                }
            }
            else {
                rowNumber.getAndIncrement();
                sql.append(String.format("(%s),\n", StringUtil.join(fieldList.toArray(), ",")));
            }
            //每行清空
            fieldList.clear();
        });
        XLSHelper.readStream(inputStream)
                .handleRecord(stringListHandler)
                .execute();
    }
}
