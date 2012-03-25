package nc.uap.lfw.main;

import nc.uap.lfw.core.processor.EventRequestContext;
import nc.uap.lfw.util.JsURLEncoder;

public class MainNodeUtil {
	
	/**
	 * 在Tab标签中打开节点
	 * @param url 链接地址
	 * @param title 标题
	 * @param nodeId 唯一ID
	 * @param refresh 重新打开时是否刷新页面
	 */
	public static void openNode(String nodeId, String title, String url, boolean refresh){
		StringBuffer buf = new StringBuffer();
		buf.append("openNode(\"")
		   .append(JsURLEncoder.encode(url, "UTF-8"))
		   .append("\", \"")
		   .append(title)
		   .append("\", \"")
		   .append(nodeId)
		   .append("\"");
		if (refresh)
			buf.append(", true");
		else
			buf.append(", false");
		buf.append(");\n");
		EventRequestContext.getLfwPageContext().addExecScript(buf.toString());
	}
	
	/**
	 * 在父页面的Tab标签中打开节点
	 * @param url 链接地址
	 * @param title 标题
	 * @param nodeId 唯一ID
	 * @param refresh 重新打开时是否刷新页面
	 */
	public static void openNodeInParent(String nodeId, String title, String url, boolean refresh){
		StringBuffer buf = new StringBuffer();
		buf.append("parent.openNode(\"")
		   .append(JsURLEncoder.encode(url, "UTF-8"))
		   .append("\", \"")
		   .append(title)
		   .append("\", \"")
		   .append(nodeId)
		   .append("\"");
		if (refresh)
			buf.append(", true");
		else
			buf.append(", false");
		buf.append(");\n");
		EventRequestContext.getLfwPageContext().addExecScript(buf.toString());
	}
	
	/**
	 * 在最顶层父页面的Tab标签中打开节点
	 * @param url 链接地址
	 * @param title 标题
	 * @param nodeId 唯一ID
	 * @param refresh 重新打开时是否刷新页面
	 */
	public static void openNodeInTop(String nodeId, String title, String url, boolean refresh){
		StringBuffer buf = new StringBuffer();
		// 获取顶层页面
		buf.append("var parentPage = window;")
		   .append("while (parentPage != null && parentPage.openNode == null) {\n")
		   .append("parentPage = parentPage.parent;")
		   .append("}\n");
		buf.append("if (parentPage != null) {\n");
		// 打开节点
		buf.append("parentPage.openNode(\"")
		   .append(JsURLEncoder.encode(url, "UTF-8"))
		   .append("\", \"")
		   .append(title)
		   .append("\", \"")
		   .append(nodeId)
		   .append("\"");
		if (refresh)
			buf.append(", true");
		else
			buf.append(", false");
		buf.append(");\n");
		buf.append("};\n");
		EventRequestContext.getLfwPageContext().addExecScript(buf.toString());
	}
}
