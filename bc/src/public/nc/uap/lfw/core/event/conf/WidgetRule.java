package nc.uap.lfw.core.event.conf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guoweic
 * 
 */
public class WidgetRule implements Cloneable, Serializable {

	private static final long serialVersionUID = 2083040983378517809L;

	// Widget��ID
	private String id = "";
	// Dataset�ύ���ͼ���
	private Map<String, DatasetRule> dsRuleMap = null;
	// Tree�ύ���ͼ���
	private Map<String, TreeRule> treeRuleMap = null;
	// Grid�ύ���ͼ���
	private Map<String, GridRule> gridRuleMap = null;
	// Excel�ύ���ͼ���
	private Map<String, ExcelRule> excelRuleMap = null;

	private Map<String, FormRule> formRuleMap = null;
	// widget��Tab�Ƿ��ύ
	private boolean tabSubmit = false;
	// widget��Card�Ƿ��ύ
	private boolean cardSubmit = false;
	// widget��Panel�Ƿ��ύ
	private boolean panelSubmit = false;

	public Object clone() {
		WidgetRule widgetRule = null;
		try {
			widgetRule = (WidgetRule) super.clone();
			if(dsRuleMap != null){
				for (String dsId : this.dsRuleMap.keySet()) {
					widgetRule.addDsRule((DatasetRule) this.dsRuleMap.get(dsId)
							.clone());
				}
			}
			if(treeRuleMap != null){
				for (String treeId : this.treeRuleMap.keySet()) {
					widgetRule.addTreeRule((TreeRule) this.treeRuleMap.get(treeId).clone());
				}
			}
			
			if(gridRuleMap != null){
				for (String gridId : this.gridRuleMap.keySet()) {
					widgetRule.addGridRule((GridRule) this.gridRuleMap.get(gridId)
							.clone());
				}
			}
			
			if(excelRuleMap != null){
				for (String excelId : this.excelRuleMap.keySet()) {
					widgetRule.addExcelRule((ExcelRule) this.excelRuleMap.get(excelId)
							.clone());
				}
			}
		} 
		catch (CloneNotSupportedException e) {
		}
		return widgetRule;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Map<String, DatasetRule> getDsRuleMap() {
		if(dsRuleMap == null)
			dsRuleMap = new HashMap<String, DatasetRule>();
		return dsRuleMap;
	}
//
//	public void setDsRuleMap(Map<String, DatasetRule> datasetRules) {
//		this.dsRuleMap = datasetRules;
//	}

	public DatasetRule[] getDatasetRules() {
		return dsRuleMap == null ? null : dsRuleMap.values().toArray(new DatasetRule[0]);
	}

	public DatasetRule getDatasetRule(String id){
		return  dsRuleMap == null ? null : dsRuleMap.get(id);
	}
	
	/**
	 * ����Dataset�ύ����
	 * 
	 * @param dsRule
	 */
	public void addDsRule(DatasetRule dsRule) {
		getDsRuleMap().put(dsRule.getId(), dsRule);
	}
	
	/**
	 * ɾ��Dataset�ύ����
	 * @param dsRule
	 */
	public void removeDsRule(String dsRuleId) {
		getDsRuleMap().remove(dsRuleId);
	}

	private Map<String, TreeRule> getTreeRuleMap() {
		if(treeRuleMap == null)
			treeRuleMap = new HashMap<String, TreeRule>();
		return treeRuleMap;
	}

	public TreeRule[] getTreeRules() {
		return treeRuleMap == null ? null : treeRuleMap.values().toArray(new TreeRule[0]);
	}

	/**
	 * ����Tree�ύ����
	 * 
	 * @param treeRule
	 */
	public void addTreeRule(TreeRule treeRule) {
		getTreeRuleMap().put(treeRule.getId(), treeRule);
	}
	
	/**
	 * ɾ���ύ����
	 * @param treeRule
	 */
	public void removeTreeRule(String treeRuleId) {
		getTreeRuleMap().remove(treeRuleId);
	}

	/**
	 * ����Grid�ύ����
	 * 
	 * @param gridRule
	 */
	public void addGridRule(GridRule gridRule) {
		getGridRuleMap().put(gridRule.getId(), gridRule);
	}
	
	/**
	 * ɾ��grid�ύ����
	 * @param gridRule
	 */
	public void removeGridRule(String gridRuleId) {
		getGridRuleMap().remove(gridRuleId);
	}

	/**
	 * ����Excel�ύ����
	 * 
	 * @param excelRule
	 */
	public void addExcelRule(ExcelRule excelRule) {
		getExcelRuleMap().put(excelRule.getId(), excelRule);
	}
	
	/**
	 * ɾ��Excel�ύ����
	 * @param excelRule
	 */
	public void removeExcelRule(String excelRuleId) {
		getExcelRuleMap().remove(excelRuleId);
	}
	
	/**
	 * ����Form�ύ����
	 * 
	 * @param formRule
	 */
	public void addFormRule(FormRule formRule) {
		getFormRuleMap().put(formRule.getId(), formRule);
	}
	
	/**
	 * ɾ��form�ύ����
	 * @param formRule
	 */
	public void removeFormRule(String formRuleId) {
		getFormRuleMap().remove(formRuleId);
	}

	private Map<String, GridRule> getGridRuleMap() {
		if(gridRuleMap == null)
			gridRuleMap = new HashMap<String, GridRule>();
		return gridRuleMap;
	}
	
	public GridRule[] getGridRules() {
		return gridRuleMap == null ? null : gridRuleMap.values().toArray(new GridRule[0]);
	}

	private Map<String, ExcelRule> getExcelRuleMap() {
		if(excelRuleMap == null)
			excelRuleMap = new HashMap<String, ExcelRule>();
		return excelRuleMap;
	}
	
	public ExcelRule[] getExcelRules() {
		return excelRuleMap == null ? null : excelRuleMap.values().toArray(new ExcelRule[0]);
	}

	public FormRule[] getFormRules() {
		return formRuleMap == null ? null : formRuleMap.values().toArray(new FormRule[0]);
	}
	
	private Map<String, FormRule> getFormRuleMap() {
		if(formRuleMap == null)
			formRuleMap = new HashMap<String, FormRule>();
		return formRuleMap;
	}
	
	public boolean isTabSubmit() {
		return tabSubmit;
	}

	public void setTabSubmit(boolean tabSubmit) {
		this.tabSubmit = tabSubmit;
	}

	public boolean isCardSubmit() {
		return cardSubmit;
	}

	public void setCardSubmit(boolean cardSubmit) {
		this.cardSubmit = cardSubmit;
	}

	public boolean isPanelSubmit() {
		return panelSubmit;
	}

	public void setPanelSubmit(boolean panelSubmit) {
		this.panelSubmit = panelSubmit;
	}

}
