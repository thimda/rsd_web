package nc.uap.lfw.core.comp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.SelfDefCompContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.SelfDefListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 自定义控件
 * 
 * @author guoweic
 *
 */
public class SelfDefComp extends WebComponent {

	private static final long serialVersionUID = 6926917271833717792L;

	public final static String TRIGGER_ID = "TRIGGER_ID";
	
	// 可见性
	private boolean visible = true;
	
	// 自定义的额外Context对象
	private Serializable otherCtx = null;
	
	public Object clone() {
		return super.clone();
	}

	@Override
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(SelfDefListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		SelfDefCompContext ctx = new SelfDefCompContext();
		ctx.setId(this.getId());
		ctx.setVisible(this.enabled);
		ctx.setOtherCtx(this.otherCtx);
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		SelfDefCompContext sdCtx = (SelfDefCompContext) ctx;
		this.setVisible(sdCtx.isVisible());
		this.setOtherCtx(sdCtx.getOtherCtx());
		if (sdCtx.getTriggerId() != null)
			this.setExtendAttribute(TRIGGER_ID, sdCtx.getTriggerId());
		this.setCtxChanged(false);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		if (visible != this.visible) {
			this.visible = visible;
			setCtxChanged(true);
		}
	}

	public Serializable getOtherCtx() {
		return otherCtx;
	}

	public void setOtherCtx(Serializable otherCtx) {
		if (otherCtx != this.otherCtx) {
			this.otherCtx = otherCtx;
			setCtxChanged(true);
		}
	}

}
