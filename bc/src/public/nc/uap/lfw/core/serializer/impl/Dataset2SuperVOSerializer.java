package nc.uap.lfw.core.serializer.impl;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwBeanUtil;
import nc.uap.lfw.core.common.DataTypeTranslator;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.serializer.IDataset2ObjectSerializer;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;

/**
 * Dataset到SuperVO的序列化器
 * @author gd 2010-1-28
 * @version NC6.0
 * @since NC6.0
 */
public class Dataset2SuperVOSerializer<T extends SuperVO> implements IDataset2ObjectSerializer<SuperVO[]> {

	public T[] serialize(Dataset ds, Row[] selRows) {
		// 对于superVO只序列化一个dataset
        if(ds == null || ds.getVoMeta() == null)
        	throw new IllegalArgumentException("dataset的voMeta没有配置!dsId=" + ds.getId());
		
		ArrayList<T> vos = new ArrayList<T>();
		Row[] serRows = null;
		if(selRows != null && selRows.length > 0){
			serRows = selRows;
		}
		else{
			RowData rd = ds.getCurrentRowSet().getCurrentRowData();
			if(rd == null)
				return null;
			serRows = rd.getRows();
		}
		int count = serRows.length;
		for(int i = 0; i < count; i++){
			Row row = serRows[i];
			if(row instanceof EmptyRow)
				continue;
			T vo = getSuperVO(ds, row);
			List<CellInfoVO> list = processRow2CellValue(ds, vo, row);
			int size = list.size();
			for(int j = 0; j < size; j++){
				row2VoAttributeValue(vo, list.get(j));			   
			}
			
			vo.setStatus(row.getState());
			vos.add(vo);
		}	
		if(vos.size() > 0)
			return (T[]) vos.toArray((Object[]) Array.newInstance(vos.get(0).getClass(), 0));
		else
			return (T[])vos.toArray(new SuperVO[0]);
	}
	
	protected T getSuperVO(Dataset ds, Row row) {
		if(ds.getVoMeta() != null && !ds.getVoMeta().equals(""))
		{
			Class c = LfwClassUtil.forName(ds.getVoMeta());
			LfwBeanUtil<SuperVO> bu = new LfwBeanUtil<SuperVO>();
			T vo = (T) bu.generateUIObject(c);
			String uiid = row.getRowId();
			if(uiid == null || uiid.equals(""))
				throw new LfwRuntimeException("row id can not be null");
			bu.setUIID(vo, uiid);
			return vo;
		}
		return null;
	}

	/**
	 * 将ds中的某行数据转换成vo
	 * 
	 * @param ds
	 * @param vo
	 * @param row
	 */
	protected List<CellInfoVO> processRow2CellValue(Dataset ds, Object vo ,Row row){
        int count = ds.getFieldSet().getFieldCount();
        Field field = null;
        String attribute = null, dataType = null;
        Object value = null;
        List<CellInfoVO> list = new ArrayList<CellInfoVO>();
        for(int i = 0;i < count; i++)
        {
        	field = ds.getFieldSet().getField(i);
        	attribute = field.getField();
        	dataType = field.getDataType();
        	value = row.getValue(i);
        	
        	if(attribute != null && !attribute.trim().equals(""))
        	{
        		CellInfoVO info = new CellInfoVO();
        		info.setVoAttributeName(attribute);
        		info.setVoAttributeValue(value);
        		info.setDataType(dataType);
        		info.setIndex(i);
        		list.add(info);
        	}
        }
        return list;
	}
	
	/**
	 * 为VO设置某个属性的值
	 * @param vo
	 * @param attribute
	 * @param dataType
	 * @param value
	 */
	protected void row2VoAttributeValue(Object vo, CellInfoVO info){
		String attribute = info.getVoAttributeName(); 
		String dataType = info.getDataType(); 
		Object value = info.getVoAttributeValue();
		
		Class clazz = DataTypeTranslator.translateString2Class(dataType);
		String methodName = "set"  
				             + attribute.substring(0, 1).toUpperCase() 
	                         + attribute.substring(1, attribute.length());
		try {
			Method method = null;
			try{
				method = vo.getClass().getMethod(methodName, new Class[]{clazz});
			}
			catch(NoSuchMethodException ne){
			}
			//为Object hack
			if(method == null)
				method = vo.getClass().getMethod(methodName, new Class[]{Object.class});
			method.invoke(vo, new Object[]{value});			
		} catch (SecurityException e) {	
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} catch (NoSuchMethodException e) {
			//该VO可能没有此属性，因此此处没有外抛异常信息。
			Logger.warn(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			Logger.error(e.getMessage(), e);
	        throw new LfwRuntimeException("convertor error:" + e.getMessage());		
		} catch (IllegalAccessException e) {
			Logger.error(e.getMessage(), e);
	        throw new LfwRuntimeException(e.getMessage());	
		} catch (InvocationTargetException e) {
			Logger.error(e.getMessage(), e);
	        throw new LfwRuntimeException(e.getMessage());	
		}
	}

	public T[] serialize(Dataset dataset, Row selRow) {
		if(selRow == null)
			return serialize(dataset, new Row[]{});
		return serialize(dataset, new Row[]{selRow});
	}

	public T[] serialize(Dataset dataset) {
		return serialize(dataset, new Row[0]);
	}
}
