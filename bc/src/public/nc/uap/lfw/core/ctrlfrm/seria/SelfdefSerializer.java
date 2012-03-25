package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SelfdefSerializer extends BaseSerializer<SelfDefComp> implements IViewZone{

	@Override
	public void deSerialize(SelfDefComp selfDefComp, Document doc, Element parentNode) {
		Element selfDefCompNode = doc.createElement("SelfDefComp");
		parentNode.appendChild(selfDefCompNode);
		selfDefCompNode.setAttribute("id", selfDefComp.getId());
//		if(isNotNullString(selfDefComp.getWidth()))
//			selfDefCompNode.setAttribute("width", selfDefComp.getWidth());
//		if(isNotNullString(selfDefComp.getHeight()))
//			selfDefCompNode.setAttribute("height", selfDefComp.getHeight());
//		if(isNotNullString(selfDefComp.getTop()))
//			selfDefCompNode.setAttribute("top", selfDefComp.getTop());
//		if(isNotNullString(selfDefComp.getLeft()))
//			selfDefCompNode.setAttribute("left", selfDefComp.getLeft());
//		if(isNotNullString(selfDefComp.getPosition()))
//			selfDefCompNode.setAttribute("position", selfDefComp.getPosition());
//		if(isNotNullString(selfDefComp.getClassName()))
//			selfDefCompNode.setAttribute("className", selfDefComp.getClassName());
		selfDefCompNode.setAttribute("visible", String.valueOf(selfDefComp.isVisible()));
		Map<String, JsListenerConf> jsListeners = selfDefComp.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), selfDefCompNode);
		
		//Events
		AMCUtil.addEvents(doc, selfDefComp.getEventConfs(), selfDefCompNode);
			
	}


	@Override
	public void serialize(Digester digester) {
		String selfDefCompClassName = SelfDefComp.class.getName();
		digester.addObjectCreate("Widget/Components/SelfDefComp", selfDefCompClassName);
		digester.addSetProperties("Widget/Components/SelfDefComp");
		digester.addSetNext("Widget/Components/SelfDefComp", "addComponent",
				selfDefCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/SelfDefComp/Listeners", SelfDefComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/SelfDefComp", SelfDefComp.class);
	}

}
