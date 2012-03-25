package nc.uap.lfw.jsp.uimeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UILayout extends UIElement {
	private static final long serialVersionUID = 8154264148721825061L;
	protected List<UILayoutPanel> panelList = new ArrayList<UILayoutPanel>();

	public List<UILayoutPanel> getPanelList() {
		return panelList;
	}

	public void addPanel(UILayoutPanel panel) {
		panelList.add(panel);
		panel.setLayout(this);
		super.addElement(panel);
	}

	public void removePanel(UILayoutPanel ele) {
		if (ele != null) {
			if (panelList != null) {
				super.removeElement(ele);
				if (ele != null) {
					for (UILayoutPanel panel : panelList) {
						if (panel.getAttribute(ID).equals(ele.getAttribute(ID))) {
							panelList.remove(panel);
							break;
						}
					}
				}
			}
		}

	}


//	@Override
//	public Object createSelf() {
//		return super.createSelf();
//	}
//
//	@Override
//	public void updateSelf() {
//		super.updateSelf();
//	}
//
//	@Override
//	public void destroySelf() {
//		super.destroySelf();
//	}

//	@Override
//	public Object notifyChange(String type, Object obj) {
//		if (LifeCyclePhase.ajax.equals(getPhase())) {
//			try {
//				String className = getObserverName(type, obj);
//				Class c = Class.forName("nc.uap.lfw.ra.render.observer." + className + "Observer");
//				Object instance = c.newInstance();
//				Method m = c.getMethod("notifyChange", new Class[] { String.class, UIElement.class, Object.class });
//				return m.invoke(instance, new Object[] { type, this, obj });
//			}
//			// 因此类在插件中用到，插件中无法包含日志工具。
//			catch (Throwable e) {
//				throw new LfwRuntimeException(e.getMessage());
//			}
//		}
//		return null;
//	}
	

	@Override
	public UILayout doClone() {
		UILayout layout = (UILayout) super.doClone();
		if (this.panelList != null) {
			layout.panelList = new ArrayList<UILayoutPanel>();
			Iterator<UILayoutPanel> panels = this.panelList.iterator();
			while (panels.hasNext()) {
				UILayoutPanel panel = panels.next();
				layout.panelList.add((UILayoutPanel) panel.doClone());
			}
		}
		return layout;
	}

}
