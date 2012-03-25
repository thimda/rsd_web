package nc.uap.lfw.login.authfield;
/**
 * Text类型验证字段
 * @author dengjt
 * 2006-4-18
 */
public class TextExtAuthField extends ExtAuthField {
	private static final long serialVersionUID = -135560490099930483L;
	private String defaultValue;
	public TextExtAuthField() {
		super();
	}
	public TextExtAuthField(String label, String name, boolean required) {
		super(label, name, required);
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getType() {
		return ExtAuthFiledTypeConst.TYPE_TEXT;
	}
}
