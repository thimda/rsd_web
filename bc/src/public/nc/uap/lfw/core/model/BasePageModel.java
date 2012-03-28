package nc.uap.lfw.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nc.uap.lfw.core.ClientSession;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.model.util.DefaultPageMetaBuilder;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.validate.DefaultPageMetaValidator;
import nc.uap.lfw.core.page.validate.IPageMetaValidator;


/**
 * 基础页面驱动逻辑
 * @author dengjt
 *
 */
public class BasePageModel {

	//the identifier of pageModel to find it
	private String pageModelId;
	private Properties props;
	private ClientSession clientSession = new ClientSession();
	private String etag;
	private String pk_template ;
	public BasePageModel(){
		pk_template = LfwRuntimeEnvironment.getWebContext().getParameter("pk_templateDB");
		if(pk_template != null && !pk_template.equals("null")){
			LfwRuntimeEnvironment.setFromDB(true);
			LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute("pk_templateDB", pk_template);
		}
	}
	
	/**
	 * pageModelTag在构造pageModel的过程中如果发生异常,会保证调用到该方法
	 */
	public void destroy(){
	}
	
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}
	
	/**
	 * 初始化方法
	 */
	public final void internalInitialize()
	{
		String editMode = getWebContext().getParameter(WebConstant.EDIT_MODE_PARAM);
		String wmodel = getWebContext().getParameter(WebConstant.WINDOW_MODE_PARAM);
		if(wmodel != null && wmodel.equals("1"))
			getWebContext().getWebSession().setAttribute(WebConstant.WINDOW_MODE_KEY, WebConstant.MODE_PERSONALIZATION);
		
		if(editMode != null && editMode.equals("1"))
			getWebContext().getWebSession().setAttribute(WebConstant.EDIT_MODE_KEY, WebConstant.MODE_PERSONALIZATION);
		// 客户端模式（是否为离线应用）
		String clientMode = getWebContext().getParameter(WebConstant.CLIENT_MODE);
		if(clientMode != null)
			getWebContext().getWebSession().setAttribute(WebConstant.CLIENT_MODE, clientMode);
		
		String cachePath = getWebContext().getParameter(WebConstant.OFFLINE_CACHEPATH);
		if(cachePath != null)
			getWebContext().getWebSession().setAttribute(WebConstant.OFFLINE_CACHEPATH, cachePath);
		
		//创建Pagemeta,建议此处仅进行Pagemeta的基础创建（此处获得的Pagemeta一般对所有人都一样）
		PageMeta pageMeta = createPageMeta();
		
		//创建其它UI辅助元素
		IUIMeta uiMeta = createUIMeta(pageMeta);
		
		//将Pagemeta放到上下文中，以便以后的方法可以取到
		getWebContext().setPageMeta(pageMeta);
		
		getWebContext().setUIMeta(uiMeta);
		
		if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
			String appUniqueId = getWebContext().getAppUniqueId();
			getClientSession().setAttribute(WebConstant.APP_UNIQUE_ID, appUniqueId, true);
		}
		pageMeta.adjustForRuntime();
		
		if(uiMeta != null)
			uiMeta.adjustForRuntime();
		
		//Pagemeta的个性初始化，可在此处根据用户权限进行Pagemeta的“剪裁”
		initPageMetaStruct();
		
		//UIMeta的个性初始化，可在此处根据用户权限进行UIMeta的“剪裁”
		initUIMetaStruct();
		
		if(LfwRuntimeEnvironment.getMode() == WebConstant.DEBUG_MODE){
			validate();
		}
		//将剪裁的Pagemeta“副本”放入用户当前页面session中进行存储，以便其它ajax逻辑使用。因为这以后的Pagemeta可能再次根据逻辑被填充数据。
		//PageMetaManager.getInstance().pubPageMetaToSession(getWebContext(), pageUniqueId, (PageMeta) pageMeta.clone());
		if(!LfwRuntimeEnvironment.isClientMode())
			adjustPageMeta(pageMeta);
		
		getWebContext().getWebSession().setAttribute(WebContext.PAGEMETA_KEY, pageMeta);
		if(uiMeta != null)
			getWebContext().getWebSession().setAttribute(WebContext.UIMETA_KEY, uiMeta);
		
		getEtag();
		
		LfwRuntimeEnvironment.getWebContext().adjustPageUniqueId(etag);
		String pageUniqueId = getWebContext().getPageUniqueId();
		//将页面唯一ID写到客户端，确保客户端能够获取到对应信息
		getClientSession().setAttribute(WebConstant.PAGE_UNIQUE_ID, pageUniqueId, true);
		String pageId = getPageModelId();
		//将页面节点ID写到客户端。
		getClientSession().setAttribute(WebConstant.PAGE_ID, pageId, true);
		
		String parentUniqueId = getWebContext().getParentPageUniqueId();
		if(parentUniqueId != null)
			//将页面节点ID写到客户端。
			getClientSession().setAttribute(WebConstant.OTHER_PAGE_UNIQUE_ID, parentUniqueId, true);
		String parentPageId = getWebContext().getParentPageId();
		if(parentPageId != null)
			getClientSession().setAttribute(WebConstant.OTHER_PAGE_ID, parentPageId, true);
		
		//Map<String, String> langResMap = processLangRes();
		afterInternalInitialize();
	}
	
	
	protected void afterInternalInitialize(){
		ButtonStateManager.updateButtons(getPageMeta());
	}

	protected void initUIMetaStruct() {
	}

	/**
	 * 生成UI的辅助元素，实现类可按照自己的UI组织形式具体化
	 * @return
	 */
	protected IUIMeta createUIMeta(PageMeta pm) {
		return null;
	}

	/**
	 * 调整页面Listener，要求每个页面必须有close方法，以确保内存及时释放
	 * @param pageMeta
	 */
	private void adjustPageMeta(PageMeta pageMeta) {
		IRuntimeAdjuster adjuster = RuntimeAdjusterFactory.getRuntimeAdjuster();
		if(adjuster != null)
			adjuster.adjust(pageMeta);
		
		IRuntimeAdjuster codeRuleAdjuster = new CodeRuleAdjuster();
		codeRuleAdjuster.adjust(pageMeta);
	}
	
	

	protected Map<String, String> processLangRes() {
		return null;
	}

	protected void initPageMetaStruct() {
	}
	
	protected PageMeta createPageMeta() {
		if(LfwRuntimeEnvironment.isFromDB()){
			return PageModelUtil.getPageMeta(pk_template);
		}
		else{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(WebConstant.PAGE_ID_KEY, getPageModelId());
			return getPageMetaBuilder().buildPageMeta(param);
		}
	}
	
	protected IPageMetaBuilder getPageMetaBuilder() {
		return new DefaultPageMetaBuilder();
	}
	
	public PageMeta getPageMeta(){
		return getWebContext().getPageMeta();
	}

	public String getPageModelId() {
		if(pageModelId == null)
			pageModelId = getWebContext().getPageId();
		return pageModelId;
	}

	public void setPageModelId(String pageModelId) {
		this.pageModelId = pageModelId;
	}
	
	public ClientSession getClientSession() {
		return clientSession;
	}
	
	public WebContext getWebContext() {
		return LfwRuntimeEnvironment.getWebContext();
	}
	
	private void validate() {
		try {
			IPageMetaValidator validator = getValidator();
			if(validator == null)
				return;
			validator.validate(getPageMeta());
		} catch (LfwValidateException e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
	
	protected IPageMetaValidator getValidator() {
		return new DefaultPageMetaValidator();
	}
	
	/**
	 * 获取客户端缓存标识，本方法只根据文件与模板ts做缓存，如果需要做二级缓存，需谨慎判断是否则需重写此方法。
	 * @return
	 */
	public String getEtag() {
		return null;
//		if(etag == null){
//			etag = createEtag();
//		}
//		return etag;
	}

//	protected String createEtag() {
//		PageMeta pm = getPageMeta();
//		String ts = (String) pm.getExtendAttributeValue(PageMeta.MODIFY_TS);
//		if(ts == null || ts.equals("")){
//			ts = Calendar.getInstance().getTimeInMillis() + "";
//		}
//		
//		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
//		String user = null;
//		if(ses != null){
//			user = ses.getPk_user();
//		}
//		else
//			user = "anoymous";
//		return user + "_" + ts;
//	}
	
}
