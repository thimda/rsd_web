package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.CheckBoxContext;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.TextListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

public class CheckBoxComp extends TextComp {
	private static final long serialVersionUID = 1L;
	// 此checkbox的真实值
	private String value;
	// checkbox旁边显示的文字
	private String i18nName;
	// 初始是否被选中
	protected boolean checked = false;
	
	private String dataType = StringDataTypeConst.UFBOOLEAN;
	
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String name) {
		i18nName = name;
	}


	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		if(this.checked != checked){
			this.checked = checked;
			setCtxChanged(true);
		}
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(TextListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		CheckBoxContext checkCtx = new CheckBoxContext();
		super.getContext(checkCtx);
//		checkCtx.setEnabled(isEnabled());
		checkCtx.setChecked(isChecked());
		return checkCtx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		super.setContext(ctx);
		CheckBoxContext checkCtx = (CheckBoxContext)ctx;
//		this.setEnabled(checkCtx.isEnabled());
		this.setChecked(checkCtx.isChecked());
		setCtxChanged(false);
	}
}
