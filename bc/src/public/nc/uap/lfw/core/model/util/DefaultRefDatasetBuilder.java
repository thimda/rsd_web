package nc.uap.lfw.core.model.util;

import java.util.ArrayList;
import java.util.Map;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.ModifyField;
import nc.uap.lfw.core.data.PubDataset;
import nc.uap.lfw.core.data.PubField;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.IDatasetBuilder;
import nc.uap.lfw.core.page.manager.PoolObjectManager;

public class DefaultRefDatasetBuilder implements IDatasetBuilder {

//	private static final String ALREADY_PUB_REF = "ALREADY_PUB_REF";

	public Dataset buildDataset(Dataset ds, Map<String, Object> paramMap) {
//		String alreadyRef = (String) ds.getExtendAttributeValue(ALREADY_PUB_REF);
//		if(alreadyRef != null)
//			return ds;
//		ds.setExtendAttribute(ALREADY_PUB_REF, "1");
		PubDataset pubDs = (PubDataset)ds;
		// 公共池中ds
		Dataset poolDs = getRefDataset(pubDs.getRefId(), paramMap);
		if(poolDs == null)
			throw new LfwRuntimeException("没有从池中找到引用的Dataset:" + pubDs.getRefId());
		
		if(poolDs.getListenerMap() != null && poolDs.getListenerMap().size() != 0)
			pubDs.getListenerMap().putAll(pubDs.getListenerMap());
		
//		if(poolDs.getPaginationInfo().getPageSize() != 0)
//			pubDs.getPaginationInfo().setPageSize(pubDs.getPaginationInfo().getPageSize());
		
		if(pubDs.getVoMeta() != null && pubDs.getVoMeta().equals("") && (poolDs.getVoMeta() != null && !poolDs.getVoMeta().equals("")))
			pubDs.setVoMeta(poolDs.getVoMeta());
		
		ArrayList<String> modifyFieldId = new ArrayList<String>();
		FieldSet pubDsFieldSet = pubDs.getFieldSet();
		int pubFieldsCount = pubDsFieldSet.getFieldCount();
		for(int i = 0; i < pubFieldsCount; i ++)
		{
			Field field = pubDsFieldSet.getField(i);
//			if(field instanceof Field)
//				pubDs.getFieldSet().addField(field);
			if(field instanceof ModifyField)
			{
				ModifyField mField = (ModifyField)field;
				Field poolField = poolDs.getFieldSet().getField(mField.getId());
				
				if(poolField != null)
				{
					PubField pubField = convertFieldToPubField(poolField);
					
					if(mField.getI18nName() != null && !mField.getI18nName().equals(""))
						pubField.setI18nName(mField.getI18nName());
					if(mField.getText() != null && !mField.getText().equals(""))
						pubField.setText(mField.getText());
					if(mField.getDefaultValue() != null && !mField.getDefaultValue().equals(""))
						pubField.setDefaultValue(mField.getDefaultValue());
					if(mField.getMaxValue() != null && !mField.getMaxValue().equals(""))
						pubField.setMaxValue(mField.getMaxValue());
					if(mField.getMinValue() != null && !mField.getMinValue().equals(""))
						pubField.setMinValue(mField.getMinValue());
					if(mField.getField() != null && !mField.getField().equals(""))
						pubField.setField(mField.getField());
					
					modifyFieldId.add(mField.getId());
					
					//pubDs.getFieldSet().removeField(mField);
					pubDs.getFieldSet().addField(pubField);
				}
			}
		}
		
		Field[] fields = pubDs.getFieldSet().getFields();
		if(fields!=null){
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if(field instanceof ModifyField)
				pubDs.getFieldSet().removeField(field);
		}
		}
		
		// 添加引用ds的没有修改过的字段
		FieldSet poolDsFieldSet = poolDs.getFieldSet();
		if(poolDsFieldSet.getFieldCount() > 0)
		{
			for(int i = 0; i < poolDsFieldSet.getFieldCount(); i ++)
			{
				Field poolField = poolDsFieldSet.getField(i);
				if(modifyFieldId.contains(poolField.getId()))
					continue;
				//如果已经添加过此字段，则不再添加
				if(pubDs.getFieldSet().getField(poolField.getId()) != null)
					continue;
				PubField pubField = convertFieldToPubField(poolField);
				pubDs.getFieldSet().addField(pubField);
			}
		}
		
		//pubDs.setFieldRelations(pubDs.getFieldRelations());
		return pubDs;
	}

	/**
	 * 获取引用的公共ds
	 * @param dsId
	 * @param paramMap 
	 * @return
	 */
	protected Dataset getRefDataset(String dsId, Map<String, Object> paramMap)
	{
		return PoolObjectManager.getDataset(dsId);
	}
	
	
	/**
	 * 将Field转换为PubField
	 * @param field
	 * @param pubField
	 * @return
	 */
	private PubField convertFieldToPubField(Field field)
	{
		PubField pubField = new PubField();
		pubField.setId(field.getId());
		pubField.setDataType(field.getDataType());
		pubField.setDefaultValue(field.getDefaultValue());
		pubField.setField(field.getField());
		pubField.setI18nName(field.getI18nName());
		pubField.setIdColName(field.getIdColName());
		pubField.setNullAble(field.isNullAble());
		pubField.setPrimaryKey(field.isPrimaryKey());
		pubField.setText(field.getText());
		pubField.setMaxValue(field.getMaxValue());
		pubField.setMinValue(field.getMinValue());
		return pubField;
	}
}
