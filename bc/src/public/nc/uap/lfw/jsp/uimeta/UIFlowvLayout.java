package nc.uap.lfw.jsp.uimeta;

import java.util.Iterator;


public class UIFlowvLayout extends UILayout {
	private static final long serialVersionUID = -4255303814886822777L;
	private static final String GEN_PANEL_PRE = "g_p_";
	private int currentPanelId = 1;
	
	/**
	 * ��ǰ����������Ԫ�أ���API���������һ��Panel������Ԫ����ӵ���ӦԪ����
	 * @param ele �����Ԫ��
	 */
	public UIFlowvPanel addElementToPanel(UIElement ele) {
		UIFlowvPanel panel = new UIFlowvPanel();
		panel.setId(GEN_PANEL_PRE + (currentPanelId ++));
		this.addPanel(panel);
		if(ele != null)
			panel.setElement(ele);
		return panel;
	}
	
	/**
	 * ��ȡ��ǰ�����е�Ԫ�أ���API����������Panel��������ID��ȵ�Ԫ��
	 * @param id
	 * @return
	 */
	public UIElement getElementFromPanel(String id) {
		Iterator<UILayoutPanel> it = getPanelList().iterator();
		while(it.hasNext()){
			UIFlowvPanel panel = (UIFlowvPanel) it.next();
			UIElement ele = panel.getElement();
			if(ele == null)
				continue;
			if(id.equals(ele.getId()))
				return ele;
		}
		return null;
	}
}
