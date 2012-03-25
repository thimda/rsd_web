package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ContextMenuSerializer extends MenuBaseSerializer<ContextMenuComp> implements IViewZone{
	@Override
	public void deSerialize(ContextMenuComp contextMenu, Document doc, Element parentNode) {
		Element contextMenuNode = doc.createElement("ContextMenuComp");
		parentNode.appendChild(contextMenuNode);
		if(isNotNullString( contextMenu.getId()))
			contextMenuNode.setAttribute("id", contextMenu.getId());
		if(isNotNullString(contextMenu.getContextMenu()))
			contextMenuNode.setAttribute("contextMenu", contextMenu.getContextMenu());
	
		Map<String, JsListenerConf> listenerMap = contextMenu.getListenerMap();
		if(listenerMap != null && !listenerMap.isEmpty())
		{
			JsListenerConf[] listeners = listenerMap.values().toArray(new JsListenerConf[0]);
			PersistenceUtil.addListeners(doc, listeners, contextMenuNode);
		}
		
		//Events
		AMCUtil.addEvents(doc, contextMenu.getEventConfs(), contextMenuNode);
		
		List<MenuItem> menuItems = contextMenu.getItemList();
		if(menuItems != null && menuItems.size() > 0)
			processMenuItem(menuItems.toArray(new MenuItem[0]),contextMenuNode, doc);
			
	}


	@Override
	public void serialize(Digester digester) {
		String contextMenuClassName = ContextMenuComp.class.getName();
		//Ω‚ŒˆMenuBarComp
		digester.addObjectCreate("Widget/Menus/ContextMenuComp", contextMenuClassName);
		digester.addSetProperties("Widget/Menus/ContextMenuComp");
		
		ListenersParser.parseListeners(digester, "Widget/Menus/ContextMenuComp/Listeners", ContextMenuComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Menus/ContextMenuComp", ContextMenuComp.class);
		
		String menuItemClassName = MenuItem.class.getName();
		String basePath = "Widget/Menus/ContextMenuComp";
		for (int i = 0; i < 7; i++) {
			basePath += "/MenuItem";
			digester.addObjectCreate(basePath, menuItemClassName);
			digester.addSetProperties(basePath);
			ListenersParser.parseListeners(digester, basePath + "/Listeners", MenuItem.class);
			
			EventConfParser.parseEvents(digester, basePath, MenuItem.class);
			
			digester.addSetNext(basePath, "addMenuItem", menuItemClassName);
		}
		digester.addSetNext("Widget/Menus/ContextMenuComp", "addContextMenu", contextMenuClassName);
	}

}
