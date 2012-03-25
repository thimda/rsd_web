package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.CheckboxGroupComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class CheckboxGroupSerializer extends BaseSerializer<CheckboxGroupComp> implements IViewZone {

	@Override
	public void deSerialize(CheckboxGroupComp checkboxGroupcomp, Document doc,
			Element parentNode) {
		Element checkBoxGroupNode = doc.createElement("CheckboxGroupComp");
		parentNode.appendChild(checkBoxGroupNode);
		checkBoxGroupNode.setAttribute("id", checkboxGroupcomp.getId());
		checkBoxGroupNode.setAttribute("visible", String.valueOf(checkboxGroupcomp.isVisible()));
		checkBoxGroupNode.setAttribute("editorType", checkboxGroupcomp.getEditorType());
		checkBoxGroupNode.setAttribute("enabled", String.valueOf(checkboxGroupcomp.isEnabled()));
		if(isNotNullString(checkboxGroupcomp.getText()))
			checkBoxGroupNode.setAttribute("text", checkboxGroupcomp.getText());
		checkBoxGroupNode.setAttribute("textWidth", String.valueOf(checkboxGroupcomp.getTextWidth()));
		if(isNotNullString(checkboxGroupcomp.getI18nName()))
			checkBoxGroupNode.setAttribute("i18nName", checkboxGroupcomp.getI18nName());
		if(isNotNullString(checkboxGroupcomp.getLangDir()))
			checkBoxGroupNode.setAttribute("langDir", checkboxGroupcomp.getLangDir());
		if(isNotNullString(checkboxGroupcomp.getTextAlign()))
			checkBoxGroupNode.setAttribute("textAlign", checkboxGroupcomp.getTextAlign());
//		if(isNotNullString(checkboxGroupcomp.getWidth()))
//			checkBoxGroupNode.setAttribute("width", checkboxGroupcomp.getWidth());
//		if(isNotNullString(checkboxGroupcomp.getHeight()))
//			checkBoxGroupNode.setAttribute("height", checkboxGroupcomp.getHeight());
		checkBoxGroupNode.setAttribute("focus", String.valueOf(checkboxGroupcomp.isFocus()));
		if(isNotNullString(checkboxGroupcomp.getComboDataId()))
			checkBoxGroupNode.setAttribute("comboDataId", checkboxGroupcomp.getComboDataId());
//		if(isNotNullString(checkboxGroupcomp.getTop()))
//			checkBoxGroupNode.setAttribute("top", checkboxGroupcomp.getTop());
//		if(isNotNullString(checkboxGroupcomp.getLeft()))
//			checkBoxGroupNode.setAttribute("left", checkboxGroupcomp.getLeft());
		if(isNotNullString(checkboxGroupcomp.getContextMenu()))
			checkBoxGroupNode.setAttribute("contextMenu", checkboxGroupcomp.getContextMenu());
		if(isNotNullString(checkboxGroupcomp.getValue()))
			checkBoxGroupNode.setAttribute("value", checkboxGroupcomp.getValue());
//		if(isNotNullString(checkboxGroupcomp.getPosition()))
//			checkBoxGroupNode.setAttribute("position", checkboxGroupcomp.getPosition());
		checkBoxGroupNode.setAttribute("tabIndex", String.valueOf(checkboxGroupcomp.getTabIndex()));
		checkBoxGroupNode.setAttribute("sepWidth", String.valueOf(checkboxGroupcomp.getSepWidth()));
//		if(isNotNullString(checkboxGroupcomp.getClassName()))
//			checkBoxGroupNode.setAttribute("className", checkboxGroupcomp.getClassName());
		Map<String, JsListenerConf> jsListeners = checkboxGroupcomp.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), checkBoxGroupNode);
		
		//Events
		AMCUtil.addEvents(doc, checkboxGroupcomp.getEventConfs(), checkBoxGroupNode);
	}

	@Override
	public void serialize(Digester digester) {
		String checkboxGroupCompClassName = CheckboxGroupComp.class.getName();
		digester.addObjectCreate("Widget/Components/CheckboxGroupComp", checkboxGroupCompClassName);
		digester.addSetProperties("Widget/Components/CheckboxGroupComp");
		digester.addSetNext("Widget/Components/CheckboxGroupComp", "addComponent", checkboxGroupCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/CheckboxGroupComp/Listeners", CheckboxGroupComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/CheckboxGroupComp", CheckboxGroupComp.class);
	}

}
