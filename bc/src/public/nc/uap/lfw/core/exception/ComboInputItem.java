package nc.uap.lfw.core.exception;

import nc.uap.lfw.core.combodata.ComboData;

public class ComboInputItem extends InputItem {
	private static final long serialVersionUID = -8230849515897991756L;
	private ComboData comboData;
	private boolean selectOnly;
	
	public ComboInputItem(String inputId, String labelText, boolean required)
	{
		super(inputId, labelText, required);
	}
	
	@Override
	public String getInputType() {
		return COMBO_TYPE;
	}


	public ComboData getComboData() {
		return comboData;
	}


	public void setComboData(ComboData comboData) {
		this.comboData = comboData;
	}


	public boolean isSelectOnly() {
		return selectOnly;
	}


	public void setSelectOnly(boolean selectOnly) {
		this.selectOnly = selectOnly;
	}

}
