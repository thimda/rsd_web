package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextSerializer extends BaseSerializer<TextComp> implements IViewZone{

	@Override
	public void deSerialize(TextComp text, Document doc, Element parentNode) {
		Element stringNode = doc.createElement("TextComp");
		parentNode.appendChild(stringNode);
		stringNode.setAttribute("id", text.getId());
		stringNode.setAttribute("visible", String.valueOf(text.isVisible()));
		stringNode.setAttribute("editorType", text.getEditorType());
		if(isNotNullString(text.getText()))
			stringNode.setAttribute("text", text.getText());
		if(isNotNullString(text.getI18nName()))
			stringNode.setAttribute("i18nName", text.getI18nName());
		if(isNotNullString(text.getLangDir()))
			stringNode.setAttribute("langDir", text.getLangDir());
		if(isNotNullString(text.getTextAlign()))
			stringNode.setAttribute("textAlign", text.getTextAlign());
		if(isNotNullString(text.getContextMenu()))
			stringNode.setAttribute("contextMenu", text.getContextMenu());
		if(isNotNullString(String.valueOf(text.getTextWidth())) && !(text.getTextWidth() == 0))
			stringNode.setAttribute("textWidth", String.valueOf(text.getTextWidth()));
		stringNode.setAttribute("focus", String.valueOf(text.isFocus()));
		stringNode.setAttribute("enabled", String.valueOf(text.isEnabled()));
//			if(isNotNullString(textcomp.getClassName()))
//				stringNode.setAttribute("className", textcomp.getClassName());
		if(isNotNullString(text.getValue()))
			stringNode.setAttribute("value", text.getValue());
//			if(isNotNullString(textcomp.getWidth()))
//				stringNode.setAttribute("width", textcomp.getWidth());
//			if(isNotNullString(textcomp.getHeight()))
//				stringNode.setAttribute("height", textcomp.getHeight());
		if(isNotNullString(text.getTip()))
			stringNode.setAttribute("tip", text.getTip());
//			if(isNotNullString(textcomp.getLeft()))
//				stringNode.setAttribute("left", textcomp.getLeft());
//			if(isNotNullString(textcomp.getTop()))
//				stringNode.setAttribute("top", textcomp.getTop());
//			if(isNotNullString(textcomp.getPosition()))
//				stringNode.setAttribute("position", textcomp.getPosition());
		//integer
		if(text.getEditorType().equals(EditorTypeConst.INTEGERTEXT)){
			if(isNotNullString(text.getMaxValue()))
				stringNode.setAttribute("maxValue", text.getMaxValue());
			if(isNotNullString(text.getMinValue()))
				stringNode.setAttribute("minValue", text.getMinValue());
		}
		if(text.getEditorType().equals(EditorTypeConst.FILECOMP)){
			if(isNotNullString(text.getSizeLimit()))
				stringNode.setAttribute("sizeLimit", text.getSizeLimit());
		}
		//decimal
		else if(text.getEditorType().equals(EditorTypeConst.DECIMALTEXT)){
			if(isNotNullString(text.getPrecision()))
				stringNode.setAttribute("precision", text.getPrecision());
		}
		Map<String, JsListenerConf> jsListeners = text.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), stringNode);
		
		//Events
		AMCUtil.addEvents(doc, text.getEventConfs(), stringNode);
	
	}

	@Override
	public void serialize(Digester digester) {
		String textCompClassName = TextComp.class.getName(); 
		digester.addObjectCreate("Widget/Components/TextComp", textCompClassName);
		digester.addSetProperties("Widget/Components/TextComp");
		digester.addSetNext("Widget/Components/TextComp", "addComponent",
				textCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/TextComp/Listeners", TextComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/TextComp", TextComp.class);
		
		
//		String comboboxCompClassName = ComboBoxComp.class.getName(); 
//		digester.addObjectCreate("Widget/Components/ComBoBoxComp", comboboxCompClassName);
//		digester.addSetProperties("Widget/Components/ComBoBoxComp");
//		digester.addSetNext("Widget/Components/ComBoBoxComp", "addComponent",
//				comboboxCompClassName);
//		ListenersParser.parseListeners(digester, "Widget/Components/ComBoBoxComp/Listeners", ComboBoxComp.class);
//		
//		EventConfParser.parseEvents(digester, "Widget/Components/ComBoBoxComp", ComboBoxComp.class);
	}

}
