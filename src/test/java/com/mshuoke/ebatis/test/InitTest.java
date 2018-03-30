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
		
		// step 1
		// ActionContext<FiledTest> act = new ActionContext<FiledTest>();
		InputStream inputStream = null;
		
		// step 2
		// act.setObjects(new FiledTest());
		
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
		System.out.println(a2 / 1000);
		
		// step 3
		// act.setInputStream(inputStream);
		// step 4
		// VerificationTable<FiledTest> a = new VerificationTable<FiledTest>();
		// step 5
		// a.prepare(act);
		
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
