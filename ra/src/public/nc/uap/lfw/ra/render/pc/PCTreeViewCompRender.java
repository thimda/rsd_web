package nc.uap.lfw.ra.render.pc;

import java.util.Iterator;

import nc.uap.lfw.core.comp.RecursiveTreeLevel;
import nc.uap.lfw.core.comp.SimpleTreeLevel;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WebTreeNode;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.conf.TreeNodeListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITreeComp;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;
import nc.uap.lfw.util.StringUtil;

/**
 * @author renxh 树形控件渲染器
 */
public class PCTreeViewCompRender extends UINormalComponentRender<UITreeComp, TreeViewComp> {

	public PCTreeViewCompRender(UITreeComp uiEle,TreeViewComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}
	@Override
	public String generateBodyHtml() {

		return super.generateBodyHtml();
	}
	@Override
	public String generateBodyHtmlDynamic() {

		return super.generateBodyHtmlDynamic();
	}

	public String generateBodyScript() {

		TreeViewComp tree = this.getWebElement();
		StringBuffer buf = new StringBuffer();

		String treeId = getVarId();
		// buf.append("var " + treeId +
		// " = new TreeViewComp(document.getElementById(\"" + DIV_PRE + getId()
		// + "\"), \"" + getId() + "\", 0, 0, \"100%\", \"100%\", \"relative\","
		// + tree.isRootOpen() + "," + tree.isWithRoot() + ", null);\n")
		buf.append("" + treeId + " = new TreeViewComp(document.getElementById(\"" + getDivId() + "\"), \"" + getId());
		buf.append("\", 0, 0, \"100%\", \"100%\", \"relative\",");	
		buf.append(tree.isWithCheckBox() + "," + tree.isRootOpen() + "," + tree.isWithRoot()+ ", null, null, {flowmode:").append(isFlowMode())
			.append(",canEdit:").append(tree.isCanEdit()).append("});\n");
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + treeId + ");\n");

		if (tree.isWithRoot() && tree.getText() != null) {
			String rootCaption = translate(tree.getText(), tree.getText(), tree.getLangDir());
			buf.append(treeId).append(".rootCaption=\"").append(rootCaption).append("\";\n");
		}
		boolean dragEnable = tree.isDragEnable();
		if (dragEnable)
			buf.append(treeId).append(".setDragEnabled(true);\n");

		// 设置复选模式
		int checkBoxModel = tree.getCheckBoxModel();
		if (checkBoxModel != -1)
			buf.append(treeId).append(".setCheckBoxModel(" + checkBoxModel + ");\n");

		// 以静态树优先
		if (tree.getTreeModel() != null) {
			WebTreeNode root = tree.getTreeModel().getRootNode();
			buf.append(genScriptForTreeNode(null, root));
		} else {
			TreeLevel level = tree.getTopLevel();
			if (level != null) {
				buf.append(genScriptForTreeLevel(level));

			}
		}

