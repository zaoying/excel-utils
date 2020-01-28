package cn.edu.gdut.zaoying.excel.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheet {
	/**
	 * Excel标签页的名称
	 * @return
	 */
	String name() default "";

	/**
	 * 强制重新计算公式
	 * @return
	 */
	boolean forceFormulaRecalculation() default true;

	/**
	 * 控制整个标签页是否可编辑，SheetHeader.except()==true的列除外
	 * @return
	 */
	boolean editable() default true;
}
