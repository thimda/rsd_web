package nc.uap.lfw.core.ctrlfrm.seria;

public abstract class BaseSerializer<T> implements IControlSerializer<T> {
	protected boolean isNotNullString(String param){
		if(param != null && !param.equals(""))
			return true;
		else
			return false;
	}
}
