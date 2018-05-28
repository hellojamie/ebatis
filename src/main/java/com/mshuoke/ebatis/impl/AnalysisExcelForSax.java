package com.mshuoke.ebatis.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.mshuoke.ebatis.api.DataHandleAction;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.pojo.SheetInfo;
import com.mshuoke.ebatis.util.ReflexObject;

/**
 * 使用sax解析 excel(针对于BigData)
 * @author Administrator
 *
 * @param <T>
 */
public class AnalysisExcelForSax<T> implements DataHandleAction<T>{

	@Override
	public void prepare(ActionContext<T> act) {

		// 判断是否使用sax方式处理文件,如果不使用，跳过该链
		if(!act.getUseSax()) {
			commit(act);
			return;
		}
		
		// 正式代码开始
		
		
		try {
			ReflexVO<T> reflexVO = new ReflexVO<T>();
			reflexVO.setAct(act);
			processAllSheets(act.getFile(), reflexVO);
		} catch (Exception e) {
			rollback(act);
			e.printStackTrace();
		}
		
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

	private static StylesTable stylesTable;
	
	public void processAllSheets(File file, ReflexVO<T> reflexVO) throws Exception {
		OPCPackage pkg = OPCPackage.open(file,PackageAccess.READ);
		XSSFReader r = new XSSFReader(pkg);
		
		stylesTable = r.getStylesTable();
		SharedStringsTable sst = r.getSharedStringsTable();
		
		XMLReader parser = fetchSheetParser(sst,reflexVO);
		
		// Iterator<InputStream> sheets = r.getSheetsData();
		
		XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) r.getSheetsData();
		
		Integer sheetSize = 0;
		
		while(sheets.hasNext()) {
			
			// System.out.println("Processing new sheet:\n");
			InputStream sheet = sheets.next();
			// 将name保存下来 新建sheetInfo对象
			SheetInfo<T> sheetInfo = new SheetInfo<T>();
			// 保存名字
			sheetInfo.setSheetName(sheets.getSheetName());
			// 设置对象进VO中
			reflexVO.setSheetInfo(sheetInfo);
			// sheet数量加一
			sheetSize++;
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			// 在解析完毕一个sheet后，设置列的数量，数量为头的列数
			sheetInfo.setColumn(reflexVO.getListHeader().size());
			// 设置错误、空白、重复数量
			sheetInfo.setBlankLineSize(sheetInfo.getBlankLine().size());
			sheetInfo.setErrorLineSize(sheetInfo.getErrorLine().size());
			sheetInfo.setRepeatLineSize(sheetInfo.getRepeatLine().size());
			// 在解析完毕后，将sheetInfo加入到act中
			reflexVO.getAct().addSheets(reflexVO.getSheetInfo());
			// 下一个sheet操作
		}
		// 全部解析完毕后，设置sheet数量
		reflexVO.getAct().setSheetSize(sheetSize);
		// 设置解析结果为true
		reflexVO.getAct().setResult(true);
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst, ReflexVO<T> reflexVO) throws SAXException {
		
		XMLReader parser =
			XMLReaderFactory.createXMLReader(
					"org.apache.xerces.parsers.SAXParser"
			);
		ContentHandler handler = new SheetHandler<T>(sst, reflexVO);
		parser.setContentHandler(handler);
		return parser;
	}

	/**
	 * 封装了每一条记录信息的对象
	 * @author Administrator
	 *
	 */
	private static class ReflexVO<T>{
		
		// 存放头信息
		private List<String> listHeader;
		// 存放行信息
		private List<String> rowInfo;
		// 行数
		private Integer lineNum;
		// act引用
		private ActionContext<T> act;
		// sheetInfo
		private SheetInfo<T> sheetInfo;
		// 去重使用
		private Set<T> distinctSet;
		
		public ReflexVO() {
			this.listHeader = new ArrayList<String>();
			this.rowInfo = new ArrayList<String>();
			this.lineNum = 0;
			this.distinctSet = new HashSet<T>();
		}
		
		public Set<T> getDistinctSet() {
			return distinctSet;
		}

