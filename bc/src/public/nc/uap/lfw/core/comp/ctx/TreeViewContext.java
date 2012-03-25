package nc.uap.lfw.core.comp.ctx;

import nc.uap.lfw.core.comp.WebTreeModel;

public class TreeViewContext extends BaseContext {
	private static final long serialVersionUID = 6857680416324844837L;
	private boolean enabled;
	private boolean withRoot;
	private WebTreeModel treeModel;
	// 树根节点默认显示值
	private String text;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isWithRoot() {
		return withRoot;
	}

	public void setWithRoot(boolean withRoot) {
		this.withRoot = withRoot;
	}

	public WebTreeModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(WebTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
