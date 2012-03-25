package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.MenuItem;

public class CtxMenuMouseEvent extends MouseEvent<MenuItem> {
	private String triggerId;
	public CtxMenuMouseEvent(MenuItem webElement) {
		super(webElement);
	}
	public String getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}
}
