package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.LinkContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.LinkListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 链接控件配置
 * @author dengjt
 */
public class LinkComp extends WebComponent {
	private static final long serialVersionUID = -1529867689635200981L;
	private String href;
	private String i18nName;
	private boolean hasImg;
	private String image;
	
	// 图片路径是否改变
	private boolean imageChanged = true;
	// 图片真实路径，在context中使用
	private String realImage;

	private String langDir;
	private String target = "_blank";
	private boolean visible = true;
	
	public LinkComp() {
		this(null);
	}
	public LinkComp(String id) {
		super(id);
//		height = "20";
	}
	
	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
	
	public boolean isHasImg() {
		return hasImg;
	}
	public void setHasImg(boolean hasImg) {
		this.hasImg = hasImg;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String name) {
		i18nName = name;
		setCtxChanged(true);
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
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
	
	public boolean isImageChanged() {
		return imageChanged;
	}
	public void setImageChanged(boolean imageChanged) {
		this.imageChanged = imageChanged;
	}
	public String getRealImage() {
		if (isImageChanged()) {
			realImage = getRealImgPath(this.image, null);
			setImageChanged(false);
		}
		return realImage;
	}
	public void setRealImage(String realImage) {
		this.realImage = realImage;
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(LinkListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		LinkContext ctx = new LinkContext();
		ctx.setVisible(isVisible());
		ctx.setText(getI18nName());
		ctx.setEnabled(isEnabled());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		LinkContext linkCtx = (LinkContext) ctx;
		this.setVisible(linkCtx.isVisible());
		this.setI18nName(linkCtx.getText());
		this.setEnabled(linkCtx.isEnabled());
		this.setCtxChanged(false);
	}
}
