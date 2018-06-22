package cc.ebatis.impl;

import java.io.File;

import cc.ebatis.pojo.ActionContext;

/**
 * 
 * 链入口
 * @author 杨硕
 *
 */
public class Init<T> {
	
	private ActionContext<T> act = new ActionContext<T>();
	
	private VerificationTable<T> ver;
	
	public Init(File file, Class<? extends T> t, boolean distinct) {
		super();
		act.setFile(file);
		act.setObjects(t);
		act.setDistinct(distinct);
	}
	
	public ActionContext<T> start(){
		ver = new VerificationTable<T>();
		ver.prepare(act);
		return act;
	}
	
	public VerificationTable<T> getVer() {
		return ver;
	}

	public void setVer(VerificationTable<T> ver) {
		this.ver = ver;
	}

	public ActionContext<T> getAct() {
		return act;
	}

	public void setAct(ActionContext<T> act) {
		this.act = act;
	}

}