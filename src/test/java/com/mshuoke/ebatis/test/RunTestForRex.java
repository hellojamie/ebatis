package com.mshuoke.ebatis.test;

import java.io.File;

import org.junit.Test;

import com.mshuoke.ebatis.impl.Init;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.test.pojo.ImportPojo;
import com.mshuoke.ebatis.test.pojo.ImportPojoForRex;

public class RunTestForRex {
	
	// 计时
	public long a1;
	public long a2;
	
	// 正则测试（因为正则判断在通用方法中，所以不用分文件格式） ========================================================
	
	@Test
	public void rexTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/rexAllRight.xlsx", false);
	}
	
	/*
	 * run 程序
	 */
	public void runInit(String fileName, boolean distinct) throws Exception {
		ActionContext<ImportPojoForRex> act = null;
		Init<ImportPojoForRex> init = null;
		a1 = System.currentTimeMillis();
		File file = new File(fileName);
		init = new Init<ImportPojoForRex>(file, ImportPojoForRex.class, distinct);
		act = init.start();
		printInfo(act);
	}
	
	/**
	 * 打印信息结果
	 * @param act
	 */
	public void printInfo(ActionContext<ImportPojoForRex> act) {
		System.out.println("全部信息如下 ===================================");
		System.out.println(act);
		a2 = System.currentTimeMillis() - a1;
		System.out.println("耗时（s）：" + a2 / 1000);
		System.out.println("耗时（ms）：" + a2);
	}
}
