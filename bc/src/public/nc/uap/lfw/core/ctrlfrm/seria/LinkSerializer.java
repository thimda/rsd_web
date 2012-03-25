package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LinkSerializer extends BaseSerializer<LinkComp> implements IViewZone{

	@Override
	public void deSerialize(LinkComp link, Document doc, Element parentNode) {
		Element linkNode = doc.createElement("LinkComp");
		parentNode.appendChild(linkNode);
		linkNode.setAttribute("id", link.getId());
		if(isNotNullString(link.getHref()))
			linkNode.setAttribute("href", link.getHref());
//		if(isNotNullString(link.getHeight()))
//			linkNode.setAttribute("height", link.getHeight());
//		if(isNotNullString(link.getWidth()))
//			linkNode.setAttribute("width", link.getWidth());
//		if(isNotNullString(link.getTop()))
//			linkNode.setAttribute("top", link.getTop());
//		if(isNotNullString(link.getLeft()))
//			linkNode.setAttribute("left", link.getLeft());
//		if(isNotNullString(link.getPosition()))
//			linkNode.setAttribute("position", link.getPosition());
		if(isNotNullString(link.getContextMenu()))
			linkNode.setAttribute("contextMenu", link.getContextMenu());
		if(isNotNullString(link.getI18nName()))
			linkNode.setAttribute("i18nName", link.getI18nName());
		if(isNotNullString(link.getImage()))
			linkNode.setAttribute("image", link.getImage());
		if(isNotNullString(link.getTarget()))
			linkNode.setAttribute("target", link.getTarget());
		if(isNotNullString(link.getLangDir()))
			linkNode.setAttribute("langDir", link.getLangDir());
//		if(isNotNullString(link.getClassName()))
//			linkNode.setAttribute("className", link.getClassName());
		linkNode.setAttribute("hasImg", String.valueOf(link.isHasImg()));
		Map<String, JsListenerConf> jsListeners = link.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), linkNode);
		//Events
		AMCUtil.addEvents(doc, link.getEventConfs(), linkNode);
			
	}

	@Override
	public void serialize(Digester digester) {
		String linkClassName = LinkComp.class.getName();
		digester.addObjectCreate("Widget/Components/LinkComp", linkClassName);
		digester.addSetProperties("Widget/Components/LinkComp");
		digester.addSetNext("Widget/Components/LinkComp", "addComponent",
				linkClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/LinkComp/Listeners", LinkComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/LinkComp", LinkComp.class);
	}

}
