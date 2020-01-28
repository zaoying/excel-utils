package cn.edu.gdut.zaoying.excel.legacy.usermodel;

import org.apache.poi.ss.usermodel.Row;

/**
 * Created by DRAGON on 2017/4/24.
 */
public interface RowTraversal<T> {
    <T> void traversalRow(Row row, int firstRowNum);
}
