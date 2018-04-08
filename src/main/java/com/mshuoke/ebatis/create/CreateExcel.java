package com.mshuoke.ebatis.create;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import com.mshuoke.ebatis.test.Kid;

public class CreateExcel {
	
	public static void create(List<Kid> list) {
		HSSFWorkbook info = new HSSFWorkbook();
		HSSFSheet sheet = info.createSheet("信息");
		HSSFRow createRow = sheet.createRow(0);
		createRow.createCell(0,CellType.STRING).setCellValue("联系方式");
		createRow.createCell(1,CellType.STRING).setCellValue("孩子姓名");
		createRow.createCell(2,CellType.STRING).setCellValue("年龄（月）");
		createRow.createCell(3,CellType.STRING).setCellValue("城市");
		
		for(int i=0;i<list.size();i++) {
			Kid kid = list.get(i);
			HSSFRow row = sheet.createRow(i + 1);
			row.createCell(0,CellType.STRING).setCellValue(kid.getPhone());
			row.createCell(1,CellType.STRING).setCellValue(kid.getName());
			row.createCell(2,CellType.STRING).setCellValue(kid.getAge());
			row.createCell(3,CellType.STRING).setCellValue(kid.getCity());
		}
		
		try {
			info.write(new File("拆解.xlsx"));
			info.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
