package nc.uap.lfw.reference.action;

import nc.uap.lfw.core.uif.delegator.AbstractUifDelegator;

/**
 * ����ȡ��ִ�ж���
 * @author zhangxya
 *
 */
public class RefCancelDelegator extends AbstractUifDelegator {

	public void execute() {
		getGlobalContext().addExecScript("if (parent.window.currentReferenceWithDivOpened) parent.window.currentReferenceWithDivOpened.hideRefDiv();");
		// �ر�Dialog
		getGlobalContext().getParentGlobalContext().hideCurrentDialog();
	}

}
