package org.ys.diamonds.exception;

/**
 * 文件大小错误异常
 * @author 杨硕
 *
 */
public class FileSizeErrorException extends Exception {
	public FileSizeErrorException(){
		super();
	}
	
	public FileSizeErrorException(String message){
		super(message);
	}
}
