package cc.ebatis.api;

import cc.ebatis.pojo.ActionContext;

public interface DataHandleAction<T> {
	
	void prepare(ActionContext<T> act);
	
	boolean commit(ActionContext<T> act);
	
	boolean rollback(ActionContext<T> act);
}
