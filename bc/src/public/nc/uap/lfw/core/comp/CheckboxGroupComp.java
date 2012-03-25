package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.CheckBoxContext;
import nc.uap.lfw.core.comp.ctx.CheckboxGroupContext;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.KeyListener;
import nc.uap.lfw.core.event.conf.MouseListener;

/**
 * @author guoweic
 *
 */
public class CheckboxGroupComp extends TextComp {
	
	private static final long serialVersionUID = 7116611611792966304L;

	private List<CheckBoxComp> elementList = new ArrayList<CheckBoxComp>();

	private String comboDataId;

	// 选中的值（逗号,分隔的字符串）
	private String value;

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

	public List<CheckBoxComp> getElementList() {
		return elementList;
	}

	public void setElementList(List<CheckBoxComp> elementList) {
		this.elementList = elementList;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null && !value.equals(this.value)) {
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

	public void addElement(CheckBoxComp ele) {
		this.elementList.add(ele);
	}

	public int getSepWidth() {
		return sepWidth;
	}

	public void setSepWidth(int sepWidth) {
		this.sepWidth = sepWidth;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		setCtxChanged(true);
	}
	
	public Object clone() {
		CheckboxGroupComp comp = (CheckboxGroupComp) super.clone();
		if (this.elementList != null) {
			comp.elementList = new ArrayList<CheckBoxComp>();
			Iterator<CheckBoxComp> it = this.elementList.iterator();
			while (it.hasNext()) {
				comp.addElement((CheckBoxComp) it.next().clone());
			}
		}
		return comp;
	}

	@Override
	public boolean isCtxChanged() {
		for (int i = 0; i < elementList.size(); i++) {
			CheckBoxComp checkbox = elementList.get(i);
			if (checkbox.isCtxChanged())
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
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		CheckboxGroupContext ctx = new CheckboxGroupContext();
		ctx.setEnabled(isEnabled());
		ctx.setValue(getValue());
		ctx.setVisible(isVisible());
		// 子元素
		if (this.elementList.size() > 0) {
			List<CheckBoxContext> ctxList = new ArrayList<CheckBoxContext>();
			Iterator<CheckBoxComp> it = elementList.iterator();
			while (it.hasNext()) {
				CheckBoxComp checkbox = it.next();
				if (checkbox.isCtxChanged()) {
					ctxList.add((CheckBoxContext) checkbox.getContext());
				}
			}
			ctx.setCheckboxContexts(ctxList.toArray(new CheckBoxContext[0]));
		}
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		CheckboxGroupContext ckgCtx = (CheckboxGroupContext)ctx;
		this.setEnabled(ckgCtx.isEnabled());
		this.setComboDataId(ckgCtx.getComboDataId());
		this.setValue(ckgCtx.getValue());
		this.setVisible(ckgCtx.isVisible());
		// 子元素
		CheckBoxContext[] checkboxContexts = ckgCtx.getCheckboxContexts();
		if (checkboxContexts != null) {
			for (int i = 0, n = checkboxContexts.length; i < n; i++) {
				CheckBoxContext cbCtx = checkboxContexts[i];
				for (int j = 0, m = elementList.size(); j < m; j++) {
					if (elementList.get(j).getId().equals(cbCtx.getId())) {
						elementList.get(j).setContext(cbCtx);
						break;
					}
				}
			}
		}
		setCtxChanged(false);
	}

}
