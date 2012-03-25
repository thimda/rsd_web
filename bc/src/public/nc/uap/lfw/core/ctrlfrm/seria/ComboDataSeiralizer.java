package nc.uap.lfw.core.ctrlfrm.seria;

import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.combodata.DynamicComboDataConf;
import nc.uap.lfw.core.combodata.MDComboDataConf;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.ctrlfrm.IViewZone;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ComboDataSeiralizer extends BaseSerializer<ComboData> implements IViewZone{

	@Override
	public void deSerialize(ComboData cd, Document doc, Element parentNode) {
//		Element comboDatasNode = doc.createElement("ComboDatas");
//		parentNode.appendChild(comboDatasNode);
		if(cd.getFrom() == null){
			if(cd instanceof StaticComboData)
			{
				Element staticComboDataNode = doc.createElement("StaticComboData");
				parentNode.appendChild(staticComboDataNode);
				staticComboDataNode.setAttribute("id", cd.getId());
				if(isNotNullString(cd.getCaption()))
					staticComboDataNode.setAttribute("caption", cd.getCaption());
				CombItem[] combItems = cd.getAllCombItems();
				if(combItems != null)
				{
					for (int i = 0; i < combItems.length; i++) {
						Element combItemNode = doc.createElement("ComboItem");
						staticComboDataNode.appendChild(combItemNode);
						combItemNode.setAttribute("value", combItems[i].getValue());
						if(isNotNullString(combItems[i].getI18nName()))
							combItemNode.setAttribute("i18nName", combItems[i].getI18nName());
						if(isNotNullString(combItems[i].getText()))
							combItemNode.setAttribute("text", combItems[i].getText());
						if(isNotNullString(combItems[i].getImage())){
							combItemNode.setAttribute("image", combItems[i].getImage());
						}
					}
				}
			}
			else if(cd instanceof DynamicComboDataConf)
			{
				DynamicComboDataConf dCd = (DynamicComboDataConf) cd;
				Element dymComboDataNode = doc.createElement("DynamicComboData");
				parentNode.appendChild(dymComboDataNode);
				dymComboDataNode.setAttribute("id", dCd.getId());
				if(isNotNullString(dCd.getCaption()))
					dymComboDataNode.setAttribute("caption", dCd.getCaption());
				dymComboDataNode.setAttribute("className", dCd.getClassName() == null?"":dCd.getClassName());
			}
			else if(cd instanceof MDComboDataConf){
				MDComboDataConf mdCd = (MDComboDataConf) cd;
				Element mdComboDataNode = doc.createElement("MDComboDataConf");
				parentNode.appendChild(mdComboDataNode);
				mdComboDataNode.setAttribute("id", mdCd.getId());
				if(isNotNullString(mdCd.getCaption()))
					mdComboDataNode.setAttribute("caption", mdCd.getCaption());
				mdComboDataNode.setAttribute("fullclassName", mdCd.getFullclassName() == null?"":mdCd.getFullclassName());
			}
		}
	}

	@Override
	public void serialize(Digester digester) {
		String staticComboDataCompClassName = StaticComboData.class.getName();
		digester.addObjectCreate("Widget/Models/ComboDatas/StaticComboData",
				staticComboDataCompClassName);
		digester.addSetProperties("Widget/Models/ComboDatas/StaticComboData");

		String combItemClassName = CombItem.class.getName();
		digester.addObjectCreate("Widget/Models/ComboDatas/StaticComboData/ComboItem",
				combItemClassName);
		digester.addSetProperties("Widget/Models/ComboDatas/StaticComboData/ComboItem");
		digester.addSetNext("Widget/Models/ComboDatas/StaticComboData/ComboItem",
				"addCombItem", combItemClassName);

		digester.addSetNext("Widget/Models/ComboDatas/StaticComboData", "addComboData",
				staticComboDataCompClassName);
		
		
//		String metaComboDataCompClassName = LazyLoadComboData.class.getName();
//		digester.addObjectCreate("Datasets/LazyLoadComboData",
//				metaComboDataCompClassName);
//		digester.addSetProperties("Datasets/LazyLoadComboData");
//		digester.addSetNext("Datasets/LazyLoadComboData", "addComboData",
//				metaComboDataCompClassName);
		
		String dynamicClassName = DynamicComboDataConf.class.getName();
		digester.addObjectCreate("Widget/Models/ComboDatas/DynamicComboData", dynamicClassName);
		digester.addSetProperties("Widget/Models/ComboDatas/DynamicComboData");
		digester.addSetNext("Widget/Models/ComboDatas/DynamicComboData", "addComboData", dynamicClassName);
		
		String mcClassName = MDComboDataConf.class.getName();
		digester.addObjectCreate("Widget/Models/ComboDatas/MDComboDataConf", mcClassName);
		digester.addSetProperties("Widget/Models/ComboDatas/MDComboDataConf");
		digester.addSetNext("Widget/Models/ComboDatas/MDComboDataConf", "addComboData", mcClassName);
	}

}
