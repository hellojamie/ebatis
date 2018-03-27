package org.ys.diamonds.exception;

/**
 * 文件类型异常
 * @author 杨硕
 *
 */
public class FileTypeErrorException extends Exception {
	
	
	public FileTypeErrorException(){
		super();
	}
	
	public FileTypeErrorException(String message){
		super(message);
	}
	
	
}
