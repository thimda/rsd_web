package nc.uap.lfw.core.ctrlfrm.seria;

import nc.uap.lfw.core.comp.EditorComp;
import nc.uap.lfw.core.comp.WebSilverlightWidget;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SilverLightSerializer extends BaseSerializer<WebSilverlightWidget> implements IViewZone {

	@Override
	public void deSerialize(WebSilverlightWidget t, Document doc,
			Element parentNode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serialize(Digester digester) {
		String webPartClassName = WebSilverlightWidget.class.getName();
		digester.addObjectCreate("Widget/Components/WebSilverlightWidget", webPartClassName);
		digester.addSetProperties("Widget/Components/WebSilverlightWidget");
		digester.addSetNext("Widget/Components/WebSilverlightWidget", "addComponent",
				webPartClassName);
		EventConfParser.parseEvents(digester, "Widget/Components/WebSilverlightWidget", WebSilverlightWidget.class);
	}

}
