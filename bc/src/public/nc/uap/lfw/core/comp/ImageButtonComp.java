package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ImageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 图片按钮配置类
 * @author gd 2007-01-29
 *
 */
public class ImageButtonComp extends WebComponent {
	private static final long serialVersionUID = -4505934238986111767L;
	private String command;
	private String tip;
	private String text;
	private String refImg1;
	private String refImg2;

	// 图片路径是否改变
	private boolean refImg1Changed = true;
	// 图片真实路径，在context中使用
	private String realRefImg1;
	// 图片路径是否改变
	private boolean refImg2Changed = true;
	// 图片真实路径，在context中使用
	private String realRefImg2;
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getRefImg1() {
		return refImg1;
	}
	public void setRefImg1(String refImg1) {
		this.refImg1 = refImg1;
	}
	public String getRefImg2() {
		return refImg2;
	}
	public void setRefImg2(String refImg2) {
		this.refImg2 = refImg2;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	
	public boolean isRefImg1Changed() {
		return refImg1Changed;
	}
	
	public void setRefImg1Changed(boolean refImg1Changed) {
		this.refImg1Changed = refImg1Changed;
	}
	
	public String getRealRefImg1() {
		if (isRefImg1Changed()) {
			realRefImg1 = getRealImgPath(this.refImg1, null);
			setRefImg1Changed(false);
		}
		return realRefImg1;
	}
	
	public void setRealRefImg1(String realRefImg1) {
		this.realRefImg1 = realRefImg1;
	}
	
	public boolean isRefImg2Changed() {
		return refImg2Changed;
	}
	
	public void setRefImg2Changed(boolean refImg2Changed) {
		this.refImg2Changed = refImg2Changed;
	}
	
	public String getRealRefImg2() {
		if (isRefImg2Changed()) {
			realRefImg2 = getRealImgPath(this.refImg2, null);
			setRefImg2Changed(false);
		}
		return realRefImg2;
	}
	
	public void setRealRefImg2(String realRefImg2) {
		this.realRefImg2 = realRefImg2;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	
	@Override
	public BaseContext getContext() {
		ImageContext ctx = new ImageContext();
		ctx.setId(this.getId());
		ctx.setEnabled(this.enabled);
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ImageContext imgCtx = (ImageContext) ctx;
		this.setEnabled(imgCtx.isEnabled());
		this.setCtxChanged(false);
	}
}
