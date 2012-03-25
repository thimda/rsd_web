package nc.uap.lfw.core.persistence;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.page.manager.PoolObjectManager;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 将一个dataset保存为xml
 * @author gd
 *
 */
public class DatasetToXml {
	public static void toXml(String filePath, String fileName, Dataset ds) 
	{
		Document doc = XMLUtil.getNewDocument();
		Element rootNode = doc.createElement("Dataset");
		doc.appendChild(rootNode);
		if(ds instanceof MdDataset){
			MdDataset mdDs = (MdDataset) ds;
			if(mdDs.getObjMeta() != null && !(mdDs.getObjMeta().equals("")))
				rootNode.setAttribute("objMeta", mdDs.getObjMeta());
		}
		if(ds.getVoMeta() != null && !(ds.getVoMeta().equals("")))
			rootNode.setAttribute("voMeta", ds.getVoMeta());
		Field[] fields = ds.getFieldSet().getFields();
		if(fields != null && fields.length > 0)
		{
			Element fieldsNode = doc.createElement("Fields");
			rootNode.appendChild(fieldsNode);
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Element fieldNode = doc.createElement("Field");
				fieldsNode.appendChild(fieldNode);
				if(field.getId() != null && !field.getId().equals(""))
					fieldNode.setAttribute("id", field.getId());
				if(field.getField() != null && !field.getField().equals(""))
					fieldNode.setAttribute("field", field.getField());
				if(field.getText() != null && !field.getText().equals(""))
					fieldNode.setAttribute("text", field.getText());
				if(field.getDataType() != null && !field.getDataType().equals(""))
					fieldNode.setAttribute("dataType", field.getDataType());
				if(field.getI18nName() != null && !field.getI18nName().equals(""))
					fieldNode.setAttribute("i18nName", field.getI18nName());
				if(field.getLangDir() != null && !field.getLangDir().equals(""))
					fieldNode.setAttribute("langDir", field.getLangDir());
				if(field.getMaxValue() != null && !field.getMaxValue().equals(""))
					fieldNode.setAttribute("maxValue", field.getMaxValue());
				if(field.getMinValue() != null && !field.getMinValue().equals(""))
					fieldNode.setAttribute("minValue", field.getMinValue());
				if(field.getDefaultValue() != null)
					fieldNode.setAttribute("defaultValue", String.valueOf(field.getDefaultValue()));
				}
		}
		
		// 写出文件
		PersistenceUtil.toXmlFile(doc, filePath, fileName);
		
		// 刷新缓存,同步最新数据
		PoolObjectManager.refreshDatasetPool(LfwRuntimeEnvironment.getRootPath(), ds);
	}
	
	/**
	 * 从缓存中删除此ds
	 * @param filePath
	 * @param fileName
	 * @param ds
	 */
	public static void deletePoolDs(Dataset ds){
		PoolObjectManager.removeDatasetFromPool(LfwRuntimeEnvironment.getRootPath(), ds);
	}
}
