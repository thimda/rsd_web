package nc.uap.lfw.core.comp.ctx;

import java.io.Serializable;

/**
 * @author guoweic
 *
 */
public class SelfDefCompContext extends BaseContext {
	
	private static final long serialVersionUID = -6499443806251580889L;
	
	private boolean visible = true;
	
	private String triggerId = null;
	
	// �Զ���Ķ���Context����
	private Serializable otherCtx = null;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Serializable getOtherCtx() {
		return otherCtx;
	}

	public void setOtherCtx(Serializable otherCtx) {
		this.otherCtx = otherCtx;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

}
