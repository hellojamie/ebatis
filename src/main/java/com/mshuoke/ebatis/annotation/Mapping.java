package com.mshuoke.ebatis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * 映射注解类
 * @author 杨硕
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Mapping {
	public String key();
}
