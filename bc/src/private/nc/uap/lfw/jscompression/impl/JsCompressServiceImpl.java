package nc.uap.lfw.jscompression.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.jscompression.itf.IJsCompressionService;
import nc.uap.lfw.jsutil.jstools.CompressorUtil;

/**
 * JS代码压缩服务类
 * 
 * @author gd 2010-4-23
 * 
 */
public class JsCompressServiceImpl implements IJsCompressionService {

	@Override
	public String compressCss(String[] cssFiles) {
		InputStream[] inputs = null;
		try{
			inputs = new FileInputStream[cssFiles.length];
			for (int i = 0; i < cssFiles.length; i++) {
				String filePath = cssFiles[i];
				inputs[i] = new FileInputStream(filePath);
			}
			return CompressorUtil.compressCss(inputs);
		}
		catch(Exception e){
			LfwLogger.error(e);
		}
		finally{
			if(inputs != null){
				for (int i = 0; i < inputs.length; i++) {
					try {
						inputs[i].close();
					} 
					catch (IOException e) {
						LfwLogger.error(e);
					}
				}
			}
		}
		return null;
	}

	@Override
	public String compressJs(String[] jsFiles) {
		InputStream[] inputs = null;
		try{
			inputs = new FileInputStream[jsFiles.length];
			for (int i = 0; i < jsFiles.length; i++) {
				String filePath = jsFiles[i];
				File file = ContextResourceUtil.getLfwFile(filePath);
				inputs[i] = new FileInputStream(file);
			}
			return CompressorUtil.compressJs(inputs);
		}
		catch(Exception e){
			LfwLogger.error(e);
		}
		finally{
			if(inputs != null){
				for (int i = 0; i < inputs.length; i++) {
					try {
						inputs[i].close();
					} 
					catch (IOException e) {
						LfwLogger.error(e);
					}
				}
			}
		}
		return null;
	}
}
