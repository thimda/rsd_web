package nc.uap.lfw.jsp.uimeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.page.IUIMeta;

public class UIMeta extends UILayoutPanel implements IUIMeta{
	private static final long serialVersionUID = 5927522368179835547L;
	private String folderPath;
	private Map<String, UIWidget> dialogMap = new HashMap<String, UIWidget>();
	public static String ISJQUERY = "jquery";
	public static String INCLUDEJS ="includejs";
	public static String INCLUDEID ="includeid";
	public static String INCLUDECSS = "includecss";
	public static String LFWINCLUDEJS = "lfwincludejs";
	public static String LFWINCLUDECSS= "lfwincludecss";
	public static String ISREFERENCE = "isReference";
	//页面级样式表，直接添加在head区域
	public static String PageCss = "pagecss";
	
	//动态生成uimeta的class类
	public static String GENERATECLASS = "generateclass";
	
	//是否在tab页签生成tabfolder
	public static String TABBODY = "tabBody";
	
	public static String FLOWMODE = "flowmode";
	
	public static String UIPROVIDER = "uiprovider";
	
	public UIMeta() {
		setFlowmode(Boolean.FALSE);
	}
	
	public Boolean getTabBody() {
		return (Boolean) getAttribute(TABBODY);
	}
	public void setTabBody(Boolean tabBody) {
		setAttribute(TABBODY, tabBody);
	}
	
	public Boolean getFlowmode() {
		return (Boolean) getAttribute(FLOWMODE);
	}
	
	public void setFlowmode(Boolean flowMode){
		setAttribute(FLOWMODE, flowMode);
	}
	
	public String getUiprovider() {
		return (String) getAttribute(UIPROVIDER);
	}
	public void setUiprovider(String uiprovider) {
		setAttribute(UIPROVIDER, uiprovider);
	}
	public String getFolderPath() {
		return folderPath;
	}
	
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
	public Map<String, UIWidget> getDialogMap() {
		return dialogMap;
	}
	public void setDialogMap(Map<String, UIWidget> dialogMap) {
		this.dialogMap = dialogMap;
	}
	
	public UIWidget getDialog(String id){
		return dialogMap.get(id);
	}
	
	public void setDialog(String id, UIWidget dialog){
		dialogMap.put(id, dialog);
		this.notifyChange(ADD, dialog);
	}
	
	public String getId(){
		return (String)getAttribute(ID);
	}
	
	public void setId(String id){
		setAttribute(ID, id);
	}
	
	public Integer isJquery() {
		return (Integer) getAttribute(ISJQUERY);
	}
	
	public void setIncludejs(String includejs){
		setAttribute(INCLUDEJS, includejs);
	}
	
	public void setIncludecss(String includecss){
		setAttribute(INCLUDECSS, includecss);
	}
	
	public void setLfwIncludejs(String includejs){
		setAttribute(LFWINCLUDEJS, includejs);
	}
	
	public void setLfwIncludecss(String includecss){
		setAttribute(LFWINCLUDECSS, includecss);
	}
	
	public String getPagecss(){
		return (String)getAttribute(PageCss);
	}
	public void setPagecss(String pagecss){
		setAttribute(PageCss, pagecss);
	}
	
	public String getIncludejs(){
		return (String)getAttribute(INCLUDEJS);
	}
	
	public String getIncludeId(){
		return (String) getAttribute(INCLUDEID);
	}
	
	public void setIncludeId(String ids){
		setAttribute(INCLUDEID, ids);
	}
	
	public String getLfwIncudejs(){
		return (String)getAttribute(LFWINCLUDEJS);
	}

	public String getLfwIncudecss(){
		return (String)getAttribute(LFWINCLUDECSS);
	}
	
	public String getIncludecss(){
		return (String)getAttribute(INCLUDECSS);
	}
	
	public void setJquery(Integer jquery) {
		setAttribute(ISJQUERY, jquery);
	}

	public void setReference(Integer isReference) {
		setAttribute(ISREFERENCE, isReference);
	}
	public Integer isReference() {
		return (Integer) getAttribute(ISREFERENCE);
	}

	
	protected Map<String, Serializable> createAttrMap() {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put(ISJQUERY, 0);
		return map;
	}
	
	public void adjustForRuntime() {
		
	}
	
	@Override
	public UIMeta doClone() {
		UIMeta uimeta = (UIMeta)super.doClone();		
		if(this.dialogMap != null){
			uimeta.dialogMap = new HashMap<String, UIWidget>();
			Iterator<String> keys = this.dialogMap.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				UIWidget widget = this.dialogMap.get(key);
				uimeta.dialogMap.put(key,(UIWidget)widget.doClone());
			}
		}
		
		return uimeta;		
	}
	
	/**
	 * 返回找到的第一个VIEW
	 * @return
	 */
	public UIWidget getFirstView() {
		UIElement ele = this.getElement();
		if(ele instanceof UIWidget)
			return (UIWidget) ele;
		else if(ele instanceof UILayout){
			return getFirstView((UILayout) ele);
		}
		return null;
	}
	
	private UIWidget getFirstView(UILayout layout) {
		List<UILayoutPanel> list = layout.getPanelList();
		Iterator<UILayoutPanel> it = list.iterator();
		while(it.hasNext()){
			UILayoutPanel panel = it.next();
			UIElement ele = panel.getElement();
			if(ele instanceof UIWidget)
				return (UIWidget) ele;
			else if(ele instanceof UILayout)
				return getFirstView((UILayout) ele);
		}
		return null;
	}
	
//	@Override
//	public void addElement(UIElement ele) {
//		super.setElement(ele);
//	}
	@Override
	public void removeElement(UIElement ele) {
		super.removeElement(ele);
	}
	
	/**
	 * 调整UI必须属性
	 */
	public void adjustUI(String widgetId) {
		setWidgetId(widgetId);
		modifyWidgetUIId(this.getElement(), widgetId);
	}
	
	private void modifyWidgetUIId(UIElement ele, String widgetId) {
		if(ele == null)
			return;
		ele.setWidgetId(widgetId);
		if(ele instanceof UILayout){
			Iterator<UILayoutPanel> it = ((UILayout)ele).getPanelList().iterator();
			while(it.hasNext()){
				modifyWidgetUIId(it.next(), widgetId);
			}
		}
		else if(ele instanceof UILayoutPanel){
			UIElement newEle = ((UILayoutPanel)ele).getElement();
			modifyWidgetUIId(newEle, widgetId);
		}
	}
}
