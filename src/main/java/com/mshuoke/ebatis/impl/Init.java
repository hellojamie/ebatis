package com.mshuoke.ebatis.impl;

import java.io.InputStream;

import com.mshuoke.ebatis.pojo.ActionContext;

/**
 * 
 * 链入口
 * @author 杨硕
 *
 */
public class Init<T> {
	
	private ActionContext<T> act = new ActionContext<T>();
	
	private InputStream inp;
	
	private T t;
	
	private VerificationTable<T> ver;
	
	public Init(InputStream inp, T t) {
		super();
		this.inp = inp;
		this.t = t;
		act.setInputStream(inp);
		act.setObjects(t);
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

	public InputStream getInp() {
		return inp;
	}

	public void setInp(InputStream inp) {
		this.inp = inp;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
	
	
}
