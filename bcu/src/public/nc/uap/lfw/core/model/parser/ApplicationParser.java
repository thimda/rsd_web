/**
 * 
 */
package nc.uap.lfw.core.model.parser;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.Application;

import org.apache.commons.digester.Digester;

/**
 * @author chouhl
 *
 */
public class ApplicationParser {
	
	public static Application parse(File xml){
		Application applicationConf = null;
		if(xml.exists() && xml.isFile()){
			try{
				Digester digester = ApplicationXMLRule();
				applicationConf = (Application)digester.parse(xml);
			}catch(Exception e){
				LfwLogger.error(e.getMessage(), e);
			}
		}else{
			LfwLogger.error("ApplicationConf#parse文件不存在或不是文件:" + xml.getAbsolutePath());
		}
		return applicationConf;
	}
	
	public static Digester ApplicationXMLRule(){
		Digester digester = new Digester();
		digester.addObjectCreate(Application.TagName, Application.class);
		digester.addSetProperties(Application.TagName);
		
		String windowTagName = PageMeta.TagName;
		digester.addObjectCreate(Application.TagName + "/" + windowTagName + "s/" + windowTagName, PageMeta.class);
		digester.addSetProperties(Application.TagName + "/" + windowTagName + "s/" + windowTagName);
		digester.addSetNext(Application.TagName + "/" + windowTagName + "s/" + windowTagName, "addWindow");
		
		EventConfParser.parseEvents(digester, Application.TagName, Application.class);
		
		String connectorClazz = Connector.class.getName();
		digester.addObjectCreate("Application/Connectors/Connector", connectorClazz);
		digester.addSetProperties("Application/Connectors/Connector");
		digester.addSetNext("Application/Connectors/Connector", "addConnector");		
		digester.addCallMethod("Application/Connectors/Connector/Maps/Map", "putMapping", 2);
		digester.addCallParam("Application/Connectors/Connector/Maps/Map/outValue", 0);
		digester.addCallParam("Application/Connectors/Connector/Maps/Map/inValue", 1);
		
		return digester;
	}
	
	public static Application parse(String str){
		Application application = null;
		Reader xmlStream = new StringReader(str);
		try{
			Digester digester = ApplicationXMLRule();
			application = (Application)digester.parse(xmlStream);
		}catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
		}finally{
			if(xmlStream != null){
				try {
					xmlStream.close();
					xmlStream = null;
				} catch (Exception e2) {
					LfwLogger.error("关闭流失败！",e2);
				}
			}
		}
		return application;
	}
}
