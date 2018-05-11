package com.mshuoke.ebatis.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mshuoke.ebatis.api.DataHandleAction;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.pojo.SheetInfo;
import com.mshuoke.ebatis.util.ReflexObject;

/**
 * 解析excel表格内容
 * @author 杨硕
 *
 */
public class AnalysisExcel<T> implements DataHandleAction<T> {

	private AnalysisExcelForSax<T> analysisExcelForSax = new AnalysisExcelForSax<T>();
	
	private ReflexObject<T> reflexObject = new ReflexObject<T>();
	
	@SuppressWarnings("deprecation")
	@Override
	public void prepare(ActionContext<T> act) {
		
		// 判断是否使用sax方式处理文件,如果使用，跳过该链
		if(act.getUseSax()) {
			commit(act);
			return;
		}
		
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(act.getFile());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		Workbook wb = null;
		// 创建poi对象
		
		List<SheetInfo<T>> excelInfo = act.getSheets();
		
		boolean distinct = act.getDistinct();
		
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
			rollback(act);
		}finally {
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// sheet数量
		int numberOfSheets = 0;
		if(wb != null) {
			numberOfSheets = wb.getNumberOfSheets();
		}
		act.setSheetSize(numberOfSheets);
		// 第一sheet的列数量
		int firstSheetHeadNum = -1;
		
		if(numberOfSheets >= 1){
			for(int n=0; n<numberOfSheets; n++){
				Sheet sheet = wb.getSheetAt(n);
				// 获取第一行
				Row row = sheet.getRow(0);
				
				int cellNum = 0;
				if(row != null) {
					cellNum = row.getLastCellNum();
				}
				
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
				}
				/*else if(firstSheetHeadNum != cellNum){
					try {
						throw new SheetHeadNotEqualException("Sheets head size not equal!");
					} catch (SheetHeadNotEqualException e) {
						e.printStackTrace();
						rollback(act);
					}
				}*/
				
			}
		}
		
		// ==========继续进行解析
		for(int i=0; i<numberOfSheets; i++){
			Sheet sheet = wb.getSheetAt(i);
			SheetInfo<T> sheetInfo = new SheetInfo<T>();
			List<T> analysisSheet = analysisSheet(sheet,act.getObjects(),sheetInfo,distinct);
			if(analysisSheet == null) {
				sheetInfo.setSheetName(sheet.getSheetName());
				excelInfo.add(sheetInfo);
				continue;
			}
			sheetInfo.setInfo(analysisSheet);
			sheetInfo.setSheetName(sheet.getSheetName());
			sheetInfo.setLine(sheet.getLastRowNum());
			sheetInfo.setColumn(firstSheetHeadNum);
			sheetInfo.setCorrectLine(analysisSheet.size());
			// 设置错误、空白、重复数量
			sheetInfo.setBlankLineSize(sheetInfo.getBlankLine().size());
			sheetInfo.setErrorLineSize(sheetInfo.getErrorLine().size());
			sheetInfo.setRepeatLineSize(sheetInfo.getRepeatLine().size());
			// 添加
			excelInfo.add(sheetInfo);
		}
		
		commit(act);
		
	}

	@Override
	public boolean commit(ActionContext<T> act) {
		
		analysisExcelForSax.prepare(act);
		
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
	List<T> analysisSheet(Sheet sheet,Class<? extends T> object,SheetInfo<T> sheetInfo, boolean distinct){
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
		
		Set<T> distinctSet = new HashSet<T>();	// 去重set
		
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
			T t = null;
			
			Row row2 = sheet.getRow(i);
			
			// 是否物理空行
			if(row2 == null) {
				sheetInfo.addBlankLine(i);
				continue;
			}
			
			// 判断行是否为空
			boolean rowEmpty = isRowEmpty(row2);
			
			if(rowEmpty) {
				sheetInfo.addBlankLine(i + 1);
				continue;
			}
			
			// 遍历长度最大为头长度
			List<String> analysisRow = analysisRow(row2,cellNum);
			
			/*for(int y=0; y<headStr.size(); y++){
				rowMap.put(headStr.get(y), analysisRow.get(y));
			}*/
			
			t = reflexObject.getReflexObject(object,headStr,analysisRow,sheet.getSheetName(), i + 1);
			
			// 如果在反射期间引发错误，该行将做失败处理
			if(t == null) {
				sheetInfo.addErrorLine(i + 1);
				continue;
			}
			
			boolean flag = true;
			// 在此去重, 如果为true表示去重
			if(distinct) {
				boolean add = distinctSet.add(t);
				// 如果等于false,添加失败，即相等，则不添加进集合，做重复处理
				if(!add) {
					// 将重复的记录下来,设置行数
					sheetInfo.addRepeatLine(i + 1);
					// 重复不执行后续操作
					flag = false;
				}
			}
			
			if(flag) {
				sheetList.add(t);
			}
			
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
				cellLi.add(String.valueOf(cell.getNumericCellValue()));
				// cellLi.add(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				short dataFormat = cell.getCellStyle().getDataFormat();
				if(DateUtil.isCellDateFormatted(cell)){
					Date date = cell.getDateCellValue();
					if(dataFormat == 179){
						Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
						String string = simpleDateFormat.format(javaDate);
						cellLi.add(string);
					}else if(dataFormat == 58 || dataFormat == 177) {
						Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
						String string = simpleDateFormat.format(javaDate);
						cellLi.add(string);
					}else if(date != null){
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
	
}