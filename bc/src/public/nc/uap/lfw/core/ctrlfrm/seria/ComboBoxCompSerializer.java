package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ComboBoxCompSerializer extends BaseSerializer<ComboBoxComp> implements IViewZone {

	@Override
	public void deSerialize(ComboBoxComp combocomp, Document doc, Element parentNode) {
		Element comboNode = doc.createElement("ComBoBoxComp");
		parentNode.appendChild(comboNode);
		comboNode.setAttribute("id", combocomp.getId());
		comboNode.setAttribute("visible", String.valueOf(combocomp.isVisible()));
		comboNode.setAttribute("editorType", combocomp.getEditorType());
		if(isNotNullString(combocomp.getText()))
			comboNode.setAttribute("text", combocomp.getText());
		if(isNotNullString(combocomp.getI18nName()))
			comboNode.setAttribute("i18nName", combocomp.getI18nName());
		if(isNotNullString(combocomp.getContextMenu()))
			comboNode.setAttribute("contextMenu", combocomp.getContextMenu());	
//		if(isNotNullString(combocomp.getTop()))
//			comboNode.setAttribute("top", combocomp.getTop());	
		if(isNotNullString(combocomp.getTextAlign()))
			comboNode.setAttribute("textAlign", combocomp.getTextAlign());
		if(isNotNullString(String.valueOf(combocomp.getTextWidth())))
			comboNode.setAttribute("textWidth", String.valueOf(combocomp.getTextWidth()));
		comboNode.setAttribute("focus", String.valueOf(combocomp.isFocus()));
		if(isNotNullString(combocomp.getLangDir()))
			comboNode.setAttribute("langDir", combocomp.getLangDir());
//		if(isNotNullString(combocomp.getWidth()))
//			comboNode.setAttribute("width", combocomp.getWidth());
		if(isNotNullString(combocomp.getValue()))
			comboNode.setAttribute("value", combocomp.getValue());
//		if(isNotNullString(combocomp.getClassName()))
//			comboNode.setAttribute("className", combocomp.getClassName());
//		if(isNotNullString(combocomp.getHeight()))
//			comboNode.setAttribute("height", combocomp.getHeight());
		if(isNotNullString(combocomp.getRefComboData()))
			comboNode.setAttribute("refComboData", combocomp.getRefComboData());
//		if(isNotNullString(combocomp.getLeft()))
//			comboNode.setAttribute("left", combocomp.getLeft());
//		if(isNotNullString(combocomp.getPosition()))
//			comboNode.setAttribute("position", combocomp.getPosition());
		comboNode.setAttribute("selectOnly", String.valueOf(combocomp.isSelectOnly()));
		comboNode.setAttribute("enabled", String.valueOf(combocomp.isEnabled()));
		comboNode.setAttribute("allowExtendValue", String.valueOf(combocomp.isAllowExtendValue()));
		Map<String, JsListenerConf> jsListeners = combocomp.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), comboNode);
		
		//Events
		AMCUtil.addEvents(doc, combocomp.getEventConfs(), comboNode);
	}

	@Override
	public void serialize(Digester digester) {
		String comboboxCompClassName = ComboBoxComp.class.getName(); 
		digester.addObjectCreate("Widget/Components/ComBoBoxComp", comboboxCompClassName);
		digester.addSetProperties("Widget/Components/ComBoBoxComp");
		digester.addSetNext("Widget/Components/ComBoBoxComp", "addComponent",
				comboboxCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/ComBoBoxComp/Listeners", ComboBoxComp.class);
		EventConfParser.parseEvents(digester, "Widget/Components/ComBoBoxComp", ComboBoxComp.class);
	}

}
