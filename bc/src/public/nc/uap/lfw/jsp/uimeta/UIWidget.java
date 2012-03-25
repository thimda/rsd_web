package nc.uap.lfw.jsp.uimeta;

import nc.uap.lfw.core.exception.LfwRuntimeException;


public class UIWidget extends UILayout {
	private static final long serialVersionUID = -6203854872529543007L;
	public UIMeta getUimeta() {
		if(getPanelList().size() == 0)
			return null;
		return (UIMeta) getPanelList().get(0);
	}

	public void setUimeta(UIMeta uimeta) {
		addPanel(uimeta);
	}
	
	@Override
	public void removePanel(UILayoutPanel ele) {
		if(!(ele instanceof UIMeta))
			throw new LfwRuntimeException("不能删除非UIMeta元素");
		if(getPanelList().size() == 0)
			throw new LfwRuntimeException("不存在UIMeta元素");
		super.removePanel(ele);
	}

	@Override
	public void addPanel(UILayoutPanel panel) {
		if(!(panel instanceof UIMeta))
			throw new LfwRuntimeException("不能添加非UIMeta元素");
		if(getPanelList().size() > 0)
			throw new LfwRuntimeException("已经存在UIMeta元素");
		panel.setWidgetId(this.getId());
		if(panel.getId() == null)
			panel.setId(this.getId() + "_um");
		super.addPanel(panel);
	}
	
//
//	@Override
//	public void removeElement(UIElement ele) {
//		if (this.uimeta != null) {
//			if(!(ele instanceof UIMeta)) {
//				this.notifyChange(DELETE, ele);
//				this.uimeta.removeElement(ele);
//			}
//		} else {
//			throw new LfwRuntimeException("uimeta为null，不能添加元素！");
//		}
//	}
//
//	@Override
//	public void addElement(UIElement ele) {
//		if (this.uimeta != null) {
//			this.uimeta.addElement(ele);
////			this.notifyChange(ADD, ele);
//		} else {
//			throw new LfwRuntimeException("uimeta为null，不能添加元素！");
//		}
//	}

	@Override
	public UIWidget doClone() {
//		UIWidget widget = (UIWidget)super.doClone();
//		UIMeta um = this.getUimeta();
//		if(um != null){
//			UIMeta newUm = um.doClone();
//			newUm.setParent(widget);
//			widget.setUimeta(newUm);
//		}
//		return widget;
		return (UIWidget) super.doClone();
	}

}
