package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.WebPartComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WebpartSerializer extends BaseSerializer<WebPartComp> implements IViewZone{

	@Override
	public void deSerialize(WebPartComp webPartComp, Document doc, Element parentNode) {
		Element webPartCompNode = doc.createElement("WebPartComp");
		parentNode.appendChild(webPartCompNode);
		
		webPartCompNode.setAttribute("id", webPartComp.getId());
//		if(isNotNullString(webPartComp.getWidth()))
//			webPartCompNode.setAttribute("width", webPartComp.getWidth());
//		if(isNotNullString(webPartComp.getHeight()))
//			webPartCompNode.setAttribute("height", webPartComp.getHeight());
//		if(isNotNullString(webPartComp.getLeft()))
//			webPartCompNode.setAttribute("left", webPartComp.getLeft());
//		if(isNotNullString(webPartComp.getTop()))
//			webPartCompNode.setAttribute("top", webPartComp.getTop());
//		if(isNotNullString(webPartComp.getPosition()))
//			webPartCompNode.setAttribute("position", webPartComp.getPosition());
//		if(isNotNullString(webPartComp.getClassName()))
//			webPartCompNode.setAttribute("className", webPartComp.getClassName());
		if(isNotNullString(webPartComp.getContentFetcher()))
			webPartCompNode.setAttribute("contentFetcher", webPartComp.getContentFetcher());
		
		Map<String, JsListenerConf> jsListeners = webPartComp.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), webPartCompNode);
		
		//Events
		AMCUtil.addEvents(doc, webPartComp.getEventConfs(), webPartCompNode);
			
	}

	@Override
	public void serialize(Digester digester) {
		String	webPartClassName = WebPartComp.class.getName();
		digester.addObjectCreate("Widget/Components/WebPartComp", webPartClassName);
		digester.addSetProperties("Widget/Components/WebPartComp");
		digester.addSetNext("Widget/Components/WebPartComp", "addComponent",
				webPartClassName);
//		ListenersParser.parseListeners(digester, "Widget/Components/ButtonComp/Listeners", ButtonComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/WebPartComp", WebPartComp.class);
	}

}
