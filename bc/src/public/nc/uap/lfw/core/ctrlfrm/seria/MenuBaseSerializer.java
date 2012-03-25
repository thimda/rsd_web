package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public abstract class MenuBaseSerializer<T> extends BaseSerializer<T> {
	protected void processMenuItem(MenuItem[] cItems, Element parentNode, Document doc)
	{
		for (int i = 0; i < cItems.length; i++) 
		{
			MenuItem menuItem = cItems[i];
			Element menuItemNode = doc.createElement("MenuItem");
			parentNode.appendChild(menuItemNode);
			if(isNotNullString(menuItem.getId()))
				menuItemNode.setAttribute("id", menuItem.getId());
			if(menuItem.getI18nName() != null && !menuItem.getI18nName().equals(""))
				menuItemNode.setAttribute("i18nName", menuItem.getI18nName());
			if(menuItem.getText() != null && !menuItem.getText().equals(""))
				menuItemNode.setAttribute("text", menuItem.getText());
			if(menuItem.getStateManager() != null && !menuItem.getStateManager().equals(""))
				menuItemNode.setAttribute("stateManager", menuItem.getStateManager());
//			if(menuItem.getOperatorStatusArray() != null && !menuItem.getOperatorStatusArray().equals(""))
//				menuItemNode.setAttribute("operatorStatusArray", menuItem.getOperatorStatusArray());
//			if(menuItem.getBusinessStatusArray() != null && !menuItem.getBusinessStatusArray().equals(""))
//				menuItemNode.setAttribute("businessStatusArray", menuItem.getBusinessStatusArray());
//			if(menuItem.getUserStatusArray() != null && !menuItem.getUserStatusArray().equals(""))
//				menuItemNode.setAttribute("userStatusArray", menuItem.getUserStatusArray());
//			//¿É¼û×´Ì¬±£´æ
//			if(menuItem.getOperatorVisibleStatusArray() != null && !menuItem.getOperatorVisibleStatusArray().equals(""))
//				menuItemNode.setAttribute("operatorVisibleStatusArray", menuItem.getOperatorVisibleStatusArray());
//			if(menuItem.getBusinessVisibleStatusArray() != null && !menuItem.getBusinessVisibleStatusArray().equals(""))
//				menuItemNode.setAttribute("businessVisibleStatusArray", menuItem.getBusinessVisibleStatusArray());
//			if(menuItem.getUserVisibleStatusArray() != null && !menuItem.getUserVisibleStatusArray().equals(""))
//				menuItemNode.setAttribute("userVisibleStatusArray", menuItem.getUserVisibleStatusArray());
//			//
			if(menuItem.getImgIcon() != null && !menuItem.getImgIcon().equals(""))
				menuItemNode.setAttribute("imgIcon", menuItem.getImgIcon());
			if(menuItem.getImgIconOn() != null && !menuItem.getImgIconOn().equals(""))
				menuItemNode.setAttribute("imgIconOn", menuItem.getImgIconOn());
			if(menuItem.getImgIconDisable() != null && !menuItem.getImgIconDisable().equals(""))
				menuItemNode.setAttribute("imgIconDisable", menuItem.getImgIconDisable());
			if(menuItem.getLangDir() != null && !menuItem.getLangDir().equals(""))
				menuItemNode.setAttribute("langDir", menuItem.getLangDir());
			if(isNotNullString(menuItem.getHotKey()))
				menuItemNode.setAttribute("hotKey", menuItem.getHotKey());
			if(isNotNullString(menuItem.getDisplayHotKey()))
				menuItemNode.setAttribute("displayHotKey", menuItem.getDisplayHotKey());
			if(menuItem.isSep())
				menuItemNode.setAttribute("sep", "true");
			menuItemNode.setAttribute("modifiers", String.valueOf(menuItem.getModifiers()));
			Map<String, JsListenerConf> listenerMap = menuItem.getListenerMap();
			if(listenerMap != null && !listenerMap.isEmpty())
			{
				JsListenerConf[] listeners = listenerMap.values().toArray(new JsListenerConf[0]);
				PersistenceUtil.addListeners(doc, listeners, menuItemNode);
			}
			
			//Events
			AMCUtil.addEvents(doc, menuItem.getEventConfs(), menuItemNode);
			
			if(menuItem.getChildList() != null && menuItem.getChildList().size() > 0)
				processMenuItem(menuItem.getChildList().toArray(new MenuItem[0]), menuItemNode, doc);
		}
	}
}