		public SheetInfo<T> getSheetInfo() {
			return sheetInfo;
		}
		public void setSheetInfo(SheetInfo<T> sheetInfo) {
			this.sheetInfo = sheetInfo;
		}
		public ActionContext<T> getAct() {
			return act;
		}
		public void setAct(ActionContext<T> act) {
			this.act = act;
		}
		public List<String> getListHeader() {
			return listHeader;
		}
		public void setListHeader(List<String> listHeader) {
			this.listHeader = listHeader;
		}
		public List<String> getRowInfo() {
			return rowInfo;
		}
		public void setRowInfo(List<String> rowInfo) {
			this.rowInfo = rowInfo;
		}
		public Integer getLineNum() {
			return lineNum;
		}
		public void setLineNum(Integer lineNum) {
			this.lineNum = lineNum;
		}
	}
	
	
	/** 
	 * See org.xml.sax.helpers.DefaultHandler javadocs 
	 */
	private static class SheetHandler<T> extends DefaultHandler {
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;
		private ReflexVO<T> reflexVO;
		// 是否去重
		private boolean distinct;
		// 列索引
		private Integer index;
		// 是否第一行
		private boolean isFirstLine = true;

		ReflexObject<T> reflexObject = new ReflexObject<T>();
		
		private SheetHandler(SharedStringsTable sst, ReflexVO<T> reflexVO) {
			this.sst = sst;
			// 将反射信息对象加入
			this.reflexVO = reflexVO;
			// 赋值是否去重
			this.distinct = reflexVO.getAct().getDistinct();
		}
		
		short formatIndex;
        String formatString;
		String cellStyleStr;
		
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException{		
			//System.out.println(i + name + ":开始");
			// c => cell
			
			// 判断行并设置行数
			if(name.equals("row")) {
				// 求出当前行（真实的当前行数）
				int thisLine = Integer.parseInt(attributes.getValue("r"));
				// 设置实际数据总行数为当前行 - 1（不算表头）
				reflexVO.getSheetInfo().setLine(thisLine - 1);
				// 对比当前行和上一行来判断中间手否有空行
				int range = thisLine - reflexVO.getLineNum() - 1;	// 当前行 - 上一行 - 1 正常应该等于0
				if(range > 0) {
					for(int i = 0; i < range; i++) {
						reflexVO.getSheetInfo().addBlankLine(reflexVO.getLineNum() + i + 1);
					}
				}
				
				// 设置当前实际行（包括表头）
				reflexVO.setLineNum(thisLine);
				
				// 如果为表头初始化头列表，否则初始化内容列表，内容列表初始化会替代上次使用的，为全新的
				if(thisLine == 1) {
					isFirstLine = false;
					reflexVO.setListHeader(new ArrayList<String>());
				}else {
					if(isFirstLine) {
						throw new SAXException("No table head！");
					}
					reflexVO.setRowInfo(new ArrayList<String>());
				}
			}

			if(name.equals("c")) {
				
				index = nameToColumn(attributes.getValue("r"));
				// Print the cell reference
				//System.out.print(attributes.getValue("r") + " - ");
				// Figure out if the value is an index in the SST
				/*
				 * 日期
				 */
				formatIndex = -1;
		        formatString = null;
				cellStyleStr = attributes.getValue("s");
				if (cellStyleStr != null) {
					int styleIndex = Integer.parseInt(cellStyleStr);
		            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
		            if(style != null) {
		            	formatIndex = style.getDataFormat();
			            formatString = style.getDataFormatString();
		            }
		            // 如果格式中有ymd三种字母，表示为日期格式，将他们转换
		            // System.out.println(formatIndex  + formatString);
		            if (formatIndex == 14 || formatIndex == 31 || formatIndex == 57 || formatIndex == 176 || formatIndex == 178 || formatIndex == 166) {
		                formatString = "yyyy-MM-dd";
		            }else if(formatIndex == 58 || formatIndex == 177){
		            	formatString = "MM月dd日";
		            }else if(formatIndex == 179){
		            	formatString = "yyyy年MM月";
		            }else {
		            	formatString = null;
		            }
		            
		        }
				
				/*
				 * 日期结束
				 */
				String cellType = attributes.getValue("t");
				if(cellType != null && cellType.equals("s")) {
					nextIsString = true;
				} else {
					nextIsString = false;
				}
			}
			// Clear contents cache
			
			lastContents = "";
		}
		
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			//System.out.println(i + name + ":结束");
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if(nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}
			
