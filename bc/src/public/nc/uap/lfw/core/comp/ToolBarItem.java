package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ButtonContext;
import nc.uap.lfw.core.event.conf.ContextMenuListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * toolbar 组件
 * @author zhangxya
 *
 */
public class ToolBarItem extends WebComponent {
	
	private static final long serialVersionUID = 1L;
	
	public static final String BUTTON_TYPE = "button";
	
	private String type = BUTTON_TYPE;
	private String refImg = "";
	
	// 图片路径是否改变
	private boolean refImgChanged = true;
	// 图片真实路径，在context中使用
	private String realRefImg;

	private String text = "";
	private String i18nName = "";
	private String langDir = "";
	private String tip = "";
	private String tipI18nName = "";
	private String align = "left";
	private boolean withSep = true;
	//修饰符，默认＝CTRL
	private int modifiers = java.awt.Event.CTRL_MASK;
	
	public int getModifiers() {
		return modifiers;
	}
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	//热键
	private String hotKey = null;
	//热键显示名称
	private String displayHotKey = null;
	
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}

	public static final int DEFAULT_WIDTH = 120;
	public static final int DEFAULT_HEIGHT = 20;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		if (this.text != text) {
			this.text = text;
			this.setCtxChanged(true);
		}
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
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getTipI18nName() {
		return tipI18nName;
	}
	public void setTipI18nName(String tipI18nName) {
		this.tipI18nName = tipI18nName;
	}
//	@Override
//	public String getHeight() {
//		if(this.height == null)
//			return DEFAULT_HEIGHT + "";
//		return this.height;
//	}
//	@Override
//	public String getWidth() {
//		if(this.width == null)
//			return  DEFAULT_WIDTH + "";
//		return this.width;
//	}
	
	//Item的listener
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		if (type.equals(BUTTON_TYPE)) {
			list.add(MouseListener.class);
			list.add(ContextMenuListener.class);
		}
		return list;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	public String getRefImg() {
		return refImg;
	}
	public void setRefImg(String refImg) {
		this.refImg = refImg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isWithSep() {
		return withSep;
	}
	public void setWithSep(boolean withSep) {
		this.withSep = withSep;
	}

	@Override
	public BaseContext getContext() {
		if (type.equals(BUTTON_TYPE)) {  // 按钮子项
			ButtonContext ctx = new ButtonContext();
			ctx.setId(this.getId());
			ctx.setText(this.getText());
			ctx.setEnabled(this.enabled);
			return ctx;
		} else {  //TODO 其他子项
			
		}
		return null;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		if (type.equals(BUTTON_TYPE)) {  // 按钮子项
			ButtonContext btCtx = (ButtonContext) ctx;
			this.setEnabled(btCtx.isEnabled());
			this.setText(btCtx.getText());
		} else {  //TODO 其他子项
			
		}
		
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
