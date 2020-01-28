package cn.edu.gdut.zaoying.excel.legacy.usermodel;

import java.util.List;

/**
 * Created by DRAGON on 2017/4/24.
 */
public interface ColumnTraversal {
    <T> void traversalColumn(Column column, LinearTraversal<T> traversal, List<T> colValue);
}
