package com.mshuoke.ebatis.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.mshuoke.ebatis.create.CreateExcel;
import com.mshuoke.ebatis.impl.Init;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.test.pojo.CreateExcelPOJO;
import com.mshuoke.ebatis.test.pojo.ImportPojo;

public class RunTest {
	
	@Test
	public void importTestAllRight() {
		InputStream inputStream = null;
		
		try {
			inputStream = new FileInputStream("exl/allright.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long a1 = System.currentTimeMillis();
		Init<ImportPojo> init = new Init<ImportPojo>(inputStream, new ImportPojo(), false);
		ActionContext<ImportPojo> act = init.start();
		long a2 = System.currentTimeMillis() - a1;
		
		System.out.println(act.getSheets().get(0).getInfo());
		System.out.println(act.getSheets().get(0).getInfo().size());
		System.out.println("正确行数：" + act.getSheets().get(0).getCorrectLine());
		System.out.println("空白行数：" + act.getSheets().get(0).getBlankLineSize());
		System.out.println("错误行数：" + act.getSheets().get(0).getErrorLineSize());
		System.out.println("重复行数：" + act.getSheets().get(0).getRepeatLineSize());
		System.out.println("耗时（s）：" + a2 / 1000);
		System.out.println("耗时（ms）：" + a2);
		
	}
	
	//@Test
	public void createExcelTest() {
		
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream("create.xlsx");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Init<CreateExcelPOJO> init = new Init<CreateExcelPOJO>(inputStream, new CreateExcelPOJO(), true);
		ActionContext<CreateExcelPOJO> act = init.start();
		
		List<CreateExcelPOJO> list = act.getSheets().get(0).getInfo();
		
		CreateExcel<CreateExcelPOJO> c = new CreateExcel<CreateExcelPOJO>();
		try {
			c.create(list, "这是一个sheet名", new File("生成excel.xlsx"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
