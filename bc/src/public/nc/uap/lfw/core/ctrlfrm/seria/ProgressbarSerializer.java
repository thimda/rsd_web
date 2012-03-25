package nc.uap.lfw.core.ctrlfrm.seria;

import nc.uap.lfw.core.comp.ProgressBarComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProgressbarSerializer extends BaseSerializer<ProgressBarComp> implements IViewZone{

	@Override
	public void deSerialize(ProgressBarComp progressBar, Document doc, Element parentNode) {
		Element progressNode = doc.createElement("ProgressBarComp");
		parentNode.appendChild(progressNode);
		progressNode.setAttribute("id", progressBar.getId());
//		if(isNotNullString(progressBar.getClassName()))
//			progressNode.setAttribute("className", progressBar.getClassName());
		if(isNotNullString(progressBar.getValue()))
			progressNode.setAttribute("value", progressBar.getValue());
//		if(isNotNullString(progressBar.getPosition()))
//			progressNode.setAttribute("position", progressBar.getPosition());
		if(isNotNullString(progressBar.getConfType()))
			progressNode.setAttribute("confType", progressBar.getConfType());
		if(isNotNullString(progressBar.getContextMenu()))
			progressNode.setAttribute("contextMenu", progressBar.getContextMenu());
//		if(isNotNullString(progressBar.getTop()))
//			progressNode.setAttribute("top", progressBar.getTop());
//		if(isNotNullString(progressBar.getTop()))
//			progressNode.setAttribute("height", progressBar.getHeight());
//		if(isNotNullString(progressBar.getWidth()))
//			progressNode.setAttribute("width", progressBar.getWidth());
		progressNode.setAttribute("enabled", String.valueOf(progressBar.isEnabled()));
		progressNode.setAttribute("visible", String.valueOf(progressBar.isVisible()));
		if(isNotNullString(progressBar.getValueAlign()))
			progressNode.setAttribute("valueAlign", progressBar.getValueAlign());
//		if(isNotNullString(progressBar.getLeft()))
//			progressNode.setAttribute("left", progressBar.getLeft());
			
	}

	@Override
	public void serialize(Digester digester) {
		String buttonClassName = ProgressBarComp.class.getName();
		digester.addObjectCreate("Widget/Components/ProgressBarComp", buttonClassName);
		digester.addSetProperties("Widget/Components/ProgressBarComp");
		digester.addSetNext("Widget/Components/ProgressBarComp", "addComponent",
				buttonClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/ProgressBarComp/Listeners", ProgressBarComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/ProgressBarComp", ProgressBarComp.class);
	}

}
