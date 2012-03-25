package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.List;

import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.page.PlugoutEmitItem;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PlugoutSerializer extends BaseSerializer<PlugoutDesc> implements IViewZone{

	@Override
	public void deSerialize(PlugoutDesc plugoutDesc, Document doc, Element parentNode) {
		Element plugoutDescNodes = doc.createElement("PlugoutDesc");
		parentNode.appendChild(plugoutDescNodes);
		if (isNotNullString(plugoutDesc.getId()))
			plugoutDescNodes.setAttribute("id", plugoutDesc.getId());
		
		List<PlugoutDescItem> plugoutDescItems =  plugoutDesc.getDescItemList();
		if (plugoutDescItems != null){
			for (PlugoutDescItem descItem : plugoutDescItems){
				Element plugoutDescItemNodes = doc.createElement("PlugoutDescItem");
				plugoutDescNodes.appendChild(plugoutDescItemNodes);
				if (isNotNullString(descItem.getName()))
					plugoutDescItemNodes.setAttribute("name", descItem.getName());
				if (isNotNullString(descItem.getType()))
					plugoutDescItemNodes.setAttribute("type", descItem.getType());
				if (isNotNullString(descItem.getSource()))
					plugoutDescItemNodes.setAttribute("source", descItem.getSource());
				if (isNotNullString(descItem.getValue()))
					plugoutDescItemNodes.setAttribute("value", descItem.getValue());
				if (isNotNullString(descItem.getDesc()))
					plugoutDescItemNodes.setAttribute("desc", descItem.getDesc());
				if (isNotNullString(descItem.getClazztype()))
					plugoutDescItemNodes.setAttribute("clazztype", descItem.getClazztype());
			}
		}
		List<PlugoutEmitItem> plugoutEmitItems =  plugoutDesc.getEmitList();
		if (plugoutEmitItems != null){
			for (PlugoutEmitItem emitItem : plugoutEmitItems){
				Element plugoutEmitItemNodes = doc.createElement("PlugoutEmitItem");
				plugoutDescNodes.appendChild(plugoutEmitItemNodes);
				if (isNotNullString(emitItem.getId()))
					plugoutEmitItemNodes.setAttribute("id", emitItem.getId());
				if (isNotNullString(emitItem.getSource()))
					plugoutEmitItemNodes.setAttribute("source", emitItem.getSource());
				if (isNotNullString(emitItem.getType()))
					plugoutEmitItemNodes.setAttribute("type", emitItem.getType());
				if (isNotNullString(emitItem.getDesc()))
					plugoutEmitItemNodes.setAttribute("desc", emitItem.getDesc());
			}
		}
	
	}


	@Override
	public void serialize(Digester digester) {
		//plugout
		digester.addObjectCreate("Widget/PlugoutDescs/PlugoutDesc", PlugoutDesc.class.getName());
		digester.addSetProperties("Widget/PlugoutDescs/PlugoutDesc");
		digester.addSetNext("Widget/PlugoutDescs/PlugoutDesc", "addPlugoutDescs", PlugoutDesc.class.getName());
		
		digester.addObjectCreate("Widget/PlugoutDescs/PlugoutDesc/PlugoutDescItem", PlugoutDescItem.class.getName());
		digester.addSetProperties("Widget/PlugoutDescs/PlugoutDesc/PlugoutDescItem");
		digester.addSetNext("Widget/PlugoutDescs/PlugoutDesc/PlugoutDescItem", "addDescItem", PlugoutDescItem.class.getName());
		
		digester.addObjectCreate("Widget/PlugoutDescs/PlugoutDesc/PlugoutEmitItem", PlugoutEmitItem.class.getName());
		digester.addSetProperties("Widget/PlugoutDescs/PlugoutDesc/PlugoutEmitItem");
		digester.addSetNext("Widget/PlugoutDescs/PlugoutDesc/PlugoutEmitItem", "addEmitItem", PlugoutEmitItem.class.getName());
	}

}
