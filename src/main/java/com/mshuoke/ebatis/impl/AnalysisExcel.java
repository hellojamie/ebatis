package com.mshuoke.ebatis.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mshuoke.ebatis.annotation.LineNumber;
import com.mshuoke.ebatis.annotation.Mapping;
import com.mshuoke.ebatis.annotation.MappingSheetName;
import com.mshuoke.ebatis.api.DataHandleAction;
import com.mshuoke.ebatis.exception.SheetHeadNotEqualException;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.pojo.SheetInfo;

/**
 * 解析excel表格内容
 * @author 杨硕
 *
 */
public class AnalysisExcel<T> implements DataHandleAction<T> {

	@SuppressWarnings("deprecation")
	@Override
	public void prepare(ActionContext<T> act) {
		
		InputStream inputStream = new ByteArrayInputStream(act.getByteArrayOutputStream().toByteArray());
		
		Workbook wb = null;
		// 创建poi对象
		
		List<SheetInfo<T>> excelInfo = act.getSheets();
		
		try{
			switch(act.getFileType()){
			case XLS:
				wb = new HSSFWorkbook(inputStream);
				break;
			case XLSX:
				wb = new XSSFWorkbook(inputStream);
				break;
			}
			System.out.println("POI解析完毕");
		}catch(IOException e){
			e.printStackTrace();
			rollback(act);
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
						e.printStackTrace();
						rollback(act);
					}
				}
				
			}
		}
		
		// ==========继续进行解析
		for(int i=0; i<numberOfSheets; i++){
			Sheet sheet = wb.getSheetAt(i);
			List<T> analysisSheet = analysisSheet(sheet,act.getObjects());
			if(analysisSheet == null) {
				continue;
			}
			SheetInfo<T> sheetInfo = new SheetInfo<T>();
			sheetInfo.setInfo(analysisSheet);
			sheetInfo.setSheetName(sheet.getSheetName());
			sheetInfo.setLine(sheet.getLastRowNum());
			sheetInfo.setColumn(firstSheetHeadNum);
			sheetInfo.setCorrectLine(analysisSheet.size());
			excelInfo.add(sheetInfo);
		}
		
		commit(act);
		
	}

	@Override
	public boolean commit(ActionContext<T> act) {
		
		act.setResult(true);
		
		return true;
	}

	@Override
	public boolean rollback(ActionContext<T> act) {

		act.setResult(false);
		
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
		if(row == null) {
			return null;
		}
		int cellNum = row.getLastCellNum(); // 头数量
		List<String> headStr = new ArrayList<String>();	// 头内容
		
		for(int i=0; i<cellNum; i++){
			Cell cell = row.getCell(i);
			int cellType = -1;
			if(cell != null) {
				cellType = cell.getCellType();
			}
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
			
			
			// Map<String,String> rowMap = new HashMap<String,String>();
			
			// 反射对象
			T reflexObject = null;
			
			Row row2 = sheet.getRow(i);
			
			// 是否物理空行
			if(row2 == null) {
				continue;
			}
			
			// 判断行是否为空
			boolean rowEmpty = isRowEmpty(row2);
			
			if(rowEmpty) {
				continue;
			}
			
			// 遍历长度最大为头长度
			List<String> analysisRow = analysisRow(row2,cellNum);
			
			/*for(int y=0; y<headStr.size(); y++){
				rowMap.put(headStr.get(y), analysisRow.get(y));
			}*/
			
			reflexObject = getReflexObject(object.getClass(),headStr,analysisRow,sheet.getSheetName(), i);
			
			// 如果在反射期间引发错误，该行将做失败处理
			if(reflexObject == null) {
				continue;
			}
			
			sheetList.add(reflexObject);
		}
		
		return sheetList;
	}
	

	/**
	 * 将每个cell转为字符串保存
	 * @param row
	 * @param cellNum
	 * @return
	 */
	@SuppressWarnings("deprecation")
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
	@SuppressWarnings("deprecation")
	public boolean isRowEmpty(Row row){
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			
			Cell cell = row.getCell(c);
		
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				return false;
			}
		
		}
		
		return true;
	}
	
	/**
	 * 将cell的信息反射进java bean
	 * @param class1 反射对象
	 * @param headStr 头信息
	 * @param analysisRow 行内容
	 * @param sheetName sheet名称
	 * @param lineNum 第几行
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getReflexObject(Class<?> class1, List<String> headStr, List<String> analysisRow, String sheetName, int lineNum){
		/*Object objects = act.getObjects();
		Class<?> class1 = objects.getClass();*/
		
		/*
		 * 改版代码开始================================================
		 */
		
		Object object = null;
		
		// 反射获取所有字段，遍历字段取得它们的注解参数，以map的形式保存下来复用
		try {
			
			Constructor<?> constructor = class1.getConstructor();
			object = constructor.newInstance();
			
			// 获取实体属性列表
			Field[] fields = class1.getDeclaredFields();
			Class<Mapping> mapping = Mapping.class;
			Class<MappingSheetName> mappingSheetName = MappingSheetName.class;
			Class<LineNumber> lineNumber = LineNumber.class;
			
			for(Field x : fields) {
				// 获取当前属性的注解数组，并查看是否有数量
				Annotation[] annotations = x.getAnnotations();
				if(annotations.length == 0) {
					continue;
				}
				Mapping m = x.getAnnotation(mapping);
				MappingSheetName msn = x.getAnnotation(mappingSheetName);
				LineNumber ln = x.getAnnotation(lineNumber);
				
				if(m != null) {
					// 字段映射操作
					boolean mappingOperation = this.mappingOperation(class1, object, x, m, headStr, analysisRow);
					
					// 获取注解是否删除null属性
					// 如果这次映射失败，属性值为null，并且注解标识需要删除该字段为null的信息，则删除
					if(m.delNull() && !mappingOperation) {
						return null;
					}
					
				}
				
				if(msn != null) {
					// sheet名映射操作
					this.sheetNameOperation(class1, object, x, sheetName);		
				}
				
				if(ln != null) {
					// 行数映射操作
					this.lineNumberOperation(class1, object, x, lineNum);
				}

			}
		
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return (T)object;
		
		/*
		 * 改版代码结束================================================
		 */
		
	}
	
	/**
	 * 首字母转为大写
	 * @param str
	 * @return
	 */
	public String upperCase(String str) {  
	    char[] ch = str.toCharArray();  
	    if (ch[0] >= 'a' && ch[0] <= 'z') {  
	        ch[0] = (char) (ch[0] - 32);  
	    }  
	    return new String(ch);  
	} 
	
	
	/*改版测试方法如下*/
	
	/**
	 * 字段映射操作
	 * @param object 反射对象
	 * @param headStr 表头信息
	 * @param analysisRow 行内容信息
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private boolean mappingOperation(Class<?> class1, Object object, Field field, Mapping mapping, List<String> headStr, List<String> analysisRow) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		// 获取字段名称
		String fieldName = field.getName();
		// 获取注解映射属性
		String title = mapping.key();
		// 获取注解正则属性
		String rex = mapping.rex();
		
		// 拼接方法名
		String methodName = new StringBuilder()
				.append("set").append(upperCase(fieldName)).toString();
		
		String fieldType = field.getType().toString();
		
		for(int y=0; y<headStr.size(); y++) {
			// 当前头标
			String thisHead = headStr.get(y);
			if(title.equals(thisHead) && !thisHead.equals("") && thisHead != null){
				
				String string = analysisRow.get(y);
				
				// 判断正则是否为空，为空则不处理
				if(!rex.equals("")) {
					
					Pattern compile = Pattern.compile(rex);
					Matcher matcher = compile.matcher(string);
					if(!matcher.matches()) {
						// 如果匹配失败设为null
						return false;
					}
				}
				
				this.screening(class1, object, methodName, fieldType, string);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * sheet名称映射操作
	 * @param class1
	 * @param object
	 * @param x
	 * @param m
	 * @param sheetName
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void sheetNameOperation(Class<?> class1, Object object, Field field, String sheetName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		// 获取字段名称
		String fieldName = field.getName();

		// 拼接方法名
		String methodName = new StringBuilder()
				.append("set").append(upperCase(fieldName)).toString();
		
		String fieldType = field.getType().toString();
		
		this.screening(class1, object, methodName, fieldType, sheetName);
	}
	
	/**
	 * 映射行数到实体
	 * @param class1
	 * @param object
	 * @param field
	 * @param lineNum
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void lineNumberOperation(Class<?> class1, Object object, Field field, int lineNum) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		// 获取字段名称
		String fieldName = field.getName();

		// 拼接方法名
		String methodName = new StringBuilder()
				.append("set").append(upperCase(fieldName)).toString();
		
		String fieldType = field.getType().toString();
		
		this.screening(class1, object, methodName, fieldType, String.valueOf(lineNum));
	}
	
	/**
	 * 根据类型筛选赋值
	 * @param object 反射对象
	 * @Param methodName 方法名称
	 * @param fieldType 属性类型
	 * @param fieldValue 属性值
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void screening(Class<?> class1, Object object, String methodName, String fieldType, String fieldValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		switch(fieldType){
		case "class java.util.Date":
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			Date parse = null;
			try{
				parse = format.parse(fieldValue);
			}catch(ParseException e){}
			class1.getMethod(methodName, Date.class).invoke(object, parse);
			break;
		case "class java.lang.Integer":
			Integer parseInt = null;
			try{
				parseInt = Integer.parseInt(fieldValue);
			}catch(NumberFormatException e){}
			class1.getMethod(methodName, Integer.class).invoke(object, parseInt);
			break;
		case "class java.lang.String":
			if(fieldValue.equals("")) {
				break;
			}
			class1.getMethod(methodName, String.class).invoke(object, fieldValue);
			break;
		case "class java.lang.Long":
			Long parseLong = null;
			try{
				parseLong = Long.parseLong(fieldValue);
			}catch(NumberFormatException e){}
			class1.getMethod(methodName, Long.class).invoke(object, parseLong);
			break;
		case "class java.lang.Double":
			Double parseDouble = null;
			try{
				parseDouble = Double.parseDouble(fieldValue);
			}catch(NumberFormatException e){}
			class1.getMethod(methodName, Double.class).invoke(object, parseDouble);
			break;
		case "class java.lang.Short":
			Short parseShort = null;
			try{
				parseShort = Short.parseShort(fieldValue);
			}catch(NumberFormatException e){}
			class1.getMethod(methodName, Short.class).invoke(object, parseShort);
			break;
		case "class java.lang.Boolean":
			Boolean parseBoolean = null;
			try{
				parseBoolean = Boolean.parseBoolean(fieldValue);
			}catch(Exception e){}
			class1.getMethod(methodName, Boolean.class).invoke(object, parseBoolean);
			break;
		}
	}
	
}
