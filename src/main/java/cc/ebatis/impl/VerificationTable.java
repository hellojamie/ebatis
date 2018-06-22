package cc.ebatis.impl;

import java.io.IOException;
import java.io.InputStream;

import cc.ebatis.api.DataHandleAction;
import cc.ebatis.emnu.FileType;
import cc.ebatis.exception.FileTypeErrorException;
import cc.ebatis.pojo.ActionContext;
import cc.ebatis.util.CheckFileType;

/**
 * 
 * 验证表格类型及大小
 * @author 杨硕
 *
 */
public class VerificationTable<T> implements DataHandleAction<T>{

	AnalysisExcel<T> analysisExcel = new AnalysisExcel<T>();
	
	@Override
	public void prepare(ActionContext<T> act) {
		
		// 获取文件格式是否正确，文件大小是否超量
		FileType type = null;
		InputStream inputStream = null;
		try {
			type = CheckFileType.getType(act.getFile());
			// 如果类型不存在，抛出
			if(type == null){
				throw new FileTypeErrorException("This file type is error, Not's xsl or xslx");
			}
			
			// 设置文件类型
			act.setFileType(type);
			
			// 判断文件大小 大于10M（10485760）切为xlsx的，使用sax处理
			inputStream = act.getInputStream();
			int available = inputStream.available();
			
			if(type == FileType.XLSX){
				act.setUseSax(true);
			}
			act.setFileSizeByte(available);
			
			// 下一链操作
			commit(act);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FileTypeErrorException e) {
			e.printStackTrace();
		} finally{
			try {
				if(inputStream != null) {
					inputStream.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputStream = null;
		}
		
	}

	@Override
	public boolean commit(ActionContext<T> act) {

		analysisExcel.prepare(act);
		
		return true;
	}

	@Override
	public boolean rollback(ActionContext<T> act) {
		
		act.setResult(false);
		
		return false;
	}
	
}