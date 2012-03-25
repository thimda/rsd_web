package nc.uap.lfw.core.comp;

import java.util.Iterator;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.LfwTheme;
import nc.uap.lfw.core.event.conf.JsListenerConf;


/**
 * 后台控件配置类.每一个前台js控件对应一个后台配置类.
 * @author dengjt
 * 2007-1-25
 */
public abstract class WebComponent extends WidgetElement{

	private static final long serialVersionUID = -3847364243868791054L;
	protected boolean visible = true;
	protected boolean enabled = true;
	private String contextMenu;
//	private String className = null;
	
	public WebComponent() {
		super();
	}
	
	public WebComponent(String id){
		super(id);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		if (enabled != this.enabled) {
			this.enabled = enabled;
			setCtxChanged(true);
			addCtxChangedProperty("enabled");
		}
	}


	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		if(this.visible != visible){
			this.visible = visible;
			setCtxChanged(true);
		}
	}

	
	public Object clone()
	{		
		return super.clone();
	}

	public String getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(String contextMenu) {
		this.contextMenu = contextMenu;
	}
//
//	public String getClassName() {
//		return className;
//	}
//
//	public void setClassName(String className) {
//		this.className = className;
//	}

	@Override
	public void mergeProperties(WebElement ele) {
		super.mergeProperties(ele);
		WebComponent comp = (WebComponent) ele;
//		String className = comp.getClassName();
//		if(className != null)
//			this.setClassName(className);
		String contextMenu = comp.getContextMenu();
		if(contextMenu != null)
			this.setContextMenu(contextMenu);
		Map<String, JsListenerConf> handlerMap = comp.getListenerMap();
		
		Map<String, JsListenerConf> sourceHanderMap = this.getListenerMap();
		if(handlerMap != null && handlerMap.size() > 0){
			for (Iterator it = handlerMap.keySet().iterator(); it.hasNext();) {
				String listenerId = (String) it.next();
				JsListenerConf listener  = handlerMap.get(listenerId);
				JsListenerConf originalListener = sourceHanderMap.get(listenerId);
				//如果模板ds有此Listener
				if(originalListener != null){
					//删除的Listener
					if(listener.getConfType().equals(WebElement.CONF_DEL))
						this.removeListener(listenerId);	
					else 
						this.addListener(listener);
				}else{
					//现在存在，以前不存在，是新增的Listener
					this.addListener(listener);
				}
			}
		}
		
		String confType = comp.getConfType();
		if(confType != null)
			this.setConfType(confType);
	}
	
	/**
	 * 获取图片全路径
	 * @param refImg
	 * @param nodeImagePath
	 * @return
	 */
	public String getRealImgPath(String refImg, String nodeImagePath) {
		if (refImg == null)
			return null;
		if (refImg.indexOf("http:") != -1)
			return refImg;
		if (nodeImagePath == null)
			nodeImagePath = LfwRuntimeEnvironment.getWebContext().getPageMeta().getNodeImagePath();
		if (refImg == null || refImg.trim().equals(""))
			refImg = "";
		else if (refImg.contains("${t}")) {
			LfwTheme theme = LfwRuntimeEnvironment.getTheme();
			refImg = theme.getCtxPath() + "/" + refImg.replace("${t}", theme.getId());
		}
		else {
			if ((!refImg.startsWith("/")) && (!"".equals(nodeImagePath))){
				refImg = nodeImagePath + "/" + refImg;
			}
			else{
				refImg = LfwRuntimeEnvironment.getRootPath() + "/" + refImg;
			}
		}
		return refImg;
	}

}
