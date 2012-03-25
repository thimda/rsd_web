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
 * ������תPK����̬�����߼� 
 * @author dengjt
 *
 */
public class CodeRuleAdjuster implements IRuntimeAdjuster {

	@Override
	public void adjust(PageMeta pm) {
		//Ϊ��������Ӧ��Dataset���ñ�ʶ
		processCodeRuleDataset(pm);
	}

	/**
	 * ����Ǳ���������Ϊ��Dataset���ñ�ʶ���Ա���ÿ�������ǰ̨֮ǰ��ת����PK��
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
