package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;


public class CheckboxSerializer extends BaseSerializer<CheckBoxComp> implements IViewZone {

	@Override
	public void deSerialize(CheckBoxComp checkbox, Document doc, Element parentNode) {
		Element checkboxNode = doc.createElement("CheckBoxComp");
		parentNode.appendChild(checkboxNode);
		checkboxNode.setAttribute("id", checkbox.getId());
		checkboxNode.setAttribute("visible", String.valueOf(checkbox.isVisible()));
		checkboxNode.setAttribute("editorType", checkbox.getEditorType());
		if(isNotNullString(checkbox.getI18nName()))
			checkboxNode.setAttribute("i18nName", checkbox.getI18nName());
		if(isNotNullString(checkbox.getTextAlign()))
			checkboxNode.setAttribute("textAlign", checkbox.getTextAlign());
		if(isNotNullString(String.valueOf(checkbox.getTextWidth())))
			checkboxNode.setAttribute("textWidth", String.valueOf(checkbox.getTextWidth()));
		checkboxNode.setAttribute("focus", String.valueOf(checkbox.isFocus()));
		if(isNotNullString(checkbox.getValue()))
			checkboxNode.setAttribute("value", checkbox.getValue());
		if(isNotNullString(checkbox.getContextMenu()))
			checkboxNode.setAttribute("contextMenu", checkbox.getContextMenu());
		if(isNotNullString(checkbox.getText()))
			checkboxNode.setAttribute("text", checkbox.getText());
		if(isNotNullString(checkbox.getLangDir()))
			checkboxNode.setAttribute("langDir", checkbox.getLangDir());
//		if(isNotNullString(checkbox.getLeft()))
//			checkboxNode.setAttribute("left", checkbox.getLeft());
		if(isNotNullString(checkbox.getDataType()))
			checkboxNode.setAttribute("dataType", checkbox.getDataType());
//		if(isNotNullString(checkbox.getClassName()))
//			checkboxNode.setAttribute("className", checkbox.getClassName());
//		if(isNotNullString(checkbox.getPosition()))
//			checkboxNode.setAttribute("position", checkbox.getPosition());
//		if(isNotNullString(checkbox.getWidth()))
//			checkboxNode.setAttribute("width", checkbox.getWidth());
		checkboxNode.setAttribute("enabled", String.valueOf(checkbox.isEnabled()));
		checkboxNode.setAttribute("checked", String.valueOf(checkbox.isChecked()));
		Map<String, JsListenerConf> jsListeners = checkbox.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), checkboxNode);
		
		//Events
		AMCUtil.addEvents(doc, checkbox.getEventConfs(), checkboxNode);
		
	}

	@Override
	public void serialize(Digester digester) {
		String checkboxCompClassName = CheckBoxComp.class.getName();
		digester.addObjectCreate("Widget/Components/CheckBoxComp", checkboxCompClassName);
		digester.addSetProperties("Widget/Components/CheckBoxComp");
		digester.addSetNext("Widget/Components/CheckBoxComp", "addComponent", checkboxCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/CheckBoxComp/Listeners", CheckBoxComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/CheckBoxComp", CheckBoxComp.class);		
	}

}
