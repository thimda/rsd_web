/**
 * 
 */
package nc.uap.lfw.core.util;

import nc.uap.lfw.conf.persist.WidgetToXml;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.persistence.PageMetaToXml;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.conf.Model;
import nc.uap.lfw.design.impl.UIMetaToXml;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * 
 * ×ª»»WebElementµ½XML
 * @author chouhl
 *
 */
public class AMCWebElementToXML {

	public static void applicationToXml(String filePath, String fileName, String projectPath, Application appConf){
		ApplicationToXml.toXml(filePath, fileName, projectPath, appConf);
	}
	
	public static void modelToXml(String filePath, String fileName, String projectPath, Model model){
		ModelToXml.toXml(filePath, fileName, projectPath, model);
	}
	
	public static void viewToXml(String filePath, String fileName, String projectPath, String refId){
		WidgetToXml.toXml(filePath, fileName, projectPath, refId);
	}
	
//	public static void viewToXml(String filePath, String fileName, String projectPath, ViewConf viewConf){
//		WidgetToXml.toXml(filePath, fileName, projectPath, viewConf);
//	}
	
	public static void widgetToXml(String filePath, String fileName, String projectPath, LfwWidget widget){
		WidgetToXml.toXml(filePath, fileName, projectPath, widget);
	}
	
	public static void windowToXml(String filePath, String fileName, String projectPath, PageMeta pageMeta){
		PageMetaToXml.toXml(filePath, fileName, projectPath, pageMeta);
	}
	
	public static void createUIMeta(String folderPath, UIMeta meta){
		String fp = folderPath.replaceAll("\\\\", "/");
		String id = fp.substring(fp.lastIndexOf("/") + 1) + "_um";
//		if(meta == null){
//			meta = new UIMeta();
////			meta.setAttribute(UIMeta.ISCHART, 0);
////			meta.setAttribute(UIMeta.ISJQUERY, 0);
////			meta.setAttribute(UIMeta.ISEXCEL, 0);
////			meta.setAttribute(UIMeta.JSEDITOR, 0);
//			meta.setAttribute(UIMeta.ID, id);
//		}else 
		if(meta.getId() == null || meta.getId().trim().length() == 0){
			meta.setId(id);
		}
		UIMetaToXml.toXml(meta, folderPath);
	}
	
}
