package nc.uap.lfw.pa;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.common.DatasetConstant;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.util.DefaultPageMetaBuilder;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IDatasetProvider;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.stylemgr.itf.IUwFuncnodeQry;
import nc.uap.lfw.stylemgr.vo.UwFuncNodeVO;
import nc.uap.lfw.util.LfwClassUtil;

public class PaMainController {
	public void entityDataLoad(DataLoadEvent e) {
		Dataset ds = e.getSource();
		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		String fromWhere = session.getOriginalParameter("from");
		String eclipse = session.getOriginalParameter("eclipse");
		if(eclipse != null && eclipse.equals("1")){
			String viewId = session.getOriginalParameter("viewId");
			if(viewId != null){
				//根据参数设置VO条件
				String key = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_KEYVALUE);
				if(key != null && !key.equals(Dataset.MASTER_KEY)){
					String[] keys = key.split(",");
					MdDataset mdDs = new MdDataset();
					mdDs.setObjMeta(keys[1]);
					IDatasetProvider dsProvider = (IDatasetProvider) LfwClassUtil.newInstance("nc.uap.lfw.design.impl.DatasetProviderImpl");
					mdDs = dsProvider.getMdDataset(mdDs);
					
					Field[] fss = mdDs.getFieldSet().getFields();
					if(fss != null){
						int uuidIndex = ds.nameToIndex("uuid");
						int idIndex = ds.nameToIndex("id");
						int nameIndex = ds.nameToIndex("name");
						int typeIndex = ds.nameToIndex("type");
						int pIdIndex = ds.nameToIndex("pid");
						int sourceIndex = ds.nameToIndex("source");
						int dsIndex = ds.nameToIndex("dsid");
						for(int j = 0; j < fss.length; j++){
							Field f = fss[j];
							String sourceField = f.getSourceField();
							//过滤被带出字段
							if(sourceField != null && !sourceField.equals(""))
								continue;
							Row row = ds.getEmptyRow();
							row.setValue(uuidIndex, keys[2] + "," + f.getExtSourceAttr() + "," + f.getId());
							row.setValue(pIdIndex, key);
							row.setValue(typeIndex, f.getDataType());
							row.setValue(idIndex, f.getId());
							row.setValue(nameIndex, f.getText());
							row.setValue(dsIndex, keys[0]);
							String extSource = f.getExtSource();
							if(extSource != null && extSource.equals(Field.SOURCE_MD))
								row.setValue(sourceIndex, "1");
							if(!(row == null || row.size() == 0))
								ds.addRow(row);
						}
					}
				}
				
				else{
					String pageId =	(String) session.getAttribute("_pageId");
					waitForInit(pageId, sessionId);
					IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
					PageMeta pagemeta = service.getOriPageMeta(pageId, sessionId);
					if(pagemeta == null)
						return;
					LfwWidget widget = pagemeta.getWidget(viewId);
					Dataset[] dss = widget.getViewModels().getDatasets();
					
					setCurrDsInfo(ds, dss);
				}
			}
		}
		else if(fromWhere != null && fromWhere.equals("1")){
//			String viewId = session.getOriginalParameter("viewId");
			String pageId = (String)session.getAttribute("_pageId");
			waitForInit(pageId, sessionId);
			IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
			PageMeta pagemeta = service.getOriPageMeta(pageId, sessionId);
			if(pagemeta == null)
				return;
			LfwWidget[] widgets = pagemeta.getWidgets();
			
			if(widgets == null || widgets.length == 0){
				return ;
			}
			
			for(int k = 0; k < widgets.length; k++){
				
				LfwWidget widget = widgets[k];
				Dataset[] dss = widget.getViewModels().getDatasets();
				
				setCurrDsInfo(ds, dss);
			
			}
		}
		else{
			
			String pk_funcnode = session.getOriginalParameter("pk_funcnode");
			
			if(pk_funcnode == null){
				return ;
			}
			PageMeta pm = this.getPageMetaByFuncnodePk(pk_funcnode);
			
			LfwWidget[] widgets = pm.getWidgets();
			
			if(widgets == null || widgets.length == 0){
				return ;
			}
			
			for(int k = 0; k < widgets.length; k++){
				
				LfwWidget widget = widgets[k];
				Dataset[] dss = widget.getViewModels().getDatasets();
				
				setCurrDsInfo(ds, dss);
			}
		}
	}

	private void waitForInit(String sessionId, String pageId) {
		int count = 0;
		PageMeta pagemeta = null;
		while(pagemeta == null && count < 5){
			IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
			pagemeta = service.getOriPageMeta(pageId, sessionId);
			if(pagemeta == null){
				count ++;
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					LfwLogger.error(e);
				}
			}
		}
	}

	private void setCurrDsInfo(Dataset ds, Dataset[] dss) {
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int nameIndex = ds.nameToIndex("name");
		int typeIndex = ds.nameToIndex("type");
		int pIdIndex = ds.nameToIndex("pid");
		int dragIndex = ds.nameToIndex("isdrag");
		int sourceIndex = ds.nameToIndex("source");
		int dsIndex = ds.nameToIndex("dsid");
		if(dss != null){
			for (int i = 0; i < dss.length; i++){
				Dataset currDs = dss[i];
				if(currDs instanceof IRefDataset)
					continue;
				Row row = ds.getEmptyRow();
				String pid = UUID.randomUUID().toString();
				row.setValue(uuidIndex, pid);
				row.setValue(idIndex, currDs.getId());
				row.setValue(dragIndex, "0");
				if(!(row == null || row.size() == 0))
					ds.addRow(row);
				
				Field[] fss = currDs.getFieldSet().getFields();
				if(fss != null){
					for(int j = 0; j < fss.length; j++){
						Field f = fss[j];
						String sourceField = f.getSourceField();
						//过滤被带出字段
						if(sourceField != null && !sourceField.equals(""))
							continue;
						row = ds.getEmptyRow();
						row.setValue(uuidIndex, currDs.getId() + "," + f.getExtSourceAttr() + "," + f.getId());
						row.setValue(pIdIndex, pid);
						row.setValue(typeIndex, f.getDataType());
						row.setValue(idIndex, f.getId());
						row.setValue(nameIndex, f.getText());
						row.setValue(dsIndex, currDs.getId());
						
						String extSource = f.getExtSource();
						if(extSource != null && extSource.equals(Field.SOURCE_MD))
							row.setValue(sourceIndex, "1");
						if(!(row == null || row.size() == 0))
							ds.addRow(row);
					}
				}
			}
		}
	}

	private PageMeta getPageMetaByFuncnodePk(String pk_funcnode) {

		IUwFuncnodeQry funcnodeQry = NCLocator.getInstance().lookup(IUwFuncnodeQry.class);
		PageMeta pm = null;
		
		try {
			UwFuncNodeVO vo = funcnodeQry.getUwFuncnodeVOByPk(pk_funcnode);
			DefaultPageMetaBuilder dpb = new DefaultPageMetaBuilder();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			paramMap.put(WebConstant.PAGE_ID_KEY, vo.getPageid());		
			pm = dpb.buildPageMeta(paramMap);
			
		} catch (PaBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
		
		return pm;
	}
}
