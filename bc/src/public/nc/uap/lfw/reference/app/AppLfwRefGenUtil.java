package nc.uap.lfw.reference.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.RecursiveTreeLevel;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.comp.SimpleTreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.TreeNodeListener;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.reference.ReferenceConstant;
import nc.uap.lfw.reference.base.GridRefModel;
import nc.uap.lfw.reference.base.ILfwRefModel;
import nc.uap.lfw.reference.base.TreeGridRefModel;
import nc.uap.lfw.reference.base.TreeRefModel;
import nc.uap.lfw.reference.base.TwoTreeGridRefModel;
import nc.uap.lfw.reference.base.TwoTreeRefModel;
import nc.uap.lfw.reference.util.LfwRefUtil;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.IRefConst;

/**
 * ���ݲ���model����dataset�Ϳؼ��Ĺ�����
 * @author gd 2007-9-28 
 * @author dengjt use service to break the reference to other client modules
 */
public class AppLfwRefGenUtil {
	
//	private static final String PARENT_WIDGET = "main";

	private String WIDGET_ID = "main";
	
	private ILfwRefModel model;
	private RefNode refnode;
	public AppLfwRefGenUtil(ILfwRefModel model, RefNode refnode){
		this.model = model;
		this.refnode = refnode;
	}
	
