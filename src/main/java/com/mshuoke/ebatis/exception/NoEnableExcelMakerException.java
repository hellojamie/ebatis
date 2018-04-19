package com.mshuoke.ebatis.exception;

/**
 * 没有找到实体中的EnableExcelMaker注解
 * @author Administrator
 *
 */
public class NoEnableExcelMakerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoEnableExcelMakerException(){
		super();
	}
	
	public NoEnableExcelMakerException(String message){
		super(message);
	}
	
	
}
