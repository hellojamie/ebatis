package com.mshuoke.ebatis.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.junit.Test;

import com.mshuoke.ebatis.create.CreateExcel;
import com.mshuoke.ebatis.impl.Init;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.test.pojo.CreateExcelPOJO;
import com.mshuoke.ebatis.test.pojo.ImportPojo;
import com.mshuoke.ebatis.test.pojo.Linshi01;
import com.mshuoke.ebatis.test.pojo.Linshi02;
import com.mshuoke.ebatis.test.pojo.RealPojo;
import com.mshuoke.ebatis.test.pojo.ResultPojo;

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
	 * 带函数公式的（格式完全正确）
	 */
	@Test
	public void importTestAllRightFunction() throws Exception {
		// =========== xlsx for sax
		runInit("exl/function.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/function.xls", false);
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
	 * 空表头
	 */
	@Test
	public void blankCellHeadTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/blankcellhead.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/blankcellhead.xls", false);
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
	
	/*
	 * 替换表头“你的名字”替换为“姓名”
	 */
	@Test
	public void replaceHeadTest() throws Exception {
		// =========== xlsx for sax
		runInit("exl/replacehead.xlsx", false);
		// =========== xls for usermodel
		runInit("exl/replacehead.xls", false);
	}

	//@Test
	public void realTest() {
		ActionContext<RealPojo> act = null;
		Init<RealPojo> init = null;
		a1 = System.currentTimeMillis();
		File file = new File("exl/性能测试20.xlsx");
		init = new Init<RealPojo>(file, RealPojo.class, false);
		act = init.start();
		System.out.println(act.getSheets().get(0).getInfo().get(0));
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
	
	@Test
	public void linshi() throws Exception {
		ActionContext<Linshi01> act = null;
		Init<Linshi01> init = null;
		File file = new File("C:\\Users\\Administrator\\Desktop\\11502_20180528_APP.xlsx");
		init = new Init<Linshi01>(file, Linshi01.class, false);
		act = init.start();
		List<Linshi01> info = act.getSheets().get(0).getInfo();
		System.out.println(info.size());
		
		ActionContext<Linshi02> act2 = null;
		Init<Linshi02> init2 = null;
		a1 = System.currentTimeMillis();
		File file2 = new File("C:\\Users\\Administrator\\Desktop\\第一批.xlsx");
		init2 = new Init<Linshi02>(file2, Linshi02.class, false);
		act2 = init2.start();
		List<Linshi02> info2 = act2.getSheets().get(0).getInfo();
		System.out.println(info2.size());
		
		for(Linshi01 x:info) {
			for(Linshi02 y:info2) {
				if(x.getMd5().equalsIgnoreCase(y.getMd5()) && !y.getPhone().equalsIgnoreCase("null")) {
					x.setPhone(y.getPhone());
					break;
				}
			}
		}
		
		CreateExcel<Linshi01> c = new CreateExcel<Linshi01>();
		try {
			c.create(info, "sheet1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void createExcelTest() {
		
		File file = new File("exl/create.xlsx");
		
		Init<CreateExcelPOJO> init = new Init<CreateExcelPOJO>(file, CreateExcelPOJO.class, true);
		ActionContext<CreateExcelPOJO> act = init.start();
		
		List<CreateExcelPOJO> list = act.getSheets().get(0).getInfo();
		
		CreateExcel<CreateExcelPOJO> c = new CreateExcel<CreateExcelPOJO>();
		try {
			c.create(list, "这是一个sheet名");
			HSSFSheet sheet = c.getHSSFSheet();
			HSSFRow row = sheet.createRow(10);
			HSSFCell createCell = row.createCell(0);
			createCell.setCellValue("bbbbbb");
			c.write(new File("生成excel.xlsx"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void createExcelTest2() {
		CreateExcel<ResultPojo> c = new CreateExcel<ResultPojo>();
		
		// 生成模拟数据
		String[] strings = new String[] {"上海市","东莞市","佛山市","北京市","南京市","南通市","厦门市","台州市","嘉兴市","广州市","西安市"};
		Random random = new Random();
		
		List<ResultPojo> list = new ArrayList<ResultPojo>();
		for(int i = 0; i < 10; i ++) {
			ResultPojo re = new ResultPojo("CSJ创数记",
					"行为生成日期、类目、城市、行为标记、手机号、导入日期、导入时间",
					"2018-05-30",
					"学科类辅导",
					strings[random.nextInt(10)],
					470,4398,471,4068,471,4068,471,4068);
			list.add(re);
		}
		for(int i = 0; i < 6; i ++) {
			ResultPojo re = new ResultPojo("CSJ创数记",
					"行为生成日期、类目、城市、行为标记、手机号、导入日期、导入时间",
					"2018-05-30",
					"成人英语",
					strings[random.nextInt(10)],
					470,5622,471,5622,471,5622,471,5622);
			list.add(re);
		}
		for(int i = 0; i < 10; i ++) {
			ResultPojo re = new ResultPojo("CSJ创数记",
					"行为生成日期、类目、城市、行为标记、手机号、导入日期、导入时间",
					"2018-05-30",
					"青少儿英语",
					strings[random.nextInt(10)],
					470,6666,471,6666,471,6666,471,6666);
			list.add(re);
		}
		// 生成模拟数据结束
		try {
			c.create(list, "这是一个sheet名");
			HSSFSheet sheet = c.getHSSFSheet();
			HSSFRow row = sheet.createRow(list.size() + 2);
			HSSFCell createCell = row.createCell(0);
			createCell.setCellValue("汇总");
			HSSFCell all1 = row.createCell(6);
			all1.setCellValue(10781);
			HSSFCell all2 = row.createCell(8);
			all2.setCellValue(10445);
			HSSFCell all3 = row.createCell(10);
			all3.setCellValue(10445);
			HSSFCell all4 = row.createCell(12);
			all4.setCellValue(10445);
			c.write(new File("生成excel.xlsx"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
