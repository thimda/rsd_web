package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.TextAreaContext;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.KeyListener;

/**
 * 自定义表单元素控件
 * 
 * @author guoweic
 *
 */
public class SelfDefElementComp extends TextComp {

	private static final long serialVersionUID = 6509930301132104402L;

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
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		TextAreaContext taCtx = (TextAreaContext) ctx;
		this.setEnabled(taCtx.isEnabled());
		this.setValue(taCtx.getValue());
		this.setCtxChanged(false);
	}
	
}
