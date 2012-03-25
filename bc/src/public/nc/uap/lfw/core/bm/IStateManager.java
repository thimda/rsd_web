package nc.uap.lfw.core.bm;

import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * ״̬����ӿ� 
 *
 */
public interface IStateManager {
	/*
	 * ״ֵ̬
	 */
	public enum State{
		ENABLED,
		DISABLED,
		HIDDEN, 
		VISIBLE
	}
	public State getState(WebComponent target, LfwWidget view);
}
