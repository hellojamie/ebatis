package com.mshuoke.ebatis.exception;

/**
 * 
 * sheet头数量不一致异常
 * @author 杨硕
 *
 */
public class SheetHeadNotEqualException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SheetHeadNotEqualException(){
		super();
	}
	
	public SheetHeadNotEqualException(String message){
		super(message);
	}
	
	
}
