package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ImageContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * Image控件配置
 * @author dengjt
 *
 */
public class ImageComp extends WebComponent {

	private static final long serialVersionUID = 5777383343036006996L;
	private String alt = "";
	private String image1;
	private String image2;
	// 图片路径是否改变
	private boolean image1Changed = true;
	// 图片真实路径，在context中使用
	private String realImage1;
	// 图片路径是否改变
	private boolean image2Changed = true;
	// 图片真实路径，在context中使用
	private String realImage2;

	private String imageInact;
	private boolean floatRight = false;
	private boolean floatLeft = false;
	
//	public String getWidth() {
//		if(width != null && "100%".equals(width))
//			width = "";
//		return width;
//	}
//	
//	public String getHeight() {
//		if(height != null && "100%".equals(height))
//			height = "";
//		return height;
//	}
	
	
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public boolean isFloatLeft() {
		return floatLeft;
	}
	public void setFloatLeft(boolean floatLeft) {
		this.floatLeft = floatLeft;
	}
	public boolean isFloatRight() {
		return floatRight;
	}
	public void setFloatRight(boolean floatRight) {
		this.floatRight = floatRight;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		if (!image1.equals(this.image1)) {
			this.image1 = image1;
			setImage1Changed(true);
			addCtxChangedProperty("image1");
			setCtxChanged(true);
		}
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		if (!image2.equals(this.image2)) {
			this.image2 = image2;
			setImage2Changed(true);
			addCtxChangedProperty("image2");
			setCtxChanged(true);
		}
	}
	public String getImageInact() {
		return imageInact;
	}
	public void setImageInact(String imageInact) {
		this.imageInact = imageInact;
	}	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	
	public boolean isImage1Changed() {
		return image1Changed;
	}
	
	public void setImage1Changed(boolean image1Changed) {
		this.image1Changed = image1Changed;
	}
	
	public String getRealImage1() {
		if (isImage1Changed()) {
			realImage1 = getRealImgPath(this.image1, null);
			setImage1Changed(false);
		}
		return realImage1;
	}
	
	public void setRealImage1(String realImage1) {
		this.realImage1 = realImage1;
	}
	
	public boolean isImage2Changed() {
		return image2Changed;
	}
	
	public void setImage2Changed(boolean image2Changed) {
		this.image2Changed = image2Changed;
	}
	
	public String getRealImage2() {
		if (isImage2Changed()) {
			realImage2 = getRealImgPath(this.image2, null);
			setImage2Changed(false);
		}
		return realImage2;
	}
	
	public void setRealImage2(String realImage2) {
		this.realImage2 = realImage2;
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		ImageContext ctx = new ImageContext();
		ctx.setId(this.getId());
		ctx.setEnabled(this.enabled);
		if (checkCtxPropertyChanged("image1"))
			ctx.setImage1(this.getRealImage1());
		if (checkCtxPropertyChanged("image2"))
			ctx.setImage2(this.getRealImage2());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ImageContext imgCtx = (ImageContext) ctx;
		this.setEnabled(imgCtx.isEnabled());
		this.setCtxChanged(false);
	}
}
