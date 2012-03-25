package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.FieldRelations;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.MDField;
import nc.uap.lfw.core.data.MatchField;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.ModifyField;
import nc.uap.lfw.core.data.PubDataset;
import nc.uap.lfw.core.data.PubField;
import nc.uap.lfw.core.data.RefDataset;
import nc.uap.lfw.core.data.RefMdDataset;
import nc.uap.lfw.core.data.RefPubDataset;
import nc.uap.lfw.core.data.WhereField;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.parser.AttributesParser;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.page.manager.PoolObjectManager;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;
import nc.uap.lfw.design.itf.IDatasetProvider;
import nc.uap.lfw.util.JsURLEncoder;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatasetSerializer extends BaseSerializer<Dataset> implements IViewZone{

	@Override
	public void deSerialize(Dataset ds, Document doc, Element parentNode) {
		Element dsNode = null;
		if(ds.getFrom() == null){
			if(ds instanceof RefDataset)
				dsNode = doc.createElement("RefDataset");
			else if(ds instanceof RefMdDataset){
				dsNode = doc.createElement("RefMdDataset");
				if(isNotNullString(((RefMdDataset)ds).getObjMeta()))
					dsNode.setAttribute("objMeta", ((RefMdDataset)ds).getObjMeta());
			}
			else if(ds instanceof RefPubDataset)
			{
				dsNode = doc.createElement("RefPubDataset");
				String refId = ((RefPubDataset)ds).getRefId();
				if(isNotNullString(refId))
					dsNode.setAttribute("refId", ((RefPubDataset)ds).getRefId());
			}
			else if(ds instanceof PubDataset)
			{
				dsNode = doc.createElement("PubDataset");
				String refId = ((PubDataset)ds).getRefId();
				if(isNotNullString(refId))
					dsNode.setAttribute("refId", ((PubDataset)ds).getRefId());
			}
			else if(ds instanceof MdDataset){
				dsNode = doc.createElement("MdDataset");
				if(isNotNullString(((MdDataset)ds).getObjMeta()))
					dsNode.setAttribute("objMeta", ((MdDataset)ds).getObjMeta());
			}
			else
				dsNode = doc.createElement("Dataset");
			
			dsNode.setAttribute("id", ds.getId());
			dsNode.setAttribute("enabled", String.valueOf(ds.isEnabled()));
//			dsNode.setAttribute("controloperatorStatus", String.valueOf(ds.isControloperatorStatus()));
			
			dsNode.setAttribute("controlwidgetopeStatus", String.valueOf(ds.isControlwidgetopeStatus()));
			
			if(isNotNullString(ds.getCaption()))
				dsNode.setAttribute("caption", ds.getCaption());
			if(isNotNullString(ds.getVoMeta()))
				dsNode.setAttribute("voMeta", ds.getVoMeta());
			dsNode.setAttribute("lazyLoad", String.valueOf(ds.isLazyLoad()));
//			if(isNotNullString(ds.getOperatorStatusArray()))
//				dsNode.setAttribute("operatorStatusArray", ds.getOperatorStatusArray());
			
			if(ds.getPageSize() > 0)
				dsNode.setAttribute("pageSize", String.valueOf(ds.getPageSize()));
			
			dsNode.setAttribute("notNullBody", String.valueOf(ds.isNotNullBody()));
			
			parentNode.appendChild(dsNode);
			
			// <FieldRelations>
			FieldRelations rels = ds.getFieldRelations();
			if(rels != null && rels.getFieldRelations() != null && rels.getFieldRelations().length > 0)
			{
				FieldRelation[] fieldRels = rels.getFieldRelations();
				addFieldRelations(doc, fieldRels, dsNode);
			}
			
			// <Fields>
			Field[] fields = ds.getFieldSet().getFields();
			if(fields != null && fields.length > 0)
			{
				Element fieldsNode = doc.createElement("Fields");
				dsNode.appendChild(fieldsNode);
				
				if(ds instanceof PubDataset)
				{
					PubDataset pubDs = (PubDataset)ds;
					Dataset poolDs = PoolObjectManager.getDataset(pubDs.getRefId());
					for(int m = 0, count = pubDs.getFieldSet().getFieldCount(); m < count; m++)
					{
						Field field = pubDs.getFieldSet().getField(m);
						// 该字段来自公共池,判断是否修改过
						if(field instanceof PubField)
						{
							Element mFieldNode = convertModifiedField(field, poolDs.getFieldSet(), doc);
							if(mFieldNode != null)
								fieldsNode.appendChild(mFieldNode);
						}
						// 添加的新的字段
						else
						{
							Element fieldNode = createNormalField(doc, field);
							fieldsNode.appendChild(fieldNode);
						}
					}
				}
				else if(ds instanceof MdDataset)
				{
					MdDataset mdDs = (MdDataset)ds;
					MdDataset trueMdDs = new MdDataset();
					trueMdDs.setObjMeta(mdDs.getObjMeta());
					trueMdDs = NCLocator.getInstance().lookup(IDatasetProvider.class).getMdDataset(trueMdDs);
					
					if(trueMdDs == null)
						throw new LfwRuntimeException("根据ds:" + mdDs.getId() + "配置的元数据路径mdPath=" + mdDs.getObjMeta() + "未获取实际的MdDataset!");
					
					for(int m = 0, count = mdDs.getFieldSet().getFieldCount(); m < count; m++){
						Field field = mdDs.getFieldSet().getField(m);
						// 该字段来自元数据,判断是否修改过
						if(field instanceof MDField)
						{
							Element mFieldNode = convertModifiedField(field, trueMdDs.getFieldSet(), doc);
							if(mFieldNode != null)
								fieldsNode.appendChild(mFieldNode);
						}
						// 添加的新的字段
						else//(field instanceof MDField))
						{
							Element fieldNode = createNormalField(doc, field);
							fieldsNode.appendChild(fieldNode);
						}
					}
				}
				else
				{
					for (int j = 0; j < fields.length; j++) 
					{
						Field field = fields[j];
						Element fieldNode = createNormalField(doc, field);
						fieldsNode.appendChild(fieldNode);
					}	
				}
			}
			
			// <Listeners>
			Map<String, JsListenerConf> jsListeners = ds.getListenerMap();
			if(jsListeners != null && jsListeners.size() > 0)
				PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), dsNode);
			
			//Events
			AMCUtil.addEvents(doc, ds.getEventConfs(), dsNode);
		}
		else{
			Map<String, JsListenerConf> jsListeners = ds.getListenerMap();
			boolean saveFlag = false;
			for (Iterator<String> itwd = jsListeners.keySet().iterator(); itwd.hasNext();) {
				String listenerId = (String) itwd.next();
				JsListenerConf listener = jsListeners.get(listenerId);
				if(listener.getFrom() == null){
					saveFlag = true;
					break;
				}
				else if(listener.getConfType() != null  && listener.getConfType().equals(WebElement.CONF_DEL)){
					saveFlag = true;
					break;
				}
			}
			if(saveFlag){
				if(jsListeners != null && jsListeners.size() > 0){
					dsNode = doc.createElement("Dataset");
					dsNode.setAttribute("id", ds.getId());
					dsNode.setAttribute("confType", ds.getConfType());
					parentNode.appendChild(dsNode);
					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), dsNode);
					
					//Events
					AMCUtil.addEvents(doc, ds.getEventConfs(), dsNode);
				}
			}
		}
			
	}

	/**
	 * 判断field是否被修改过
	 * @param field
	 * @param fields
	 * @return
	 */
	private Element convertModifiedField(Field field, FieldSet fields, Document doc) {
		Field poolField = fields.getField(field.getId());
		
		if(poolField == null)
			return null;
		Object dftValue = poolField.getDefaultValue();
		Element node = null;
		if((field.getDefaultValue() != null && dftValue == null) || (field.getDefaultValue() != null && dftValue != null && !field.getDefaultValue().equals(dftValue)))
		{
			node = doc.createElement("ModifyField");
			node.setAttribute("defaultValue", field.getDefaultValue().toString());
		}
		
		String i18nName = poolField.getI18nName();
		if((field.getI18nName() != null && i18nName == null) || (field.getI18nName() != null && i18nName != null && !field.getI18nName().equals(i18nName)))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("i18nName", field.getI18nName());
		}
		
		String text = poolField.getText();
		if((field.getText() != null && text == null) || (field.getText() != null && text != null && !field.getText().equals(text)))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("text", field.getText());
		}
		
		String editorFormular = poolField.getEditFormular();
		if((field.getEditFormular() != null && editorFormular == null) || (field.getEditFormular() != null 
				&& editorFormular != null && !field.getEditFormular().equals(editorFormular)))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("editFormular", JsURLEncoder.encode(field.getEditFormular(), "UTF-8"));
		}
		
		String validateFormular = poolField.getValidateFormula();
		if((field.getValidateFormula() != null && validateFormular == null) || (field.getValidateFormula() != null 
				&& validateFormular != null && !field.getValidateFormula().equals(validateFormular)))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("validateFormula", JsURLEncoder.encode(field.getValidateFormula(), "UTF-8"));
		}
		String formatter = poolField.getFormater();
		if((field.getFormater() != null && formatter == null) || (field.getFormater() != null 
				&& formatter != null && !field.getFormater().equals(formatter)))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("formater", JsURLEncoder.encode(field.getFormater(), "UTF-8"));
		}
		
		String maxValue = poolField.getMaxValue();
		if((field.getMaxValue() != null && maxValue == null) || (field.getMaxValue() != null 
				&& maxValue != null && !field.getMaxValue().equals(maxValue)))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("maxValue", field.getMaxValue());
		}
		
		String minValue = poolField.getMinValue();
		if((field.getMinValue() != null && minValue == null) || (field.getMinValue() != null 
				&& minValue != null && !field.getMinValue().equals(minValue)))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("minValue", field.getMinValue());
		}
		
		String presion = poolField.getPrecision();
		if((field.getPrecision() != null && presion == null) || (field.getPrecision() != null 
				&& presion != null && !field.getPrecision().equals(presion)))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("precision", field.getPrecision());
		}
		
		Boolean nullAble = poolField.isNullAble();
		if((field.isNullAble() != nullAble))
		{
			if(node == null)
				node = doc.createElement("ModifyField");
			node.setAttribute("nullAble", String.valueOf(field.isNullAble()));
		}
		
		if(node != null)
			node.setAttribute("id", poolField.getId());
		return node;
	}
	
	
	private void addFieldRelations(Document doc, FieldRelation[] fieldRels, Element parentNode) {
		if(fieldRels != null && fieldRels.length > 0)
		{
			Element fieldRelsNode = doc.createElement("FieldRelations");
			parentNode.appendChild(fieldRelsNode);
			
			for (int j = 0; j < fieldRels.length; j++) 
			{
				FieldRelation fieldRel = fieldRels[j];
				Element fieldRelNode = doc.createElement("FieldRelation");
				fieldRelsNode.appendChild(fieldRelNode);
				
				if(isNotNullString(fieldRel.getId()))
					fieldRelNode.setAttribute("id", fieldRel.getId());
				if(isNotNullString(fieldRel.getRefDataset()))
					fieldRelNode.setAttribute("refDataset", fieldRel.getRefDataset());
				
				MatchField[] matchFields = fieldRel.getMatchFields();
				if(matchFields != null && matchFields.length > 0)
				{
					Element matchFieldsNode = doc.createElement("MatchFields");
					fieldRelNode.appendChild(matchFieldsNode);
					for (int k = 0; k < matchFields.length; k++) {
						MatchField matchField = matchFields[k];
						Element fieldNode = doc.createElement("Field");
						if(isNotNullString(matchField.getReadField()))
							fieldNode.setAttribute("readField", matchField.getReadField());
						if(isNotNullString(matchField.getWriteField()))
							fieldNode.setAttribute("writeField", matchField.getWriteField());
						if(isNotNullString(matchField.getIscontains()))
							fieldNode.setAttribute("ismatch", matchField.getIscontains());
						if(isNotNullString(matchField.getIscontains()))
							fieldNode.setAttribute("iscontains", matchField.getIscontains());
						matchFieldsNode.appendChild(fieldNode);
					}
				}
				
				WhereField whereField = fieldRel.getWhereField();
				if(whereField != null)
				{
					Element whereFieldsNode = doc.createElement("WhereField");
					fieldRelNode.appendChild(whereFieldsNode);
					Element fieldNode = doc.createElement("Field");
					if(isNotNullString(whereField.getKey()))
						fieldNode.setAttribute("key", whereField.getKey());
					if(isNotNullString(whereField.getValue()))
						fieldNode.setAttribute("value", whereField.getValue());
					whereFieldsNode.appendChild(fieldNode);
				}
				
				List<FieldRelation> childRels = fieldRel.getChildRelationList();
				if(childRels != null && childRels.size() > 0)
					addChildFieldRelations(doc, childRels.toArray(new FieldRelation[0]), fieldRelNode);
			}
		}
	}
	
	private void addChildFieldRelations(Document doc, FieldRelation[] fieldRels, Element parentNode) {
		if(fieldRels != null && fieldRels.length > 0)
		{
					
			for (int j = 0; j < fieldRels.length; j++) 
			{
				FieldRelation fieldRel = fieldRels[j];
				Element fieldRelNode = doc.createElement("FieldRelation");
				parentNode.appendChild(fieldRelNode);
				
				if(isNotNullString(fieldRel.getId()))
					fieldRelNode.setAttribute("id", fieldRel.getId());
				if(isNotNullString(fieldRel.getRefDataset()))
					fieldRelNode.setAttribute("refDataset", fieldRel.getRefDataset());
				
				MatchField[] matchFields = fieldRel.getMatchFields();
				if(matchFields != null && matchFields.length > 0)
				{
					Element matchFieldsNode = doc.createElement("MatchFields");
					fieldRelNode.appendChild(matchFieldsNode);
					for (int k = 0; k < matchFields.length; k++) {
						MatchField matchField = matchFields[k];
						Element fieldNode = doc.createElement("Field");
						if(isNotNullString(matchField.getReadField()))
							fieldNode.setAttribute("readField", matchField.getReadField());
						if(isNotNullString(matchField.getWriteField()))
							fieldNode.setAttribute("writeField", matchField.getWriteField());
						if(isNotNullString(matchField.getIscontains()))
							fieldNode.setAttribute("ismatch", matchField.getIscontains());
						if(isNotNullString(matchField.getIscontains()))
							fieldNode.setAttribute("iscontains", matchField.getIscontains());
						matchFieldsNode.appendChild(fieldNode);
					}
				}
				
				WhereField whereField = fieldRel.getWhereField();
				if(whereField != null)
				{
					Element whereFieldsNode = doc.createElement("WhereField");
					fieldRelNode.appendChild(whereFieldsNode);
					Element fieldNode = doc.createElement("Field");
					if(isNotNullString(whereField.getKey()))
						fieldNode.setAttribute("key", whereField.getKey());
					if(isNotNullString(whereField.getValue()))
						fieldNode.setAttribute("value", whereField.getValue());
					whereFieldsNode.appendChild(fieldNode);
				}
				
				List<FieldRelation> childRels = fieldRel.getChildRelationList();
				if(childRels != null && childRels.size() > 0)
					addChildFieldRelations(doc, childRels.toArray(new FieldRelation[0]), fieldRelNode);
			}
		}
	}
	
	private Element createNormalField(Document doc, Field field) {
		Element fieldNode = doc.createElement("Field");
		if(isNotNullString(field.getId()))
			fieldNode.setAttribute("id", field.getId());
		if(isNotNullString(field.getField()))
			fieldNode.setAttribute("field", field.getField());
		if(isNotNullString(field.getText()))
			fieldNode.setAttribute("text", field.getText());
		if(isNotNullString(field.getLangDir()))
			fieldNode.setAttribute("langDir", field.getLangDir());
		if(isNotNullString(field.getDataType()))
			fieldNode.setAttribute("dataType", field.getDataType());
		if(isNotNullString(field.getI18nName()))
			fieldNode.setAttribute("i18nName", field.getI18nName());
		if(isNotNullString(field.getFormater()))
			fieldNode.setAttribute("formater", field.getFormater());
		if(isNotNullString(field.getPrecision()))
			fieldNode.setAttribute("precision", field.getPrecision());
		if(isNotNullString(field.getMaxValue()))
			fieldNode.setAttribute("maxValue", field.getMaxValue());
		if(isNotNullString(field.getMinValue()))
			fieldNode.setAttribute("minValue", field.getMinValue());
		if(isNotNullString(field.getEditFormular()))
			try {
				fieldNode.setAttribute("editFormular", JsURLEncoder.encode(field.getEditFormular(), "UTF-8"));
			} catch (Exception e) {
				Logger.error(e, e);
		}
		if(isNotNullString(field.getValidateFormula()))
			try {
				fieldNode.setAttribute("validateFormula", JsURLEncoder.encode(field.getValidateFormula(), "UTF-8"));
			} catch (Exception e) {
				Logger.error(e, e);
		}
		if(field.getDefaultValue() != null)
			fieldNode.setAttribute("defaultValue", String.valueOf(field.getDefaultValue()));
		if(field.getSourceField() != null)
			fieldNode.setAttribute("sourceField", String.valueOf(field.getSourceField()));
		fieldNode.setAttribute("primaryKey", String.valueOf(field.isPrimaryKey()));
		fieldNode.setAttribute("nullAble", String.valueOf(field.isNullAble()));
		return fieldNode;
	}

	@Override
	public void serialize(Digester digester) {
		String viewModelDatasetClassName = Dataset.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/Dataset", viewModelDatasetClassName);
		digester.addSetProperties("Widget/Models/Datasets/Dataset");
		
		String pubDatasetClassName = PubDataset.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/PubDataset", pubDatasetClassName);
		digester.addSetProperties("Widget/Models/Datasets/PubDataset");
	
		String mdDatasetClassName = MdDataset.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/MdDataset", mdDatasetClassName);
		digester.addSetProperties("Widget/Models/Datasets/MdDataset");
		
		String refDatasetClassName = RefDataset.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/RefDataset", refDatasetClassName);
		digester.addSetProperties("Widget/Models/Datasets/RefDataset");
		
		String refPubDatasetClassName = RefPubDataset.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/RefPubDataset", refPubDatasetClassName);
		digester.addSetProperties("Widget/Models/Datasets/RefPubDataset");
		
		String refMdDatasetClassName = RefMdDataset.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/RefMdDataset", refMdDatasetClassName);
		digester.addSetProperties("Widget/Models/Datasets/RefMdDataset");
		
		
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/Dataset", Dataset.class);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/PubDataset", PubDataset.class);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/MdDataset", MdDataset.class);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/RefDataset", RefDataset.class);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/RefPubDataset", RefPubDataset.class);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/RefMdDataset", RefMdDataset.class);
		
		initDatasetFieldRelationRule(digester);
		
		initDatasetFields(digester);
		
		digester.addSetNext("Widget/Models/Datasets/Dataset", "addDataset", viewModelDatasetClassName);
		digester.addSetNext("Widget/Models/Datasets/PubDataset", "addDataset", pubDatasetClassName);
		digester.addSetNext("Widget/Models/Datasets/MdDataset", "addDataset", mdDatasetClassName);
		digester.addSetNext("Widget/Models/Datasets/RefDataset", "addDataset", refDatasetClassName);
		digester.addSetNext("Widget/Models/Datasets/RefPubDataset", "addDataset", refPubDatasetClassName);
		digester.addSetNext("Widget/Models/Datasets/RefMdDataset", "addDataset", refMdDatasetClassName);
		
		ListenersParser.parseListeners(digester, "Widget/Models/Datasets/Dataset/Listeners", Dataset.class);
		ListenersParser.parseListeners(digester, "Widget/Models/Datasets/PubDataset/Listeners", PubDataset.class);
		ListenersParser.parseListeners(digester, "Widget/Models/Datasets/MdDataset/Listeners", MdDataset.class);
		
		EventConfParser.parseEvents(digester, "Widget/Models/Datasets/Dataset", Dataset.class);
		EventConfParser.parseEvents(digester, "Widget/Models/Datasets/PubDataset", PubDataset.class);
		EventConfParser.parseEvents(digester, "Widget/Models/Datasets/MdDataset", MdDataset.class);
	}
	
	private void initDatasetFields(Digester digester) {
		
		String fieldsetClassName=FieldSet.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/Dataset/Fields", fieldsetClassName);
		digester.addSetProperties("Widget/Models/Datasets/Dataset/Fields");
		
		digester.addObjectCreate("Widget/Models/Datasets/PubDataset/Fields", fieldsetClassName);
		digester.addSetProperties("Widget/Models/Datasets/PubDataset/Fields");
		
		digester.addObjectCreate("Widget/Models/Datasets/MdDataset/Fields", fieldsetClassName);
		digester.addSetProperties("Widget/Models/Datasets/MdDataset/Fields");
		
		// 普通dataset的field配置
		String fieldClassName=Field.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/Dataset/Fields/Field", fieldClassName);
		digester.addSetProperties("Widget/Models/Datasets/Dataset/Fields/Field");
		digester.addSetNext("Widget/Models/Datasets/Dataset/Fields/Field", "addField", fieldClassName);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/Dataset/Fields/Field", Field.class);
		
		// pubdataset的field配置
		String modifyFieldClassName = ModifyField.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/PubDataset/Fields/ModifyField", modifyFieldClassName);
		digester.addSetProperties("Widget/Models/Datasets/PubDataset/Fields/ModifyField");
		digester.addSetNext("Widget/Models/Datasets/PubDataset/Fields/ModifyField", "addField", modifyFieldClassName);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/PubDataset/Fields/ModifyField", ModifyField.class);
		
		digester.addObjectCreate("Widget/Models/Datasets/PubDataset/Fields/Field", fieldClassName);
		digester.addSetProperties("Widget/Models/Datasets/PubDataset/Fields/Field");
		digester.addSetNext("Widget/Models/Datasets/PubDataset/Fields/Field", "addField", fieldClassName);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/PubDataset/Fields/Field", Field.class);
		
		// MdDataset的field配置
		digester.addObjectCreate("Widget/Models/Datasets/MdDataset/Fields/ModifyField", modifyFieldClassName);
		digester.addSetProperties("Widget/Models/Datasets/MdDataset/Fields/ModifyField");
		digester.addSetNext("Widget/Models/Datasets/MdDataset/Fields/ModifyField", "addField", modifyFieldClassName);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/MdDataset/Fields/ModifyField", ModifyField.class);
		
		digester.addObjectCreate("Widget/Models/Datasets/MdDataset/Fields/Field", fieldClassName);
		digester.addSetProperties("Widget/Models/Datasets/MdDataset/Fields/Field");
		digester.addSetNext("Widget/Models/Datasets/MdDataset/Fields/Field", "addField", fieldClassName);
		AttributesParser.parseAttributes(digester, "Widget/Models/Datasets/MdDataset/Fields/Field", Field.class);
		
		digester.addSetNext("Widget/Models/Datasets/Dataset/Fields", "setFieldSet", fieldsetClassName);
		digester.addSetNext("Widget/Models/Datasets/PubDataset/Fields", "setFieldSet", fieldsetClassName);
		digester.addSetNext("Widget/Models/Datasets/MdDataset/Fields", "setFieldSet", fieldsetClassName);
	}

	private void initDatasetFieldRelationRule(Digester digester) {
		
		String fieldRelationsClazz = FieldRelations.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/Dataset/FieldRelations", fieldRelationsClazz);
		digester.addSetProperties("Widget/Models/Datasets/Dataset/FieldRelations");
		digester.addSetNext("Widget/Models/Datasets/Dataset/FieldRelations", "setFieldRelations", fieldRelationsClazz);
		
		digester.addObjectCreate("Widget/Models/Datasets/PubDataset/FieldRelations", fieldRelationsClazz);
		digester.addSetProperties("Widget/Models/Datasets/PubDataset/FieldRelations");
		digester.addSetNext("Widget/Models/Datasets/PubDataset/FieldRelations", "setFieldRelations", fieldRelationsClazz);
		
		digester.addObjectCreate("Widget/Models/Datasets/MdDataset/FieldRelations", fieldRelationsClazz);
		digester.addSetProperties("Widget/Models/Datasets/MdDataset/FieldRelations");
		digester.addSetNext("Widget/Models/Datasets/MdDataset/FieldRelations", "setFieldRelations", fieldRelationsClazz);
		
		String fieldrelationClassName = FieldRelation.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation", fieldrelationClassName);
		digester.addSetProperties("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation");
		
		digester.addObjectCreate("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation", fieldrelationClassName);
		digester.addSetProperties("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation");
		
		digester.addObjectCreate("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation", fieldrelationClassName);
		digester.addSetProperties("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation");

		String matchFieldClassName=MatchField.class.getName();
		String whereFieldsClassName=WhereField.class.getName();

		digester.addSetNext("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation", "addFieldRelation", fieldrelationClassName);
		digester.addSetNext("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation", "addFieldRelation", fieldrelationClassName);
		digester.addSetNext("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation", "addFieldRelation", fieldrelationClassName);

		digester.addObjectCreate("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation/MatchFields/Field", matchFieldClassName);
		digester.addSetProperties("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation/MatchFields/Field");
		digester.addSetNext("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation/MatchFields/Field", "addMatchField", matchFieldClassName);
		
		digester.addObjectCreate("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation/MatchFields/Field", matchFieldClassName);
		digester.addSetProperties("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation/MatchFields/Field");
		digester.addSetNext("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation/MatchFields/Field", "addMatchField", matchFieldClassName);
		
		digester.addObjectCreate("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation/MatchFields/Field", matchFieldClassName);
		digester.addSetProperties("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation/MatchFields/Field");
		digester.addSetNext("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation/MatchFields/Field", "addMatchField", matchFieldClassName);
		
		digester.addObjectCreate("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation/WhereField/Field", whereFieldsClassName);
		digester.addSetProperties("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation/WhereField/Field");
		digester.addSetNext("Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation/WhereField/Field", "setWhereField", whereFieldsClassName);
		
		digester.addObjectCreate("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation/WhereField/Field", whereFieldsClassName);
		digester.addSetProperties("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation/WhereField/Field");
		digester.addSetNext("Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation/WhereField/Field", "setWhereField", whereFieldsClassName);
		
		digester.addObjectCreate("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation/WhereField/Field", whereFieldsClassName);
		digester.addSetProperties("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation/WhereField/Field");
		digester.addSetNext("Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation/WhereField/Field", "setWhereField", whereFieldsClassName);
		
		String base1Path = "Widget/Models/Datasets/Dataset/FieldRelations/FieldRelation";
		String base2Path = "Widget/Models/Datasets/PubDataset/FieldRelations/FieldRelation";
		String base3Path = "Widget/Models/Datasets/MdDataset/FieldRelations/FieldRelation";
		String[] basePath = new String[]{base1Path,base2Path, base3Path};
		for(int j = 0; j < basePath.length; j++)
		{
			//暂时支持4层
			for(int i = 0; i < 1; i ++)
			{
				String path = basePath[j] + "/FieldRelation";
				digester.addObjectCreate(path, fieldrelationClassName);
				digester.addSetProperties(path);
				digester.addSetNext(path, "addChildRelation", fieldrelationClassName);
				digester.addObjectCreate(path + "/MatchFields/Field", matchFieldClassName);
				digester.addSetProperties(path + "/MatchFields/Field");
				digester.addSetNext(path + "/MatchFields/Field", "addMatchField", matchFieldClassName);
				digester.addObjectCreate(path + "/WhereField/Field", whereFieldsClassName);
				digester.addSetProperties(path + "/WhereField/Field");
				digester.addSetNext(path + "/WhereField/Field", "setWhereField", whereFieldsClassName);
			}
		}	
	}
}
