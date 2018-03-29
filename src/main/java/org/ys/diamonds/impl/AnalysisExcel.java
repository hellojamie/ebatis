package org.ys.diamonds.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ys.diamonds.annotation.Mapping;
import org.ys.diamonds.annotation.MappingSheetName;
import org.ys.diamonds.api.DataHandleAction;
import org.ys.diamonds.exception.SheetHeadNotEqualException;
import org.ys.diamonds.pojo.ActionContext;
import org.ys.diamonds.pojo.FiledTest;
import org.ys.diamonds.pojo.SheetInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 解析excel表格内容
 * @author 杨硕
 *
 */
public class AnalysisExcel<T> implements DataHandleAction {

	@SuppressWarnings("deprecation")
	@Override
	public boolean prepare(ActionContext act) {
		
		boolean b = false;
		
		InputStream inputStream = new ByteArrayInputStream(act.getByteArrayOutputStream().toByteArray());
		
		Workbook wb = null;
		// 创建poi对象
		
		List<SheetInfo> excelInfo = act.getInfo();
		
		try{
			switch(act.getFileType()){
			case XLS:
				wb = new HSSFWorkbook(inputStream);
				break;
			case XLSX:
				wb = new XSSFWorkbook(inputStream);
				break;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		// sheet数量
		int numberOfSheets = wb.getNumberOfSheets();
		act.setSheetSize(numberOfSheets);
		// 第一sheet的列数量
		int firstSheetHeadNum = -1;
		
		if(numberOfSheets >= 1){
			for(int n=0; n<numberOfSheets; n++){
				Sheet sheet = wb.getSheetAt(n);
				// 获取第一行
				Row row = sheet.getRow(0);
				if(row == null)
					continue;
				int cellNum = row.getLastCellNum();
				for(int i=0; i<cellNum; i++){
					Cell cell = row.getCell(i);
					int cellType = -1;
					if(cell != null)
						cellType = cell.getCellType();
					// 如果表头中间有空的，则截断
					if(cellType == -1 || cellType == Cell.CELL_TYPE_BLANK){
						cellNum = i;
					}
				}
				
				// 判断每个sheet的列是否一致，不一致则抛出错误
				if(firstSheetHeadNum == -1){
					firstSheetHeadNum = cellNum;
				}else if(firstSheetHeadNum != cellNum){
					try {
						throw new SheetHeadNotEqualException("Sheets head size not equal!");
					} catch (SheetHeadNotEqualException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		
		// ==========继续进行解析
		for(int i=0; i<numberOfSheets; i++){
			Sheet sheet = wb.getSheetAt(i);
			List<T> analysisSheet = analysisSheet(sheet,act.getObjects());
			SheetInfo sheetInfo = new SheetInfo();
			sheetInfo.setInfo(analysisSheet);
			sheetInfo.setSheetName(sheet.getSheetName());
			sheetInfo.setLine(sheet.getLastRowNum());
			sheetInfo.setColumn(firstSheetHeadNum);
			sheetInfo.setCorrectLine(analysisSheet.size());
			excelInfo.add(sheetInfo);
		}
		
		return b;
		
	}

	@Override
	public boolean commit(ActionContext act) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean rollback(ActionContext act) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 
	 * 解析sheet为row列表
	 * @param sheet
	 * @return   
	 * @return List<String>
	 */
	@SuppressWarnings("deprecation")
	List<T> analysisSheet(Sheet sheet,Object object){
		// 信息数据
		int lastRowNum = sheet.getLastRowNum(); // 一共几行
		
		List<T> sheetList = new ArrayList<T>();
		// 获取头信息
		Row row = sheet.getRow(0);
		if(row == null)
			return null;
		int cellNum = row.getLastCellNum(); // 头数量
		List<String> headStr = new ArrayList<String>();	// 头内容
		
		for(int i=0; i<cellNum; i++){
			Cell cell = row.getCell(i);
			int cellType = -1;
			if(cell != null)
				cellType = cell.getCellType();
			// 如果表头中间有空的，则截断
			if(cellType == -1 || cellType == Cell.CELL_TYPE_BLANK){
				cellNum = i;
				break;
			}
			
			switch(cellType){
			case Cell.CELL_TYPE_BLANK:
				headStr.add("");
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				headStr.add(cell.getBooleanCellValue() + "");
				break;
			case Cell.CELL_TYPE_FORMULA:
				headStr.add(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				headStr.add(cell.getNumericCellValue() + "");
				break;
			case Cell.CELL_TYPE_STRING:
				headStr.add(cell.getStringCellValue() + "");
				break;
			case Cell.CELL_TYPE_ERROR:
				break;
			default:
				headStr.add("");
			}
		}
		// 解析cell
		
		for(int i=1; i<=lastRowNum; i++){
			
			/*
			 * 替换自动映射
			 */
			
			
			Map<String,String> rowMap = new HashMap<String,String>();
			
			// 反射对象
			T reflexObject = null;
			
			Row row2 = sheet.getRow(i);
			
			// 判断行是否为空
			boolean rowEmpty = isRowEmpty(row2);
			
			if(rowEmpty)
				continue;
			
			// 遍历长度最大为头长度
			List<String> analysisRow = analysisRow(row2,cellNum);
			
			for(int y=0; y<headStr.size(); y++){
				rowMap.put(headStr.get(y), analysisRow.get(y));
			}
			
			reflexObject = getReflexObject(object.getClass(),headStr,analysisRow,sheet.getSheetName());
			
			sheetList.add(reflexObject);
		}
		
		return sheetList;
	}
	
	List<String> analysisRow(Row row,int cellNum){
		List<String> cellLi = new ArrayList<String>();
		for(int y = 0; y < cellNum; y++){
			Cell cell = row.getCell(y);
			int cellType = 3; // 默认空白
			if(cell != null)
				cellType = cell.getCellType();
			switch(cellType){
			case Cell.CELL_TYPE_BLANK:
				cellLi.add("");
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellLi.add(cell.getBooleanCellValue() + "");
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellLi.add(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				
				if(HSSFDateUtil.isCellDateFormatted(cell)){
					DataFormatter dataFormatter = new DataFormatter();
					Format format = dataFormatter.createFormat(cell);
					Date date = cell.getDateCellValue();
					if(date != null){
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						String string = simpleDateFormat.format(date);
						cellLi.add(string);
					}else{
						cellLi.add("1970-01-01");
					}
					break;
				}
				
				DecimalFormat df = new DecimalFormat("#.######");
				String dateString = df.format(cell.getNumericCellValue());
				cellLi.add(dateString);
				break;
			case Cell.CELL_TYPE_STRING:
				cellLi.add(cell.getStringCellValue() + "");
				break;
			case Cell.CELL_TYPE_ERROR:
				break;
			default:
				cellLi.add("");
			}
		}	
		return cellLi;
	}
	
	/**
	 * 
	 * 判断行是否为空
	 * @param row
	 * @return   
	 * @return boolean
	 */
	public boolean isRowEmpty(Row row){
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			
			Cell cell = row.getCell(c);
		
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
		
			return false;
		
		}
		
		return true;
	}
	
	public T getReflexObject(Class<?> class1, List<String> headStr, List<String> analysisRow, String sheetName){
		/*Object objects = act.getObjects();
		Class<?> class1 = objects.getClass();*/
		
		Object object = null;
		
		try {
			Constructor<?> constructor = class1.getConstructor();
			object = constructor.newInstance();
			
			// 获取实体属性列表
			Field[] fields = class1.getDeclaredFields();
			
			for(int y=0; y<headStr.size(); y++){
				// rowMap.put(headStr.get(y), analysisRow.get(y));
				
				String string = headStr.get(y);
				for(Field x:fields){
					
					// 表中的键
					Mapping annotation = x.getAnnotation(Mapping.class);
					if(annotation == null) {
						continue;
					}
					
					String key = annotation.key();
					if(key.equals(string)){
						// 实体中的属性名
						String filedName = x.getName();
						// 获取属性名的set方法名
						String methodName = "set" + upperCase(filedName);
						// 获取参数类型
						Class<?> type = x.getType();
						// 甄别
						if(analysisRow.get(y) == null || analysisRow.get(y).equals(""))
							continue;
						switch(type.toString()){
						case "class java.util.Date":
							String dateStr = analysisRow.get(y);
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							format.setLenient(false);
							Date parse = null;
							try{
								parse = format.parse(dateStr);
							}catch(ParseException e){}
								class1.getMethod(methodName, Date.class).invoke(object, parse);
							break;
						case "class java.lang.Integer":
							Integer parseInt = null;
							try{
								parseInt = Integer.parseInt(analysisRow.get(y));
							}catch(NumberFormatException e){}
							class1.getMethod(methodName, Integer.class).invoke(object, parseInt);
							break;
						case "class java.lang.String":
							class1.getMethod(methodName, String.class).invoke(object, analysisRow.get(y));
							break;
						case "class java.lang.Long":
							Long parseLong = null;
							try{
								parseLong = Long.parseLong(analysisRow.get(y));
							}catch(NumberFormatException e){}
							class1.getMethod(methodName, Long.class).invoke(object, parseLong);
							break;
						case "class java.lang.Double":
							Double parseDouble = null;
							try{
								parseDouble = Double.parseDouble(analysisRow.get(y));
							}catch(NumberFormatException e){}
							class1.getMethod(methodName, Double.class).invoke(object, parseDouble);
							break;
						case "class java.lang.Short":
							Short parseShort = null;
							try{
								parseShort = Short.parseShort(analysisRow.get(y));
							}catch(NumberFormatException e){}
							class1.getMethod(methodName, Short.class).invoke(object, parseShort);
							break;
						case "class java.lang.Boolean":
							Boolean parseBoolean = null;
							try{
								parseBoolean = Boolean.parseBoolean(analysisRow.get(y));
							}catch(Exception e){}
							class1.getMethod(methodName, Boolean.class).invoke(object, parseBoolean);
							break;
						}
						break;
					}
				}
				
			} // 遍历头完毕
			
			// 遍历寻找sheetname
			for(Field x:fields){
				MappingSheetName mappingSheetName = x.getAnnotation(MappingSheetName.class);

				if(mappingSheetName == null) {
					continue;
				}
				
				// 如果发现mappingSheetName注解，解析sheet名到实体中
				
					String name = x.getName(); // 属性名
					String methodName = "set" + upperCase(name); // 方法名
					Class<?> type = x.getType(); // 类型
					switch(type.toString()){
					case "class java.util.Date":
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						format.setLenient(false);
						Date parse = null;
						try{
							parse = format.parse(sheetName);
						}catch(ParseException e){}
							class1.getMethod(methodName, Date.class).invoke(object, parse);
						break;
					case "class java.lang.Integer":
						Integer parseInt = null;
						try{
							parseInt = Integer.parseInt(sheetName);
						}catch(NumberFormatException e){}
						class1.getMethod(methodName, Integer.class).invoke(object, parseInt);
						break;
					case "class java.lang.String":
						class1.getMethod(methodName, String.class).invoke(object, sheetName);
						break;
					case "class java.lang.Long":
						Long parseLong = null;
						try{
							parseLong = Long.parseLong(sheetName);
						}catch(NumberFormatException e){}
						class1.getMethod(methodName, Long.class).invoke(object, parseLong);
						break;
					case "class java.lang.Double":
						Double parseDouble = null;
						try{
							parseDouble = Double.parseDouble(sheetName);
						}catch(NumberFormatException e){}
						class1.getMethod(methodName, Double.class).invoke(object, parseDouble);
						break;
					case "class java.lang.Short":
						Short parseShort = null;
						try{
							parseShort = Short.parseShort(sheetName);
						}catch(NumberFormatException e){}
						class1.getMethod(methodName, Short.class).invoke(object, parseShort);
						break;
					case "class java.lang.Boolean":
						Boolean parseBoolean = null;
						try{
							parseBoolean = Boolean.parseBoolean(sheetName);
						}catch(Exception e){}
						class1.getMethod(methodName, Boolean.class).invoke(object, parseBoolean);
						break;
					}
			
			break;
			
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T)object;
		
	}
	
	public String upperCase(String str) {  
	    char[] ch = str.toCharArray();  
	    if (ch[0] >= 'a' && ch[0] <= 'z') {  
	        ch[0] = (char) (ch[0] - 32);  
	    }  
	    return new String(ch);  
	} 
	
}
