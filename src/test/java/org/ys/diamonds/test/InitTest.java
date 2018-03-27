package org.ys.diamonds.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.ys.diamonds.impl.Init;
import org.ys.diamonds.impl.VerificationTable;
import org.ys.diamonds.pojo.ActionContext;
import org.ys.diamonds.pojo.FiledTest;
import org.ys.diamonds.pojo.SheetInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InitTest {

	//@Test
	public void getTest(){
		
		// step 1
		// ActionContext<FiledTest> act = new ActionContext<FiledTest>();
		InputStream inputStream = null;
		
		// step 2
		// act.setObjects(new FiledTest());
		
		try {
			inputStream = new FileInputStream("d.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Init<FiledTest> init = new Init<FiledTest>(new ActionContext<FiledTest>(), inputStream, new FiledTest());
		ActionContext<FiledTest> act = init.start();
		
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
			System.out.println(mapper.writeValueAsString(info2));
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
