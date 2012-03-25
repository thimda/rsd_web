package nc.uap.lfw.reference.action;

import nc.uap.lfw.core.uif.delegator.AbstractUifDelegator;

/**
 * 参照取消执行动作
 * @author zhangxya
 *
 */
public class RefCancelDelegator extends AbstractUifDelegator {

	public void execute() {
		getGlobalContext().addExecScript("if (parent.window.currentReferenceWithDivOpened) parent.window.currentReferenceWithDivOpened.hideRefDiv();");
		// 关闭Dialog
		getGlobalContext().getParentGlobalContext().hideCurrentDialog();
	}

}
