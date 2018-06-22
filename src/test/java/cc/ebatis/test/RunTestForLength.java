package cc.ebatis.test;

import java.io.File;

import org.junit.Test;

import cc.ebatis.impl.Init;
import cc.ebatis.pojo.ActionContext;
import cc.ebatis.test.pojo.ImportPojoForLength;

public class RunTestForLength {
	
	// 计时
	public long a1;
	public long a2;
	
	// 正则测试（因为长度判断在通用方法中，所以不用分文件格式） ========================================================
	
	@Test
	public void lengthTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/allRight.xlsx", false);
	}
	
	/*
	 * run 程序
	 */
	public void runInit(String fileName, boolean distinct) throws Exception {
		ActionContext<ImportPojoForLength> act = null;
		Init<ImportPojoForLength> init = null;
		a1 = System.currentTimeMillis();
		File file = new File(fileName);
		init = new Init<ImportPojoForLength>(file, ImportPojoForLength.class, distinct);
		act = init.start();
		printInfo(act);
	}
	
	/**
	 * 打印信息结果
	 * @param act
	 */
	public void printInfo(ActionContext<ImportPojoForLength> act) {
		System.out.println("全部信息如下 ===================================");
		System.out.println(act);
		a2 = System.currentTimeMillis() - a1;
		System.out.println("耗时（s）：" + a2 / 1000);
		System.out.println("耗时（ms）：" + a2);
	}
}
