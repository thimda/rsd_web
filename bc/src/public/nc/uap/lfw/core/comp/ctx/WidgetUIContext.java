package nc.uap.lfw.core.comp.ctx;

public class WidgetUIContext extends BaseContext {
	private static final long serialVersionUID = 2326808729582916410L;
	private boolean visible;
	//��ǰ����״̬
	private String cos;
	//��ǰҵ��״̬
	private String cbs;
	//��ǰ�Զ���״̬
	private String cus;
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getCos() {
		return cos;
	}
	public void setCos(String cos) {
		this.cos = cos;
	}
	public String getCbs() {
		return cbs;
	}
	public void setCbs(String cbs) {
		this.cbs = cbs;
	}
	public String getCus() {
		return cus;
	}
	public void setCus(String cus) {
		this.cus = cus;
	}
}