			// v => contents of a cell
			// Output after we've seen the string contents
			
			if(name.equals("v")) {
				if(reflexVO.getLineNum() == 1) {
					List<String> lh = reflexVO.getListHeader();
					if(lh.size() == index) {
						lh.add(lastContents);
					}else {
						lh.add("");
						lh.add(lastContents);
					}
				}else {
					/*
					 * 日期
					 */
					DataFormatter formatter = new DataFormatter();
		            if (formatString != null) {
		            	try {
			            	double parseDouble = Double.parseDouble(lastContents);
			            	String thisStr = formatter.formatRawCellContents(parseDouble, formatIndex, formatString).trim();
			                lastContents = thisStr;
		            	}catch(NumberFormatException e) {
		            		//e.printStackTrace();
		            	}
		                
		            }

					/*
					 * 日期
					 */
		            
		            // 将不足的cell补齐为""
					List<String> ri = reflexVO.getRowInfo();
					
					if(ri.size() == index) {
						ri.add(lastContents);
					}else {
						// 算出要循环的次数
						int allTime = index-ri.size();
						for(int i=0;i < allTime;i++) {
							ri.add("");
						}
						ri.add(lastContents);
					}
				}
				//System.out.println("--->:" + lastContents);
			}
			
			// 将数据进行反射，第一行是头，不处理
			if(name.equals("row") && reflexVO.getLineNum() != 1) {
				// 将数据反射保存
				List<String> listHeader = reflexVO.getListHeader();
				List<String> rowInfo = reflexVO.getRowInfo();
				boolean isAllEmpty = true;
				
				// 补齐之前首先判断list中是否本身就为空
				for(String x : rowInfo) {
					if(x != null && !x.trim().equals("")) {
						isAllEmpty = false;
						break;
					}
				}
				if(isAllEmpty) {
					// 重置列索引,直接返回
					index = 0;
					SheetInfo<T> sheetInfo = reflexVO.getSheetInfo();
					sheetInfo.addBlankLine(reflexVO.getLineNum());
					return;
				}
				
				// 如果内容列表数量小于头列表数量，则补充""（这个是判断是否和头的 长度一致，不一致补""）
				int range = (rowInfo.size() - listHeader.size()) * -1;
				if(range > 0) {
					for(int i = 0; i < range; i++) {
						rowInfo.add("");
					}
				}
				// 调用反射方法
//				T obj = null;
				T obj = (T)reflexObject.getReflexObject(reflexVO.getAct().getObjects(), 
						listHeader, 
						rowInfo, 
						reflexVO.getSheetInfo().getSheetName(), 
						reflexVO.getLineNum());
				
				// 如果反射中未出现问题，保留信息进列表
				if(obj != null) {
					boolean flag = true;
					// 在此处去重,如果为true
					if(distinct) {
						boolean add = reflexVO.getDistinctSet().add(obj);
						// 如果等于false,添加失败，即相等，则不添加进集合，做重复处理
						if(!add) {
							// 将重复的记录下来,设置行数
							reflexVO.getSheetInfo().addRepeatLine(reflexVO.getLineNum());
							// 重复不执行后续操作
							flag = false;
						}
					}
					
					if(flag) {
						reflexVO.getSheetInfo().getInfo().add(obj);
						Integer correctLine = reflexVO.getSheetInfo().getCorrectLine() + 1;
						reflexVO.getSheetInfo().setCorrectLine(correctLine);
					}
					
				}
				
				if(obj == null) {
					reflexVO.getSheetInfo().addErrorLine(reflexVO.getLineNum());
				}
				
				// 一行结束，重置列索引
				index = 0;
				
				// 将行信息列表清空
				
				// 将行号清空
				
				
			}
			
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			lastContents += new String(ch, start, length);
		}
	}
	
	private static int nameToColumn(String name) {
		name = name.replaceAll("\\d+","");
	    int column = -1;    
	    for (int i = 0; i < name.length(); ++i) {    
	        int c = name.charAt(i);    
	        column = (column + 1) * 26 + c - 'A';
	    }    
	    return column;    
	}

}