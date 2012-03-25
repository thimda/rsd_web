package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.TextAreaContext;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.KeyListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 文本域控件后台属性配置类
 * @author gd 2007-11-26
 */
public class TextAreaComp extends TextComp {
	private static final long serialVersionUID = -5959445344434487325L;
	// 行宽
	private String rows;
	// 列宽
	private String cols;
	
	public String getCols() {
		return cols;
	}
	public void setCols(String cols) {
		this.cols = cols;
	}
	
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}

		
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(FocusListener.class);
		list.add(KeyListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		TextAreaContext ctx = new TextAreaContext();
		ctx.setId(this.getId());
		ctx.setEnabled(this.enabled);
		ctx.setValue(this.getValue());
		ctx.setFocus(this.isFocus());
		ctx.setReadOnly(this.isReadOnly());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		TextAreaContext taCtx = (TextAreaContext) ctx;
		this.setEnabled(taCtx.isEnabled());
		this.setValue(taCtx.getValue());
		this.setReadOnly(taCtx.isReadOnly());
		this.setCtxChanged(false);
		
	}
	
}
