package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ButtonContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * Button 控件配置
 * 
 * @author dengjt
 * 
 */
public class ButtonComp extends WebComponent {

	private static final long serialVersionUID = -3640014425289622883L;
	private String tip;
	private String i18nName;
	
	private String refImg;
	
	// 图片路径是否改变
	private boolean refImgChanged = true;
	// 图片真实路径，在context中使用
	private String realRefImg;
	
//	private String align = "left";
	private String langDir;
	private String text;
	private String tipI18nName;
	//热键
	private String hotKey = null;
	//热键显示名称
	private String displayHotKey = null;

	public ButtonComp() {
//		this.setHeight("22");
//		this.setWidth("60");
	}

	public ButtonComp(String id) {
		super(id);
//		this.setHeight("22");
//		this.setWidth("60");
	}

//	public String getAlign() {
//		return align;
//	}
//
//	public void setAlign(String align) {
//		this.align = align;
//	}

	public String getRefImg() {
		return refImg;
	}

	public void setRefImg(String refImg) {
		this.refImg = refImg;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Object clone() {
		return super.clone();
	}

	@Override
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		setCtxChanged(true);
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		return list;
	}

	public String getTipI18nName() {
		return tipI18nName;
	}

	public void setTipI18nName(String tipI18nName) {
		this.tipI18nName = tipI18nName;
	}
	
	@Override
	public BaseContext getContext() {
		ButtonContext ctx = new ButtonContext();
		ctx.setId(this.getId());
		ctx.setEnabled(this.enabled);
		ctx.setVisible(this.isVisible());
		ctx.setText(this.text);
//		ctx.setHeight(this.height);
//		ctx.setWidth(this.width);
		ctx.setRefImg(this.realRefImg);
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ButtonContext btCtx = (ButtonContext) ctx;
		this.setEnabled(btCtx.isEnabled());
		this.setVisible(btCtx.isVisible());
		this.setCtxChanged(false);
	}

	public String getHotKey() {
		return hotKey;
	}

	public void setHotKey(String hotKey) {
		this.hotKey = hotKey;
	}

	public String getDisplayHotKey() {
		return displayHotKey;
	}

	public void setDisplayHotKey(String displayHotKey) {
		this.displayHotKey = displayHotKey;
	}

	public boolean isRefImgChanged() {
		return refImgChanged;
	}

	public void setRefImgChanged(boolean refImgChanged) {
		this.refImgChanged = refImgChanged;
	}

	public String getRealRefImg() {
		if (isRefImgChanged()) {
			realRefImg = getRealImgPath(this.refImg, null);
			setRefImgChanged(false);
		}
		return realRefImg;
	}

	public void setRealRefImg(String realRefImg) {
		this.realRefImg = realRefImg;
	}
	
}
