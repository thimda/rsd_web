package nc.uap.lfw.jsp.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import nc.uap.lfw.core.log.LfwLogger;

public class TestParser {
	public static void main(String[] args){
		FileInputStream input;
		try {
			input = new FileInputStream("d:\\uimeta.um");
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			StringBuffer buf = new StringBuffer();
			String str = reader.readLine();
			while(str != null){
				buf.append(str);
				str = reader.readLine();
			}
			String jsp = new UIMetaConvertUtil().convert2Jsp(buf.toString());
		} 
		catch (FileNotFoundException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		//new UIMetaParserUtil().parseUIMeta(source);
		catch (UnsupportedEncodingException e) {
			LfwLogger.error(e.getMessage(), e);
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}
}
