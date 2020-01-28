package cn.edu.gdut.zaoying.excel.common.annotations;

import cn.edu.gdut.zaoying.excel.common.constants.CellType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SheetHeader {
	/**
	 * 表头名
	 * @return
	 */
	String name() default "";

	/**
	 * 控制当前列是否编辑，当值为true时将ExcelSheet.editable()的值取反
	 * @return
	 */
	boolean except() default false;

	/**
	 * 表格单元类型
	 * @return
	 */
	CellType type() default CellType.TEXT;

	/**
	 * 自动调整列宽度
	 * @return
	 */
	boolean autoSizeColumn() default true;

	/**
	 * 公式
	 * 当type为CellType.FORMULA有效
	 * @return
	 */
	String formula() default "";

	/**
	 * 日期格式
	 * 当type为CellType.DATE_TIME有效
	 * @return
	 */
	String pattern() default "yyyy-MM-dd";

	/**
	 * 数字格式
	 * 当type为CellType.NUMBER有效
	 * @return
	 */
	String numberFormat() default "0.00";

}
