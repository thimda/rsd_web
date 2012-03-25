package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.List;

import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginDescItem;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.page.PlugoutEmitItem;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PluginSerializer extends BaseSerializer<PluginDesc> implements IViewZone{

	@Override
	public void deSerialize(PluginDesc pluginDesc, Document doc, Element parentNode) {
		Element pluginDescNodes = doc.createElement("PluginDesc");
		parentNode.appendChild(pluginDescNodes);
		if (isNotNullString(pluginDesc.getId()))
			pluginDescNodes.setAttribute("id", pluginDesc.getId());
		
		List<PluginDescItem> pluginDescItems =  pluginDesc.getDescItemList();
		if (pluginDescItems != null){
			for (PluginDescItem descItem : pluginDescItems){
				Element pluginDescItemNodes = doc.createElement("PluginDescItem");
				pluginDescNodes.appendChild(pluginDescItemNodes);
				if (isNotNullString(descItem.getId()))
					pluginDescItemNodes.setAttribute("id", descItem.getId());
				if (isNotNullString(descItem.getValue()))
					pluginDescItemNodes.setAttribute("value", descItem.getValue());
				if (isNotNullString(descItem.getClazztype()))
					pluginDescItemNodes.setAttribute("clazztype", descItem.getClazztype());
			}
		}
	}

	@Override
	public void serialize(Digester digester) {
		//plugin
		digester.addObjectCreate("Widget/PluginDescs/PluginDesc", PluginDesc.class.getName());
		digester.addSetProperties("Widget/PluginDescs/PluginDesc");
		digester.addSetNext("Widget/PluginDescs/PluginDesc", "addPluginDescs", PluginDesc.class.getName());
		
		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/PluginDescItem", PluginDescItem.class.getName());
		digester.addSetProperties("Widget/PluginDescs/PluginDesc/PluginDescItem");
		digester.addSetNext("Widget/PluginDescs/PluginDesc/PluginDescItem", "addDescItem", PluginDescItem.class.getName());
	}

}
