package nc.uap.lfw.core.bm;

import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * 状态管理接口 
 *
 */
public interface IStateManager {
	/*
	 * 状态值
	 */
	public enum State{
		ENABLED,
		DISABLED,
		HIDDEN, 
		VISIBLE
	}
	public State getState(WebComponent target, LfwWidget view);
}
