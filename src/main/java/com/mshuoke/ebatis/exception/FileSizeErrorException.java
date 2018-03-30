package com.mshuoke.ebatis.exception;

/**
 * 文件大小错误异常
 * @author 杨硕
 *
 */
public class FileSizeErrorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileSizeErrorException(){
		super();
	}
	
	public FileSizeErrorException(String message){
		super(message);
	}
}
