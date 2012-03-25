package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.SelfDefComp;

/**
 * @author guoweic
 *
 */
public class SelfDefEvent<SelfDefComp> extends AbstractServerEvent<SelfDefComp> {

	private String triggerId;
	
	public SelfDefEvent(SelfDefComp webElement) {
		super(webElement);
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

}
