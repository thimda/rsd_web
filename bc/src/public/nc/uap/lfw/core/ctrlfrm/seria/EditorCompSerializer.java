package nc.uap.lfw.core.ctrlfrm.seria;

import nc.uap.lfw.core.comp.EditorComp;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EditorCompSerializer extends BaseSerializer<EditorComp> implements IViewZone {

	@Override
	public void deSerialize(EditorComp t, Document doc, Element parentNode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serialize(Digester digester) {
		String editorClassName = EditorComp.class.getName();
		digester.addObjectCreate("Widget/Components/EditorComp", editorClassName);
		digester.addSetProperties("Widget/Components/EditorComp");
		digester.addSetNext("Widget/Components/EditorComp", "addComponent",
				editorClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/EditorComp/Listeners", EditorComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/EditorComp", EditorComp.class);
	}

}
