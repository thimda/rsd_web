package nc.uap.lfw.core.ctrlfrm.seria;

import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LabelSerializer extends BaseSerializer<LabelComp> implements IViewZone{

	@Override
	public void deSerialize(LabelComp label, Document doc, Element parentNode) {
		Element labelNode = doc.createElement("LabelComp");
		parentNode.appendChild(labelNode);
		labelNode.setAttribute("id", label.getId());
		if(isNotNullString(label.getInnerHTML()))
			labelNode.setAttribute("innerHTML", label.getInnerHTML());
		if(isNotNullString(label.getText()))
			labelNode.setAttribute("text", label.getText());
		if(isNotNullString(label.getColor()))
			labelNode.setAttribute("color", label.getColor());
	
		if(isNotNullString(label.getI18nName()))
			labelNode.setAttribute("i18nName", label.getI18nName());
//		if(isNotNullString(label.getWidth()))
//			labelNode.setAttribute("width", label.getWidth());
//		if(isNotNullString(label.getHeight()))
//			labelNode.setAttribute("height", label.getHeight());
//		if(isNotNullString(label.getTop()))
//			labelNode.setAttribute("top", label.getTop());
//		if(isNotNullString(label.getLeft()))
//			labelNode.setAttribute("left", label.getLeft());
		if(isNotNullString(label.getLangDir()))
			labelNode.setAttribute("langDir", label.getLangDir());
//		if(isNotNullString(label.getPosition()))
//			labelNode.setAttribute("position", label.getPosition());
		if(isNotNullString(label.getContextMenu()))
			labelNode.setAttribute("contextMenu", label.getContextMenu());
//		if(isNotNullString(label.getClassName()))
//			labelNode.setAttribute("className", label.getClassName());
		labelNode.setAttribute("enabled", String.valueOf(label.isEnabled()));
		labelNode.setAttribute("visible", String.valueOf(label.isVisible()));
		
			
	}

	@Override
	public void serialize(Digester digester) {
		String labelClassName = LabelComp.class.getName();
		digester.addObjectCreate("Widget/Components/LabelComp", labelClassName);
		digester.addSetProperties("Widget/Components/LabelComp");
		digester.addSetNext("Widget/Components/LabelComp", "addComponent",
				labelClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/LabelComp/Listeners", LabelComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/LabelComp", LabelComp.class);
	}

}
