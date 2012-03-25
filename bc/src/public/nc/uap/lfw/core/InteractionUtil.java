package nc.uap.lfw.core;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.InputInteractionInfo;
import nc.uap.lfw.core.exception.InputItem;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.exception.MessageInteractionInfo;
import nc.uap.lfw.core.exception.OkCancelInteractionInfo;
import nc.uap.lfw.core.processor.EventRequestContext;

/**
 * ǰ��̨����������,ͨ�����෢�𽻻�,��ȡ���������
 * 
 * @author gd
 * @version NC6.0
 */
public class InteractionUtil {
	private static final String INTERACT_FLAG = "interactflag";
	private static final String INTERACT_RESULT = "interactresult";

	public static void showMessageDialog(LfwPageContext ctx, String msg) {
		ctx.addExecScript("showMessageDialog('" + msg + "')");
	}

	public static void showMessageDialog(String msg) {
		LfwPageContext ctx = EventRequestContext.getLfwPageContext();
		ctx.addExecScript("showMessageDialog('" + msg + "')");
	}

	/**
	 * ��ʾ������Ϣ
	 * 
	 * @param msg
	 *            ��Ϣ����
	 * @param width
	 *            ������Ϣ������
	 */
	public static void showMessage(String msg, String width) {
		showMessage(msg, width, "parent");
	}

	/**
	 * ��ʾ������Ϣ
	 * 
	 * @param msg
	 *            ������Ϣ
	 */
	public static void showMessage(String msg) {
		showMessage(msg, "169", "parent");
	}

	/**
	 * ��ʾ������Ϣ
	 * 
	 * @param msg
	 *            ������Ϣ
	 * @param width
	 *            ������Ϣ������
	 * @param sope
	 *            ������ ����parent ,window.openner��
	 */
	public static void showMessage(String msg, String width, String sope) {
		LfwPageContext ctx = EventRequestContext.getLfwPageContext();
		String execScript = "";
		if (sope != null && !sope.equals("")) {
			execScript += sope;
			if (!sope.endsWith(".")) {
				execScript += ".";
			}
		}
		execScript += "showMessage('" + msg + "','" + width + "')";
		ctx.addExecScript(execScript);
	}

	/**
	 * ��ʾ��ʾ��Ϣ�Ի���
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showErrorDialog(String msg) {
		return showErrorDialog(msg, false);
	}

	public static boolean showErrorDialog(String msg, boolean okReturn) {
		return showErrorDialog("MSG_", msg, okReturn);
	}

	/**
	 * ��ʾ��ʾ��Ϣ�Ի���
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showErrorDialog(String dialogId, String msg,
			boolean okReturn) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
			.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null) {
			MessageInteractionInfo info = new MessageInteractionInfo(dialogId,
					msg, okReturn);
			throw new LfwInteractionException(info);
		}
		else if (interactionFlag.equals("false"))
			return Boolean.FALSE;
		else
			return Boolean.TRUE;
	}

	/**
	 * ��ʾ�����Ի���
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showConfirmDialog(String title, String msg) {
		return showConfirmDialog("CONF_", title, msg);
	}

	/**
	 * ��ʾ�����Ի���
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showConfirmDialog(String dialogId, String title,
			String msg) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null) {
			OkCancelInteractionInfo info = new OkCancelInteractionInfo(
					dialogId, msg, title);
			throw new LfwInteractionException(info);
		} 
		else if (interactionFlag.equals("false"))
			return Boolean.FALSE;
		else
			return Boolean.TRUE;
	}

	public static Boolean getConfirmDialogResult() {
		return getConfirmDialogResult("CONF_");
	}

	/**
	 * ���Ϊtrue,��ʾǰ̨�Ի�������ok,�����ʾǰ̨�Ի�������ȡ��
	 * 
	 * @return
	 */
	public static Boolean getConfirmDialogResult(String dialogId) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null)
			return null;
		else if (interactionFlag.equals("false"))
			return Boolean.FALSE;
		else
			return Boolean.TRUE;
	}

	
	public static boolean showInputDialog(String title, InputItem[] items) {
		return showInputDialog("INPUT_", title, items);
	}

	/**
	 * ��ʾ����Ի���
	 * 
	 * @param title
	 * @param items
	 * @return
	 */
	public static boolean showInputDialog(String dialogId, String title,
			InputItem[] items) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null) {
			InputInteractionInfo info = new InputInteractionInfo(dialogId,
					items, title);
			throw new LfwInteractionException(info);
		}
		else if (interactionFlag.equals("false"))
			return Boolean.FALSE;
		else
			return Boolean.TRUE;
	}

	public static Map<String, String> getInputDialogResult() {
		return getInputDialogResult("INPUT_");
	}

	/**
	 * ��ȡ���뽻����ǰ̨ѡ���ֵ
	 * 
	 * @return
	 */
	public static Map<String, String> getInputDialogResult(String dialogId) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(INTERACT_RESULT);
		if (interactionFlag != null && !interactionFlag.equals("")) {
			String[] pairs = interactionFlag.split(",");
			if (pairs != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < pairs.length; i++) {
					String[] pair = pairs[i].split("=");
					if (pair != null && pair.length == 2)
						map.put(pair[0], pair[1]);
				}
				return map;
			}
		}
		return null;
	}
}
