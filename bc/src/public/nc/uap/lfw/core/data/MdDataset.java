package nc.uap.lfw.core.data;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.design.itf.IDatasetProvider;




/**
 * ����Ԫ���ݵ�dataset
 * @author gd 2020-3-3
 *
 */
public class MdDataset  extends Dataset {
	private static final long serialVersionUID = 380090536121568068L;
	/* Ԫ����·�� */
	private String objMeta = null;
	
	/**
	 * ����objMeta��Ϣ
	 * 
	 * @param objMeta
	 */
	public void setObjMeta(String objMeta) {
		this.objMeta = objMeta;
		FieldSet fs = getFieldSet();
		try {
			if(fs.getFieldCount() == 0){
				IDatasetProvider provider = NCLocator.getInstance().lookup(IDatasetProvider.class);
				provider.getMdDataset(this);
			}
		}catch (Throwable e) {
			//�˴����ܼ�Log
		}
		
		
	}

	/**
	 * ��ȡobjMeta��ֵ
	 * 
	 * @return
	 */
	public String getObjMeta() {

		return this.objMeta;
	}

	@Override
	public FieldSet getFieldSet() {
		return super.getFieldSet();
	}

	@Override
	public void setFieldSet(FieldSet fieldSet) {
		FieldSet fs = super.getFieldSet();
		if(fs != null){
			int size = fieldSet.getFieldCount();
			for (int i = 0; i < size; i++) {
				fs.addField(fieldSet.getField(i));
			}
		}
		else
			super.setFieldSet(fieldSet);
	}
}