package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.LabelContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;

/**
 * 
 * @author dengjt
 *
 */
public class LabelComp extends WebComponent {
	private static final long serialVersionUID = 518406987071519211L;
	private String i18nName;
	private String langDir;
	private String text;
	private String innerHTML;
	private String color;
//	private String style;
//	private String weight;
//	private String size;
//	private String family;
	
	public LabelComp() {
//		this.setHeight("22");
//		this.setWidth("100");
//		this.setPaddingTop("4px");
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		return list;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		if (text != this.text) {
			this.text = text;
			setCtxChanged(true);
		}
	}
	
	public String getI18nName() {
		return i18nName;
	}
	
	public void setI18nName(String i18nName) {
		if (i18nName != this.i18nName) {
			this.i18nName = i18nName;
			setCtxChanged(true);
		}
	}
	
	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	@Override
	public BaseContext getContext() {
		LabelContext labelCtx = new LabelContext();
		labelCtx.setEnabled(isEnabled());
		String text = translate(this.getI18nName(), this.getText(), this.getLangDir());
		labelCtx.setText(text);
//		labelCtx.setI18nName(getI18nName());
		labelCtx.setVisible(isVisible());
		labelCtx.setInnerHTML(getInnerHTML());
		labelCtx.setColor(getColor());
//		labelCtx.setFamily(getFamily());
//		labelCtx.setSize(getSize());
//		labelCtx.setStyle(getStyle());
//		labelCtx.setWeight(getWeight());
		return labelCtx;
	}
	
	public void setContext(BaseContext ctx) {
		LabelContext labelCtx = (LabelContext) ctx;
		setVisible(labelCtx.isVisible());
		setText(labelCtx.getText());
		setEnabled(labelCtx.isEnabled());
		setCtxChanged(false);
	}
	
	protected String translate(String i18nName, String defaultI18nName, String langDir){
		if(i18nName == null || i18nName.equals(""))
			return defaultI18nName;
		return i18nName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if (color != this.color) {
			this.color = color;
			setCtxChanged(true);
		}
	}

//	public String getStyle() {
//		return style;
//	}
//
//	public void setStyle(String style) {
//		if (style != this.style) {
//			this.style = style;
//			setCtxChanged(true);
//		}
//	}
//
//	public String getWeight() {
//		return weight;
//	}
//
//	public void setWeight(String weight) {
//		if (weight != this.weight) {
//			this.weight = weight;
//			setCtxChanged(true);
//		}
//	}

	public String getInnerHTML() {
		return innerHTML;
	}

	public void setInnerHTML(String innerHTML) {
		this.innerHTML = innerHTML;
		setCtxChanged(true);
	}

//	public String getSize() {
//		return size;
//	}
//
//	public void setSize(String size) {
//		if (size != this.size) {
//			this.size = size;
//			setCtxChanged(true);
//		}
//	}
//
//	public String getFamily() {
//		return family;
//	}
//
//	public void setFamily(String family) {
//		if (family != this.family) {
//			this.family = family;
//			setCtxChanged(true);
//		}
//	}	
}
