package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MenuCompSerializer extends MenuBaseSerializer<MenubarComp> implements IViewZone{

	@Override
	public void deSerialize(MenubarComp menubar, Document doc, Element parentNode) {
		Element menubarNode = doc.createElement("MenuBarComp");
		parentNode.appendChild(menubarNode);
		if(isNotNullString(menubar.getId()))
			menubarNode.setAttribute("id", menubar.getId());
		if(isNotNullString(menubar.getContextMenu()))
			menubarNode.setAttribute("contextMenu", menubar.getContextMenu());
		
		Map<String, JsListenerConf> listenerMap = menubar.getListenerMap();
		if(listenerMap != null && !listenerMap.isEmpty()){
			JsListenerConf[] listeners = listenerMap.values().toArray(new JsListenerConf[0]);
			PersistenceUtil.addListeners(doc, listeners, menubarNode);
		}
		
		//Events
		AMCUtil.addEvents(doc, menubar.getEventConfs(), menubarNode);
			
		List<MenuItem> menuItems = menubar.getMenuList();
		if(menuItems != null && menuItems.size() > 0)
			processMenuItem(menuItems.toArray(new MenuItem[0]), menubarNode, doc);
			
	}

	@Override
	public void serialize(Digester digester) {
		String menuBarClassName = MenubarComp.class.getName();
		//Ω‚ŒˆMenuBarComp
		digester.addObjectCreate("Widget/Menus/MenuBarComp", menuBarClassName);
		digester.addSetProperties("Widget/Menus/MenuBarComp");
		digester.addSetNext("Widget/Menus/MenuBarComp", "addMenuBar", menuBarClassName);
		ListenersParser.parseListeners(digester, "Widget/Menus/MenuBarComp/Listeners", MenubarComp.class);
		EventConfParser.parseEvents(digester, "Widget/Menus/MenuBarComp", MenubarComp.class);
		String menuItemClassName = MenuItem.class.getName();
		String basePath = "Widget/Menus/MenuBarComp";
		for (int i = 0; i < 7; i++) {
			basePath += "/MenuItem";
			digester.addObjectCreate(basePath, menuItemClassName);
			digester.addSetProperties(basePath);
			ListenersParser.parseListeners(digester, basePath + "/Listeners", MenuItem.class);
			
			EventConfParser.parseEvents(digester, basePath, MenuItem.class);
			
			digester.addSetNext(basePath, "addMenuItem", menuItemClassName);
		}
	}
}
