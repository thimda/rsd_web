package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.MenuItemContext;
import nc.uap.lfw.core.event.conf.ContainerListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
/**
 * 菜单项
 *
 */
public class MenuItem extends WebComponent{
	private static final long serialVersionUID = -1068377600722925562L;
	
	private String text = "";
	
	private String i18nName;

	private List<MenuItem> childList = null;
	
	// 鼠标悬停时的提示信息
	private String tip;
	
	private String tipI18nName;

	//设置在checkbox状态下是否选中
	private boolean selected = false;
	//设置此菜单是否checkbox组
	private boolean checkBoxGroup = false;
	// 内部标识信息
	private String tag = null;
	//热键
	private String hotKey = null;
	//热键显示名称
	private String displayHotKey = null;
	//修饰符，默认＝CTRL
	private int modifiers = java.awt.Event.CTRL_MASK;
	
	private String imgIcon;
	
	private String imgIconOn;
	
	private String imgIconDisable;

	// 图片路径是否改变
	private boolean imgIconChanged = true;

	// 图片真实路径，在context中使用
	private String realImgIcon;

	private boolean imgIconOnChanged = true;

	private String realImgIconOn;

	private boolean imgIconDisableChanged = true;

	private String realImgIconDisable;

	private String langDir;
	
	private MenuItem parentItem;

	// 可见性
	private boolean visible = true;
	
	// 是否是分割线
	private boolean sep = false;
	
	//状态管理类
	private String stateManager = "";
	
	public boolean isSep() {
		return sep;
	}

	public void setSep(boolean isSep) {
		this.sep = isSep;
	}

	public MenuItem()
	{
		super();
	}
	
	public MenuItem(String id)
	{
		super(id);
	}
	
	public List<MenuItem> getChildList() {
		return childList;
	}
	
	/**
	 * 清除该item的所有孩子
	 */
	public void removeAllChildren()
	{
		if(this.childList != null)
			this.childList.clear();
	}

	public void setChildList(List<MenuItem> childList) {
		this.childList = childList;
		if(this.childList != null){
			Iterator<MenuItem> it = this.childList.iterator();
			while(it.hasNext()){
				MenuItem item = it.next();
				item.setParentItem(this);
			}
		}
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String label) {
		this.i18nName = label;
	}

	public void addMenuItem(MenuItem item)
	{
		if(childList == null)
			childList = new ArrayList<MenuItem>();
		childList.add(item);
		item.setParentItem(this);
	}

//	public String getBusinessStatusArray() {
//		return businessStatusArray;
//	}
//
//	public void setBusinessStatusArray(String businessStatusArray) {
//		this.businessStatusArray = businessStatusArray;
//	}
//
//	public String getOperatorStatusArray() {
//		return operatorStatusArray;
//	}
//
//	public void setOperatorStatusArray(String operatorStatusArray) {
//		this.operatorStatusArray = operatorStatusArray;
//	}
//
//	
//	public String getUserStatusArray() {
//		return userStatusArray;
//	}
//
//	public void setUserStatusArray(String userStatusArray) {
//		this.userStatusArray = userStatusArray;
//	}
//
//	public String getOperatorVisibleStatusArray() {
//		return operatorVisibleStatusArray;
//	}
//
//	public void setOperatorVisibleStatusArray(String operatorVisibleStatusArray) {
//		this.operatorVisibleStatusArray = operatorVisibleStatusArray;
//	}
//
//	public String getBusinessVisibleStatusArray() {
//		return businessVisibleStatusArray;
//	}
//
//	public void setBusinessVisibleStatusArray(String businessVisibleStatusArray) {
//		this.businessVisibleStatusArray = businessVisibleStatusArray;
//	}
//
//	public String getUserVisibleStatusArray() {
//		return userVisibleStatusArray;
//	}
//
//	public void setUserVisibleStatusArray(String userVisibleStatusArray) {
//		this.userVisibleStatusArray = userVisibleStatusArray;
//	}

	//TODO
	public Object clone()
	{
		MenuItem item = (MenuItem)super.clone();
		item.childList = new ArrayList<MenuItem>();
		if(this.childList != null)
		{
			for(MenuItem child: this.childList)
				item.childList.add((MenuItem)child.clone());
		}
		return item;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}


	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isCheckBoxGroup() {
		return checkBoxGroup;
	}

