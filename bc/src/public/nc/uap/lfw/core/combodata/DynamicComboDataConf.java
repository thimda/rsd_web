package nc.uap.lfw.core.combodata;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 动态下拉列表控件的配置建模类，在配置解析时使用该类进行数据状态，
 * 另外，该类作为代理，将用户配置的动态下拉列表框类进行实例化。
 *
 */
public class DynamicComboDataConf extends DynamicComboData {

	private static final long serialVersionUID = -1023276292907278981L;
	
	private String className = null;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public CombItem[] createCombItems() {
		
		if(this.className == null)
			return null;
		
		try {
			DynamicComboData dynamic = (DynamicComboData)Class.forName(className).newInstance();
			return dynamic.getAllCombItems();
			
		} catch (Exception e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
	
	public void validate(){
		StringBuffer buffer = new StringBuffer();
		if(this.getId() == null || this.getId().equals("")){
			buffer.append("动态下拉数据ID不能为空!\r\n");
		}
		if(this.getClassName() == null || this.getClassName().equals("")){
			buffer.append("动态下拉数据的实现类不能为空!\r\n");
		}
		if(buffer.length() > 0)
			throw new  LfwRuntimeException(buffer.toString());
	}	
}
