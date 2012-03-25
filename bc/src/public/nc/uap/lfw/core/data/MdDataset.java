package nc.uap.lfw.core.data;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.design.itf.IDatasetProvider;




/**
 * 来自元数据的dataset
 * @author gd 2020-3-3
 *
 */
public class MdDataset  extends Dataset {
	private static final long serialVersionUID = 380090536121568068L;
	/* 元数据路径 */
	private String objMeta = null;
	
	/**
	 * 设置objMeta信息
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
			//此处不能记Log
		}
		
		
	}

	/**
	 * 获取objMeta的值
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