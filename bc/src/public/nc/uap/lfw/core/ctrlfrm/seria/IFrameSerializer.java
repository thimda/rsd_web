package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IFrameSerializer extends BaseSerializer<IFrameComp> implements IViewZone{

	@Override
	public void deSerialize(IFrameComp frame, Document doc, Element parentNode) {
		Element frameNode = doc.createElement("IframeComp");
		parentNode.appendChild(frameNode);
		frameNode.setAttribute("visible", String.valueOf(frame.isVisible()));
		frameNode.setAttribute("enabled", String.valueOf(frame.isEnabled()));
		if(isNotNullString(frame.getSrc()))
			frameNode.setAttribute("src", frame.getSrc());
		if(isNotNullString(frame.getContextMenu()))
			frameNode.setAttribute("contextMenu", frame.getContextMenu());
//		if(isNotNullString(frame.getTop()))
//			frameNode.setAttribute("top", frame.getTop());
//		if(isNotNullString(frame.getPosition()))
//			frameNode.setAttribute("position", frame.getPosition());
//		if(isNotNullString(frame.getLeft()))
//			frameNode.setAttribute("left", frame.getLeft());
		frameNode.setAttribute("id", frame.getId());
		if(isNotNullString(frame.getSrc()))
			frameNode.setAttribute("src", frame.getSrc());
		if(isNotNullString(frame.getName()))
			frameNode.setAttribute("name", frame.getName());
//		if(isNotNullString(frame.getHeight()))
//			frameNode.setAttribute("height", frame.getHeight());
//		if(isNotNullString(frame.getWidth()))
//			frameNode.setAttribute("width", frame.getWidth());
		if(isNotNullString(frame.getBorder()))
			frameNode.setAttribute("border", frame.getBorder());
		if(isNotNullString(frame.getFrameBorder()))
			frameNode.setAttribute("frameborder", frame.getFrameBorder());
//		if(isNotNullString(frame.getClassName()))
//			frameNode.setAttribute("className", frame.getClassName());
		if(isNotNullString(frame.getScrolling()));
			frameNode.setAttribute("scrolling", frame.getScrolling());
		Map<String, JsListenerConf> jsListeners = frame.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), frameNode);
		
		//Events
		AMCUtil.addEvents(doc, frame.getEventConfs(), frameNode);
			
	}


	@Override
	public void serialize(Digester digester) {
		String iframeClassName = IFrameComp.class.getName();
		digester.addObjectCreate("Widget/Components/IframeComp", iframeClassName);
		digester.addSetProperties("Widget/Components/IframeComp");
		digester.addSetNext("Widget/Components/IframeComp", "addComponent", iframeClassName);
		
		EventConfParser.parseEvents(digester, "Widget/Components/IframeComp", IFrameComp.class);
	}

}