	/**
	 * ����refNodeName����dataset
	 * @param dsId
	 * @return
	 */
	public Dataset[] getDataset() {
		ArrayList<Dataset> datasets = new ArrayList<Dataset>(); 
		// ��ȡ��������
		int refType = LfwRefUtil.getRefType(model);
			
		// "���Ͳ���"
		if(refType == ILfwRefModel.REFTYPE_GRID) {
			GridRefModel gmodel = (GridRefModel) model;
			// Ҫ���ɵ�ds
			Dataset ds = generateGridDataset(gmodel);
			datasets.add(ds);
		}
		// "���Ͳ���"
		else if(refType == IRefConst.TREE) {
			TreeRefModel treeAbsModel = (TreeRefModel)model;
			if(treeAbsModel.getCodingRule() != null)
			{
			}
			//��ͨ��
			else {
				// Ҫ���ɵ�ds
				Dataset ds = generateTreeDataset();
				datasets.add(ds);
			}
		}
		// �����Ͳ���
		else if(refType == IRefConst.GRIDTREE) {
			TreeGridRefModel treeGridAbsModel = (TreeGridRefModel)model;
			/** Ҫ���ɵ���ds */
			Dataset ds = new Dataset();
			ds.setId(ReferenceConstant.MASTER_DS_ID + "_tree");
			
			if(treeGridAbsModel.getClassCodingRule() != null)
			{
			}
			else{
				String[] codes = treeGridAbsModel.getClassFieldCodes();
				if(codes != null){
					for (int j = 0; j < codes.length; j++) {
						if(codes[j] == null)
							continue;
						Field field = new Field();
						//field.setField(codes[j]);
						field.setDataType(StringDataTypeConst.STRING);
						if(codes[j].indexOf(".") != -1){
							field.setField(codes[j].split("\\.")[1].replaceAll("\\.|\\|\\||'|-", "_"));
							field.setId(codes[j].split("\\.")[1].replaceAll("\\.|\\|\\||'|-", "_"));
						}
						else{
							field.setField(codes[j].replaceAll("\\.|\\|\\||'|-", "_"));
							field.setId(codes[j].replaceAll("\\.|\\|\\||'|-", "_"));
						}
						//field.setId(codes[j]);
						ds.getFieldSet().addField(field);
					}
				}
			}
			
			ds.setLazyLoad(false);
			//�����Ҫ��ҵ��Ԫ���ˣ��������ݼ����óɻ�����
			if(refnode instanceof NCRefNode){
				NCRefNode ncRefNode = (NCRefNode) refnode;
				if(ncRefNode.isOrgs())
					ds.setLazyLoad(true);
			}
			datasets.add(ds);
			
			//����Dataset���¼�������
			geneDatasetLoadEvent(ds);
			//��ѡ���¼�
			geneDatasetRowSelEvent(ds);
			
				
			/** Ҫ���ɵı�ds */
			Dataset gridDs = new Dataset();
			gridDs.setId(ReferenceConstant.MASTER_DS_ID);
			// ��ʾ�ֶ��б�
			String[] showColumns = treeGridAbsModel.getVisibleFieldCodes();
			// ����ʾ�ֶ��б�
			String[] hiddenColums = treeGridAbsModel.getHiddenFieldCodes();
			if(showColumns != null)
			{
				for(int i = 0; i < showColumns.length; i ++)
				{
					Field field = new Field();
					
					field.setI18nName(treeGridAbsModel.getVisibleFieldNames()[i]);
					field.setDataType(StringDataTypeConst.STRING);
					
					if(showColumns[i].startsWith("substring")){
						showColumns[i] = showColumns[i].substring("substring".length() + 1,  showColumns[i].indexOf(","));
					}
					if(showColumns[i].indexOf(".") != -1){
						if(gridDs.getFieldSet().getField(showColumns[i].split("\\.")[1]) == null){
							field.setField(showColumns[i].split("\\.")[1]);
							field.setId(showColumns[i].split("\\.")[1].replaceAll("\\.|\\|\\||'|-", "_"));
						}
						else{
							field.setField(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
							field.setId(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
						}
					}
					else{
						field.setField(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
						field.setId(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
					}
					gridDs.getFieldSet().addField(field);
				}
			}
			if(hiddenColums != null)
			{ 
				for(int i = 0; i < hiddenColums.length; i ++)
				{
					Field field = new Field();
					field.setField(hiddenColums[i]);
					field.setI18nName(treeGridAbsModel.getHiddenFieldNames()[i]);
					field.setDataType(StringDataTypeConst.STRING);
					if(hiddenColums[i].indexOf(".") != -1){
						field.setId(hiddenColums[i].split("\\.")[1].replaceAll("\\.", "_"));
					}else
						field.setId(hiddenColums[i].replaceAll("\\.", "_"));
					gridDs.getFieldSet().addField(field);
				}
			}
			gridDs.setLazyLoad(true);
			datasets.add(gridDs);
		}
		
		//2����grid����
		else if(refType == ILfwRefModel.REFTYPE_TWOTREEGRID){
			TwoTreeGridRefModel twoTreeGridAbsModel = (TwoTreeGridRefModel)model;
			if(twoTreeGridAbsModel.getFirstLevelRefMode() != null){
				TreeRefModel firstRefMode = twoTreeGridAbsModel.getFirstLevelRefMode();
				if(firstRefMode.getCodingRule() != null)
				{}
	
				//��ͨ��
				else {
					// Ҫ���ɵ�ds
					Dataset ds = new Dataset();
					ds.setPageSize(20);
					ds.setId(ReferenceConstant.MASTER_DS_ID + "_tree_1");
					// ��ʾ�ֶ��б�
					String[] showColumns = firstRefMode.getVisibleFieldCodes();
					// ����ʾ�ֶ��б�
					String[] hiddenColums = firstRefMode.getHiddenFieldCodes();
					if(showColumns != null)
					{
						for(int i = 0; i < showColumns.length; i ++)
						{
							Field field = new Field(); 
							field.setField(showColumns[i]);
							field.setI18nName(showColumns[i]);
							field.setDataType(StringDataTypeConst.STRING);
							field.setId(showColumns[i].replaceAll("\\.", "_"));
							ds.getFieldSet().addField(field);
						}
					}
					if(hiddenColums != null)
					{
						for(int i = 0; i < hiddenColums.length; i ++)
						{
							Field field = new Field();
							field.setField(hiddenColums[i]);
							field.setI18nName(hiddenColums[i]);
							field.setDataType(StringDataTypeConst.STRING);
							field.setId(hiddenColums[i].replaceAll("\\.", "_"));
							ds.getFieldSet().addField(field);
						}
					}
					ds.setLazyLoad(false);
					//���ɼ������¼�
					geneDatasetLoadEvent(ds);
					//������ѡ���¼�
					geneDatasetRowSelEvent(ds);
					
					datasets.add(ds);
				}
			}
			
			
			/** Ҫ���ɵ���ds */
			Dataset detialDs = new Dataset();
			detialDs.setId(ReferenceConstant.MASTER_DS_ID + "_tree");
			
			if(twoTreeGridAbsModel.getClassCodingRule() != null)
			{
			}
			else{
				String[] codes = new String[]{twoTreeGridAbsModel.getClassRefPkField(), twoTreeGridAbsModel.getClassRefCodeField(), twoTreeGridAbsModel.getClassRefNameField(), twoTreeGridAbsModel.getDetailParameter()};
				if(codes != null){
					for (int j = 0; j < codes.length; j++) {
						if(codes[j] == null)
							continue;
						Field field = new Field();
						field.setField(codes[j]);
						field.setDataType(StringDataTypeConst.STRING);
						field.setId(codes[j]);
						detialDs.getFieldSet().addField(field);
					}
				}
			}
			
			detialDs.setLazyLoad(true);
			datasets.add(detialDs);
			
			//���ڶ����������ݼ��Ӽ�����
			geneDatasetLoadEvent(detialDs);
			geneDatasetRowSelEvent(detialDs);
			
			
			/** Ҫ���ɵı�ds */
			Dataset gridDs = new Dataset();
			//gridDs.setPageSize(twoTreeGridAbsModel.getPageSize());
			gridDs.setId(ReferenceConstant.MASTER_DS_ID);
			// ��ʾ�ֶ��б�
			String[] showColumns = twoTreeGridAbsModel.getVisibleFieldCodes();
			// ����ʾ�ֶ��б�
			String[] hiddenColums = twoTreeGridAbsModel.getHiddenFieldCodes();
			if(showColumns != null)
			{
				for(int i = 0; i < showColumns.length; i ++)
				{
					Field field = new Field();
					field.setField(showColumns[i]);
					field.setI18nName(twoTreeGridAbsModel.getVisibleFieldNames()[i]);
					field.setDataType(StringDataTypeConst.STRING);
					field.setId(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
					gridDs.getFieldSet().addField(field);
				}
			}
			if(hiddenColums != null)
			{ 
				for(int i = 0; i < hiddenColums.length; i ++)
				{
					Field field = new Field();
					field.setField(hiddenColums[i]);
					field.setI18nName(twoTreeGridAbsModel.getHiddenFieldNames()[i]);
					field.setDataType(StringDataTypeConst.STRING);
					field.setId(hiddenColums[i].replaceAll("\\.", "_"));
					gridDs.getFieldSet().addField(field);
				}
			}
			gridDs.setLazyLoad(true);
			datasets.add(gridDs);
			
		}
		else if(refType ==ILfwRefModel.REFTYPE_TWOTREE) {
			TwoTreeRefModel treeAbsModel = (TwoTreeRefModel)model;
			if(treeAbsModel.getFirstLevelRefMode() != null){
				TreeRefModel firstRefMode = treeAbsModel.getFirstLevelRefMode();
				if(firstRefMode.getCodingRule() != null)
				{}
	
				//��ͨ��
				else {					
					// Ҫ���ɵ�ds
					Dataset ds = new Dataset();
					ds.setPageSize(20);
					ds.setId(ReferenceConstant.MASTER_DS_ID + "_tree_1");
					// ��ʾ�ֶ��б�
					String[] showColumns = firstRefMode.getVisibleFieldCodes();
					// ����ʾ�ֶ��б�
					String[] hiddenColums = firstRefMode.getHiddenFieldCodes();
					if(showColumns != null)
					{
						for(int i = 0; i < showColumns.length; i ++)
						{
							Field field = new Field(); 
							field.setField(showColumns[i]);
							field.setI18nName(showColumns[i]);
							field.setDataType(StringDataTypeConst.STRING);
							field.setId(showColumns[i].replaceAll("\\.", "_"));
							ds.getFieldSet().addField(field);
						}
					}
					if(hiddenColums != null)
					{
						for(int i = 0; i < hiddenColums.length; i ++)
						{
							Field field = new Field();
							field.setField(hiddenColums[i]);
							field.setI18nName(hiddenColums[i]);
							field.setDataType(StringDataTypeConst.STRING);
							field.setId(hiddenColums[i].replaceAll("\\.", "_"));
							ds.getFieldSet().addField(field);
						}
					}
					
					ds.setLazyLoad(false);
					geneDatasetLoadEvent(ds);
					geneDatasetRowSelEvent(ds);
					
					datasets.add(ds);
				}
			}
						
			
			if(treeAbsModel.getCodingRule() != null)
			{}
//				
//			}
			//��ͨ��
			else {
				
				
				// Ҫ���ɵı�����ds
				Dataset dsDetail = new Dataset();
				dsDetail.setPageSize(20);
				dsDetail.setId(ReferenceConstant.MASTER_DS_ID);
				// ��ʾ�ֶ��б�
				String[] showColumns = model.getVisibleFieldCodes();
				// ����ʾ�ֶ��б�
				String[] hiddenColums = model.getHiddenFieldCodes();
				if(showColumns != null)
				{
					for(int i = 0; i < showColumns.length; i ++)
					{
						Field field = new Field(); 
						field.setField(showColumns[i]);
						field.setI18nName(showColumns[i]);
						field.setDataType(StringDataTypeConst.STRING);
						field.setId(showColumns[i].replaceAll("\\.", "_"));
						dsDetail.getFieldSet().addField(field);
					}
				}
				if(hiddenColums != null)
				{
					for(int i = 0; i < hiddenColums.length; i ++)
					{
						Field field = new Field();
						field.setField(hiddenColums[i]);
						field.setI18nName(hiddenColums[i]);
						field.setDataType(StringDataTypeConst.STRING);
						field.setId(hiddenColums[i].replaceAll("\\.", "_"));
						dsDetail.getFieldSet().addField(field);
					}
				}
				
				dsDetail.setLazyLoad(true);
				//detailds���¼�������
				geneDatasetLoadEvent(dsDetail);
				datasets.add(dsDetail);
				
			}
		}
		return datasets.toArray(new Dataset[0]);
	}

	private void geneDatasetRowSelEvent(Dataset ds) {
		EventSubmitRule sr = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		sr.addWidgetRule(wr);
		sr.setParentSubmitRule(generateParentSubmitRule());
		EventConf datasetSelevent = new EventConf();
		datasetSelevent.setJsEventClaszz("nc.uap.lfw.core.event.conf.DatasetListener");
		datasetSelevent.setOnserver(true);
		datasetSelevent.setSubmitRule(sr);
		datasetSelevent.setName("onAfterRowSelect");
		datasetSelevent.setMethodName("onAfterRowSelect");
		if(refnode.getDataListener() != null)
			datasetSelevent.setControllerClazz(refnode.getDataListener());
		ds.addEventConf(datasetSelevent);
	}

	private void geneDatasetLoadEvent(Dataset ds) {
		EventSubmitRule sr = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		sr.addWidgetRule(wr);
		sr.setParentSubmitRule(generateParentSubmitRule());
		
		EventConf event = new EventConf();
		event.setJsEventClaszz("nc.uap.lfw.core.event.conf.DatasetListener");
		
		if(refnode.isClientCache() == true){
			event.setOnserver(false);
			StringBuffer buf = new StringBuffer();
			buf.append("if(getFromCache(dataLoadEvent) == false)" +
					"{\n var proxy = new ServerProxy(this.$TEMP_onDataLoad" +  ", " +
							"'onDataLoad', true);\n if(dataLoadEvent.userObj != null) " +
							"{\n proxy.setReturnArgs(dataLoadEvent.userObj);\n}\n " +
							"showDefaultLoadingBar();\n");
			buf.append("proxy.addParam('clc',");
			buf.append(refnode.getDataListener() == null || refnode.getDataListener() == ""? "'nc.uap.lfw.reference.app.AppReferenceController');" : "'" + refnode.getDataListener() + "');");
			buf.append("\n proxy.addParam('el', '2');\n proxy.addParam('source_id',");
			buf.append("'" + ds.getId() + "');\n proxy.addParam('m_n', 'onDataLoad');\n return proxy.execute();\n }\n");
			event.setScript(buf.toString());
		}
		else
			event.setOnserver(true);
		
		event.setSubmitRule(sr);
		event.setName("onDataLoad");
		if(refnode.getDataListener() != null)
			event.setControllerClazz(refnode.getDataListener());
		event.setMethodName("onDataLoad");
		ds.addEventConf(event);	
	}
	
	/**
	 * ���Բ����������ݼ�ds
	 * @return
	 */
	private Dataset generateTreeDataset() {
		Dataset ds = new Dataset();
		if(model.getPageSize() != 0)
			ds.setPageSize(model.getPageSize());
		ds.setId(ReferenceConstant.MASTER_DS_ID);
		// ��ʾ�ֶ��б�
		String[] showColumns = model.getVisibleFieldCodes();
		// ����ʾ�ֶ��б�
		String[] hiddenColums = model.getHiddenFieldCodes();
		if(showColumns != null)
		{
			for(int i = 0; i < showColumns.length; i ++)
			{
				Field field = new Field(); 
				String showColumn = showColumns[i];
				//���˱���
				if(showColumn.indexOf(".") != -1){
					field.setId(showColumn.split("\\.")[1].replaceAll("\\.", "_"));
					field.setField(showColumn.split("\\.")[1].replaceAll("\\.", "_"));
				}
				else {
					field.setId(showColumn.replaceAll("\\.", "_"));
					field.setField(showColumn);
				}
				field.setDataType(StringDataTypeConst.STRING);
				ds.getFieldSet().addField(field);
			}
		}
		if(hiddenColums != null)
		{
			for(int i = 0; i < hiddenColums.length; i ++)
			{
				Field field = new Field();
				String hiddenColumn = hiddenColums[i];
				if(hiddenColumn.indexOf(".") != -1){
					field.setId(hiddenColumn.split("\\.")[1].replaceAll("\\.", "_"));
					field.setField(hiddenColumn.split("\\.")[1]);
				}
				else {
					field.setId(hiddenColumn.replaceAll("\\.", "_"));
					field.setField(hiddenColumn);
				}
				field.setI18nName(hiddenColums[i]);
				field.setDataType(StringDataTypeConst.STRING);
				ds.getFieldSet().addField(field);
			}
		}
		
		ds.setLazyLoad(false);
		geneDatasetLoadEvent(ds);
		return ds;
	}

	/**
	 * ���Ͳ�������Dataset
	 * @param gmodel
	 * @return
	 */
	private Dataset generateGridDataset(GridRefModel gmodel) {
		Dataset ds = new Dataset();
		ds.setPageSize(20);
		ds.setId(ReferenceConstant.MASTER_DS_ID);
		// ��ʾ�ֶ��б�
		String[] showColumns = gmodel.getVisibleFieldCodes();
		// ����ʾ�ֶ��б�
		String[] hiddenColums = gmodel.getHiddenFieldCodes();
		
//			String pkField = gmodel.getRefPkField();
//			boolean hasPrimaryKey = false;
		if(showColumns != null)
		{
			String[] showNames = gmodel.getVisibleFieldNames();
			for(int i = 0; i < showColumns.length; i ++)
			{
//					if(showColumns[i].equals(pkField))
//						hasPrimaryKey = true;
//					if(showColumns[i].indexOf(" case ") != -1)
//						continue;
				
				String showColumn = showColumns[i];
				Field field = new Field();
				String text = null;
				if(i >= showNames.length){
					text = showColumn;
				}
				else
					text = showNames[i];
				field.setText(text);
				field.setDataType(StringDataTypeConst.STRING);
				
				
				int asIndex = showColumn.indexOf(" as ");
				int caseIndex = showColumn.indexOf("case ");
				if(asIndex != -1){
					String fid = showColumn.substring(asIndex + 4);
					field.setId(fid);
					field.setField(fid);
				}
				else if(caseIndex != -1) {
					int whenIndex = showColumn.indexOf(" when");
					String fid = showColumn.substring(caseIndex + 4, whenIndex).trim();
					field.setId(fid);
					field.setField(fid);
				}
				else if(showColumn.indexOf(".") != -1){
					String showCol = showColumn.split("\\.")[1].replaceAll("\\.", "_");
					//todo����ΪId�����ظ�����ʱ���˴���
					if(ds.getFieldSet().getField(showCol) == null){
						field.setId(showColumn.split("\\.")[1].replaceAll("\\.", "_"));
						field.setField(showColumn.split("\\.")[1]);
					}
					else {
						field.setId(showColumn.replaceAll("\\.", "_"));
						field.setField(showColumn.replaceAll("\\.", "_"));
					}
				}
				else {
					field.setId(showColumn.replaceAll("\\.", "_"));
					field.setField(showColumn.replaceAll("\\.", "_"));
				}
				ds.getFieldSet().addField(field);
			}
		}
		if(hiddenColums != null)
		{
			for(int i = 0; i < hiddenColums.length; i ++)
			{
				Field field = new Field();
				field.setText(gmodel.getHiddenFieldNames()[i]);
				field.setDataType(StringDataTypeConst.STRING);
				
				int asIndex = hiddenColums[i].indexOf(" as ");
				if(asIndex != -1){
					String fid = hiddenColums[i].substring(asIndex + 4);
					field.setField(fid);
					field.setId(fid);
				}
				else if(hiddenColums[i].indexOf(".") != -1){
					field.setId(hiddenColums[i].split("\\.")[1].replaceAll("\\.", "_"));
					field.setField(hiddenColums[i].split("\\.")[1]);
				}
				else {
					field.setField(hiddenColums[i]);
					field.setId(hiddenColums[i].replaceAll("\\.", "_"));
				}
				
				ds.getFieldSet().addField(field);
			}
		}
		
		ds.setLazyLoad(false);
		//ds�¼�������
		geneDatasetLoadEvent(ds);
		return ds;
	}


	/**
	 * ���ɲ��տؼ�����
	 * @return
	 */
	public WebComponent[] getComponent()
	{
		int refType = LfwRefUtil.getRefType(model);
		if(refType == ILfwRefModel.REFTYPE_GRID) {
			if(refnode instanceof NCRefNode){
				 NCRefNode ncRefNode = (NCRefNode) refnode;
				 if(ncRefNode.isOrgs()){
					 return getnerateGridText(ReferenceConstant.MASTER_DS_ID);
				 }
			 }
			return new GridComp[]{generateGrid(ReferenceConstant.MASTER_DS_ID)};
		}
		else if(refType == ILfwRefModel.REFTYPE_TREE) {
			//������
			TreeViewComp treeView =  (TreeViewComp) generateTree(ReferenceConstant.MASTER_DS_ID)[0];
			if(refnode instanceof NCRefNode){
				 NCRefNode ncRefNode = (NCRefNode) refnode;
				 if(ncRefNode.isOrgs()){
					 ReferenceComp refComp = genOrgRefComp();
					 return new WebComponent[]{treeView, refComp};
				 }
			 }
			return new WebComponent[]{treeView};
		}
		else if(refType == ILfwRefModel.REFTYPE_TREEGRID) {
			if(refnode instanceof NCRefNode){
				 NCRefNode ncRefNode = (NCRefNode) refnode;
				 if(ncRefNode.isOrgs()){
					 return getnerateTreeGridText(ReferenceConstant.MASTER_DS_ID);
				 }
			 }
			 WebComponent[] webComponents = generateGridTree(ReferenceConstant.MASTER_DS_ID);
			 return webComponents;
		}
		else if(refType == ILfwRefModel.REFTYPE_TWOTREE){
			return getnerateTwoLevelTree(ReferenceConstant.MASTER_DS_ID);
		}
		else if(refType == ILfwRefModel.REFTYPE_TWOTREEGRID){
			return generateTwoTreeGridTree(ReferenceConstant.MASTER_DS_ID);
		}
		return null;
	}
	
	public RefNode[] getRefNodes(){
		if(refnode instanceof NCRefNode){
			NCRefNode ncRefNode = (NCRefNode) refnode;
			if(ncRefNode.isOrgs()){
				RefNode refNode=this.genOrgRefRefNode();
				return  new RefNode[]{refNode};
			}
		}
		return null;
	}
	
	/**
	 * ���ɴ���֯����ѡ���������Ͳ���
	 * @param dsId
	 * @return
	 */
	public WebComponent[] getnerateTreeGridText(String dsId){
		TreeViewComp tree = generateTgTree(dsId + "_tree");
		GridComp grid = generateGrid(dsId);
		ReferenceComp refComp=genOrgRefComp();
		return new WebComponent[]{tree, grid, refComp};
	}
	
	
	/*
	 * ���ɴ���֯�Ĳ��ձ�����
	 */
	public WebComponent[] getnerateGridText(String dsId){
		GridComp grid = generateGrid(dsId);
		ReferenceComp refComp=genOrgRefComp();
		return new WebComponent[]{grid, refComp};
	}
	
	/**
	 * ������֯�����ı���
	 * @return
	 */
	protected ReferenceComp genOrgRefComp(){
		ReferenceComp refComp=new ReferenceComp();
		refComp.setText("ҵ��Ԫ");
		//refComp.setWidth("250");
		refComp.setRefcode("refnode_org");
		refComp.setEditorType("Reference");
		refComp.setEnabled(true);
		refComp.setId("refcomp_org");
		
		//���textListener��ֵ�ı�ʱִ�д�Listener
		geneTextEvent(refComp);
		
		return refComp;
	}

	private void geneTextEvent(ReferenceComp refComp) {
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		submitRule.addWidgetRule(wr);
		
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(ReferenceConstant.MASTER_DS_ID +  "_tree");
		dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		wr.addDsRule(dsRule);
		EventConf textEvent = new EventConf();
		textEvent.setJsEventClaszz("nc.uap.lfw.core.event.conf.TextListener");
		textEvent.setOnserver(true);
		textEvent.setSubmitRule(submitRule);
		textEvent.setName("valueChanged");
		textEvent.setMethodName("orgValueChanged");
		if(refnode.getDataListener() != null && !"".equals(refnode.getDataListener()))
			textEvent.setControllerClazz(refnode.getDataListener());
		refComp.addEventConf(textEvent);
	}
	
	
	protected NCRefNode genOrgRefRefNode(){
		NCRefNode refNode = new NCRefNode();
		refNode.setAllowInput(false);
		refNode.setRefcode("ҵ��Ԫ+����");
		refNode.setId("refnode_org");
		refNode.setDialog(true);
		refNode.setRefresh(true);
		refNode.setPagemeta("reference");
		refNode.setPath("reference/reftree.jsp");
		refNode.setFrom(Dataset.FROM_NC);
		refNode.setPagemeta("reference");
		refNode.setReadDs("masterDs");
		
		refNode.setReadFields("pk_org,name");
		//����֯���ռ�����
		//refNode.setDataListener("nc.portal.org.group.refrence.GroupRefDatasetServerListener");
		return refNode;
	}
	
	/**
	 * ΪGRIDTREE����dsRelation
	 * @param treeDs
	 * @param gridDs
	 * @return
	 */
	public DatasetRelation getDsRelation(Dataset treeDs, Dataset gridDs)
	{
		//AbstractRefModel refModel = getRefModel(refNodeName);
		AbstractRefGridTreeModel treeGridAbsModel = (AbstractRefGridTreeModel)model;
		DatasetRelation rel = new DatasetRelation();
		rel.setMasterDataset(treeDs.getId());
		rel.setDetailDataset(gridDs.getId());
		rel.setMasterKeyField(treeGridAbsModel.getClassJoinField().replaceAll("\\.", "_"));
		rel.setDetailForeignKey(treeGridAbsModel.getDocJoinField().replaceAll("\\.", "_"));
		rel.setId(treeDs.getId());
		return rel;
	}
	
	/**
	 * �õ����յ�3��Ҫ��,pk,code,name
	 * @return
	 */
	public String[] getRefElements()
	{
		String refPk = model.getRefPkField();
		if(refPk != null){
			if(refPk.indexOf(".") != -1)
				refPk = refPk.split("\\.")[1];
		}
		String code = model.getRefCodeField();
		if(code != null){
			//code = code.replaceAll("\\.", "_");
			if(code.indexOf(".") != -1)
				code = code.split("\\.")[1];
		}
		
		String name = model.getRefNameField();
		if(name != null){
			//name = name.replaceAll("\\.", "_");
			if(name.indexOf(".") != -1)
				name = name.split("\\.")[1];
		}
		//AbstractRefModel refModel = getRefModel(refNodeName);
		return new String[]{refPk, code, name};
	}
	
	public String[] getRefElementsNoTrans() {
		return new String[]{model.getRefPkField(), model.getRefCodeField(), model.getRefNameField()};
	}
	
	/**
	 * ����model����grid�ؼ�����
	 * @param refModel
	 * @return
	 */
	private GridComp generateGrid(String dsId)
	{
//		boolean isGridTree = (LfwRefUtil.getRefType(model) == ILfwRefModel.REFTYPE_TREEGRID);
		// ��ʾ�ֶ��б�
		String[] showColumns = model.getVisibleFieldCodes();
		//�����ֶβ���ʾ
		String[] hiddenColumns = model.getHiddenFieldCodes();
		List<String> hiddenList = null;
		if (hiddenColumns != null)
			hiddenList = Arrays.asList(hiddenColumns);
		String[] nameColumns = ((GridRefModel)model).getVisibleFieldNames();
		// ��ʾ�ֶ�������
		//TODO:Ŀǰ��ʾ��ȫ������,��Ҫ����displayCount������Ӧ����
		ArrayList<IGridColumn> columns = new ArrayList<IGridColumn>();
		ArrayList<String> columnIds = new ArrayList<String>();
		for(int i = 0; i < showColumns.length && i < nameColumns.length; i++)
		{
			if(showColumns[i].indexOf(" case ") != -1)
				continue;
			//���˵������ֶ�
			if(hiddenList != null && hiddenList.contains(showColumns[i]))
				continue;
			GridColumn column = new GridColumn();
			
			if(showColumns[i].startsWith("substring")){
				showColumns[i] = showColumns[i].substring("substring".length() + 1,  showColumns[i].indexOf(","));
			}
			
			if(showColumns[i].indexOf(" as ") != -1){
				column.setField(showColumns[i].substring(showColumns[i].indexOf(" as ")+ 4).trim());
				//column.setId(showColumns[i].replaceAll("\\.", "_"));
				column.setId(showColumns[i].substring(showColumns[i].indexOf(" as ") + 4).trim());
			}
			else if(showColumns[i].indexOf(".") != -1){
				String col = showColumns[i].split("\\.")[1].replaceAll("\\.", "_");
				//todo,��Ϊ��ʾID�����ظ�����ʱ���˴���
				if(columnIds.contains(col)){
					column.setField(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
					//column.setId(showColumns[i].replaceAll("\\.", "_"));
					column.setId(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
				}
				else{
					column.setId(showColumns[i].split("\\.")[1].replaceAll("\\.", "_"));
					column.setField(showColumns[i].split("\\.")[1]);
				}
			}

			else{
				column.setField(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
				//column.setId(showColumns[i].replaceAll("\\.", "_"));
				column.setId(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
				//column.setI18nName(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
			}
			column.setWidth(120);
			column.setDataType(StringDataTypeConst.STRING);
			column.setVisible(true);
			column.setEditable(false);
			if(i == showColumns.length - 1)
				column.setAutoExpand(true);
			columns.add(column);
			columnIds.add(column.getId());
		}
		
		GridComp grid = null;
		grid = new GridComp();
		grid.setDataset(dsId);
		grid.setColumnList(columns);
		grid.setEditable(false);
		if(refnode.isMultiSel())
			grid.setMultiSelect(true);
		else
			grid.setMultiSelect(false);
		//grid.setWidth("100%");
		//grid.setHeight("100%");
		// ����grid id
		grid.setId("refgrid");
		
		
		// ����˫���¼�֧��
		geneGridDbRowEvent(grid);
		
		return grid;
	}

	private void geneGridDbRowEvent(GridComp grid) {
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		submitRule.addWidgetRule(wr);
		
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(ReferenceConstant.MASTER_DS_ID);
		dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		wr.addDsRule(dsRule);
		
		// ��ҳ��Widget�ύ����
		EventSubmitRule parentSubmitRule = generateParentSubmitRule();
		submitRule.setParentSubmitRule(parentSubmitRule);
		
		EventConf rowSelEvent = new EventConf();
		rowSelEvent.setJsEventClaszz("nc.uap.lfw.core.event.conf.GridRowListener");
		rowSelEvent.setOnserver(true);
		rowSelEvent.setSubmitRule(submitRule);
		rowSelEvent.setName("onRowDbClick");
		rowSelEvent.setMethodName("onRowDbClick");
		if(refnode.getDataListener() != null && !"".equals(refnode.getDataListener()))
			rowSelEvent.setControllerClazz(refnode.getDataListener());
		grid.addEventConf(rowSelEvent);
	}
	
	
	
	
	
	private TreeViewComp generateTgTree(String dsId){
		TreeGridRefModel treeModel = (TreeGridRefModel)model;
		TreeViewComp tree = new TreeViewComp();
		tree.setId("reftree");
		//tree.setHeight("100%");
		tree.setWithRoot(true);
		tree.setRootOpen(true);
		tree.setText(treeModel.getClassRootName());
		int refType = LfwRefUtil.getRefType(model);
		//������
		if(treeModel.getClassCodingRule() != null)
		{}
		//����
		else if(treeModel.getClassChildField() == null || treeModel.getClassFatherField() == null){
			SimpleTreeLevel level = new SimpleTreeLevel();
			level.setId("level1");
			level.setDataset(dsId);
			if(refType == ILfwRefModel.REFTYPE_TREE){
				level.setMasterKeyField(treeModel.getRefPkField());
				level.setLabelFields(treeModel.getRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getRefNameField().replaceAll("\\.", "_"));
			}
			else if(refType == ILfwRefModel.REFTYPE_TREEGRID){
				if(treeModel.getClassRefPkField().indexOf(".") != -1)
					level.setMasterKeyField(treeModel.getClassRefPkField().split("\\.")[1].replaceAll("\\.", "_"));
				else
					level.setMasterKeyField(treeModel.getClassRefPkField().replaceAll("\\.", "_"));
				
				if(treeModel.getClassRefCodeField().indexOf(".") != -1 && treeModel.getClassRefNameField().indexOf(".") != -1){
					level.setLabelFields(treeModel.getClassRefCodeField().split("\\.")[1].replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().split("\\.")[1].replaceAll("\\.", "_"));
				}
				else
					level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			}
			tree.setTopLevel(level);
			LfwLogger.debug("LfwRefGenUtil���generateTgTree()������MasterKeyField��" + level.getMasterKeyField() + ",LabelFileds��" +
					level.getLabelFields() + ",����������" +  refType);

		}
		//�ݹ���
		else{
			RecursiveTreeLevel level = new RecursiveTreeLevel();
			level.setId("level1");
			level.setDataset(dsId);
			if(refType == ILfwRefModel.REFTYPE_TREE){
				level.setMasterKeyField(treeModel.getRefPkField());
				String code = treeModel.getRefCodeField();
				String name = treeModel.getRefNameField();
				String labelFields = "";
				if(code!=null&&!"".equals(code))
					labelFields = code.replaceAll("\\.", "_") + "," + name.replaceAll("\\.", "_");
				else labelFields = name.replaceAll("\\.", "_");				
					level.setLabelFields(labelFields);
				level.setLabelFields(labelFields);
			}
			else if(refType == ILfwRefModel.REFTYPE_TREEGRID){
				level.setMasterKeyField(treeModel.getClassRefPkField());
				//level.setMasterKeyField(treeModel.getClassChildField());
				String code = treeModel.getClassRefCodeField();
				String name = treeModel.getClassRefNameField();
				String labelFields = "";
				if(code!=null&&!"".equals(code))
					labelFields = code.replaceAll("\\.", "_") + "," + name.replaceAll("\\.", "_");
				else labelFields = name.replaceAll("\\.", "_");				
					level.setLabelFields(labelFields);
			}
			//level.setRecursiveKeyField(treeModel.getClassJoinField());
			level.setRecursiveKeyField(treeModel.getClassChildField());
			level.setRecursivePKeyField(treeModel.getClassFatherField());
			tree.setTopLevel(level);
			LfwLogger.debug("LfwRefGenUtil���generateTgTree()������MasterKeyField��" + level.getMasterKeyField() + ",LabelFileds��" +
					level.getLabelFields() + ", RecursiveKeyFiled��" + level.getRecursiveKeyField() + ", RecursivePkField��" 
					+ level.getRecursivePKeyField() + ",����������" +  refType);
		}
		
		return tree;
	}
	
	/**
	 * ����gridTree��ߵ�2����
	 * @param dsId
	 * @return
	 */
	private TreeViewComp generateTwoTgTree(String dsId){
		TreeGridRefModel treeModel = (TreeGridRefModel)model;
		TreeViewComp tree = new TreeViewComp();
		tree.setId("reftree");
		//tree.setHeight("100%");
		tree.setWithRoot(true);
		tree.setRootOpen(true);
		tree.setText(treeModel.getClassRootName());
		int refType = LfwRefUtil.getRefType(model);
		TwoTreeGridRefModel treeTreeGridModel = (TwoTreeGridRefModel) model;
		if(refType == ILfwRefModel.REFTYPE_TWOTREEGRID){
			if(treeTreeGridModel.getFirstLevelRefMode() != null){
				TreeRefModel firstRefMode = treeTreeGridModel.getFirstLevelRefMode();
				//������
				if(firstRefMode.getCodingRule() != null)
				{
				}

				//����
				else if(firstRefMode.getChildField() == null || firstRefMode.getFatherField() == null){
					SimpleTreeLevel level = new SimpleTreeLevel();
					level.setId("level1");
					level.setDataset(ReferenceConstant.MASTER_DS_ID + "_tree_1");
					level.setMasterKeyField(firstRefMode.getRefPkField());
					level.setLabelFields(firstRefMode.getRefCodeField().replaceAll("\\.", "_") + "," + firstRefMode.getRefNameField().replaceAll("\\.", "_"));
					tree.setTopLevel(level);
				}
				//�ݹ���
				else{
					RecursiveTreeLevel level = new RecursiveTreeLevel();
					level.setId("level1");
					level.setDataset(ReferenceConstant.MASTER_DS_ID + "_tree_1");
					level.setRecursiveKeyField(firstRefMode.getChildField().replaceAll("\\.", "_"));
					level.setRecursivePKeyField(firstRefMode.getFatherField().replaceAll("\\.", "_"));
					level.setMasterKeyField(firstRefMode.getChildField());
					level.setLabelFields(firstRefMode.getRefCodeField().replaceAll("\\.", "_") + "," + firstRefMode.getRefNameField().replaceAll("\\.", "_"));
					tree.setTopLevel(level);
				}
			}
		}
		//������
		if(treeModel.getClassCodingRule() != null)
		{}
		//����
		else if(treeModel.getClassChildField() == null || treeModel.getClassFatherField() == null){
			SimpleTreeLevel level = new SimpleTreeLevel();
			level.setId("level2");
			level.setDataset(ReferenceConstant.MASTER_DS_ID + "_tree");
			if(refType == ILfwRefModel.REFTYPE_TREE){
				level.setMasterKeyField(treeModel.getRefPkField());
				level.setLabelFields(treeModel.getRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getRefNameField().replaceAll("\\.", "_"));
			}
			else if(refType == ILfwRefModel.REFTYPE_TREEGRID){
				level.setMasterKeyField(treeModel.getClassRefPkField());
				if(treeModel.getClassRefCodeField() != null && "".equals(treeModel.getClassRefCodeField()))
					level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
				else
					level.setLabelFields(treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			}
			else if(refType == ILfwRefModel.REFTYPE_TWOTREEGRID){
				level.setMasterKeyField(treeModel.getClassRefPkField());
				if(treeModel.getClassRefCodeField() != null && "".equals(treeModel.getClassRefCodeField()))
					level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
				else
					level.setLabelFields(treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			}
			level.setDetailKeyParameter(treeTreeGridModel.getDetailParameter());
			tree.getTopLevel().setChildTreeLevel(level);
			LfwLogger.debug("LfwRefGenUtil���generateTgTree()������MasterKeyField��" + level.getMasterKeyField() + ",LabelFileds��" +
					level.getLabelFields() + ",����������" +  refType);

		}
		//�ݹ���
		else{
			RecursiveTreeLevel level = new RecursiveTreeLevel();
			level.setId("level2");
			level.setDataset(ReferenceConstant.MASTER_DS_ID + "_tree");
			level.setMasterKeyField(treeModel.getClassRefPkField());
			if(treeModel.getClassRefCodeField() != null && "".equals(treeModel.getClassRefCodeField()))
				level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			else
				level.setLabelFields(treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			level.setRecursiveKeyField(treeModel.getClassJoinField());
			level.setRecursivePKeyField(treeModel.getClassFatherField());
			level.setDetailKeyParameter(treeTreeGridModel.getDetailParameter());
			tree.getTopLevel().setChildTreeLevel(level);
			LfwLogger.debug("LfwRefGenUtil���generateTgTree()������MasterKeyField��" + level.getMasterKeyField() + ",LabelFileds��" +
					level.getLabelFields() + ", RecursiveKeyFiled��" + level.getRecursiveKeyField() + ", RecursivePkField��" 
					+ level.getRecursivePKeyField() + ",����������" +  refType);
		}
		
		return tree;
	}
	
	/**
	 * ����2��������grid
	 * @param dsId
	 * @return
	 */
	private WebComponent[] generateTwoTreeGridTree(String dsId){
		WebComponent tree = generateTwoTgTree(dsId);
		GridComp grid = generateGrid(dsId);
		return new WebComponent[]{tree, grid};
	}
	
	
	
	
	/**
	 * ����������
	 * @param dsId
	 * @return
	 */
	private WebComponent[] getnerateTwoLevelTree(String dsId){
		boolean isGridTree = (LfwRefUtil.getRefType(model) == ILfwRefModel.REFTYPE_TREEGRID);
		TwoTreeRefModel treeModel = (TwoTreeRefModel)model;
		TreeViewComp tree = new TreeViewComp();
		tree.setId("reftree");
		//tree.setHeight("100%");
		tree.setWithRoot(true);
		tree.setRootOpen(true);
		tree.setText(treeModel.getRootName());
		if(refnode.isMultiSel())
			tree.setWithCheckBox(true);
		if(treeModel.getChildField() == null || treeModel.getFatherField() == null){
			SimpleTreeLevel level = new SimpleTreeLevel();
			level.setId("level1");
			level.setDataset(ReferenceConstant.MASTER_DS_ID);
			level.setMasterKeyField(treeModel.getRefPkField());
			if(isGridTree)
			{
				//level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			}
			else
				level.setLabelFields(treeModel.getRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getRefNameField().replaceAll("\\.", "_"));
			tree.setTopLevel(level);
		}
		//�ݹ���
		else{
			RecursiveTreeLevel level = new RecursiveTreeLevel();
			level.setId("level1");
			level.setDataset(ReferenceConstant.MASTER_DS_ID);
			level.setRecursiveKeyField(treeModel.getChildField().replaceAll("\\.", "_"));
			level.setRecursivePKeyField(treeModel.getFatherField().replaceAll("\\.", "_"));
			level.setMasterKeyField(treeModel.getChildField());
			if(isGridTree)
			{
				//level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			}
			else
				level.setLabelFields(treeModel.getRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getRefNameField().replaceAll("\\.", "_"));
			tree.setTopLevel(level);
			
			//��˫��listener
			geneTreeDbEvent(tree);
			
		}
		TreeViewComp mainTree = new TreeViewComp();
		if(treeModel.getFirstLevelRefMode() != null){
			TreeRefModel firstRefMode = treeModel.getFirstLevelRefMode();
			//����
			if(firstRefMode.getChildField() == null || firstRefMode.getFatherField() == null){
				mainTree = new TreeViewComp();
				mainTree.setId("maintree");
				//mainTree.setHeight("100%");
				mainTree.setWithRoot(true);
				mainTree.setRootOpen(true);
				mainTree.setText(firstRefMode.getRootName());
				SimpleTreeLevel level = new SimpleTreeLevel();
				level.setId("level1");
				level.setDataset(ReferenceConstant.MASTER_DS_ID + "_tree_1");
				level.setMasterKeyField(treeModel.getRefPkField());
				if(isGridTree)
				{
					//level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
				}
				else
					level.setLabelFields(firstRefMode.getRefCodeField().replaceAll("\\.", "_") + "," + firstRefMode.getRefNameField().replaceAll("\\.", "_"));
				mainTree.setTopLevel(level);
			}
			//�ݹ���
			else{
				mainTree = new TreeViewComp();
				mainTree.setId("maintree");
				//mainTree.setHeight("100%");
				mainTree.setWithRoot(false);
				mainTree.setRootOpen(true);
				mainTree.setText(firstRefMode.getRootName());
				RecursiveTreeLevel level = new RecursiveTreeLevel();
				level.setId("level1");
				level.setDataset(ReferenceConstant.MASTER_DS_ID + "_tree_1");
				level.setRecursiveKeyField(firstRefMode.getChildField().replaceAll("\\.", "_"));
				level.setRecursivePKeyField(firstRefMode.getFatherField().replaceAll("\\.", "_"));
				level.setMasterKeyField(firstRefMode.getChildField());
				if(isGridTree)
				{
					//level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
				}
				else
					level.setLabelFields(firstRefMode.getRefCodeField().replaceAll("\\.", "_") + "," + firstRefMode.getRefNameField().replaceAll("\\.", "_"));
				mainTree.setTopLevel(level);
			}
		}
		return new WebComponent[]{tree, mainTree};
	}

	private void geneTreeDbEvent(TreeViewComp tree) {
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		submitRule.addWidgetRule(wr);
		
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(ReferenceConstant.MASTER_DS_ID);
		dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		submitRule.getWidgetRules().get(WIDGET_ID).addDsRule(dsRule);
			
		// ��ҳ��Widget�ύ����
		EventSubmitRule parentSubmitRule = generateParentSubmitRule();
		submitRule.setParentSubmitRule(parentSubmitRule);

		EventConf rowSelEvent = new EventConf();
		rowSelEvent.setJsEventClaszz("nc.uap.lfw.core.event.conf.TreeNodeListener");
		rowSelEvent.setOnserver(true);
		rowSelEvent.setSubmitRule(submitRule);
		rowSelEvent.setName("ondbclick");
		rowSelEvent.setMethodName("onTreeNodedbclick");
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		rowSelEvent.addParam(param);
		
		
		if(refnode.getDataListener() != null && !"".equals(refnode.getDataListener()))
			rowSelEvent.setControllerClazz(refnode.getDataListener());	
		tree.addEventConf(rowSelEvent);
	}
	
	/**
	 * ����model����tree�ؼ�
	 * @param dsId
	 * @param refModel
	 * @return
	 */
	private WebComponent[] generateTree(String dsId)
	{
		boolean isGridTree = (LfwRefUtil.getRefType(model) == ILfwRefModel.REFTYPE_TREEGRID);
		TreeRefModel treeModel = (TreeRefModel)model;
		TreeViewComp tree = new TreeViewComp();
		tree.setId("reftree");
		//tree.setHeight("100%");
		tree.setWithRoot(true);
		tree.setRootOpen(true);
		tree.setText(treeModel.getRootName());
		if(refnode.isMultiSel())
			tree.setWithCheckBox(true);

		//������
		if(treeModel.getCodingRule() != null)
		{}

		//����
		else if(treeModel.getChildField() == null || treeModel.getFatherField() == null){
			SimpleTreeLevel level = new SimpleTreeLevel();
			level.setId("level1");
			level.setDataset(dsId);
			if(treeModel.getRefPkField().indexOf(".") != -1){
				level.setMasterKeyField(treeModel.getRefPkField().split("\\.")[1].replaceAll("\\.", "_"));
			}
			else
				level.setMasterKeyField(treeModel.getRefPkField().replaceAll("\\.", "_"));
			if(isGridTree)
			{
				//level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			}
			else{
				String code = treeModel.getRefCodeField().indexOf(".") != -1 ? treeModel.getRefCodeField().split("\\.")[1].replaceAll("\\.", "_"): treeModel.getRefCodeField().replaceAll("\\.", "_");
				String name = treeModel.getRefNameField().indexOf(".") != -1 ? treeModel.getRefNameField().split("\\.")[1].replaceAll("\\.", "_"): treeModel.getRefNameField().replaceAll("\\.", "_");
				level.setLabelFields(code + ","+ name);
			}
			tree.setTopLevel(level);
		}
		//�ݹ���
		else{
			RecursiveTreeLevel level = new RecursiveTreeLevel();
			level.setId("level1");
			level.setDataset(dsId);
			if(treeModel.getChildField().indexOf(".") != -1){
				level.setRecursiveKeyField(treeModel.getChildField().split("\\.")[1].replaceAll("\\.", "_"));
			}
			else
				level.setRecursiveKeyField(treeModel.getChildField().replaceAll("\\.", "_"));
			if(treeModel.getFatherField().indexOf(".") != -1){
				level.setRecursivePKeyField(treeModel.getFatherField().split("\\.")[1].replaceAll("\\.", "_"));
			}
			else
				level.setRecursivePKeyField(treeModel.getFatherField().replaceAll("\\.", "_"));
			level.setMasterKeyField(level.getRecursiveKeyField());
			if(isGridTree)
			{
				//level.setLabelFields(treeModel.getClassRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getClassRefNameField().replaceAll("\\.", "_"));
			}
			else{
				if(treeModel.getRefCodeField().indexOf(".") != -1){
					level.setLabelFields(treeModel.getRefCodeField().split("\\.")[1].replaceAll("\\.", "_") + "," + treeModel.getRefNameField().split("\\.")[1].replaceAll("\\.", "_"));
				}
				else
					level.setLabelFields(treeModel.getRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getRefNameField().replaceAll("\\.", "_"));
			}
			tree.setTopLevel(level);
		}
		
		// ��������Ͳ���,���Ҵ������������л�,����grid�ؼ�
		//TODO ���ڵ��˫���¼�֧��
		geneTreeDbEvent(tree);
		return new WebComponent[]{tree};
		//return null;
	}
	
	/**
	 * ���ɸ�ҳ���ύ����
	 * @return
	 */
	private EventSubmitRule generateParentSubmitRule() {
		EventSubmitRule submitRule = new EventSubmitRule();
		String pwidgetId = refnode.getWidget().getId();
		WidgetRule wr = new WidgetRule();
		wr.setId(pwidgetId);
		submitRule.addWidgetRule(wr);
		
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getParentPageMeta().getWidget(pwidgetId);
		Dataset[] dss = widget.getViewModels().getDatasets();
		for (int i = 0; i < dss.length; i++) {
			DatasetRule parentDsRule = new DatasetRule();
			parentDsRule.setId(dss[i].getId());
			parentDsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
			wr.addDsRule(parentDsRule);
		}
		return submitRule;
	}
	 
	/**
	 * ����model����tree,grid�ؼ�
	 * @param dsId
	 * @param refModel
	 * @return
	 */
	private WebComponent[] generateGridTree(String dsId)
	{
		TreeViewComp tree = generateTgTree(dsId + "_tree");
		GridComp grid = generateGrid(dsId);
		return new WebComponent[]{tree, grid};
	}
	
	/**
	 * ��ȡrefModel
	 * @param refNodeName
	 * @return
	 */
//	public AbstractRefModel getRefModel(String refNodeName)
//	{
//		if(refModel == null)
//			refModel = LfwRegridDstil.getRefModel(refNodeName);
//		return refModel;
//	}
	
//	/**
//	 * �������Ͳ��յ�ds
//	 * @param ds
//	 * @return
//	 */
//	private Dataset processDsForRefTree(Dataset ds)
//	{
//		TreeRefModel treeModel = (TreeRefModel) model;
//		// id 
//		Field idField = new Field();
//		
//		if(treeModel.getChildField() != null){
//			idField.setId(treeModel.getChildField().replaceAll("\\.", "_"));
//			idField.setField(treeModel.getChildField());
//			idField.setDataType(StringDataTypeConst.STRING);
//			ds.getFieldSet().addField(idField);
//		}
//		if(treeModel.getFatherField() != null){
//			// pId
//			Field pIdField = new Field();
//			pIdField.setId(treeModel.getFatherField().replaceAll("\\.", "_"));
//			pIdField.setDataType(StringDataTypeConst.STRING);
//			pIdField.setField(treeModel.getFatherField());
//			ds.getFieldSet().addField(pIdField);
//		}
//		
//		// showName(��ʱ��treeAbsModel.getChildField())
//		Field showNameField = new Field();
//		showNameField.setId("showName");
//		showNameField.setDataType(StringDataTypeConst.STRING);
//		showNameField.setField(treeModel.getRefNameField());
//		ds.getFieldSet().addField(showNameField);
//		
//		// pk
//		Field pkField = new Field();
//		pkField.setId(treeModel.getRefPkField().replaceAll("\\.", "_"));
//		pkField.setField(treeModel.getRefPkField());
//		pkField.setDataType(StringDataTypeConst.STRING);
//		ds.getFieldSet().addField(pkField);
//		
//		// name
//		Field nameField = new Field();
//		nameField.setId(treeModel.getRefNameField().replaceAll("\\.", "_"));
//		nameField.setField(treeModel.getRefNameField());
//		nameField.setDataType(StringDataTypeConst.STRING);
//		ds.getFieldSet().addField(nameField);
//		
//		// code
//		Field codeField = new Field();
//		codeField.setId(treeModel.getRefCodeField().replaceAll("\\.", "_"));
//		codeField.setDataType(StringDataTypeConst.STRING);
//		codeField.setField(treeModel.getRefCodeField());
//		ds.getFieldSet().addField(codeField);
//		
//		//����������ֶ�
////		if(treeModel.getDocJoinField() != null){
////			Field classField = new Field();
////			classField.setId(treeModel.getDocJoinField().replaceAll("\\.", "_"));
////			classField.setDataType(StringDataTypeConst.STRING);
////			classField.setField(treeModel.getDocJoinField());
////			ds.getFieldSet().addField(classField);
////		}
//		
//		return ds;
//	}

	public DatasetRelation[] getRelation() {
		// ��ȡ��������
		int refType = LfwRefUtil.getRefType(model);
		// "���Ͳ���"
		List<DatasetRelation> relation = new ArrayList<DatasetRelation>();
		if(refType == ILfwRefModel.REFTYPE_TREEGRID) {
			TreeGridRefModel treeGridAbsModel = (TreeGridRefModel) model;
			DatasetRelation rel = new DatasetRelation();
			rel.setId("master_slave_rel");
			rel.setDetailDataset(ReferenceConstant.MASTER_DS_ID);
			rel.setMasterDataset(ReferenceConstant.MASTER_DS_ID + "_tree");
			if(treeGridAbsModel.getClassJoinField().indexOf(".") != -1)
				rel.setMasterKeyField(treeGridAbsModel.getClassJoinField().split("\\.")[1]);
			else
				rel.setMasterKeyField(treeGridAbsModel.getClassJoinField());
			if(treeGridAbsModel.getDocJoinField().indexOf(".") != -1)
				rel.setDetailForeignKey(treeGridAbsModel.getDocJoinField().split("\\.")[1].replaceAll("\\.", "_"));
			else
				rel.setDetailForeignKey(treeGridAbsModel.getDocJoinField().replaceAll("\\.", "_"));
			//rel.setListenerClazz(getRefDatasetListener());
			relation.add(rel);
			//return rel;
		}
		else if(refType == ILfwRefModel.REFTYPE_TWOTREE) {
			TwoTreeRefModel twoTreeAbsModel = (TwoTreeRefModel) model;
			DatasetRelation rel = new DatasetRelation();
			rel.setId("master_slave_rel");
			rel.setDetailDataset(ReferenceConstant.MASTER_DS_ID);
//			rel.setDetailDataset(ReferenceConstant.MASTER_DS_ID + "_tree");
			rel.setMasterDataset(ReferenceConstant.MASTER_DS_ID + "_tree_1");
			rel.setMasterKeyField(twoTreeAbsModel.getClassJoinField());
			rel.setDetailForeignKey(twoTreeAbsModel.getDocJoinField().replaceAll("\\.", "_"));
			//rel.setListenerClazz(getRefDatasetListener());
			//return rel;
			relation.add(rel);
		}
		else if(refType == ILfwRefModel.REFTYPE_TWOTREEGRID){
			TwoTreeGridRefModel twoTreeGridAbsModel = (TwoTreeGridRefModel) model;
			if(twoTreeGridAbsModel.getFirstClassJoinField() != null && twoTreeGridAbsModel.isFirstLevelRelation()){
				DatasetRelation rel1 = new DatasetRelation();
				rel1.setId("master_slave_rel1");
				rel1.setDetailDataset(ReferenceConstant.MASTER_DS_ID);
				rel1.setMasterDataset(ReferenceConstant.MASTER_DS_ID + "_tree_1");
				
				rel1.setMasterKeyField(twoTreeGridAbsModel.getFirstClassJoinField());
				rel1.setDetailForeignKey(twoTreeGridAbsModel.getFirstDocJoinField().replaceAll("\\.", "_"));
				//rel1.setListenerClazz(getRefDatasetListener());
				relation.add(rel1);
			}
			DatasetRelation rel2 = new DatasetRelation();
			rel2.setId("master_slave_rel2");
			rel2.setDetailDataset(ReferenceConstant.MASTER_DS_ID);
			rel2.setMasterDataset(ReferenceConstant.MASTER_DS_ID + "_tree");
			rel2.setMasterKeyField(twoTreeGridAbsModel.getClassJoinField());
			rel2.setDetailForeignKey(twoTreeGridAbsModel.getDocJoinField().replaceAll("\\.", "_"));
			//rel2.setListenerClazz(getRefDatasetListener());
			
			relation.add(rel2);
			
			//return rel1;
		}
		return 	(DatasetRelation[]) relation.toArray(new DatasetRelation[0]);
	}

}
