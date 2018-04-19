package com.mshuoke.ebatis.create;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import com.mshuoke.ebatis.annotation.EnableExcelMaker;
import com.mshuoke.ebatis.annotation.ExcelField;
import com.mshuoke.ebatis.annotation.ExcelTitle;
import com.mshuoke.ebatis.exception.NoEnableExcelMakerException;
import com.mshuoke.ebatis.util.ConvertUtil;

public class CreateExcel<T> {
	
	public void create(List<T> list, String sheetName, File file) throws NoEnableExcelMakerException {
		
		// 获取一个实体用于获取对象拥有哪些注解
		Class<? extends Object> class1 = list.get(0).getClass();
		// 查看该对象是否拥有EnableExcelMaker
		EnableExcelMaker enableExcelMaker = class1.getAnnotation(EnableExcelMaker.class);
		// excelTitle注解，也就是表标题
		ExcelTitle excelTitle;
		// title文字
		String title = null;
		// 属性列表
		Field[] fields;
		// 用于存放属性上注解的信息
		Map<Integer,String[]> map = new HashMap<Integer,String[]>();
		// 遍历使用
		Set<Integer> keySet;
		
		if(enableExcelMaker == null) {
			// 没这个注解不执行，抛错
			throw new NoEnableExcelMakerException("Can't find @EnableExcelMaker annotation on class");
		}
		// 获取类上的的ExcelTitle注解
		excelTitle = class1.getAnnotation(ExcelTitle.class);
		
		if(excelTitle != null) {
			// 如果有title则保存下来 相反没有就是null
			title = excelTitle.value();
		}
		
		// 获取所有字段
		fields = class1.getDeclaredFields();
		for(Field x : fields) {
			ExcelField excelField = x.getAnnotation(ExcelField.class);
			if(excelField == null) {
				// 没有换下一个属性
				continue;
			}
			// 要生成的列名
			String name = excelField.name();
			// 要出现的为位置，0 -> ？
			int position = excelField.position();
			// 列宽
			String width = String.valueOf(excelField.width());
			// 属性名称
			String fieldName = x.getName();
			// 将这些需要的属性存放{生成的列名，属性名，排序方式，是否合并}
			String[] infos = new String[]{name, fieldName, width};
			map.put(position, infos);
		}

		
		// 创建对象准备生成
		HSSFWorkbook info = new HSSFWorkbook();
		// 设置sheet名称
		HSSFSheet sheet = info.createSheet(sheetName);
		// 样式
		HSSFCellStyle cellStyle = info.createCellStyle();
		// 设置居中
		cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 居中
		

		int firstLineIndex = 0;
		
		if(title != null) {
			// 如果等于null，说明没有设置title
			firstLineIndex = 1;
		}
		
		/*这个是生成表头*/
		HSSFRow createRow = sheet.createRow(firstLineIndex);
		
		// 头样式
		HSSFCellStyle crs = getStyleBold(info);
		// 行样式
		HSSFCellStyle cellCrs = getStyle(info);
		
		// 获取刚刚从字段中拿到的map
		keySet = map.keySet();
		// 用于找到最大列下标
		int maxCell = -1;
		// 遍历map拿到最大值，并且将每一个列头进行设置
		for(Integer x : keySet) {
			if(x > maxCell) {
				maxCell = x;
			}
			// 拿到列头的名称
			String string = map.get(x)[0];
			//sheet.setDefaultColumnStyle(x, cellStyle);
			// 设置cell内容（表头）
			HSSFCell createCell = createRow.createCell(x,CellType.STRING);
			createCell.setCellStyle(crs);
			createCell.setCellValue(string);
			// 自动调整列宽
			String lengthStr = map.get(x)[2];
			if(!lengthStr.equals("-1")) {
				sheet.setColumnWidth(x, Integer.parseInt(map.get(x)[2]) * 400);
			}
			
		}
		
		// 如果有title的话，则给表头赋值并且以最大列合并单元格
		if(firstLineIndex == 1) {
			HSSFRow titleRow = sheet.createRow(0);
			titleRow.setHeight((short)666);
			HSSFCell createCell = titleRow.createCell(0);
			createCell.setCellType(CellType.STRING);
			createCell.setCellValue(title);
			// 合并单元格  
	        CellRangeAddress cra =new CellRangeAddress(0, 0, 0, maxCell); // 起始行, 终止行, 起始列, 终止列  
	        sheet.addMergedRegion(cra);
	        createCell.setCellStyle(getStyleTitle(info));
		}
		
		for(int i = 0; i < list.size(); i++) {
			// 循环集合获取对象，准备生成cell
			T t = list.get(i);
			// 获取反射对象
			Class<? extends Object> class2 = t.getClass();
			// 创建行
			HSSFRow row = sheet.createRow(i + 1 + firstLineIndex);
			for(Integer x : keySet) {
				String string = map.get(x)[1];
				String method = "get" + ConvertUtil.upperCase(string);
				Object invoke = null;
				try {
					invoke = class2.getMethod(method).invoke(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(invoke == null) {
					continue;
				}
				String typeName = invoke.getClass().getTypeName();
				HSSFCell createCell = row.createCell(x);
				createCell.setCellStyle(cellCrs);
				// 写每一个单元格内容
				switch(typeName) {
				case "java.lang.String":
					createCell.setCellType(CellType.STRING);
					createCell.setCellValue((String)invoke);
					break;
				case "java.lang.Double":
					createCell.setCellType(CellType.NUMERIC);
					createCell.setCellValue((Double)invoke);
					break;
				case "java.lang.Short":
					createCell.setCellType(CellType.NUMERIC);
					createCell.setCellValue((Short)invoke);
					break;
				case "java.lang.Long":
					createCell.setCellType(CellType.NUMERIC);
					createCell.setCellValue((Long)invoke);
					break;
				case "java.lang.Integer":
					createCell.setCellType(CellType.NUMERIC);
					createCell.setCellValue((Integer)invoke);
					break;
				case "java.lang.Boolean":
					createCell.setCellType(CellType.BOOLEAN);
					createCell.setCellValue((Boolean)invoke);
					break;
				case "java.util.Date":
					Date date = (Date)invoke;
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String format2 = format.format(date);
					createCell.setCellType(CellType.STRING);
					createCell.setCellValue(format2);
					break;
				}
				
			}
		}
		
		try {
			info.write(file);
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取大标题样式
	 * @param info
	 * @return
	 */
	public HSSFCellStyle getStyleTitle(HSSFWorkbook info) {
		HSSFCellStyle style = getStyle(info);
		HSSFFont crsFont = info.createFont();
		crsFont.setBold(true);
		crsFont.setFontHeightInPoints((short) 14);//设置字体大小    
		style.setFont(crsFont);
		return style;
	}
	
	/**
	 * 获取表头样式
	 * @param info
	 * @return
	 */
	public HSSFCellStyle getStyleBold(HSSFWorkbook info) {
		HSSFCellStyle style = getStyle(info);
		HSSFFont crsFont = info.createFont();
		crsFont.setBold(true);
		style.setFont(crsFont);
		return style;
	}
	
	/**
	 * 获取普通cell样式（居中，边框）
	 * @param info
	 * @return
	 */
	public HSSFCellStyle getStyle(HSSFWorkbook info) {
		// 头样式
		HSSFCellStyle crs = info.createCellStyle();
		crs.setBorderBottom(BorderStyle.THIN); //下边框    
		crs.setBorderLeft(BorderStyle.THIN);//左边框    
		crs.setBorderTop(BorderStyle.THIN);//上边框    
		crs.setBorderRight(BorderStyle.THIN);//右边框 
		crs.setAlignment(HorizontalAlignment.CENTER); // 居中
		crs.setVerticalAlignment(VerticalAlignment.CENTER); // 居中
		return crs;
	}
	
}
