package nc.uap.lfw.core;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.exception.InputInteractionInfo;
import nc.uap.lfw.core.exception.InputItem;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.exception.MessageInteractionInfo;
import nc.uap.lfw.core.exception.OkCancelInteractionInfo;
import nc.uap.lfw.core.exception.ThreeButtonInteractionInfo;

/**
 * 前后台交互工具类,通过该类发起交互,获取交互结果等
 * 
 * @author dingrf
 * @version NC6.1
 */
public class AppInteractionUtil {
	private static final String INTERACT_FLAG = "interactflag";
	private static final String INTERACT_RESULT = "interactresult";
	
	public enum TbsDialogResult{
		FIRST,
		THIRD,
		SECOND
	}

	public static void showMessageDialog(String msg) {
		ApplicationContext ctx = AppLifeCycleContext.current().getApplicationContext();
		ctx.addExecScript("showMessageDialog('" + msg + "')");
	}
	
	public static void showMessageDialog(String msg, String title, String btnText) {
		ApplicationContext ctx = AppLifeCycleContext.current().getApplicationContext();
		ctx.addExecScript("showMessageDialog('" + msg + "', null, '" + title + "', '" + btnText + "')");
	}

	/**
	 * 显示错误信息对话框,注意，错误对话框是任务中断型的，后续代码不再执行。如果在事务中，也将导致事务回退。
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showErrorDialog(String msg) {
		return showErrorDialog(msg, null, null, false);
	}
	
	/**
	 * 显示错误信息对话框,注意，错误对话框是任务中断型的，后续代码不再执行。如果在事务中，也将导致事务回退。
	 * 
	 * @param btnText
	 * @param msg
	 * @return
	 */
	public static boolean showErrorDialog(String msg, String title, String btnText) {
		return showErrorDialog("MSG_", msg, btnText, false);
	}

	public static boolean showErrorDialog(String msg, String title, String btnText, boolean okReturn) {
		return showErrorDialog("MSG_", msg, title, btnText, okReturn);
	}

	/**
	 * 显示提示信息对话框
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showErrorDialog(String dialogId, String msg, String title, String btnText, boolean okReturn) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null) {
			MessageInteractionInfo info = new MessageInteractionInfo(dialogId,
					msg, title, btnText, okReturn);
			throw new LfwInteractionException(info);
		}
		else if (interactionFlag.equals("false"))
			return Boolean.FALSE;
		else
			return Boolean.TRUE;
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
		ApplicationContext ctx = AppLifeCycleContext.current().getApplicationContext();
		ctx.addExecScript("showMessageDialog('" + msg + "')");
		//showMessage(msg, width, "parent");
	}

	/**
	 * 显示反馈信息
	 * 
	 * @param msg
	 *            反馈信息
	 */
	public static void showMessage(String msg) {
		ApplicationContext ctx = AppLifeCycleContext.current().getApplicationContext();
		ctx.addExecScript("showMessageDialog('" + msg + "')");
		//showMessage(msg, "169", "parent");
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
		ApplicationContext ctx = AppLifeCycleContext.current().getApplicationContext();
		ctx.addExecScript("showMessageDialog('" + msg + "')");
		//		ApplicationContext ctx = AppLifeCycleContext.current().getApplicationContext();
//		String execScript = "";
//		if (sope != null && !sope.equals("")) {
//			execScript += sope;
//			if (!sope.endsWith(".")) {
//				execScript += ".";
//			}
//		}
//		execScript += "showMessage('" + msg + "','" + width + "')";
//		ctx.addExecScript(execScript);
	}

	
	/**
	 * 显示即时信息
	 * @param msg
	 */
	public static void showShortMessage(String msg) {
		showShortMessage(msg, "parent");
	}
	
	

	/**
	 * 显示即时反馈信息
	 * 
	 * @param msg
	 *            反馈信息
	 * @param width
	 *            反馈信息区域宽度
	 * @param sope
	 *            作用域 （如parent ,window.openner）
	 */
	public static void showShortMessage(String msg,String sope) {
		ApplicationContext ctx = AppLifeCycleContext.current().getApplicationContext();
		String execScript = "";
//		if (sope != null && !sope.equals("")) {
//			execScript += sope;
//			if (!sope.endsWith(".")) {
//				execScript += ".";
//			}
//		}
//		execScript += "showMessage('" + msg + "','" + width + "')";
		execScript += "showMessage('" + msg + "')";
		ctx.addExecScript(execScript);
	}

	

	/**
	 * 显示交互对话框
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showConfirmDialog(String title, String msg) {
		return showConfirmDialog(title, msg, null);
	}

	/**
	 * 显示交互对话框
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showConfirmDialog(String title, String msg, String btnText) {
		return showConfirmDialog("CONF_", title, msg, btnText);
	}
	
	/**
	 * 显示交互对话框
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static boolean showConfirmDialog(String dialogId, String title,
			String msg, String btnText) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null) {
			OkCancelInteractionInfo info = new OkCancelInteractionInfo(
					dialogId, msg, title, btnText);
			throw new LfwInteractionException(info);
		} 
		else if (interactionFlag.equals("false"))
			return Boolean.FALSE;
		else
			return Boolean.TRUE;
	}
	
	
	/**
	 * 确认对话框可以设置“确定”、“取消”按钮的显示值
	 * @param dialogId
	 * @param title
	 * @param msg
	 * @param okText
	 * @param cancelText
	 * @return
	 */
	public static boolean showConfirmDialog(String dialogId, String title,	String msg, String okText, String cancelText) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null) {
			OkCancelInteractionInfo info = new OkCancelInteractionInfo(
					dialogId, msg, title, okText, cancelText);
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
	
	/**
	 * 显示交互对话框
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static TbsDialogResult show3ButtonsDialog(String title, String msg, String[] btnTexts) {
		return show3ButtonsDialog("THREEBUTTONS_", title, msg, btnTexts);
	}
	
	/**
	 * 显示交互对话框
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public static TbsDialogResult show3ButtonsDialog(String dialogId, String title, String msg, String[] btnTexts) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null) {
			ThreeButtonInteractionInfo info = new ThreeButtonInteractionInfo(
					dialogId, msg, title);
			info.setBtnTexts(btnTexts);
			throw new LfwInteractionException(info);
		} 
		return get3ButtonsDialogResult(dialogId);
	}

	public static TbsDialogResult get3ButtonsDialogResult() {
		return get3ButtonsDialogResult("CONF_");
	}

	/**
	 * 结果为true,表示前台对话框点击了ok,否则表示前台对话框点击了取消
	 * 
	 * @return
	 */
	public static TbsDialogResult get3ButtonsDialogResult(String dialogId) {
		String interactionFlag = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getParameter(dialogId + INTERACT_FLAG);
		if (interactionFlag == null)
			return null;
		else if (interactionFlag.equals("true"))
			return TbsDialogResult.FIRST;
		else if (interactionFlag.equals("false"))
			return TbsDialogResult.THIRD;
		return TbsDialogResult.SECOND;
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
