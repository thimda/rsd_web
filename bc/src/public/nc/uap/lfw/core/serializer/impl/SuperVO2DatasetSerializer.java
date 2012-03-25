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
			if (vos != null && pInfo.getPageSize() == -1) { // ����ҳʱ������recordsCountΪvos������
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
			if (vos != null && pInfo.getPageSize() == -1) { // ����ҳʱ������recordsCountΪvos������
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
	 * �����VOֵת����Dataset�еĶ������ݣ���RowSet�еĶ��Row���� ע�⣺�÷���û�н���FieldRelation�Ĵ���
	 * 
	 * @param vos
	 * @param ds
	 * @param rows
	 * @return
	 */
	public Row[] vo2DataSet(Object[] vos, Dataset ds, Row[] rows) {
		if (vos == null || ds == null || rows == null || vos.length != rows.length)
			throw new IllegalArgumentException("��������");
		for (int i = 0; i < vos.length; i++) {
			vo2DataSet(vos[i], ds, rows[i]);
		}
		return rows;
	}
	/**
	 * ��һ��VOֵ������ݸ�����DataSet�ṹ����Row���󣬲���Row���� ��ӵ�Dataset�з��ء�
	 * ��ΪRow�����кܶ����״̬��id,parentid��������ԣ������Ҫͨ�� �ⲿ���룬������Ҫ����ܶ�Row���������������Ϣ���������úܶࡣ
	 * 
	 * ע�⣺�÷���û�н���FieldRelation�Ĵ���
	 * 
	 * @param vo
	 * @param ds
	 * @param row
	 * @return
	 */
	public Row vo2DataSet(Object vo, Dataset ds, Row row) {
		if (vo == null || ds == null || row == null)
			throw new IllegalArgumentException("VoDataSetChangeUtil.vo2DataSet(Object vo, Dataset ds, Row row):��������");
		List<CellInfoVO> list = this.voDs2CellInfoVO(vo, ds);
		for (int i = 0; i < list.size(); i++) {
			this.voAttributeValue2Row(vo, ds, row, list.get(i));
		}
		return row;
	}
	/**
	 * ��ȡvo��row�����ж�Ӧ��ϵ
	 * <Strong>ע�⣺�˴�����Row��Ԫ�ظ���Ϊ׼����ds���ڶ��ٸ�field���ͷ��ض���Ԫ�أ�vo�в����ڵ�����������Ϊnull
	 * ��</Strong>
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
	 * �÷���ʵ����row�������ض�Cell��ֵ��Ϣ����ǰ�漰��������Ϣ��CellInfoVO�� ����˵����
	 * 
	 * @param vo
	 * @param ds
	 * @param row
	 * @param cellInfo
	 */
	public void voAttributeValue2Row(Object vo, Dataset ds, Row row, CellInfoVO cellInfo) {
		row.setValue(cellInfo.getIndex(), cellInfo.getVoAttributeValue());// ������Ҫ��nullֵ�����Խ��ж�nullȥ��
	}
	/**
	 * ��ȡĳ�������ĳ������ֵ
	 * 
	 * @param vo
	 * @param attribute
	 * @param isBooleanType
	 * @return
	 */
	private Object getAttributeValue(Object vo, String attribute, boolean isBooleanType) {
		/* �˴����⴦��NC��m_���ԣ��д�ȷ���Ƿ���ʺ�ͨ�á� */
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
					throw new LfwRuntimeException("���߱���Ӧ��UIID");
				}
				Row row = ds.getRowById(rowId);
				row.setState(vo.getStatus());
				vo2DataSet(vo, ds, row);
			}
		}
	}
}
