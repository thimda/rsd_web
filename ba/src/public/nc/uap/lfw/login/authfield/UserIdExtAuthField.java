package nc.uap.lfw.login.authfield;

/**
 * �û�����֤�ֶ�
 * 
 * @author licza
 * 
 */
public class UserIdExtAuthField extends TextExtAuthField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7249869838310836638L;
	public UserIdExtAuthField(String label, String name, boolean required) {
		super(label, name, required);
	}
	@Override
	public int getType() {
		return ExtAuthFiledTypeConst.TYPE_USERID;
	}
}
