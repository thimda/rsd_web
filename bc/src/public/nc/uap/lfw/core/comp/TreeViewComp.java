package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.TreeViewContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.TreeContextMenuListener;
import nc.uap.lfw.core.event.conf.TreeNodeListener;
import nc.uap.lfw.core.event.conf.TreeRowListener;
import nc.uap.lfw.core.exception.LfwPluginException;

/**
 * Tree控件描述类
 * 
 * @author dengjt
 * 
 */
public class TreeViewComp extends WebComponent implements IDataBinding,IDetachable {

	private static final long serialVersionUID = 820546587109276281L;
	private WebTreeModel treeModel;
	private TreeLevel topLevel = null;
	// 是否可以拖拽
	private boolean dragEnable;
	// 是否展开根节点
	private boolean rootOpen = true;
	// 默认展开级别
	private int openLevel = -1;
	// 树根节点默认显示值
	private String text = null;
	private String i18nName;
	private boolean withRoot = true;
	// 是否是CheckBoxTree
	private boolean withCheckBox = false;
	// 复选策略 0:只设置自己 1:设置自己和子 2:设置自己和子和父
	private int checkBoxModel = 0;
	
	private boolean canEdit = false;
	
	private String langDir;

	//显示名称
	private String caption;


	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public TreeViewComp() {
		super();
	}

	public TreeViewComp(String id) {
		super(id);
	}

	public TreeViewComp(WebTreeModel mode) {

	}

	public WebTreeModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(WebTreeModel model) {
		this.treeModel = model;
	}

	public String getDataset() {
		if (topLevel == null)
			return null;
		return topLevel.getDataset();
	}

	public TreeLevel getTopLevel() {
		return topLevel;
	}

	public void setTopLevel(TreeLevel topLevel) {
		this.topLevel = topLevel;
	}

	public boolean isDragEnable() {
		return dragEnable;
	}

	public void setDragEnable(boolean dragEnable) {
		this.dragEnable = dragEnable;
	}

	public Object clone() {
		TreeViewComp comp = (TreeViewComp) super.clone();
		if (this.topLevel != null)
			comp.setTopLevel((TreeLevel) this.topLevel.clone());
		if (this.treeModel != null)
			comp.setTreeModel((WebTreeModel) this.treeModel.clone());
		return comp;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public boolean isRootOpen() {
		return rootOpen;
	}

	public void setRootOpen(boolean rootOpen) {
		this.rootOpen = rootOpen;
	}

	
	public void mergeProperties(WebElement ele) {
		super.mergeProperties(ele);
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(TreeNodeListener.class);
		list.add(TreeRowListener.class);
		list.add(TreeContextMenuListener.class);
		return list;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public boolean isWithRoot() {
		return withRoot;
	}

	public void setWithRoot(boolean withRoot) {
		this.withRoot = withRoot;
	}

	@Override
	public BaseContext getContext() {
		TreeViewContext ctx = new TreeViewContext();
		ctx.setEnabled(this.enabled);
		ctx.setText(this.text);
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		TreeViewContext treectx = (TreeViewContext) ctx;
		this.setEnabled(treectx.isEnabled());
		this.setWithRoot(treectx.isWithRoot());
		this.setTreeModel(treectx.getTreeModel());
		this.setText(treectx.getText());
		this.setCtxChanged(false);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if(text == null)
			return;
		if (!text.equals(this.text)) {
			this.text = text;
			this.setCtxChanged(true);
		}
	}
	
	public void validate(){
		StringBuffer buffer = new StringBuffer();
		if(this.getId() == null || this.getId().equals("")){
			buffer.append("树的ID不能为空!\r\n");
		}
		if(buffer.length() > 0)
			throw new  LfwPluginException(buffer.toString());
	}

	public boolean isWithCheckBox() {
		return withCheckBox;
	}

	public void setWithCheckBox(boolean withCheckBox) {
		this.withCheckBox = withCheckBox;
	}

	public int getCheckBoxModel() {
		return checkBoxModel;
	}

	public void setCheckBoxModel(int checkBoxModel) {
		this.checkBoxModel = checkBoxModel;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	@Override
	public void detach() {
		treeModel = null;
	}
	
	
}
