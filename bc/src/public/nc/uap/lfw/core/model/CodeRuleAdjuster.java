package nc.uap.lfw.core.model;

import java.util.Iterator;

import nc.uap.lfw.core.comp.CodeTreeLevel;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.ViewComponents;

/**
 * 编码树转PK树动态调整逻辑 
 * @author dengjt
 *
 */
public class CodeRuleAdjuster implements IRuntimeAdjuster {

	@Override
	public void adjust(PageMeta pm) {
		//为编码树对应的Dataset设置标识
		processCodeRuleDataset(pm);
	}

	/**
	 * 如果是编码树，则为此Dataset设置标识，以便于每次输出到前台之前，转换成PK树
	 */
	protected void processCodeRuleDataset(PageMeta pm) {
		LfwWidget[] widgets = pm.getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			LfwWidget widget = widgets[i];
			ViewComponents comps = widget.getViewComponents();
			Iterator<WebComponent> cit = comps.getComponentsMap().values().iterator();
			while(cit.hasNext())
			{
				WebComponent comp = cit.next();
				if(comp instanceof TreeViewComp){
					TreeViewComp tree = (TreeViewComp) comp;
					TreeLevel level = tree.getTopLevel();
					if(level != null)
						setRuleSignByLevelType(widget, level);
				}
			}	
		}
	}

	protected void setRuleSignByLevelType(LfwWidget widget, TreeLevel level) {
		if(level instanceof CodeTreeLevel){
			Dataset ds = widget.getViewModels().getDataset(level.getDataset());
			ds.setExtendAttribute(Dataset.CODE_LEVEL_CLAZZ, "nc.uap.lfw.core.model.util.CodeRuleDsUtil");
			CodeTreeLevel treeLevel = (CodeTreeLevel) level;
			String codeRule = treeLevel.getCodeRule();
			String codeField = treeLevel.getCodeField();
			String keyField =  treeLevel.getMasterKeyField();
			
			String parentPkField = treeLevel.getRecursivePKeyField();
			if(parentPkField == null)
				parentPkField = Dataset.PARENT_PK_COLUMN;
			ds.setExtendAttribute(Dataset.CODE_LEVEL_CODEFIELD, codeField);
			ds.setExtendAttribute(Dataset.CODE_LEVEL_PPK, parentPkField);
			ds.setExtendAttribute(Dataset.CODE_LEVEL_PK, keyField);
			ds.setExtendAttribute(Dataset.CODE_LEVEL_RULE, codeRule);
			
			if(ds.getFieldSet().getField(parentPkField) == null){
				Field field = new Field(parentPkField);
				ds.getFieldSet().addField(field);
			}
		}
		TreeLevel clevel = level.getChildTreeLevel();
		if(clevel != null)
			setRuleSignByLevelType(widget, clevel);
	}
}
