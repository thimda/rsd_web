package nc.uap.lfw.jsp.uimeta;

import java.io.Serializable;

public class UIMenuGroupItem extends UILayoutPanel {
	private static final long serialVersionUID = 9211264854706138210L;
	public static final String STATE = "state";
	
	public Integer getState(){
		return Integer.valueOf(getAttribute(STATE).toString());
	}
	
	public void setState(Integer value){
		setAttribute(STATE, value);
	}
	
	@Override
	public Serializable getAttribute(String key) {
		Serializable obj = super.getAttribute(key);
		if(key.equals(STATE)){
			if(obj == null)
				return new Integer(-1);
		}
		return obj;
	}
	

}
