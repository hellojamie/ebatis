package com.mshuoke.ebatis.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.mshuoke.ebatis.api.DataHandleAction;
import com.mshuoke.ebatis.emnu.FileType;
import com.mshuoke.ebatis.exception.FileSizeErrorException;
import com.mshuoke.ebatis.exception.FileTypeErrorException;
import com.mshuoke.ebatis.pojo.ActionContext;
import com.mshuoke.ebatis.util.CheckFileType;

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
		ByteArrayInputStream inputStream = null;
		
		try {
			type = CheckFileType.getType(act.getInputStream());
			// 如果类型不存在，抛出
			if(type == null){
				throw new FileTypeErrorException("This file type is error, Not's xsl or xslx");
			}
			
			// 设置文件类型
			act.setFileType(type);
			
			// 判断文件大小 大于10M（10485760）切为xlsx的，使用sax处理
			inputStream = new ByteArrayInputStream(act.getByteArrayOutputStream().toByteArray());
			int available = inputStream.available();
			if(available > 1 && type == FileType.XLSX){
				act.setUseSax(true);
				// throw new FileSizeErrorException("The file size error max: 31457280 you file size: " + available);
			}
			act.setFileSizeByte(available);
			
			// 下一链操作
			commit(act);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileTypeErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
				try {
					if(inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					rollback(act);
				}
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
