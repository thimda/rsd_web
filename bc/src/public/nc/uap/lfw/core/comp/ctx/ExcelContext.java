package nc.uap.lfw.core.comp.ctx;

public class ExcelContext extends BaseContext {
	private static final long serialVersionUID = -1969504825610051676L;
	private boolean enabled;
	private boolean editable;
	// ��ʾ��ID�ַ������á�,���ָ�м䲻���пո������ں�̨����ǰ̨����context
	private String showColumns;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getShowColumns() {
		return showColumns;
	}

	public void setShowColumns(String showColumns) {
		this.showColumns = showColumns;
	}
}
