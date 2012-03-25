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
 * 前后台交互工具类,通过该类发起交互,获取交互结果等
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
	 * 显示反馈信息
	 * 
	 * @param msg
	 *            信息内容
	 * @param width
	 *            反馈信息区域宽度
	 */
	public static void showMessage(String msg, String width) {
		showMessage(msg, width, "parent");
	}

	/**
	 * 显示反馈信息
	 * 
	 * @param msg
	 *            反馈信息
	 */
	public static void showMessage(String msg) {
		showMessage(msg, "169", "parent");
	}

	/**
	 * 显示反馈信息
	 * 
	 * @param msg
	 *            反馈信息
	 * @param width
	 *            反馈信息区域宽度
	 * @param sope
	 *            作用域 （如parent ,window.openner）
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
	 * 显示提示信息对话框
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
	 * 显示提示信息对话框
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
	 * 显示交互对话框
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showConfirmDialog(String title, String msg) {
		return showConfirmDialog("CONF_", title, msg);
	}

	/**
	 * 显示交互对话框
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
	 * 结果为true,表示前台对话框点击了ok,否则表示前台对话框点击了取消
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
	 * 显示输入对话框
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
	 * 获取输入交互中前台选择的值
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
