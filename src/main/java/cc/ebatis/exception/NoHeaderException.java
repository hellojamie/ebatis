package cc.ebatis.exception;

/**
 * 没有表头exception
 * @author Administrator
 *
 */
public class NoHeaderException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoHeaderException(){
		super();
	}
	
	public NoHeaderException(String message){
		super(message);
	}
}
