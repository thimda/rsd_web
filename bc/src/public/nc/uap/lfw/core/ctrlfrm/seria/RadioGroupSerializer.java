package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

public class RadioGroupSerializer extends BaseSerializer<RadioGroupComp> implements IViewZone {

	@Override
	public void deSerialize(RadioGroupComp radioGroupcomp, Document doc, Element parentNode) {
		Element radioGroupNode = doc.createElement("RadioGroupComp");
		parentNode.appendChild(radioGroupNode);
		radioGroupNode.setAttribute("id", radioGroupcomp.getId());
		radioGroupNode.setAttribute("visible", String.valueOf(radioGroupcomp.isVisible()));
		radioGroupNode.setAttribute("editorType", radioGroupcomp.getEditorType());
		if(isNotNullString(radioGroupcomp.getText()))
			radioGroupNode.setAttribute("text", radioGroupcomp.getText());
		if(isNotNullString(radioGroupcomp.getI18nName()))
			radioGroupNode.setAttribute("i18nName", radioGroupcomp.getI18nName());
		if(isNotNullString(radioGroupcomp.getLangDir()))
			radioGroupNode.setAttribute("langDir", radioGroupcomp.getLangDir());
		if(isNotNullString(radioGroupcomp.getTextAlign()))
			radioGroupNode.setAttribute("textAlign", radioGroupcomp.getTextAlign());
		radioGroupNode.setAttribute("focus", String.valueOf(radioGroupcomp.isFocus()));
		if(isNotNullString(radioGroupcomp.getComboDataId()))
			radioGroupNode.setAttribute("comboDataId", radioGroupcomp.getComboDataId());
		if(isNotNullString(radioGroupcomp.getValue()))
			radioGroupNode.setAttribute("value", radioGroupcomp.getValue());
		if(isNotNullString(radioGroupcomp.getContextMenu()))
			radioGroupNode.setAttribute("contextMenu", radioGroupcomp.getContextMenu());
		radioGroupNode.setAttribute("textWidth", String.valueOf(radioGroupcomp.getTextWidth()));
//		if(isNotNullString(radioGroupcomp.getWidth()))
//			radioGroupNode.setAttribute("width", radioGroupcomp.getWidth());
//		if(isNotNullString(radioGroupcomp.getHeight()))
//			radioGroupNode.setAttribute("height", radioGroupcomp.getHeight());
//		if(isNotNullString(radioGroupcomp.getLeft()))
//			radioGroupNode.setAttribute("left", radioGroupcomp.getLeft());
		radioGroupNode.setAttribute("tabIndex", String.valueOf(radioGroupcomp.getTabIndex()));
		radioGroupNode.setAttribute("sepWidth", String.valueOf(radioGroupcomp.getSepWidth()));
		radioGroupNode.setAttribute("index", String.valueOf(radioGroupcomp.getIndex()));
		radioGroupNode.setAttribute("enabled", String.valueOf(radioGroupcomp.isEnabled()));
//		if(isNotNullString(radioGroupcomp.getPosition()))
//			radioGroupNode.setAttribute("position", radioGroupcomp.getPosition());
//		if(isNotNullString(radioGroupcomp.getClassName()))
//			radioGroupNode.setAttribute("className", radioGroupcomp.getClassName());
		Map<String, JsListenerConf> jsListeners =radioGroupcomp.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), radioGroupNode);
		
		//Events
		AMCUtil.addEvents(doc, radioGroupcomp.getEventConfs(), radioGroupNode);

	}

	@Override
	public void serialize(Digester digester) {
		String radioGroupCompClassName = RadioGroupComp.class.getName();
		digester.addObjectCreate("Widget/Components/RadioGroupComp", radioGroupCompClassName);
		digester.addSetProperties("Widget/Components/RadioGroupComp");
		digester.addSetNext("Widget/Components/RadioGroupComp", "addComponent", radioGroupCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/RadioGroupComp/Listeners", RadioGroupComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/RadioGroupComp", RadioGroupComp.class);		
	}

}
