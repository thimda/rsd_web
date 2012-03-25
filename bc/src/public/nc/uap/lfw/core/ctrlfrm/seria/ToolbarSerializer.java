package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.ToolBarTitle;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ToolbarSerializer extends BaseSerializer<ToolBarComp> implements IViewZone{

	@Override
	public void deSerialize(ToolBarComp toolbar, Document doc, Element parentNode) {
		Element toolbarNode = doc.createElement("ToolBarComp");
		parentNode.appendChild(toolbarNode);
		toolbarNode.setAttribute("id", toolbar.getId());
//		if(isNotNullString(toolbar.getWidth()))
//			toolbarNode.setAttribute("width", toolbar.getWidth());
//		if(isNotNullString(toolbar.getHeight()))
//			toolbarNode.setAttribute("height", toolbar.getHeight());
//		if(isNotNullString(toolbar.getTop()))
//			toolbarNode.setAttribute("top", toolbar.getTop());
//		if(isNotNullString(toolbar.getLeft()))
//			toolbarNode.setAttribute("left", toolbar.getLeft());
		toolbarNode.setAttribute("enabled", String.valueOf(toolbar.isEnabled()));
		toolbarNode.setAttribute("visible", String.valueOf(toolbar.isVisible()));
		toolbarNode.setAttribute("transparent",String.valueOf(toolbar.isTransparent()));
		if(isNotNullString(toolbar.getContextMenu()))
			toolbarNode.setAttribute("contextMenu", toolbar.getContextMenu());
//		if(isNotNullString(toolbar.getClassName()))
//			toolbarNode.setAttribute("className", toolbar.getClassName());
		if(isNotNullString(toolbar.getContextMenu()))
			toolbarNode.setAttribute("contextMenu", toolbar.getContextMenu());
//		if(isNotNullString(toolbar.getPosition()))
//			toolbarNode.setAttribute("position", toolbar.getPosition());
		ToolBarItem[] eles = toolbar.getElements();
		if(eles != null && eles.length > 0)
		{
			for(int j = 0; j < eles.length; j++)
			{
				ToolBarItem ele = eles[j];
				Element toolbarEleNode = doc.createElement("ToolBarItem");
				toolbarNode.appendChild(toolbarEleNode);
				toolbarEleNode.setAttribute("id", ele.getId());
				if(isNotNullString(ele.getText()))
					toolbarEleNode.setAttribute("text", ele.getText());
//				if(isNotNullString(ele.getWidth()))
//					toolbarEleNode.setAttribute("width", ele.getWidth());
				if(isNotNullString(ele.getRefImg()))
					toolbarEleNode.setAttribute("refImg", ele.getRefImg());
				if(isNotNullString(ele.getI18nName()))
					toolbarEleNode.setAttribute("i18nName", ele.getI18nName());
				if(isNotNullString(ele.getLangDir()))
					toolbarEleNode.setAttribute("langdir", ele.getLangDir());
				if(isNotNullString(ele.getTip()))
					toolbarEleNode.setAttribute("tip", ele.getTip());
				if(isNotNullString(ele.getTipI18nName()))
					toolbarEleNode.setAttribute("tipI18nName", ele.getTipI18nName());
				if(isNotNullString(ele.getAlign()))
					toolbarEleNode.setAttribute("align", ele.getAlign());
				toolbarEleNode.setAttribute("modifiers", String.valueOf(ele.getModifiers()));
				if(isNotNullString(ele.getHotKey()))
					toolbarEleNode.setAttribute("hotKey", ele.getHotKey());
				if(isNotNullString(ele.getDisplayHotKey()))
					toolbarEleNode.setAttribute("displayHotKey", ele.getDisplayHotKey());
				if(isNotNullString(ele.getLangDir()))
					toolbarEleNode.setAttribute("langDir", ele.getLangDir());
				toolbarEleNode.setAttribute("type", ele.getType());
				toolbarEleNode.setAttribute("withSep", String.valueOf(ele.isWithSep()));
				Map<String, JsListenerConf> jsListeners = ele.getListenerMap();
				if(jsListeners != null && jsListeners.size() > 0)
					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), toolbarEleNode);
				
				//Events
				AMCUtil.addEvents(doc, ele.getEventConfs(), toolbarEleNode);
			}
		}
		ToolBarTitle title = toolbar.getTitle();
		if(title != null){
			Element toolbarTitelNode = doc.createElement("ToolBarTitle");
			toolbarNode.appendChild(toolbarTitelNode);
			if(isNotNullString(title.getText()))
				toolbarTitelNode.setAttribute("text", title.getText());
			if(isNotNullString(title.getRefImg1()))
				toolbarTitelNode.setAttribute("refImg1", title.getRefImg1());
			if(isNotNullString(title.getRefImg2()))
				toolbarTitelNode.setAttribute("refImg2", title.getRefImg2());
			if(isNotNullString(title.getI18nName()))
				toolbarTitelNode.setAttribute("i18nName", title.getI18nName());
			if(isNotNullString(title.getLangDir()))
				toolbarTitelNode.setAttribute("langDir", title.getLangDir());
			if(isNotNullString(title.getColor()))
				toolbarTitelNode.setAttribute("color", title.getColor());
			if(isNotNullString(title.getMenuId()))
				toolbarTitelNode.setAttribute("menuId", title.getMenuId());
			toolbarTitelNode.setAttribute("bold", String.valueOf(title.isBold()));
		}
	}


	@Override
	public void serialize(Digester digester) {
		String toolbarClassName = ToolBarComp.class.getName();
		//ToolbarComp解析
		digester.addObjectCreate("Widget/Components/ToolBarComp", toolbarClassName);
		digester.addSetProperties("Widget/Components/ToolBarComp");
		
		
		String titleClassName = ToolBarTitle.class.getName();
		//toolbar的title解析
		digester.addObjectCreate("Widget/Components/ToolBarComp/ToolBarTitle",
				titleClassName);
		digester.addSetProperties("Widget/Components/ToolBarComp/ToolBarTitle");
		digester.addSetNext("Widget/Components/ToolBarComp/ToolBarTitle", "setTitle",
				titleClassName);
//		digester.addSetNext("Widget/Components/ToolBarComp", "addComponent",
//				toolbarClassName);
		
		String elementClassName = ToolBarItem.class.getName();
		//toolbar的Element，Event解析
		digester.addObjectCreate("Widget/Components/ToolBarComp/ToolBarItem",
				elementClassName);
		digester.addSetProperties("Widget/Components/ToolBarComp/ToolBarItem");
		digester.addSetNext("Widget/Components/ToolBarComp/ToolBarItem", "addElement",
				elementClassName);
		digester.addSetNext("Widget/Components/ToolBarComp", "addComponent",
				toolbarClassName);
		
		ListenersParser.parseListeners(digester, "Widget/Components/ToolBarComp/ToolBarItem/Listeners", ToolBarItem.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/ToolBarComp/ToolBarItem", ToolBarItem.class);
	}

}
