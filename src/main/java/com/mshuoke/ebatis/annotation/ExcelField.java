package com.mshuoke.ebatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定生成的excel文件包含的属性和属性名还有位置,已经是否合并,顺序
 * @author 杨硕
 *
 */
@Documented
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ExcelField {
	
	public int position();
	
	public String name() default "";
	
	public int width() default -1;
	
}