		// StringBuffer buf = new StringBuffer();
		// TreeViewComp tree = (TreeViewComp)getComponent();
		TreeLevel level = tree.getTopLevel();
		String levelId = TL_PRE + getCurrWidget().getId() + "_" + level.getId();
		if (level != null) {
			buf.append(getVarId()).append(".setTreeLevel(").append(levelId).append(");\n");
		}
		if (tree.getOpenLevel() != -1){
//			buf.append(getVarId()).append(".openNodesByLevel(" + tree.getOpenLevel();\n");
			buf.append("TreeViewComp.currTreeComp = " + getVarId() + ";\n");
			buf.append("setTimeout(\"openNodesByLevel(" + tree.getOpenLevel() + ",'" + getId() + "')\",500);\n");
		}
		
			
		// return buf.toString();
		return buf.toString();
	}

	private String genScriptForTreeLevel(TreeLevel level) {
		StringBuffer buf = new StringBuffer();
		String levelId = TL_PRE + getCurrWidget().getId() + "_" + level.getId();
		if (level.getType().equals(TreeLevel.TREE_LEVEL_TYPE_RECURSIVE)) {
			RecursiveTreeLevel rLevel = (RecursiveTreeLevel) level;
			// buf.append("var ")
			buf.append("").append(levelId).append(" = ").append("new TreeLevel(\"").append(rLevel.getId()).append("\", ").append(
					getDatasetVarShowId(rLevel.getDataset(), getCurrWidget().getId())).append(",\"").append(rLevel.getType()).append("\",\"").append(
					rLevel.getRecursiveKeyField()).append("\",\"").append(rLevel.getRecursivePKeyField()).append("\",").append(
					StringUtil.mergeScriptArray(rLevel.getLabelFields())).append(",\"").append(rLevel.getMasterKeyField()).append("\",\"").append(
					rLevel.getDetailKeyParameter()).append("\",").append(StringUtil.mergeScriptArray(rLevel.getLabelDelims())).append(",\"").append(rLevel.getLoadField() == null ? "" : rLevel.getLoadField()).append("\"");

			buf.append(");\n");
			if (rLevel.getContextMenu() != null) {
				buf.append(addContextMenu(rLevel.getContextMenu(), levelId));
			}
		}
		// Simple Tree
		else {
			SimpleTreeLevel rLevel = (SimpleTreeLevel) level;
			// buf.append("var ")
			buf.append("").append(levelId).append(" = ").append("new TreeLevel(\"").append(rLevel.getId()).append("\", ").append(
					getDatasetVarShowId(rLevel.getDataset(), getCurrWidget().getId())).append(",\"").append(rLevel.getType()).append("\",null,null,")
					.append(StringUtil.mergeScriptArray(rLevel.getLabelFields())).append(",").append(
							StringUtil.mergeScriptArray(rLevel.getMasterKeyField())).append(",").append(
							StringUtil.mergeScriptArray(rLevel.getDetailKeyParameter()));
			buf.append(",").append(StringUtil.mergeScriptArray(rLevel.getLabelDelims())).append(",\"").append(rLevel.getLoadField() == null ? "" : rLevel.getLoadField()).append("\"");
			buf.append(");\n");

			if (rLevel.getContextMenu() != null) {
				buf.append(addContextMenu(rLevel.getContextMenu(), levelId));
			}
		}
		if (level.getChildTreeLevel() != null) {
			TreeLevel childLevel = level.getChildTreeLevel();
			if (childLevel != null) {
				buf.append(genScriptForTreeLevel(childLevel));
				String cId = TL_PRE + getCurrWidget().getId() + "_" + childLevel.getId();
				buf.append(levelId).append(".addTreeLevel(").append(cId).append(");\n");
			}
		}
		return buf.toString();
	}

	private String genScriptForTreeNode(String pId, WebTreeNode node) {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(node.getId()).append(" = new TreeNode(\"").append(node.getId()).append("\",\"").append(node.getLabel()).append(
				"\",\"").append(node.getValue()).append("\",\"").append("\",\"\",true,true,false,null);\n");
		if (pId != null)
			buf.append(pId).append(".add(").append(node.getId()).append(");\n");
		else {
			buf.append(COMP_PRE).append(getId()).append(".addRoot(").append(node.getId()).append(");\n");
		}
		if (node.getChildNodeList() != null) {
			Iterator<WebTreeNode> it = node.getChildNodeList().iterator();
			while (it.hasNext()) {
				buf.append(genScriptForTreeNode(node.getId(), it.next()));
			}
		}

		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TREE;
	}

	protected void addProxyParam(StringBuffer buf, String eventName) {
		if (TreeNodeListener.ON_DRAG_START.equals(eventName)) {
			buf.append("proxy.addParam('sourceNodeRowId', treeNodeDragEvent.sourceNode.nodeData.rowId);\n");
		} else if (TreeNodeListener.ON_DRAG_END.equals(eventName)) {
			buf.append("proxy.addParam('sourceNodeRowId', treeNodeDragEvent.sourceNode.nodeData.rowId);\n");
			buf.append("proxy.addParam('targetNodeRowId', treeNodeDragEvent.targetNode.nodeData.rowId);\n");
		} else if (TreeNodeListener.ON_DBCLICK.equals(eventName) || TreeNodeListener.ON_CLICK.equals(eventName)) {
			buf.append("proxy.addParam('nodeRowId', treeNodeEvent.node.nodeData == null ? null : treeNodeEvent.node.nodeData.rowId);\n");
			//buf.append("proxy.addParam('nodeRowId', treeNodeMouseEvent.node.nodeData == null ? null : treeNodeMouseEvent.node.nodeData.rowId);\n");
		} else if (TreeNodeListener.ON_CHECKED.equals(eventName)) {
			buf.append("proxy.addParam('nodeRowId', treeNodeEvent.node.nodeData.rowId);\n");
		} else if(TreeNodeListener.AFTER_SEL_NODE_CHANGE.equals(eventName)){
			buf.append("proxy.addParam('nodeRowId', treeNodeEvent.node.nodeData.rowId);\n");
			buf.append("proxy.addParam('datasetId', treeNodeEvent.node.nodeData.dataset.id);\n");
		}
	}

	public String generalEditableHeadScript() {
		if(this.isEditMode() && getDivId() != null){
			return toResize("$ge(\""+getDivId()+"\")","editableDivResize");
		}
		return "";
	}

}
