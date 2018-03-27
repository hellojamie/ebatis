package org.ys.diamonds.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.ys.diamonds.api.DataHandleAction;
import org.ys.diamonds.emnu.FileType;
import org.ys.diamonds.exception.FileSizeErrorException;
import org.ys.diamonds.exception.FileTypeErrorException;
import org.ys.diamonds.pojo.ActionContext;
import org.ys.diamonds.util.CheckFileType;

/**
 * 
 * 验证表格类型及大小
 * @author 杨硕
 *
 */
public class VerificationTable<T> implements DataHandleAction{

	AnalysisExcel<T> analysisExcel = new AnalysisExcel<T>();
	
	@Override
	public boolean prepare(ActionContext act) {
		
		boolean b = false;
		
		// 获取文件格式是否正确，文件大小是否超量
		FileType type = null;
		ByteArrayInputStream inputStream = null;
		byte[] buffer;
		int len;
		
		try {
			type = CheckFileType.getType(act.getInputStream());
			// 如果类型不存在，抛出
			if(type == null){
				throw new FileTypeErrorException("This file type is error, Not's xsl or xslx");
			}
			
			// 设置文件类型
			act.setFileType(type);
			
			// 判断文件大小
			inputStream = new ByteArrayInputStream(act.getByteArrayOutputStream().toByteArray());
			int available = inputStream.available();
			if(available > 31457280){
				throw new FileSizeErrorException("The file size error max: 31457280 you file size: " + available);
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
		} catch (FileSizeErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
				try {
					if(inputStream != null)
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return b;
		
	}

	@Override
	public boolean commit(ActionContext act) {

		analysisExcel.prepare(act);
		
		return true;
	}

	@Override
	public boolean rollback(ActionContext act) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
