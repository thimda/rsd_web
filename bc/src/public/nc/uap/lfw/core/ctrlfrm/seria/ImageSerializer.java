package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ImageSerializer extends BaseSerializer<ImageComp> implements IViewZone{

	@Override
	public void deSerialize(ImageComp image, Document doc, Element parentNode) {
		Element imageNode = doc.createElement("ImageComp");
		parentNode.appendChild(imageNode);
		imageNode.setAttribute("id", image.getId());
//		if(isNotNullString(image.getWidth()))
//			imageNode.setAttribute("width", image.getWidth());
//		if(isNotNullString(image.getHeight()))
//			imageNode.setAttribute("height", image.getHeight());
		if(isNotNullString(image.getContextMenu()))
			imageNode.setAttribute("contextMenu", image.getContextMenu());
//		if(isNotNullString(image.getPosition()))
//			imageNode.setAttribute("position", image.getPosition());
//		if(isNotNullString(image.getTop()))
//			imageNode.setAttribute("top", image.getTop());
//		if(isNotNullString(image.getLeft()))
//			imageNode.setAttribute("left", image.getLeft());
		imageNode.setAttribute("enabled", String.valueOf(image.isEnabled()));
		imageNode.setAttribute("visible", String.valueOf(image.isVisible()));
//		if(isNotNullString(image.getClassName()))
//			imageNode.setAttribute("className", image.getClassName());
		if(isNotNullString(image.getImage1()))
			imageNode.setAttribute("image1", image.getImage1());
		if(isNotNullString(image.getImage2()))
			imageNode.setAttribute("image2", image.getImage2());
		if(isNotNullString(image.getAlt()))
			imageNode.setAttribute("alt", image.getAlt());
		if(isNotNullString(image.getImageInact()))
			imageNode.setAttribute("imageInact", image.getImageInact());
		imageNode.setAttribute("floatRight", String.valueOf(image.isFloatRight()));
		imageNode.setAttribute("floatLeft", String.valueOf(image.isFloatLeft()));
		Map<String, JsListenerConf> jsListeners = image.getListenerMap();
		if(jsListeners != null && jsListeners.size() > 0)
			PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), imageNode);
		
		//Events
		AMCUtil.addEvents(doc, image.getEventConfs(), imageNode);
			
	}


	@Override
	public void serialize(Digester digester) {
		String imageCompClassName = ImageComp.class.getName();
		digester.addObjectCreate("Widget/Components/ImageComp", imageCompClassName);
		digester.addSetProperties("Widget/Components/ImageComp");
		digester.addSetNext("Widget/Components/ImageComp", "addComponent", imageCompClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/ImageComp/Listeners", ImageComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/ImageComp", ImageComp.class);
	}

}
