package nc.uap.lfw.main;

import nc.uap.lfw.core.processor.EventRequestContext;
import nc.uap.lfw.util.JsURLEncoder;

public class MainNodeUtil {
	
	/**
	 * ��Tab��ǩ�д򿪽ڵ�
	 * @param url ���ӵ�ַ
	 * @param title ����
	 * @param nodeId ΨһID
	 * @param refresh ���´�ʱ�Ƿ�ˢ��ҳ��
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
	 * �ڸ�ҳ���Tab��ǩ�д򿪽ڵ�
	 * @param url ���ӵ�ַ
	 * @param title ����
	 * @param nodeId ΨһID
	 * @param refresh ���´�ʱ�Ƿ�ˢ��ҳ��
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
	 * ����㸸ҳ���Tab��ǩ�д򿪽ڵ�
	 * @param url ���ӵ�ַ
	 * @param title ����
	 * @param nodeId ΨһID
	 * @param refresh ���´�ʱ�Ƿ�ˢ��ҳ��
	 */
	public static void openNodeInTop(String nodeId, String title, String url, boolean refresh){
		StringBuffer buf = new StringBuffer();
		// ��ȡ����ҳ��
		buf.append("var parentPage = window;")
		   .append("while (parentPage != null && parentPage.openNode == null) {\n")
		   .append("parentPage = parentPage.parent;")
		   .append("}\n");
		buf.append("if (parentPage != null) {\n");
		// �򿪽ڵ�
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
