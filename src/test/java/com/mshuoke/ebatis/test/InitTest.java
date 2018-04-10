package com.mshuoke.ebatis.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mshuoke.ebatis.create.CreateExcel;
import com.mshuoke.ebatis.impl.Init;
import com.mshuoke.ebatis.pojo.ActionContext;

public class InitTest {
	


	
	@Test
	public void test2() {
		InputStream inputStream = null;
		
		try {
			inputStream = new FileInputStream("000.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long a1 = System.currentTimeMillis();
		Init<People2> init = new Init<People2>(inputStream, new People2(), true);
		ActionContext<People2> act = init.start();
		long a2 = System.currentTimeMillis() - a1;
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String string = mapper.writeValueAsString(act);
			System.out.println(string);
			//System.out.println(act.getSheets().get(0).getInfo().size());
			System.out.println("正确行数：" + act.getSheets().get(0).getCorrectLine());
			System.out.println("空白行数：" + act.getSheets().get(0).getBlankLineSize());
			System.out.println("错误行数：" + act.getSheets().get(0).getErrorLineSize());
			System.out.println("重复行数：" + act.getSheets().get(0).getRepeatLineSize());
			// System.out.println("重复行数具体：" + act.getSheets().get(0).getRepeatLine().get(0));
			System.out.println("耗时（s）：" + a2 / 1000);
			System.out.println("耗时（ms）：" + a2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void y(List<Kid> list) {
		for(int i=0;i<list.size();i++) {
			Kid kid = list.get(i);
			String info = kid.getInfo();
			
			int isb = 0;
			
			// 空的     只有一个数字的       带/的        不带/的         只有名字的         名字年龄连在一起的 
			// 带几岁半的
			if(info != null && !info.equals("")) {
				kid.setName(info);
				kid.setCity("上海市");
				// 纯数字的
				try {
					int parseInt = Integer.parseInt(info);
					kid.setAge(String.valueOf(parseInt));
					continue;
				}catch(Exception e) {}
				
				// 包含个月
				for(int c=0;c<40;c++) {
					if(info.indexOf(c + "个月") != -1) {
						kid.setAge(c + "");
					}
				}
				
				
				// 岁半的
				for(int c=0;c<40;c++) {
					if(info.indexOf(c + "岁半") != -1) {
						kid.setAge((c * 12 + 6) + "");
						isb = 1;
					}
				}
				
				if(isb != 1) {
					// 岁的
					for(int c=0;c<40;c++) {
						if(info.indexOf(c + "岁") != -1) {
							kid.setAge((c * 12) + "");
						}
					}
				}
				
				// 名字和数字，没有尾巴
				for(int c=0;c<40;c++) {
					if(info.indexOf(String.valueOf(c)) != -1 && info.indexOf("岁") == -1 && info.indexOf("个月") == -1) {
						kid.setAge(c + "");
					}
				}
				
				
				
			}
			
			
		}
		
		CreateExcel.create(list);
	}
	
	
	
}
