package com.mshuoke.ebatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 映射注解类
 * @author 杨硕
 *
 */
@Documented
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Mapping {
	public String key();
	
	public int length() default -1;
	
	public String rex() default "";
	
	public boolean delNull() default false;
}
