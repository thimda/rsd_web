package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.RadioContext;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 单选按钮后台配置类
 * @author gd 2007-11-26
 */
public class RadioComp extends TextComp{
	private static final long serialVersionUID = 1531534959905591230L;
	// 此radio的真实值
	private String value;
	// radio旁边显示的文字
	private String text;
	// 初始是否被选中
	private boolean checked = false;
	// radio所属的组
	private String group;
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		if(this.checked != checked){
			this.checked = checked;
			setCtxChanged(true);
		}
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		RadioContext ctx = new RadioContext();
		ctx.setEnabled(isEnabled());
		ctx.setChecked(isChecked());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		RadioContext radioctx = (RadioContext)ctx;
		this.setEnabled(radioctx.isEnabled());
		this.setChecked(radioctx.isChecked());
		setCtxChanged(false);
	}
}
