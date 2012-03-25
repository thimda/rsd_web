package nc.uap.lfw.core;

import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.util.PageNodeUtil;
import nc.uap.lfw.reference.app.AppDefaultReferencePageModel;
import nc.uap.lfw.util.LfwClassUtil;
/**
 * ��ͨҳ��������
 * @author dengjt
 *
 */
public class PageControlPlugin implements ControlPlugin {
	protected static final String MODEL = "model";
	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res,
			String path) throws Exception {
		RequestLifeCycleContext appCtx = new RequestLifeCycleContext();
		RequestLifeCycleContext.set(appCtx);
		appCtx.setPhase(LifeCyclePhase.render);
		String referer = req.getHeader(WebSession.REFERER);
		if(referer != null && !referer.equals(""))
			LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute(WebSession.REFERER, referer);
		
		/*���ThemeId,���½��е�ǰ�����ڵ�·����Ϣ�������㿪����Աֱ�ӻ�ȡ������·��
		 * ע�⣬����Ŀǰ���ܵĲ�ͨ�������������ҳ���������˴�pageId����Ϊ�գ�������������ݲ�������
		 */
		
		String pageId = req.getParameter("pageId");
		String pagePath = null;
		if(pageId != null){
			pagePath = PageNodeUtil.getPageNodeDir(pageId);
			if(pagePath == null){
				pagePath = pageId;
			}
		}
		
		String themeId = LfwRuntimeEnvironment.getThemeId();
		
		/*
		 * ����jsp�������£�
		 * �������pageId����������pageIdû�á�.���ָ���������Ѱ�Ҷ�ӦpageIdĿ¼�µ�jsp�ļ����������Ȳ��Ҵ�Ŀ¼��themesĿ¼�µĶ�Ӧjsp���Խ��ж�Ƥ��֧��
		 * ����ļ������ڣ������Ȳ���ָ��template����Ŀ¼�µ�ģ���ļ�
		 * ����Բ����ڣ����Ҹ�Ŀ¼��ģ���ļ�
		*/
		//if(splitIndex == -1){
		int lastIndex = path.lastIndexOf("/");
		if(lastIndex != -1){
			boolean exist = false;
			String templateJsp = path.substring(lastIndex);
			String targetJsp = "";
			PresentPlugin plugin = PresentPluginFactory.getPresentPlugin(templateJsp);
			
			plugin.prepare(req, pagePath, themeId, templateJsp);
			if(!processPageModel(req, res,plugin))
				return;
			exist = plugin.translate(req, pagePath, themeId, templateJsp);
			targetJsp = plugin.getTargetJsp();
			
			
			if(exist){
				String nodeId = (String) LfwRuntimeEnvironment.getWebContext().getRequest().getAttribute(WebConstant.PERSONAL_PAGE_ID_KEY);
				String nodePath = "html/nodes/" + nodeId;
				String nodeThemePath = nodePath + "/themes/" + themeId;
				String nodeStyleSheetPath = nodeThemePath + "/stylesheets";
				String nodeImagePath = nodeThemePath + "/images";
				req.setAttribute(WebConstant.NODE_PATH, nodePath);
				req.setAttribute(WebConstant.NODE_THEME_PATH, nodeThemePath);
				req.setAttribute(WebConstant.NODE_STYLE_PATH, nodeStyleSheetPath);
				req.setAttribute(WebConstant.NODE_IMAGE_PATH, nodeImagePath);
				
				PageModel pageModel = (PageModel) req.getAttribute("pageModel");
				pageModel.getPageMeta().setNodeImagePath(nodeImagePath);
				LfwLogger.debug("��·��" + targetJsp + "���ҵ�ģ���ļ�");
				
				ServletContext ctx = req.getSession().getServletContext();
				ctx.getRequestDispatcher(targetJsp).forward(req, res);
				return;
			}
		}
		RequestDispatcher dispatcher = req.getSession().getServletContext().getRequestDispatcher(path);
		dispatcher.forward(req, res);
	}
	

	protected boolean processPageModel(HttpServletRequest req,
			HttpServletResponse res,PresentPlugin plugin) {
		beforeInitPageModel(req, res);
		
		String className = (String) req.getAttribute(MODEL);
		if(className == null || className.equals("")){
			className = req.getParameter(MODEL);
		}
		if(className==null||className.equals(""))
			className = plugin.getModel();
		if(className == null || className.equals(""))
			className = PageModel.class.getName();
		
		
		
		String pageId = req.getParameter("pageId");;
		
		Logger.debug("PageModelTag���ȡ��modelClazz=" + className);
		PageModel model = (PageModel) LfwClassUtil.newInstance(className);
		String pagePath = PageNodeUtil.getPageNodeDir(pageId);
		if(pagePath != null){
			String nodePath = "html/nodes/" + pagePath + "/node.properties";
			Properties props = AbstractPresentPlugin.loadNodePropertie(nodePath);
			//����node.properties�е�����
			model.setProps(props);
		}
		//���г�ʼ��
		model.internalInitialize();
		String newEtag = model.getEtag();
		
		String etag = req.getHeader("If-None-Match");
		if(etag != null){
			if(etag.equals(newEtag)){
				res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return false;
			}
		}
		//����model����ҳ�������
		req.setAttribute("pageModel", model);
		if(newEtag != null){
			res.addHeader("ETag", newEtag);
		}
		
		if(pagePath != null){
			String langResoFilePath = "html/nodes/" + pagePath + "/langres.properties";
			Properties langResources = AbstractPresentPlugin.loadNodeLangResources(langResoFilePath);
			req.setAttribute("langResources", langResources);
		}
		return true;
	}
	
	
	protected void beforeInitPageModel(HttpServletRequest req, HttpServletResponse res) {
		String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
//		if(pageId.equals("login")){
//			req.setAttribute(MODEL, LoginPageModel.class.getName());
//		}
		if (pageId.equals("reference")){
			String isReference = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("isReference");
			if ( isReference != null && isReference.equals("true")){
				req.setAttribute(MODEL, AppDefaultReferencePageModel.class.getName());
			}
		}
//		else if(pageId.endsWith(".qry")){
//			req.setAttribute(MODEL, NCQueryTemplatePageModel.class.getName());
//		}
	}

}