	public void setCheckBoxGroup(boolean checkBoxGroup) {
		this.checkBoxGroup = checkBoxGroup;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

//
//	public int getBtnAttribute() {
//		return btnAttribute;
//	}
//
//	public void setBtnAttribute(int btnAttribute) {
//		this.btnAttribute = btnAttribute;
//	}

//	public String getBtnCode() {
//		return btnCode;
//	}
//
//	public void setBtnCode(String btnCode) {
//		this.btnCode = btnCode;
//	}
//
//	public String getBtnName() {
//		return btnName;
//	}
//
//	public void setBtnName(String btnName) {
//		this.btnName = btnName;
//	}
//
//	public int getBtnNo() {
//		return btnNo;
//	}
//
//	public void setBtnNo(int btnNo) {
//		this.btnNo = btnNo;
//	}

	public String getDisplayHotKey() {
		return displayHotKey;
	}

	public void setDisplayHotKey(String displayHotKey) {
		this.displayHotKey = displayHotKey;
	}
//
//	public String getExtendStatusArray() {
//		return extendStatusArray;
//	}
//
//	public void setExtendStatusArray(String extendStatusArray) {
//		this.extendStatusArray = extendStatusArray;
//	}

	public String getHotKey() {
		return hotKey;
	}

	public void setHotKey(String hotKey) {
		this.hotKey = hotKey;
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	public String getImgIcon() {
		return imgIcon;
	}

	public void setImgIcon(String imgIcon) {
		if (this.imgIcon != imgIcon) {
			this.imgIcon = imgIcon;
			setCtxChanged(true);
			setImgIconChanged(true);
		}
	}

	public String getImgIconOn() {
		return imgIconOn;
	}

	public void setImgIconOn(String imgIconOn) {
		this.imgIconOn = imgIconOn;
	}

	public String getImgIconDisable() {
		return imgIconDisable;
	}

	public void setImgIconDisable(String imgIconDisable) {
		this.imgIconDisable = imgIconDisable;
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	@Override
	public String toString() {
		return getId() + ":" + getText();
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(ContainerListener.class);
		return list;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text != null && !text.equals(this.text)) {
			this.text = text;
			setCtxChanged(true);
			addCtxChangedProperty("text");
		}
	}

	public String getTipI18nName() {
		return tipI18nName;
	}

	public void setTipI18nName(String tipI18nName) {
		this.tipI18nName = tipI18nName;
	}

	public MenuItem getParentItem() {
		return parentItem;
	}

	public void setParentItem(MenuItem parentItem) {
		this.parentItem = parentItem;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			setCtxChanged(true);
			addCtxChangedProperty("visible");
		}
	}
	
	public boolean isImgIconChanged() {
		return imgIconChanged;
	}

	public void setImgIconChanged(boolean imgIconChanged) {
		this.imgIconChanged = imgIconChanged;
	}
	
	/**
	 * 获取图片真实路径
	 * @return
	 */
	public String getRealImgIcon() {
		if (isImgIconChanged()) {
			realImgIcon = getRealImgPath(this.imgIcon, null);
			setImgIconChanged(false);
		}
		return realImgIcon;
	}

	public void setRealImgIcon(String realImgIcon) {
		this.realImgIcon = realImgIcon;
	}

	public boolean isImgIconOnChanged() {
		return imgIconOnChanged;
	}

	public void setImgIconOnChanged(boolean imgIconOnChanged) {
		this.imgIconOnChanged = imgIconOnChanged;
	}

	public String getRealImgIconOn() {
		if (isImgIconOnChanged()) {
			realImgIconOn = getRealImgPath(this.imgIconOn, null);
			setImgIconOnChanged(false);
		}
		return realImgIconOn;
	}

	public void setRealImgIconOn(String realImgIconOn) {
		this.realImgIconOn = realImgIconOn;
	}

	public boolean isImgIconDisableChanged() {
		return imgIconDisableChanged;
	}

	public void setImgIconDisableChanged(boolean imgIconDisableChanged) {
		this.imgIconDisableChanged = imgIconDisableChanged;
	}

	public String getRealImgIconDisable() {
		if (isImgIconDisableChanged()) {
			realImgIconDisable = getRealImgPath(this.imgIconDisable, null);
			setImgIconDisableChanged(false);
		}
		return realImgIconDisable;
	}

	public void setRealImgIconDisable(String realImgIconDisable) {
		this.realImgIconDisable = realImgIconDisable;
	}
	
	@Override
	public BaseContext getContext() {
		MenuItemContext ctx = new MenuItemContext();
		if (checkCtxPropertyChanged("enabled"))
			ctx.setEnabled(this.enabled);
		if (checkCtxPropertyChanged("visible"))
			ctx.setVisible(this.visible);
		if (checkCtxPropertyChanged("text"))
			ctx.setText(this.text);
//		if (checkCtxPropertyChanged("imgIcon"))
//			ctx.setRefImg(this.imgIcon);
		ctx.setRefImg(this.getRealImgIcon());
		ctx.setId(this.getId());
		if (this.childList != null && this.childList.size() > 0) {
			MenuItemContext[] childItemContexts = new MenuItemContext[this.childList.size()];
			for (int i = 0, n = this.childList.size(); i < n; i++) {
				childItemContexts[i] = (MenuItemContext) this.childList.get(i).getContext();
			}
			ctx.setChildItemContexts(childItemContexts);
		}
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		MenuItemContext miCtx = (MenuItemContext) ctx;
		this.setEnabled(miCtx.getEnabled());
		this.setVisible(miCtx.getVisible());
		this.setText(miCtx.getText());
//		this.setImgIcon(miCtx.getRefImg());
		this.setRealImgIcon(miCtx.getRefImg());
		MenuItemContext[] childItemContexts = miCtx.getChildItemContexts();
		if(childItemContexts != null){
			for (int i = 0, n = childItemContexts.length; i < n; i++) {
				MenuItemContext itemCtx = childItemContexts[i];
				for (int j = 0, m = this.childList.size(); j < m; j++) {
					if (childList.get(j).getId().equals(itemCtx.getId())) {
						childList.get(j).setContext(itemCtx);
						break;
					}
				}
			}
		}
		this.setCtxChanged(false);
	}

	public String getStateManager() {
		return stateManager;
	}

	public void setStateManager(String stateManager) {
		this.stateManager = stateManager;
	}
}
