package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ButtonSerializer extends BaseSerializer<ButtonComp> implements IViewZone{

	@Override
	public void deSerialize(ButtonComp btn, Document doc, Element parentNode) {
		Element btnNode = doc.createElement("ButtonComp");
		parentNode.appendChild(btnNode);
		btnNode.setAttribute("id", btn.getId());
		btnNode.setAttribute("enabled",String.valueOf(btn.isEnabled()));
		btnNode.setAttribute("visible",String.valueOf(btn.isVisible()));
		if(isNotNullString(btn.getContextMenu()))
			btnNode.setAttribute("contextMenu", btn.getContextMenu());
		if(isNotNullString(btn.getLangDir()))
			btnNode.setAttribute("langDir", btn.getLangDir());
		if(isNotNullString(btn.getTip()))
			btnNode.setAttribute("tip", btn.getTip());
		if(isNotNullString(btn.getTipI18nName()))
			btnNode.setAttribute("tipI18nName", btn.getTipI18nName());
		if(isNotNullString(btn.getText()))
			btnNode.setAttribute("text", btn.getText());

		if(isNotNullString(btn.getHotKey()))
			btnNode.setAttribute("hotKey", btn.getHotKey());
		if(isNotNullString(btn.getDisplayHotKey()))
			btnNode.setAttribute("displayHotKey", btn.getDisplayHotKey());
		if(isNotNullString(btn.getI18nName()))
			btnNode.setAttribute("i18nName", btn.getI18nName());
		if(isNotNullString(btn.getRefImg()))
			btnNode.setAttribute("refImg", btn.getRefImg());
		if(isNotNullString(btn.getHotKey()))
			btnNode.setAttribute("hotKey", btn.getHotKey());
		if(isNotNullString(btn.getDisplayHotKey()))
			btnNode.setAttribute("displayHotKey", btn.getDisplayHotKey());
		Map<String, JsListenerConf> jsListeners = btn.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), btnNode);
		
		//Events
		AMCUtil.addEvents(doc, btn.getEventConfs(), btnNode);
	}

	@Override
	public void serialize(Digester digester) {
		String buttonClassName = ButtonComp.class.getName();
		digester.addObjectCreate("Widget/Components/ButtonComp", buttonClassName);
		digester.addSetProperties("Widget/Components/ButtonComp");
		digester.addSetNext("Widget/Components/ButtonComp", "addComponent",
				buttonClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/ButtonComp/Listeners", ButtonComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/ButtonComp", ButtonComp.class);
	}
}
