package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.IFrameContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;

/**
 * iframe¿Ø¼þ
 * @author zhangxya
 *
 */
public class IFrameComp extends WebComponent{
	private static final long serialVersionUID = 1L;
	private String src;
	private String name;
	private String border;
	private String frameBorder;
	private String scrolling;
	private boolean visible = true;
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		if (src != null && src != this.src) {
			this.src = src;
			setCtxChanged(true);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBorder() {
		return border;
	}
	public void setBorder(String border) {
		this.border = border;
	}
	public String getFrameBorder() {
		return frameBorder;
	}
	public void setFrameBorder(String frameBorder) {
		this.frameBorder = frameBorder;
	}
	public String getScrolling() {
		return scrolling;
	}
	public void setScrolling(String scrolling) {
		this.scrolling = scrolling;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			setCtxChanged(true);
		}
	}
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		IFrameContext ctx = new IFrameContext();
		ctx.setSrc(this.getSrc());
		ctx.setVisible(this.isVisible());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		IFrameContext iframeCtx = (IFrameContext) ctx;
		this.setSrc(iframeCtx.getSrc());
		this.setVisible(iframeCtx.isVisible());
		this.setCtxChanged(false);
	}

}
