package nc.uap.lfw.login.authfield;
/**
 * 日期类型外部程序验证字段
 * @author dengjt
 * 2006-4-18
 */
public class DateExtAuthField extends ExtAuthField {

	private static final long serialVersionUID = -1166790591156585576L;
	private String defaultValue;
	public DateExtAuthField(String label, String name, boolean required) {
		super(label, name,  required);
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	@Override
	public int getType() {
		
		return ExtAuthFiledTypeConst.TYPE_DATE;
	}
}
