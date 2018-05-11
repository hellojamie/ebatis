package com.mshuoke.ebatis.exception;

/**
 * 文件类型异常
 * @author 杨硕
 *
 */
public class FileTypeErrorException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileTypeErrorException(){
		super();
	}
	
	public FileTypeErrorException(String message){
		super(message);
	}
	
	
}
