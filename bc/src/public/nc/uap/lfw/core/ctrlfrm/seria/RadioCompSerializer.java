package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nc.uap.lfw.core.comp.RadioComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

public class RadioCompSerializer extends BaseSerializer<RadioComp> implements IViewZone {

	@Override
	public void deSerialize(RadioComp radiocomp, Document doc, Element parentNode) {
		Element radioNode = doc.createElement("RadioComp");
		parentNode.appendChild(radioNode);
		radioNode.setAttribute("id", radiocomp.getId());
		radioNode.setAttribute("visible", String.valueOf(radiocomp.isVisible()));
		radioNode.setAttribute("enabled", String.valueOf(radiocomp.isEnabled()));
		radioNode.setAttribute("editorType", radiocomp.getEditorType());
		if(isNotNullString(radiocomp.getText()))
			radioNode.setAttribute("text", radiocomp.getText());
//		if(isNotNullString(radiocomp.getWidth()))
//			radioNode.setAttribute("width", radiocomp.getWidth());
//		if(isNotNullString(radiocomp.getHeight()))
//			radioNode.setAttribute("height", radiocomp.getHeight());
		if(isNotNullString(radiocomp.getI18nName()))
			radioNode.setAttribute("i18nName", radiocomp.getI18nName());
		if(isNotNullString(radiocomp.getLangDir()))
			radioNode.setAttribute("langDir", radiocomp.getLangDir());
		if(isNotNullString(radiocomp.getGroup()))
			radioNode.setAttribute("group", radiocomp.getGroup());
//		if(isNotNullString(radiocomp.getLeft()))
//			radioNode.setAttribute("left", radiocomp.getLeft());
//		if(isNotNullString(radiocomp.getTop()))
//			radioNode.setAttribute("top", radiocomp.getTop());
		if(isNotNullString(radiocomp.getContextMenu()))
			radioNode.setAttribute("contextMenu", radiocomp.getContextMenu());
		if(isNotNullString(radiocomp.getTextAlign()))
			radioNode.setAttribute("textAlign", radiocomp.getTextAlign());
		if(isNotNullString(String.valueOf(radiocomp.getTextWidth())))
			radioNode.setAttribute("textWidth", String.valueOf(radiocomp.getTextWidth()));
//		if(isNotNullString(radiocomp.getPosition()))
//			radioNode.setAttribute("position", radiocomp.getPosition());
		radioNode.setAttribute("focus", String.valueOf(radiocomp.isFocus()));
//		if(isNotNullString(radiocomp.getClassName()))
//			radioNode.setAttribute("className", radiocomp.getClassName());
		if(isNotNullString(radiocomp.getValue()))
			radioNode.setAttribute("value", radiocomp.getValue());
		radioNode.setAttribute("isChecked", String.valueOf(radiocomp.isChecked()));
		if(isNotNullString(radiocomp.getGroup()))
			radioNode.setAttribute("radioGroup", radiocomp.getGroup());
//		if(isNotNullString(radiocomp.getLeft()))
//			radioNode.setAttribute("left", radiocomp.getLeft());
		Map<String, JsListenerConf> jsListeners = radiocomp.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), radioNode);
		
		//Events
		AMCUtil.addEvents(doc, radiocomp.getEventConfs(), radioNode);
		
	}

	@Override
	public void serialize(Digester digester) {
		String radioCompClassName = RadioComp.class.getName();
		digester.addObjectCreate("Widget/Components/RadioComp", radioCompClassName);
		digester.addSetProperties("Widget/Components/RadioComp");
		digester.addSetNext("Widget/Components/RadioComp", "addComponent", radioCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/RadioComp/Listeners", RadioComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/RadioComp", RadioComp.class);
	}

}
