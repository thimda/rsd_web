package nc.uap.lfw.pa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.ScriptServerListener;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.util.DefaultUIMetaBuilder;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.serializer.IPageElementSerializer;
import nc.uap.lfw.core.vo.LfwExAggVO;
import nc.uap.lfw.design.impl.DevelopeEditorSaveHandlerImpl;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.pa.info.BaseInfo;
import nc.uap.lfw.pa.info.IPropertyInfo;
import nc.uap.lfw.pa.info.InfoCategory;
import nc.uap.lfw.pa.itf.IPaPersistService;
import nc.uap.lfw.pa.setting.IEditorSaveHandler;
import nc.uap.lfw.stylemgr.itf.IUwTemplateService;
import nc.uap.lfw.stylemgr.vo.UwIncrementVO;
import nc.uap.lfw.stylemgr.vo.UwTemplateVO;
import nc.uap.lfw.stylemgr.vo.UwViewVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;

public class PaPropertySaveListener extends ScriptServerListener {

	public PaPropertySaveListener(LfwPageContext pageCtx, String widgetId) {
		super(pageCtx, widgetId);
	}

	@Override
	public void handlerEvent(ScriptEvent event) {
		
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget("settings");
		Dataset ds = widget.getViewModels().getDataset("ds_middle");
		
		WebSession session = getGlobalContext().getWebSession();
		String eclipse = session.getOriginalParameter("eclipse");
		
		if(eclipse != null && eclipse.equals("1")){
			saveForLocal();
			getGlobalContext().addExecScript("setSaveState()");
		}
		else{
			saveForDB();
			saveForDB(ds);
		}
	
	}
	private void saveForDB() {
		WebSession session = getGlobalContext().getWebSession();
		IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
		String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
		String sessionId = getGlobalContext().getWebContext().getRequest().getSession().getId();
		PageMeta pagemeta = service.getOriPageMeta(pageId, sessionId);
		UIMeta uimeta = service.getOriUIMeta(pageId, sessionId);
		String windowId = pagemeta.getId();
		
		String pk_template = session.getOriginalParameter("pk_template");
		
		IUwTemplateService serv = NCLocator.getInstance().lookup(IUwTemplateService.class);
		IPageElementSerializer pmSerializer = NCLocator.getInstance().lookup(IPageElementSerializer.class);
		UwTemplateVO template = null;
		try{
			String pageMetaStr = pmSerializer.serializePageMeta(pagemeta);
			String uimetaStr = pmSerializer.serializeUIMeta(uimeta);
			template = serv.getTemplateVOByPK(pk_template);
			template.doSetPageMetaStr(pageMetaStr);
			template.doSetUIMetaStr(uimetaStr);
			serv.updateTemplateVO(template);
			
			String[] ids = pagemeta.getWidgetIds();
			for(int i = 0; i < ids.length; i++){
				String widgetId = ids[i];
				LfwWidget widget = pagemeta.getWidget(widgetId);
				String widgetStr = pmSerializer.serializeWidget(widget);
				UIMeta childUIMeta = (UIMeta) UIElementFinder.findElementById(uimeta, widgetId+"_um");
				
				if(childUIMeta == null){
					DefaultUIMetaBuilder builder = new DefaultUIMetaBuilder();
					childUIMeta = builder.buildUIMeta(pagemeta, windowId, widgetId);
				}
				
				String childUIMetaStr = pmSerializer.serializeUIMeta(childUIMeta);
				
				UwViewVO viewVO = serv.getViewOrCreate(pk_template, widgetId);
				
				viewVO.doSetUIMetaStr(childUIMetaStr);
				viewVO.doSetWidgetStr(widgetStr);
				
				serv.updateViewVO(viewVO);
			}
		}catch (PaBusinessException e){
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		
	}

	private void saveForDB(Dataset ds) {
		
		List<LfwExAggVO> rlst = new ArrayList<LfwExAggVO>();
		Row[] selectedRows =  ds.getCurrentRowData().getRows();
		for(Row row : selectedRows){
			String ds_state = (String) row.getValue(ds.nameToIndex("ds_state"));
			if(ds_state.equals(PaConstant.DS_INIT) || ds_state.equals(PaConstant.DS_END)){
				continue;
			}
			UwIncrementVO vo = new UwIncrementVO();
			vo.setStatus(VOStatus.NEW);
			FieldSet fs = ds.getFieldSet();
			for(Field field : fs.getFieldList()){
				vo.setAttributeValue(field.getId(), row.getValue(ds.nameToIndex(field.getId())));
			}
			WebSession session = getGlobalContext().getWebSession();
			String pk_template = session.getOriginalParameter("pk_template");
			vo.setPk_template(pk_template);
			LfwExAggVO aggvo = new  LfwExAggVO();
			aggvo.setParentVO(vo);
			rlst.add(aggvo);
			
			row.setValue(ds.nameToIndex("ds_state"), PaConstant.DS_END);
		}
		 
		if(rlst != null && rlst.size() > 0){
			try {
				CRUDHelper.getCRUDService().saveBusinessVOs(rlst.toArray(new LfwExAggVO[0]));
			} catch (LfwBusinessException e) {
				LfwLogger.error(e.getMessage(), e);
				throw new LfwRuntimeException(e.getMessage(), e);
			}
		}
		
//		UifSaveCmd saveCmd = new UifSaveCmd(ds, null, UwIncrementVO.class);
		
		
	/*	try{
			WebSession session = getGlobalContext().getWebSession();
			String pk_template = session.getOriginalParameter("pk_template");
			//查找模板，并对有修改的组件类型进行存储标记
			IUwTemplateService tempService = NCLocator.getInstance().lookup(IUwTemplateService.class);
			UwTemplateVO templateVO = tempService.getTemplateVOByPK(pk_template);
			
			//取出模板中有修改的组件，并将类型存储到List中
			String includeType = templateVO.getCompchanged();
			List<String> types =getTypes(includeType);
			
			

			if(ds.getCurrentRowData() == null)
				return;
			
			Row[] tempRows = ds.getCurrentRowData().getRows();
			if(tempRows != null && tempRows.length > 0){
				//从ds中获取要增加、更新或者删除的vo集合
				List<SuperVO> updateVOs = new ArrayList<SuperVO>();
				List<SuperVO> deleteVOs = new ArrayList<SuperVO>();
				for(int i = 0; i < tempRows.length; i++){
					Row row = tempRows[i];
					String ds_state = (String) row.getValue(ds.nameToIndex("ds_state"));
					String type = (String) row.getValue(ds.nameToIndex("comptype"));
					
					if(ds_state.equals(PaConstant.DS_UPDATE)){
						if( types == null || !types.contains(type))
							types.add(type);
						
						SuperVO updateVO = getUpdateVO(ds, pk_template, row, type);
						if(updateVO != null)
							updateVOs.add(updateVO);
					}
					if(ds_state.equals(PaConstant.DS_DEL)){
						SuperVO deleteVO = (SuperVO)PaConstantMap.mappingTable.get(type).newInstance();
						String id = (String) row.getValue(ds.nameToIndex("string_ext1"));
						deleteVO.setAttributeValue("id", id);
						if(deleteVO != null){
							deleteVOs.add(deleteVO);
						}
					}
				}
				
				//对更新和增加的vo进行处理
				IPaPersistService service = NCLocator.getInstance().lookup(IPaPersistService.class);
				for(int i = 0; i < updateVOs.size(); i++){
					SuperVO updateVO = updateVOs.get(i);
					if(updateVO.getPrimaryKey() == null){
						service.persitCompVO(updateVO);
					}
					else{
						service.updateCompVO(updateVO);
					}
				}
				//对删除的vo进行处理
				for(int i = 0; i < deleteVOs.size(); i++){
					SuperVO delVO = deleteVOs.get(i);
					String id = (String) delVO.getAttributeValue("id");
					String clause = "pk_parent = '" + pk_template + "' and id = '" + id + "'";
					service.deleteVOByClause(delVO.getClass(), clause);
				}
				templateVO.setCompchanged(StringUtil.merge(types));
				tempService.updateTemplateVO(templateVO);
			}

		}
		catch(Exception ex){
			LfwLogger.error(ex);
			throw new LfwRuntimeException(ex);
		}*/
	}
	private SuperVO getUpdateVO(Dataset ds, String pk_template, Row row, String type) {
		SuperVO vo = getRealCompVO(ds, pk_template, row, type);		//判断是新增的还是数据库中存在的vo，并设置
		
		//根据类型，得到相应属性的信息
		BaseInfo bi = InfoCategory.getInfo(type);
		IPropertyInfo[] pi = bi.getPropertyInfos();
		//根据row中的数据向vo中设置新值
		for(int j = 0; j < pi.length; j++){
			IPropertyInfo pinfo = pi[j];
			int index = ds.nameToIndex(pinfo.getDsField());
			Object value = row.getValue(index);
			
			Object newValue = null;
			
			if(value == null)
				continue;
			
			if(value.equals("Y") || value.equals("y") || value.equals("N") || value.equals("n"))
				newValue = UFBoolean.valueOf((String) value);
			else
				newValue = value;
			
			vo.setAttributeValue(pinfo.getVoField(), newValue);
			
		}
		
		vo.setAttributeValue("pk_parent", pk_template);
		return vo;
	}

	private void saveForLocal() {
		WebSession session = getGlobalContext().getWebSession();
		IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
		
		String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
		String sessionId = getGlobalContext().getWebContext().getRequest().getSession().getId();
		PageMeta pagemeta = service.getOriPageMeta(pageId, sessionId);
		IUIMeta uimeta = service.getOriUIMeta(pageId, sessionId);
		
		
		IEditorSaveHandler handler = new DevelopeEditorSaveHandlerImpl();
		String pagemetaPath = session.getOriginalParameter("pagemetaPath");
		String widgetId = session.getOriginalParameter("viewId");
		if(widgetId != null){
			
			UIMeta meta = (UIMeta) uimeta;
			UIElement uiEle = meta.getElement();
			UIWidget uiWidget = null;
			UIMeta umeta = null;
			if(uiEle != null && uiEle instanceof UIWidget){
				uiWidget = (UIWidget) uiEle;
				umeta = uiWidget.getUimeta();
			}
			LfwWidget lwidget = pagemeta.getWidget(widgetId);
			if(widgetId != null){
				handler.save(pagemeta, umeta, lwidget, pagemetaPath);
			}
		}
		else{
			handler.save(pagemeta, (UIMeta) uimeta, null, pagemetaPath);
		}
	}
	private List<String> getTypes(String includeType) {
		String[] typeArr =null;
		if(includeType != null && includeType.length() != 0)
			typeArr= includeType.split(",");
		
		List<String> types = new ArrayList<String>();
		if(typeArr != null && typeArr.length != 0){
			types.addAll(Arrays.asList(typeArr));
		}
		return types;
	}

	/**
	 * 
	 * @param ds
	 * @param pk_template
	 * @param row
	 * @param type
	 * @param service
	 * @return 从数据库中查找vo是否存在，如果存在返回vo，不存在新建vo并返回
	 */

	private SuperVO getRealCompVO(Dataset ds, String pk_template,  Row row, String type){
		try{
			IPaPersistService service = NCLocator.getInstance().lookup(IPaPersistService.class);
			SuperVO newVO = (SuperVO) PaConstantMap.mappingTable.get(type).newInstance();
			int idIndex = ds.nameToIndex("string_ext1");
			String unqueId = (String) row.getValue(idIndex);
			String condition = "pk_parent = '" + pk_template + "' and id = '" + unqueId + "'";
			SuperVO dsVO = service.getCompVOByClause(newVO.getClass(), condition);
			if(dsVO == null)
				return newVO;
			else
				return dsVO;
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e);
		}
	}
}
