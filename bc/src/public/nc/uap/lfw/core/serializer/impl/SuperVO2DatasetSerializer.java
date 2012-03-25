package nc.uap.lfw.core.serializer.impl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwBeanUtil;
import nc.uap.lfw.core.common.DataTypeTranslator;
import nc.uap.lfw.core.common.IntDataTypeConst;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.serializer.IObject2DatasetSerializer;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
public class SuperVO2DatasetSerializer implements IObject2DatasetSerializer<SuperVO[]> {
	@Override public void serialize(SuperVO[] vos, Dataset ds) {
		serialize(vos, ds, -1);
	}
	public void serialize(SuperVO[] vos, Dataset ds, int state) {
		PaginationInfo pInfo = null;
		try {
			RowSet rs = ds.getCurrentRowSet();
			if (rs == null) {
				rs = ds.getRowSet(Dataset.MASTER_KEY, true);
			}
			pInfo = rs.getPaginationInfo();
			if (vos != null && pInfo.getPageSize() == -1) { // 不分页时，设置recordsCount为vos的数量
				pInfo.setRecordsCount(vos.length);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		serialize(ds.getCurrentKey(), pInfo, vos, ds, state);
	}
	public void serialize(String keyValue, PaginationInfo pInfo, SuperVO[] vos, Dataset ds, int state) {
		serialize(keyValue, pInfo, vos, ds, state, true);
	}
	public void serialize(SuperVO[] vos, Dataset ds, boolean clean) {
		serialize(vos, ds, -1, clean);
	}
	public void serialize(SuperVO[] vos, Dataset ds, int state, boolean clean) {
		PaginationInfo pInfo = null;
		try {
			pInfo = ds.getCurrentRowSet().getPaginationInfo();
			if (vos != null && pInfo.getPageSize() == -1) { // 不分页时，设置recordsCount为vos的数量
				pInfo.setRecordsCount(vos.length);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		serialize(ds.getCurrentKey(), pInfo, vos, ds, state, clean);
	}
	public void serialize(String keyValue, PaginationInfo pInfo, SuperVO[] vos, Dataset ds, int state, boolean clean) {
		LfwBeanUtil<SuperVO> bu = new LfwBeanUtil<SuperVO>();
		if (keyValue == null || (keyValue != null && keyValue.equals(""))) {
			keyValue = Dataset.MASTER_KEY;
			ds.setCurrentKey(keyValue);
		}
		RowSet rowSet = ds.getRowSet(keyValue);
		if (rowSet == null) {
			rowSet = new RowSet(keyValue);
			ds.addRowSet(keyValue, rowSet);
		}
		if (pInfo != null)
			rowSet.setPaginationInfo(pInfo);
		rowSet.setRowSetChanged(true);
		RowData rowData = rowSet.getCurrentRowData(true);
		if (clean)
			rowData.clear();
		if(vos != null && vos.length > pInfo.getPageSize() && pInfo != null && pInfo.getPageSize() != -1){
			for (int i = 0; i < pInfo.getPageCount(); i++) {
				for (int j = i * pInfo.getPageSize(); j < pInfo.getPageSize() * (i + 1) && j < vos.length; j++) {
					RowData rowDatanew = rowSet.getRowData(i);
					if(rowDatanew == null){
						rowDatanew = new RowData(i);
						rowSet.addRowData(i, rowDatanew);
					}
					SuperVO vo = vos[j];
					if (state == -1) {
						state = vo.getStatus();
					}
					Row row = ds.getEmptyRow();
					if (state == 3)
						row.setState(Row.STATE_FALSE_DELETED);
					else
						row.setState(state);
					rowDatanew.addRow(row);
					String rowId = bu.getUIID(vo);
					if (rowId != null)
						row.setRowId(rowId);
					vo2DataSet(vos[j], ds, row);
				}
			}
			
		}
		else{
			if (vos != null) {
				for (int i = 0; i < vos.length; i++) {
					SuperVO vo = vos[i];
					if (state == -1) {
						state = vo.getStatus();
					}
					Row row = ds.getEmptyRow();
					if (state == 3)
						row.setState(Row.STATE_FALSE_DELETED);
					else
						row.setState(state);
					rowData.addRow(row);
					String rowId = bu.getUIID(vo);
					if (rowId != null)
						row.setRowId(rowId);
					vo2DataSet(vos[i], ds, row);
				}
			}
		}
//		if (vos != null && vos.length != 0) {
//			Row[] selectedRows = ds.getSelectedRows();
//			if (selectedRows == null || selectedRows.length == 0) {
//				ds.setRowSelectIndex(0);
//			}
//		}
	}
	/**
	 * 将多个VO值转换成Dataset中的多行数据，即RowSet中的多个Row对象。 注意：该方法没有进行FieldRelation的处理
	 * 
	 * @param vos
	 * @param ds
	 * @param rows
	 * @return
	 */
	public Row[] vo2DataSet(Object[] vos, Dataset ds, Row[] rows) {
		if (vos == null || ds == null || rows == null || vos.length != rows.length)
			throw new IllegalArgumentException("参数错误！");
		for (int i = 0; i < vos.length; i++) {
			vo2DataSet(vos[i], ds, rows[i]);
		}
		return rows;
	}
	/**
	 * 将一个VO值对象根据给定的DataSet结构设置Row对象，并将Row对象 添加到Dataset中返回。
	 * 因为Row对象有很多比如状态，id,parentid等相关属性，因此需要通过 外部传入，否则需要传入很多Row对象的特征属性信息，参数会变得很多。
	 * 
	 * 注意：该方法没有进行FieldRelation的处理。
	 * 
	 * @param vo
	 * @param ds
	 * @param row
	 * @return
	 */
	public Row vo2DataSet(Object vo, Dataset ds, Row row) {
		if (vo == null || ds == null || row == null)
			throw new IllegalArgumentException("VoDataSetChangeUtil.vo2DataSet(Object vo, Dataset ds, Row row):参数错误！");
		List<CellInfoVO> list = this.voDs2CellInfoVO(vo, ds);
		for (int i = 0; i < list.size(); i++) {
			this.voAttributeValue2Row(vo, ds, row, list.get(i));
		}
		return row;
	}
	/**
	 * 获取vo与row的所有对应关系
	 * <Strong>注意：此处是以Row的元素个数为准，该ds存在多少个field，就返回多少元素，vo中不存在的属性则设置为null
	 * 。</Strong>
	 * 
	 * @param vo
	 * @param ds
	 * @param row
	 */
	public List<CellInfoVO> voDs2CellInfoVO(Object vo, Dataset ds) {
		FieldSet fieldSet = ds.getFieldSet();
		List<CellInfoVO> list = new ArrayList<CellInfoVO>();
		String key = null;
		Object value = null;
		int count = fieldSet.getFieldCount();
		for (int i = 0; i < count; i++) {
			CellInfoVO cellInfo = new CellInfoVO();
			list.add(cellInfo);
			cellInfo.setIndex(i);
			key = fieldSet.getField(i).getField();
			if (key == null || key.trim().equals(""))
				continue;
			cellInfo.setVoAttributeName(key);
			if (vo instanceof CircularlyAccessibleValueObject)
				value = ((CircularlyAccessibleValueObject) vo).getAttributeValue(key);
			else {
				String type = fieldSet.getField(i).getDataType();
				boolean isBooleanType = false;
				if (DataTypeTranslator.translateString2Int(type) == IntDataTypeConst.boolean_TYPE)
					isBooleanType = true;
				value = getAttributeValue(vo, key, isBooleanType);
			}
			cellInfo.setVoAttributeValue(value);
			cellInfo.setIndex(i);
		}
		return list;
	}
	/**
	 * 该方法实现向row中设置特定Cell的值信息，当前涉及的属性信息在CellInfoVO中 进行说明。
	 * 
	 * @param vo
	 * @param ds
	 * @param row
	 * @param cellInfo
	 */
	public void voAttributeValue2Row(Object vo, Dataset ds, Row row, CellInfoVO cellInfo) {
		row.setValue(cellInfo.getIndex(), cellInfo.getVoAttributeValue());// 可能需要塞null值，所以将判断null去掉
	}
	/**
	 * 获取某个对象的某个属性值
	 * 
	 * @param vo
	 * @param attribute
	 * @param isBooleanType
	 * @return
	 */
	private Object getAttributeValue(Object vo, String attribute, boolean isBooleanType) {
		/* 此处特殊处理NC的m_属性，有待确定是否合适和通用。 */
		if (attribute.startsWith("m_"))
			attribute = attribute.substring(2);
		String postfix = attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
		String methodName;
		if (isBooleanType)
			methodName = "is" + postfix;
		else
			methodName = "get" + postfix;
		Method method;
		try {
			method = vo.getClass().getMethod(methodName, new Class[] {});
			Object value = method.invoke(vo, new Object[] {});
			return value;
		} catch (SecurityException e) {
			throw new LfwRuntimeException(e.getMessage());
		} catch (NoSuchMethodException e) {
			return null;
		} catch (IllegalArgumentException e) {
			throw new LfwRuntimeException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new LfwRuntimeException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	public void update(SuperVO[] vos, Dataset ds) {
		LfwBeanUtil<SuperVO> bu = new LfwBeanUtil<SuperVO>();
		if (vos != null) {
			for (int i = 0; i < vos.length; i++) {
				SuperVO vo = vos[i];
				String rowId = bu.getUIID(vo);
				if (rowId == null || rowId.equals("")) {
					throw new LfwRuntimeException("不具备对应的UIID");
				}
				Row row = ds.getRowById(rowId);
				row.setState(vo.getStatus());
				vo2DataSet(vo, ds, row);
			}
		}
	}
}
