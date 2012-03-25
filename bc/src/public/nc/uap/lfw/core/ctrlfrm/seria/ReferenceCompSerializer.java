package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ReferenceCompSerializer extends BaseSerializer<ReferenceComp> implements IViewZone {

	@Override
	public void deSerialize(ReferenceComp refcomp, Document doc, Element parentNode) {
		// TODO Auto-generated method stub
		Element refNode = doc.createElement("ReferenceComp");
		parentNode.appendChild(refNode);
		refNode.setAttribute("id", refcomp.getId());
		refNode.setAttribute("visible", String.valueOf(refcomp.isVisible()));
		refNode.setAttribute("enabled", String.valueOf(refcomp.isEnabled()));
		refNode.setAttribute("editorType", refcomp.getEditorType());
	//			if(isNotNullString(refcomp.getWidth()))
	//				refNode.setAttribute("width", refcomp.getWidth());
		if(isNotNullString(refcomp.getText()))
			refNode.setAttribute("text", refcomp.getText());
	//			if(isNotNullString(textcomp.getLeft()))
	//				refNode.setAttribute("left", refcomp.getLeft());
	//			if(isNotNullString(refcomp.getTop()))
	//				refNode.setAttribute("top", refcomp.getTop());	
		if(isNotNullString(refcomp.getI18nName()))
			refNode.setAttribute("i18nName", refcomp.getI18nName());
		if(isNotNullString(refcomp.getLangDir()))
			refNode.setAttribute("langDir", refcomp.getLangDir());
		if(isNotNullString(refcomp.getContextMenu()))
			refNode.setAttribute("contextMenu", refcomp.getContextMenu());
		if(isNotNullString(refcomp.getTextAlign()))
			refNode.setAttribute("textAlign", refcomp.getTextAlign());
		if(isNotNullString(String.valueOf(refcomp.getTextWidth())))
			refNode.setAttribute("textWidth", String.valueOf(refcomp.getTextWidth()));
		refNode.setAttribute("focus", String.valueOf(refcomp.isFocus()));
		if(isNotNullString(refcomp.getRefcode()))
			refNode.setAttribute("refcode", refcomp.getRefcode());
		if(isNotNullString(refcomp.getTip()))
			refNode.setAttribute("tip", refcomp.getTip());
		Map<String, JsListenerConf> jsListeners =refcomp.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), refNode);
		
		//Events
		AMCUtil.addEvents(doc, refcomp.getEventConfs(), refNode);
	}

	@Override
	public void serialize(Digester digester) {
		String referCompClassName = ReferenceComp.class.getName();
		digester.addObjectCreate("Widget/Components/ReferenceComp", referCompClassName);
		digester.addSetProperties("Widget/Components/ReferenceComp");
		digester.addSetNext("Widget/Components/ReferenceComp", "addComponent", referCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/ReferenceComp/Listeners", ReferenceComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/ReferenceComp", ReferenceComp.class);
	}

}
