package nc.uap.lfw.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * ����ʱ����������˲���������·��������Դ������չʾΪjsp��ʽ��
 * @author dengjt
 *
 */
public interface PresentPlugin {
	/**
	 * ��Դ׼���׶Σ��˽׶ν�������Դ����Դ������ָ��·��
	 * @param req
	 * @param pagePath
	 * @param themeId
	 * @param templateJsp
	 * @return
	 * @throws IOException
	 */
	public void prepare(HttpServletRequest req, String pagePath, String themeId, String templateJsp) throws IOException;
	
	/**
	 * ��������׶Σ���ԭʼ��ʽ������jspģ�壬��ͨ������ֵ��ʶ�Ƿ��Ѿ�׼����
	 * @param req
	 * @param pagePath
	 * @param themeId
	 * @param templateJsp
	 * @return
	 * @throws IOException
	 */
	public boolean translate(HttpServletRequest req, String pagePath, String themeId, String templateJsp) throws IOException;
	
	/**
	 * �õ�׼���õ�JSP·��
	 * @return
	 */
	public String getTargetJsp();
	
	/**
	 * ��ȡ�˹��ܵ��Ӧ��ҳ�������·��
	 * @return
	 */
	public String getModel();
}
