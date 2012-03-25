package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.RadioGroupContext;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.KeyListener;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.TextListener;

/**
 * @author guoweic
 *
 */
public class RadioGroupComp extends TextComp {

	private static final long serialVersionUID = 8517132806746485488L;
	
	private List<RadioComp> elementList = new ArrayList<RadioComp>();

	private String value;

	private String comboDataId;

	// 选中项索引
	private int index;
	
	private int tabIndex;
	
	// 每个子项的间隔
	private int sepWidth;
	
	// 是否每个子项占一行
	private boolean changeLine = false;
	
	private boolean readOnly = false;
	
	public boolean isChangeLine() {
		return changeLine;
	}

	public void setChangeLine(boolean changeLine) {
		this.changeLine = changeLine;
	}

	public List<RadioComp> getElementList() {
		return elementList;
	}

	public void setElementList(List<RadioComp> elementList) {
		this.elementList = elementList;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null && value != this.value) {
			this.value = value;
			setCtxChanged(true);
		}
	}

	public String getComboDataId() {
		return comboDataId;
	}

	public void setComboDataId(String comboDataId) {
		this.comboDataId = comboDataId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void addElement(RadioComp ele) {
		this.elementList.add(ele);
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public int getSepWidth() {
		return sepWidth;
	}

	public void setSepWidth(int sepWidth) {
		this.sepWidth = sepWidth;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		setCtxChanged(true);
	}
	
	public Object clone() {
		RadioGroupComp comp = (RadioGroupComp) super.clone();
		if (this.elementList != null) {
			comp.elementList = new ArrayList<RadioComp>();
			Iterator<RadioComp> it = this.elementList.iterator();
			while (it.hasNext()) {
				comp.addElement((RadioComp) it.next().clone());
			}
		}
		return comp;
	}

	@Override
	public boolean isCtxChanged() {
		for (int i = 0; i < elementList.size(); i++) {
			RadioComp radio = elementList.get(i);
			if (radio.isCtxChanged())
				return true;
		}
		return super.isCtxChanged();
	}
	
	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(KeyListener.class);
		list.add(FocusListener.class);
		list.add(TextListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		RadioGroupContext ctx = new RadioGroupContext();
		ctx.setEnabled(isEnabled());
		ctx.setValue(getValue());
		// 子元素
//		if (this.elementList.size() > 0) {
//			List<RadioContext> ctxList = new ArrayList<RadioContext>();
//			Iterator<RadioComp> it = elementList.iterator();
//			while (it.hasNext()) {
//				RadioComp radio = it.next();
//				if (radio.isCtxChanged()) {
//					ctxList.add((RadioContext) radio.getContext());
//				}
//			}
//			ctx.setRadioContexts(ctxList.toArray(new RadioContext[0]));
//		}
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		RadioGroupContext rgCtx = (RadioGroupContext)ctx;
		this.setEnabled(rgCtx.isEnabled());
		this.setComboDataId(rgCtx.getComboDataId());
		this.setIndex(rgCtx.getIndex());
		this.setValue(rgCtx.getValue());
		// 子元素
//		RadioContext[] radioContexts = rgCtx.getRadioContexts();
//		if (radioContexts != null) {
//			for (int i = 0, n = radioContexts.length; i < n; i++) {
//				RadioContext radioCtx = radioContexts[i];
//				for (int j = 0, m = elementList.size(); j < m; j++) {
//					if (elementList.get(j).getId().equals(radioCtx.getId())) {
//						elementList.get(j).setContext(radioCtx);
//						break;
//					}
//				}
//			}
//		}
		setCtxChanged(false);
	}
	
}
