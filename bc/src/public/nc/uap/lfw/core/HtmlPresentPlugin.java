package nc.uap.lfw.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.util.LfwClassUtil;

public class HtmlPresentPlugin extends AbstractPresentPlugin {

	public void prepare(HttpServletRequest req, String pagePath,
			String themeId, String templateJsp) throws IOException {
	
	}

	public boolean translate(HttpServletRequest req, String pagePath,
			String themeId, String templateJsp) throws IOException {
		String folderPath = "/html/nodes/" + pagePath;
		targetJsp = folderPath + templateJsp;
		String targetJspCopy = "/" + WEBTEMP + "/" + targetJsp.replace(".html", ".jsp");
		boolean exist = ContextResourceUtil.resourceExist(targetJspCopy);
		if(!exist){
			InputStream sin = null;
			Writer writer = null;
			try{
				sin = ContextResourceUtil.getResourceAsStream(targetJsp);
				if(sin != null){
					BufferedReader reader = new BufferedReader(new InputStreamReader(sin, "UTF-8"));
					StringBuffer buf = new StringBuffer();
					String str = reader.readLine();
					while(str != null){
						buf.append(str);
						str = reader.readLine();
					}
					
					File f = new File(ContextResourceUtil.getCurrentAppPath() + "/" + WEBTEMP + folderPath);
					if(!f.exists())
						f.mkdirs();
					
					Class<?> utilC = LfwClassUtil.forName("nc.lfw.jsp.parser.HtmlConvertUtil");
					Method m = utilC.getDeclaredMethod("convert2Jsp", new Class[]{String.class});
					Object util = LfwClassUtil.newInstance(utilC);
					String jsp = (String) m.invoke(util, buf.toString());
					writer = ContextResourceUtil.getOutputStream(targetJspCopy);
					writer.write(jsp);
					exist = true;
				}
			}
			catch(Exception e){
				LfwLogger.error(e.getMessage(), e);
			}
			finally{
				try{
					if(sin != null)
						sin.close();
				}
				catch(Exception e){
				}
				try{
					if(writer != null)
						writer.close();
				}
				catch(Exception e){
				}
			}
		}
		targetJsp = targetJspCopy;
		return exist;
	}

}
