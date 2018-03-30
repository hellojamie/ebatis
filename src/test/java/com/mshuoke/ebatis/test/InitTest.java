package com.mshuoke.ebatis.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mshuoke.ebatis.impl.Init;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.pojo.FiledTest;
import com.mshuoke.ebatis.pojo.SheetInfo;

public class InitTest {

	@Test
	public void getTest(){
		
		InputStream inputStream = null;
		
		try {
			inputStream = new FileInputStream("000.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long a1 = System.currentTimeMillis();
		// start============================================================================
		Init<FiledTest> init = new Init<FiledTest>(inputStream, new FiledTest());
		ActionContext<FiledTest> act = init.start();
		// end==============================================================================
		long a2 = System.currentTimeMillis() - a1;
		System.out.println("耗时（s）：" + a2 / 1000);
		System.out.println("耗时（ms）：" + a2);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String string = mapper.writeValueAsString(act);
			System.out.println(string);	
			List<SheetInfo<FiledTest>> info = act.getInfo();
			List<FiledTest> info2 = info.get(0).getInfo();
			//System.out.println(mapper.writeValueAsString(info2));
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
