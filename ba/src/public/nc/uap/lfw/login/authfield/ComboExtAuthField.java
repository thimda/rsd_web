package nc.uap.lfw.login.authfield;
/**
 * 下拉类型验证字段
 * @author dengjt
 * 2006-4-18
 */
public class ComboExtAuthField extends ExtAuthField {
	private static final long serialVersionUID = -8079924277920349800L;
	private String[][] options = null;
	private int selectedIndex = -1;
	public ComboExtAuthField() {
		super();
	}
	public ComboExtAuthField(String label, String name,  boolean required) {
		super(label, name,  required);
	}
	public String[][] getOptions() {
		return options;
	}
	public void setOptions(String[][] options) {
		this.options = options;
	}
	public int getSelectedIndex() {
		return selectedIndex;
	}
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	@Override
	public int getType() {
		
		return ExtAuthFiledTypeConst.TYPE_CHOOSE;
	}
}
