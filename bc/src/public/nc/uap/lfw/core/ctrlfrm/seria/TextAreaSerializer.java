package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextAreaSerializer extends BaseSerializer<TextAreaComp> implements IViewZone {

	@Override
	public void deSerialize(TextAreaComp textareacomp, Document doc, Element parentNode) {
		Element textareaNode = doc.createElement("TextAreaComp");
		parentNode.appendChild(textareaNode);
		textareaNode.setAttribute("id", textareacomp.getId());
		textareaNode.setAttribute("visible", String.valueOf(textareacomp.isVisible()));
		textareaNode.setAttribute("editorType", textareacomp.getEditorType());
		textareaNode.setAttribute("enabled", String.valueOf(textareacomp.isEnabled()));
		if(isNotNullString(textareacomp.getText()))
			textareaNode.setAttribute("text", textareacomp.getText());
		textareaNode.setAttribute("textWidth", String.valueOf(textareacomp.getTextWidth()));
//		if(isNotNullString(textareacomp.getWidth()))
//			textareaNode.setAttribute("width", textareacomp.getWidth());
//		if(isNotNullString(textareacomp.getHeight()))
//			textareaNode.setAttribute("height", textareacomp.getHeight());
		if(isNotNullString(textareacomp.getI18nName()))
			textareaNode.setAttribute("i18nName", textareacomp.getI18nName());
		if(isNotNullString(textareacomp.getLangDir()))
			textareaNode.setAttribute("langDir", textareacomp.getLangDir());
		if(isNotNullString(textareacomp.getTextAlign()))
			textareaNode.setAttribute("textAlign", textareacomp.getTextAlign());
		textareaNode.setAttribute("focus", String.valueOf(textareacomp.isFocus()));
		if(isNotNullString(textareacomp.getRows()))
			textareaNode.setAttribute("rows", textareacomp.getRows());
		if(isNotNullString(textareacomp.getCols()))
			textareaNode.setAttribute("cols", textareacomp.getCols());
//		if(isNotNullString(textareacomp.getClassName()))
//			textareaNode.setAttribute("className", textareacomp.getClassName());
		if(isNotNullString(textareacomp.getTip()))
			textareaNode.setAttribute("tip", textareacomp.getTip());
//		if(isNotNullString(textareacomp.getTop()))
//			textareaNode.setAttribute("top", textareacomp.getTop());
//		if(isNotNullString(textareacomp.getLeft()))
//			textareaNode.setAttribute("left", textareacomp.getLeft());
		if(isNotNullString(textareacomp.getContextMenu()))
			textareaNode.setAttribute("contextMenu", textareacomp.getContextMenu());
//		if(isNotNullString(textareacomp.getPosition()))
//			textareaNode.setAttribute("position", textareacomp.getPosition());
		Map<String, JsListenerConf> jsListeners = textareacomp.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), textareaNode);
		
		//Events
		AMCUtil.addEvents(doc, textareacomp.getEventConfs(), textareaNode);
	}

	@Override
	public void serialize(Digester digester) {
		String textAreaCompClassName = TextAreaComp.class.getName();
		digester.addObjectCreate("Widget/Components/TextAreaComp", textAreaCompClassName);
		digester.addSetProperties("Widget/Components/TextAreaComp");
		digester.addSetNext("Widget/Components/TextAreaComp", "addComponent", textAreaCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/TextAreaComp/Listeners", TextAreaComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/TextAreaComp", TextAreaComp.class);
	}

}
