package cn.edu.gdut.zaoying.excel.legacy.usermodel;

import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

/**
 * @author huangzurong
 */
public interface LinearTraversal<T> {
	public void each(Cell cell, List<T> rowValue);
}
