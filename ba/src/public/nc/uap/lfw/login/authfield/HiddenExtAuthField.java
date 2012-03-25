package nc.uap.lfw.login.authfield;
/**
 * 隐藏类型外部程序验证字段
 * @author dengjt
 * 2006-4-26
 */
public class HiddenExtAuthField extends ExtAuthField{

	private static final long serialVersionUID = -5366676298638556391L;
	
	private String defaultValue;
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}
	
	public HiddenExtAuthField()
	{
		
	}
	
	public HiddenExtAuthField(String name, boolean required)
	{
		super("", name,  required);
	}
	//隐藏控件值

	@Override
	public int getType() {
		return ExtAuthFiledTypeConst.TYPE_HIDDEN;
	}

}
