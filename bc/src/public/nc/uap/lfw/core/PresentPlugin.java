package nc.uap.lfw.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * 运行时解析插件。此插件可适配多路径，多来源，最终展示为jsp格式。
 * @author dengjt
 *
 */
public interface PresentPlugin {
	/**
	 * 资源准备阶段，此阶段将任意来源的资源拷贝到指定路径
	 * @param req
	 * @param pagePath
	 * @param themeId
	 * @param templateJsp
	 * @return
	 * @throws IOException
	 */
	public void prepare(HttpServletRequest req, String pagePath, String themeId, String templateJsp) throws IOException;
	
	/**
	 * 翻译解析阶段，将原始格式解析成jsp模板，并通过返回值标识是否已经准备好
	 * @param req
	 * @param pagePath
	 * @param themeId
	 * @param templateJsp
	 * @return
	 * @throws IOException
	 */
	public boolean translate(HttpServletRequest req, String pagePath, String themeId, String templateJsp) throws IOException;
	
	/**
	 * 得到准备好的JSP路径
	 * @return
	 */
	public String getTargetJsp();
	
	/**
	 * 获取此功能点对应的页面控制类路径
	 * @return
	 */
	public String getModel();
}
