package org.ys.diamonds.api;

import org.ys.diamonds.pojo.ActionContext;

public interface DataHandleAction<T> {
	
	boolean prepare(ActionContext<T> act);
	
	boolean commit(ActionContext<T> act);
	
	boolean rollback(ActionContext<T> act);
}
