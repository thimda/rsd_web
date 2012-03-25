package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ButtonContext;
import nc.uap.lfw.core.comp.ctx.ToolbarContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * toolbar 组件
 * 
 * @author zhangxya
 * 
 */
public class ToolBarComp extends WebComponent {

	private static final long serialVersionUID = -2518084293546367343L;

	private ToolBarTitle title;

	private boolean transparent = false;

	private List<ToolBarItem> elementList = new ArrayList<ToolBarItem>();

	private List<String> delIds;

	public ToolBarComp() {
		super();
	}

	public ToolBarComp(String id) {
		super(id);
	}

	public ToolBarItem[] getElements() {
		return elementList.toArray(new ToolBarItem[0]);
	}

	public int getElementCountWithoutHidden() {
		if (elementList == null)
			return 0;
		int count = 0;
		for (ToolBarItem ele : elementList) {
			if (ele.isVisible())
				count++;
		}
		return count;
	}

	public ToolBarItem getElementById(String id) {
		Iterator<ToolBarItem> it = elementList.iterator();
		while (it.hasNext()) {
			ToolBarItem ele = it.next();
			if (ele.getId().equals(id))
				return ele;
		}
		return null;
	}

	public void addElement(ToolBarItem ele) {
		this.elementList.add(ele);
	}

	public void deleteElement(ToolBarItem ele) {
		this.elementList.remove(ele);
		if (delIds == null)
			delIds = new ArrayList<String>();
		delIds.add(ele.getId());
		this.setCtxChanged(true);
	}

	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		return list;
	}

	public Object clone() {
		ToolBarComp comp = (ToolBarComp) super.clone();
		if (this.elementList != null) {
			comp.elementList = new ArrayList<ToolBarItem>();
			Iterator<ToolBarItem> it = this.elementList.iterator();
			while (it.hasNext()) {
				comp.addElement((ToolBarItem) it.next().clone());
			}
		}
		return comp;
	}

	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	public ToolBarTitle getTitle() {
		return title;
	}

	public void setTitle(ToolBarTitle title) {
		this.title = title;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	@Override
	public boolean isCtxChanged() {
		ToolBarItem[] items = getElements();
		for (int i = 0; i < items.length; i++) {
			ToolBarItem toolBarItem = items[i];
			if (toolBarItem.isCtxChanged())
				return true;
		}
		return super.isCtxChanged();
	}

	/**
	 * 获取所有按钮子项
	 * 
	 * @return
	 */
	private List<ToolBarItem> getButtonItemList() {
		List<ToolBarItem> buttonItemList = new ArrayList<ToolBarItem>();
		for (ToolBarItem item : elementList) {
			if (item.getType().equals(ToolBarItem.BUTTON_TYPE))
				buttonItemList.add(item);
		}
		return buttonItemList;
	}

	@Override
	public BaseContext getContext() {
		ToolbarContext ctx = new ToolbarContext();
		ctx.setId(this.getId());
		if (this.elementList.size() > 0) {
			// Button子项
			List<ToolBarItem> buttonItemList = getButtonItemList();
			List<ButtonContext> ctxList = new ArrayList<ButtonContext>();
			Iterator<ToolBarItem> it = buttonItemList.iterator();
			while (it.hasNext()) {
				ToolBarItem item = it.next();
				if (item.isCtxChanged()) {
					ctxList.add((ButtonContext) item.getContext());
				}
			}
			ctx.setButtonItemContexts(ctxList.toArray(new ButtonContext[0]));
			// TODO 其他子项

		}
		if (delIds != null && delIds.size() > 0)
			ctx.setDelIds(delIds.toArray(new String[0]));
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		ToolbarContext tbCtx = (ToolbarContext) ctx;
		// Button子项
		ButtonContext[] buttonItemContexts = tbCtx.getButtonItemContexts();
		if (buttonItemContexts != null) {
			for (int i = 0, n = buttonItemContexts.length; i < n; i++) {
				ButtonContext btCtx = buttonItemContexts[i];
				List<ToolBarItem> buttonItemList = getButtonItemList();
				for (int j = 0, m = buttonItemList.size(); j < m; j++) {
					if (buttonItemList.get(j).getId().equals(btCtx.getId())) {
						buttonItemList.get(j).setContext(btCtx);
						break;
					}
				}
			}
		}
		// TODO 其他子项

		this.setCtxChanged(false);
	}
}
