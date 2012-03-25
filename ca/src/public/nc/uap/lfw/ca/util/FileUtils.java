/**
 * 
 */
package nc.uap.lfw.ca.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author chouhl
 *
 */
public class FileUtils {

	public static String readFileToString(File file) throws IOException{
		if(file != null && file.exists() && file.isFile()){
			InputStreamReader isr = null;
			try{
				isr = new InputStreamReader(new FileInputStream(file));
				char[] chuf = new char[1024];
				StringBuffer content = new StringBuffer();
				int len = 0;
				while((len = isr.read(chuf))!=-1){
					content.append(String.copyValueOf(chuf, 0, len));
				}
				isr.close();
				return content.toString();
			}catch(IOException e){
				if(isr != null){
					isr.close();
				}
				throw e;
			}
		}
		return null;
	}
}
