package com.mshuoke.ebatis.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.mshuoke.ebatis.create.CreateExcel;
import com.mshuoke.ebatis.impl.Init;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.test.pojo.CreateExcelPOJO;
import com.mshuoke.ebatis.test.pojo.ImportPojo;
import com.mshuoke.ebatis.test.pojo.RealPojo;

public class RunTest {
	
	// 计时
	public long a1;
	public long a2;
	
	/*
	 * 完全正确的数据（格式完全正确）
	 */
	@Test
	public void importTestAllRight() throws Exception {
		// =========== xlsx for sax
		runInit("exl/allright.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/allright.xls", false);
	}
	
	/*
	 * 日期包含多种格式的（但单元格都是日期类型）
	 */
	@Test
	public void manyDateTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/manydate.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/manydate.xls", false);
	}
	
	/*
	 * 仅包含空cell，空的将赋值为null
	 */
	@Test
	public void blankCellTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/blankcell.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/blankcell.xls", false);
	}
	
	/*
	 * 空行测试
	 */
	@Test
	public void blankRowTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/blankrow.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/blankrow.xls", false);
	}
	
	/*
	 * 多sheet测试（格式完全正确）
	 */
	@Test
	public void manyRightSheetTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/manyrightsheet.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/manyrightsheet.xls", false);
	}
	
	/*
	 * 空的sheet包含（只包含表头或什么都没有）
	 */
	@Test
	public void manyBlankSheetTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/manyblanksheet.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/manyblanksheet.xls", false);
	}

	// @Test
	public void realTest() {
		ActionContext<RealPojo> act = null;
		Init<RealPojo> init = null;
		a1 = System.currentTimeMillis();
		File file = new File("exl/moushi20180321(100W).xlsx");
		init = new Init<RealPojo>(file, RealPojo.class, false);
		act = init.start();
		System.out.println(act.getSheets().get(0).getInfo().get(0).getPhone());
		System.out.println("over my head");
	}
	
	
	/*
	 * run 程序
	 */
	public void runInit(String fileName, boolean distinct) throws Exception {
		ActionContext<ImportPojo> act = null;
		Init<ImportPojo> init = null;
		a1 = System.currentTimeMillis();
		File file = new File(fileName);
		init = new Init<ImportPojo>(file, ImportPojo.class, distinct);
		act = init.start();
		printInfo(act);
	}
	
	/**
	 * 打印信息结果
	 * @param act
	 */
	public void printInfo(ActionContext<ImportPojo> act) {
		System.out.println("全部信息如下 ===================================");
		System.out.println(act);
		a2 = System.currentTimeMillis() - a1;
		System.out.println("耗时（s）：" + a2 / 1000);
		System.out.println("耗时（ms）：" + a2);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//@Test
	public void createExcelTest() {
		
		File file = new File("exl/create.xlsx");
		
		Init<CreateExcelPOJO> init = new Init<CreateExcelPOJO>(file, CreateExcelPOJO.class, true);
		ActionContext<CreateExcelPOJO> act = init.start();
		
		List<CreateExcelPOJO> list = act.getSheets().get(0).getInfo();
		
		CreateExcel<CreateExcelPOJO> c = new CreateExcel<CreateExcelPOJO>();
		try {
			c.create(list, "这是一个sheet名", new File("生成excel.xlsx"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
