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
 * ����ҳ�������߼�
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
	 * pageModelTag�ڹ���pageModel�Ĺ�������������쳣,�ᱣ֤���õ��÷���
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
	 * ��ʼ������
	 */
	public final void internalInitialize()
	{
		String editMode = getWebContext().getParameter(WebConstant.EDIT_MODE_PARAM);
		String wmodel = getWebContext().getParameter(WebConstant.WINDOW_MODE_PARAM);
		if(wmodel != null && wmodel.equals("1"))
			getWebContext().getWebSession().setAttribute(WebConstant.WINDOW_MODE_KEY, WebConstant.MODE_PERSONALIZATION);
		
		if(editMode != null && editMode.equals("1"))
			getWebContext().getWebSession().setAttribute(WebConstant.EDIT_MODE_KEY, WebConstant.MODE_PERSONALIZATION);
		// �ͻ���ģʽ���Ƿ�Ϊ����Ӧ�ã�
		String clientMode = getWebContext().getParameter(WebConstant.CLIENT_MODE);
		if(clientMode != null)
			getWebContext().getWebSession().setAttribute(WebConstant.CLIENT_MODE, clientMode);
		
		String cachePath = getWebContext().getParameter(WebConstant.OFFLINE_CACHEPATH);
		if(cachePath != null)
			getWebContext().getWebSession().setAttribute(WebConstant.OFFLINE_CACHEPATH, cachePath);
		
		//����Pagemeta,����˴�������Pagemeta�Ļ����������˴���õ�Pagemetaһ��������˶�һ����
		PageMeta pageMeta = createPageMeta();
		
		//��������UI����Ԫ��
		IUIMeta uiMeta = createUIMeta(pageMeta);
		
		//��Pagemeta�ŵ��������У��Ա��Ժ�ķ�������ȡ��
		getWebContext().setPageMeta(pageMeta);
		
		getWebContext().setUIMeta(uiMeta);
		
		if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
			String appUniqueId = getWebContext().getAppUniqueId();
			getClientSession().setAttribute(WebConstant.APP_UNIQUE_ID, appUniqueId, true);
		}
		pageMeta.adjustForRuntime();
		
		if(uiMeta != null)
			uiMeta.adjustForRuntime();
		
		//Pagemeta�ĸ��Գ�ʼ�������ڴ˴������û�Ȩ�޽���Pagemeta�ġ����á�
		initPageMetaStruct();
		
		//UIMeta�ĸ��Գ�ʼ�������ڴ˴������û�Ȩ�޽���UIMeta�ġ����á�
		initUIMetaStruct();
		
		if(LfwRuntimeEnvironment.getMode() == WebConstant.DEBUG_MODE){
			validate();
		}
		//�����õ�Pagemeta�������������û���ǰҳ��session�н��д洢���Ա�����ajax�߼�ʹ�á���Ϊ���Ժ��Pagemeta�����ٴθ����߼���������ݡ�
		//PageMetaManager.getInstance().pubPageMetaToSession(getWebContext(), pageUniqueId, (PageMeta) pageMeta.clone());
		if(!LfwRuntimeEnvironment.isClientMode())
			adjustPageMeta(pageMeta);
		
		getWebContext().getWebSession().setAttribute(WebContext.PAGEMETA_KEY, pageMeta);
		if(uiMeta != null)
			getWebContext().getWebSession().setAttribute(WebContext.UIMETA_KEY, uiMeta);
		
		getEtag();
		
		LfwRuntimeEnvironment.getWebContext().adjustPageUniqueId(etag);
		String pageUniqueId = getWebContext().getPageUniqueId();
		//��ҳ��ΨһIDд���ͻ��ˣ�ȷ���ͻ����ܹ���ȡ����Ӧ��Ϣ
		getClientSession().setAttribute(WebConstant.PAGE_UNIQUE_ID, pageUniqueId, true);
		String pageId = getPageModelId();
		//��ҳ��ڵ�IDд���ͻ��ˡ�
		getClientSession().setAttribute(WebConstant.PAGE_ID, pageId, true);
		
		String parentUniqueId = getWebContext().getParentPageUniqueId();
		if(parentUniqueId != null)
			//��ҳ��ڵ�IDд���ͻ��ˡ�
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
	 * ����UI�ĸ���Ԫ�أ�ʵ����ɰ����Լ���UI��֯��ʽ���廯
	 * @return
	 */
	protected IUIMeta createUIMeta(PageMeta pm) {
		return null;
	}

	/**
	 * ����ҳ��Listener��Ҫ��ÿ��ҳ�������close��������ȷ���ڴ漰ʱ�ͷ�
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
	 * ��ȡ�ͻ��˻����ʶ��������ֻ�����ļ���ģ��ts�����棬�����Ҫ���������棬������ж��Ƿ�������д�˷�����
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
