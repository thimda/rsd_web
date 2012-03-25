package nc.uap.lfw.core.cmd;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.util.DefaultPageMetaBuilder;
import nc.uap.lfw.core.model.util.DefaultUIMetaBuilder;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.persistence.PageMetaToXml;
import nc.uap.lfw.core.serializer.IPageElementSerializer;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.pa.PaBusinessException;
import nc.uap.lfw.stylemgr.itf.IUwTemplateService;
import nc.uap.lfw.stylemgr.vo.UwTemplateVO;
import nc.uap.lfw.stylemgr.vo.UwViewVO;


public class UifPersonalizationCmd extends UifCommand {
	private String appId;
	private String busiId;
	private String title;
	private String winId;
	private Map<String, String> wfmparamMap; 
	
	public UifPersonalizationCmd(String appId, String winId, String busiId){
		this(appId, winId, busiId, "¶¨ÖÆ");
	}
	
	public UifPersonalizationCmd(String appId, String winId, String busiId, String title){
		this(appId, winId,busiId, null, title);
	}
	public UifPersonalizationCmd(String appId, String winId, String busiId, Map<String, String> wfmparamMap, String title){
		this.appId = appId;
		this.winId = winId;
		this.busiId = busiId;
		this.wfmparamMap = wfmparamMap;
		this.title = title;
	}

	
	@Override
	public void execute() {
		String pk_template = null;
		if(wfmparamMap == null || wfmparamMap.size() == 0){
			pk_template = getTemplateOrCreate(appId, winId, busiId).getPrimaryKey();
		}
		else
			pk_template = getTemplateOrCreate(appId, winId, busiId, wfmparamMap).getPrimaryKey();
		
		this.initializeTemplate(pk_template, appId, winId);
		
		String url = LfwRuntimeEnvironment.getRootPath() + "/app/mockapp/pa?model=nc.uap.lfw.pa.PaEditorPageModel&from=1&appId=" + appId + "&winId="+ winId + "&pk_template=" +pk_template;
		if(wfmparamMap == null)
			AppLifeCycleContext.current().getApplicationContext().popOuterWindow(url, title, "1024", "1280");
	}

	private UwTemplateVO getTemplateOrCreate(String appId1, String winId1, String busiId1) {
		return getTemplateOrCreate(appId1, winId1, busiId1, null);
	}
	
	private UwTemplateVO getTemplateOrCreate(String appId1, String winId1, String busiId1, Map<String, String> paramMap){
		IUwTemplateService service = NCLocator.getInstance().lookup(IUwTemplateService.class);
		UwTemplateVO vo = null;
		try {
			vo = service.getTemplateOrCreate(appId1, winId1, busiId1, paramMap);
		} catch (PaBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return vo;
	}
	
	private void initializeTemplate(String pk_template, String appId, String winId){
		
		LfwRuntimeEnvironment.getWebContext().getRequest().setAttribute(WebConstant.PERSONAL_PAGE_ID_KEY, winId);
  		IUwTemplateService service = NCLocator.getInstance().lookup(IUwTemplateService.class);
  		IPageElementSerializer pmSerializer = NCLocator.getInstance().lookup(IPageElementSerializer.class);
  		try {
	  		PageMeta pageMeta = this.getPageMetaById(winId);
	  		UIMeta uimeta = this.getUIMetaByWinId(winId);
	  		String pageMetaStr = PageMetaToXml.toString(pageMeta);
	  		String uiMetaStr = pmSerializer.serializeUIMeta(uimeta);
	  		UwTemplateVO vo = service.getTemplateVOByPK(pk_template);
			vo.setAppid(appId);
			vo.setWindowid(winId);
			vo.doSetPageMetaStr(pageMetaStr);
			vo.doSetUIMetaStr(uiMetaStr);
	
  			service.updateTemplateVO(vo);
  			String[] ids = pageMeta.getWidgetIds();
  			for(int i = 0; i < ids.length; i++){
  				String widgetId = ids[i];
  				LfwWidget view = pageMeta.getWidget(widgetId);
  				String widgetStr = pmSerializer.serializeWidget(view);
  				UIMeta childUIMeta = (UIMeta) UIElementFinder.findUIMeta(uimeta, widgetId);
  				if(childUIMeta == null){
					DefaultUIMetaBuilder uiMetaBuilder = new DefaultUIMetaBuilder();
					RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
					UIMeta meta = uiMetaBuilder.buildUIMeta(pageMeta,winId, widgetId);
					childUIMeta = meta;
					RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
  				}
  				if(childUIMeta == null)
  					continue;
  				String childUIMetaStr = pmSerializer.serializeUIMeta(childUIMeta);
				
				UwViewVO viewVO = service.getViewOrCreate(pk_template, widgetId);
				
				viewVO.doSetUIMetaStr(childUIMetaStr);
				viewVO.doSetWidgetStr(widgetStr);
				viewVO.setPk_template(pk_template);
				
				service.updateViewVO(viewVO);
  			}
			
		} catch (PaBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
	  private UIMeta getUIMetaByWinId(  String winId){
		  	PageMeta pageMeta = this.getPageMetaById(winId);
			DefaultUIMetaBuilder uiMetaBuilder = new DefaultUIMetaBuilder();
			LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);		
			UIMeta meta = uiMetaBuilder.buildUIMeta(pageMeta);
			RequestLifeCycleContext.get().setPhase(oriPhase);
			return meta;
	  }
	  private PageMeta getPageMetaById(  String winId){
		  	PageMeta pm = null;
			DefaultPageMetaBuilder dpb = new DefaultPageMetaBuilder();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			paramMap.put(WebConstant.PAGE_ID_KEY, winId);	
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			pm = dpb.buildPageMeta(paramMap);
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
			return pm;
	  }
	  
	  public String responseTemplatePk(){
		  return this.getTemplateOrCreate(this.appId, this.winId, this.busiId, this.wfmparamMap).getPrimaryKey();
	  }
}
