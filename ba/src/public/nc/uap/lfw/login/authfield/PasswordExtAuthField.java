package nc.uap.lfw.login.authfield;
/**
 * 密码类型外部程序验证字段
 * @author dengjt
 * 2006-4-18
 */
public class PasswordExtAuthField extends ExtAuthField {
	private static final long serialVersionUID = 515155864537380407L;
	private String defaultValue;
	public PasswordExtAuthField() {
		super();
	}
	public PasswordExtAuthField(String label, String name, boolean required) {
		super(label, name, required);
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	@Override
	public int getType() {
		return ExtAuthFiledTypeConst.TYPE_PASSWORD;
	}

}
