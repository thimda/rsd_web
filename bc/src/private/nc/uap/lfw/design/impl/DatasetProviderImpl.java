package nc.uap.lfw.design.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.lfw.core.md.util.LfwMdUtil;
import nc.md.IMDQueryFacade;
import nc.md.MDBaseQueryFacade;
import nc.md.common.AssociationKind;
import nc.md.model.IAssociation;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.ICardinality;
import nc.md.model.IComponent;
import nc.md.model.IForeignKey;
import nc.md.model.MetaDataException;
import nc.md.model.access.javamap.AggVOStyle;
import nc.md.model.access.javamap.IBeanStyle;
import nc.md.model.impl.Attribute;
import nc.md.model.type.IRefType;
import nc.md.model.type.IType;
import nc.md.model.type.impl.EnumType;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.combodata.MDComboDataConf;
import nc.uap.lfw.core.common.CompIdGenerator;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.MDField;
import nc.uap.lfw.core.data.MatchField;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.ModifyField;
import nc.uap.lfw.core.data.RefMdDataset;
import nc.uap.lfw.core.data.WhereField;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.design.itf.IDatasetProvider;
import nc.uap.lfw.design.itf.MdClassVO;
import nc.uap.lfw.design.itf.MdComponnetVO;
import nc.uap.lfw.design.itf.MdModuleVO;
import nc.uap.lfw.ncadapter.billtemplate.gen.IdMaskUtil;
import nc.uap.lfw.ncadapter.billtemplate.gen.RefNodeGenerator;
import nc.uap.lfw.ncadapter.billtemplate.ref.LfwNCRefUtil;
import nc.uap.lfw.ncadapter.common.LfwTypeToITypeTranslator;
import nc.uap.lfw.reference.app.AppLfwRefGenUtil;
import nc.uap.lfw.reference.base.ILfwRefModel;
import nc.uap.lfw.reference.nc.NcAdapterGridRefModel;
import nc.uap.lfw.reference.nc.NcAdapterTreeGridRefModel;
import nc.uap.lfw.reference.nc.NcAdapterTreeRefModel;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IRefConst;
import nc.vo.pub.BusinessException;

/**
 * 获取元数据信息实现类
 * @author zhangxya
 *
 */
@SuppressWarnings("unchecked")
public class DatasetProviderImpl implements IDatasetProvider{

	private static final String IBDOBJECT_ITF = "nc.vo.bd.meta.IBDObject";
	
	

	public List getALlModuels() throws LfwBusinessException {
		try {
			String sql = "select id,name,displayname from md_module";
			BaseDAO dao = new BaseDAO();
			List list = (List) dao.executeQuery(sql,  new BeanListProcessor(MdModuleVO.class));
			return list;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}
	

	public List getAllClasses() throws LfwBusinessException{
		try {
			String sql = "SELECT id,componentid,classtype,displayname,name,accessorclassname " +
					"FROM md_class WHERE classtype = '201'";
			BaseDAO dao = new BaseDAO();
//			SQLParameter param = new SQLParameter();
			List list = (List) dao.executeQuery(sql, new BeanListProcessor(MdClassVO.class));
			return list;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}
	public List getAllClassByComId(String componentId)
			throws LfwBusinessException {
		try {
			String sql = "SELECT id,componentid,classtype,displayname,name,accessorclassname " +
					"FROM md_class WHERE componentid = ? and classtype = '201'";
			BaseDAO dao = new BaseDAO();
			SQLParameter param = new SQLParameter();
			param.addParam(componentId);
			List list = (List) dao.executeQuery(sql, param, new BeanListProcessor(MdClassVO.class));
			return list;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}

	public List getAllComponentByModuleId(String moduleId)
			throws LfwBusinessException {
		try {
			String sql = "SELECT id,ownmodule,name,displayname,namespace FROM md_component WHERE ownmodule = ? ";
			BaseDAO dao = new BaseDAO();
			SQLParameter param = new SQLParameter();
			param.addParam(moduleId);
			List list = (List) dao.executeQuery(sql, param, new BeanListProcessor(MdComponnetVO.class));
			return list;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}
	
	public List getAllComponents() throws LfwBusinessException{
		try {
			String sql = "SELECT id,ownmodule,name,displayname,namespace,ts,version FROM md_component";
			BaseDAO dao = new BaseDAO();
			List list = (List) dao.executeQuery(sql, new BeanListProcessor(MdComponnetVO.class));
			return list;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}
	
	
	public List<RefNode>  getNcRefNodeList(MdDataset mdds) throws LfwBusinessException{
		List<RefNode> refnodeList = new ArrayList<RefNode>();
		String mdPath = mdds.getObjMeta();
		if(mdPath != null && !mdPath.equals(""))
		{
			String[] ids = mdPath.split("\\.");
			if(ids.length != 2)
				throw new LfwBusinessException("MdDataset的元数据路径有误!");
			IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
			try {
				List<FieldRelation> fieldRelations = getFieldRelations(mdds);
				IBean bean = qry.getBeanByFullName(mdPath);
				List<IAttribute> attris = bean.getAttributes();
				for (int j = 0; j < attris.size(); j++) {
					IAttribute attr = (IAttribute)attris.get(j);
					if(attr.getName().equals("dr") || attr.getName().equals("ts") ||attr.getName().equals("status"))
						continue;
					if(attr.getDataType().getTypeType() == IType.COLLECTION)
						continue;
					if(attr.getDataType().getTypeType() == IType.REF || attr.getDataType().getTypeType() == IType.ENTITY){
						//IBusinessEntity targetBean = ((IBusinessEntity)attr.getAssociation().getEndBean());
						IRefType ref = (IRefType) attr.getDataType();
						String refCode = ref.getRefType().getDefaultRefModelName();
						if(refCode == null){
							continue;
						}
						
						AbstractRefModel model = LfwNCRefUtil.getRefModel(refCode);
						if(model == null)
							continue;
						int type = LfwNCRefUtil.getRefType(model);
						ILfwRefModel refModel = null;
						if(type == IRefConst.GRIDTREE){
							NcAdapterTreeGridRefModel tgrm = new NcAdapterTreeGridRefModel();
							tgrm.setNcModel((AbstractRefGridTreeModel) model);
							refModel = tgrm;
						}
						
						else if(type == IRefConst.TREE){
							NcAdapterTreeRefModel tgrm = new NcAdapterTreeRefModel();
							tgrm.setNcModel((AbstractRefTreeModel) model);
							refModel = tgrm;
						}
						
						else if(type == IRefConst.GRID){
							NcAdapterGridRefModel tgrm = new NcAdapterGridRefModel();
							tgrm.setNcModel(model);
							refModel = tgrm;
						}
						
						AppLfwRefGenUtil gen = new AppLfwRefGenUtil(refModel, null);
						String[] refEles = gen.getRefElements();
						
						IBusinessEntity entity = (IBusinessEntity) ((IRefType)attr.getDataType()).getRefType();
						String showName = LfwMdUtil.getMdItfAttr(entity, IBDOBJECT_ITF, "name");
						if(showName != null){
							String writeFields = attr.getName() + "," + attr.getName() + "_" + showName.replaceAll("//.",  WebConstant.DATASET_META_REPLACE_STR);
							String readFields = refEles[0] + "," + refEles[2];
								
							NCRefNode refNode = new RefNodeGenerator().createRefNode(mdds, false, attr.getName(), null, refCode, readFields, writeFields, false, null, null);
							//NCRefNode refNode = null;
							refNode.setFrom(null);
							for (int i = 0; i < fieldRelations.size(); i++) {
								FieldRelation fieldRelation = fieldRelations.get(i);
								WhereField whereField = fieldRelation.getWhereField();
								MatchField matchField = fieldRelation.getMatchFields()[0];
								//String readField = matchField.getReadField();
								String writeField = matchField.getWriteField();
								String newRefId = CompIdGenerator.generateRefCompId(mdds.getId(), whereField.getValue());
//								String newCaption = mdds.getCaption() + "_" + refNode.getRefcode();
//								refNode.setCaption(newCaption);
								if(refNode.getId().equals(newRefId)){
									String newRefDsId = CompIdGenerator.generateRefCompId(mdds.getId(), writeField);
									refNode.setId(newRefDsId);
								}
							}
							if(refNode != null)							
								refnodeList.add(refNode);
						}
					}
				}
			}
			catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new LfwBusinessException(e);
			}
		}
		return refnodeList;
	}
	
	
	public List<ComboData>  getNcComoboDataList(MdDataset ds) throws LfwBusinessException{
		List<ComboData> comboList = new ArrayList<ComboData>();
		String mdPath = ds.getObjMeta();
		if(mdPath != null && !mdPath.equals(""))
		{
			String[] ids = mdPath.split("\\.");
			if(ids.length != 2)
				throw new LfwBusinessException("MdDataset的元数据路径有误!");
			IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
			try {
				IBean bean = qry.getBeanByFullName(mdPath);
				List<IAttribute> attris = bean.getAttributes();
				for (int j = 0; j < attris.size(); j++) {
					IAttribute attr = (IAttribute)attris.get(j);
					if(attr.getDataType().getTypeType() == IType.ENUM){
						MDComboDataConf combo = new MDComboDataConf();
						String comboId = CompIdGenerator.generateComboCompId(ds.getId(), attr.getName());
						combo.setId(comboId);
						EnumType type = (EnumType) attr.getDataType();
						combo.setFullclassName(type.getID());
						combo.setCaption(bean.getDisplayName());
//						combo.setRefDsid(type.getID());
//						Iterator<IEnumValue> it = type.getEnumValues().iterator();
//						while(it.hasNext()){
//							IEnumValue ev = it.next();
//							CombItem item = new CombItem();
//							item.setText(ev.getName());
//							item.setValue(ev.getValue());
//							item.setI18nName(ev.getResID());
//							combo.addCombItem(item);
//						}
						comboList.add(combo);
						//设置下拉框选项
					}
				}
			}
			catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new LfwBusinessException(e);
			}
				
		}
		return comboList;
							
	}
	
	
	public List<MdDataset>  getRefMdDatasetList(MdDataset mdds) throws LfwBusinessException{
		String mdPath = mdds.getObjMeta();
		List<MdDataset> refDsList = new ArrayList<MdDataset>();
		if(mdPath != null && !mdPath.equals(""))
		{
			String[] ids = mdPath.split("\\.");
			if(ids.length != 2)
				throw new LfwBusinessException("MdDataset的元数据路径有误!");
			IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
			try {
				IBean bean = qry.getBeanByFullName(mdPath);
				List<IAttribute> attris = bean.getAttributes();
				for (int j = 0; j < attris.size(); j++) {
					IAttribute attr = (IAttribute)attris.get(j);
					if(attr.getDataType().getTypeType() == IType.COLLECTION)
						continue;
					if(attr.getDataType().getTypeType() == IType.REF){
						IBusinessEntity targetBean = ((IBusinessEntity)attr.getAssociation().getEndBean());
						String fullName = attr.getAssociation().getEndBean().getFullName();
						String refDsId = "$refds_" + IdMaskUtil.maskId(fullName);
						RefMdDataset mdDs = new RefMdDataset();
						mdDs.setId(refDsId);
						mdDs.setObjMeta(targetBean.getFullName());
						mdDs.setVoMeta(targetBean.getFullClassName());
						mdDs.setCaption(targetBean.getDisplayName());
						//重新调用getMdDataset，返回所有的字段
						//MdDataset newMdds = getMdDataset(mdDs);
						refDsList.add(mdDs);
					}
				}
			}
			catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new LfwBusinessException(e);
			}
				
		}
		return refDsList;
					
	}
	
	public List getFieldRelations(MdDataset mdds) throws LfwBusinessException{
		String mdPath = mdds.getObjMeta();
		if(mdPath != null && !mdPath.equals(""))
		{
			String[] ids = mdPath.split("\\.");
			if(ids.length != 2)
				throw new LfwBusinessException("MdDataset的元数据路径有误!");
			IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
			try {
				IBean bean = qry.getBeanByFullName(mdPath);
				List<IAttribute> attris = bean.getAttributes();
				List<FieldRelation> frs = new ArrayList<FieldRelation>();
				for (int j = 0; j < attris.size(); j++) {
					IAttribute attr = (IAttribute)attris.get(j);
					if(attr.getName().equals("dr") || attr.getName().equals("ts") ||attr.getName().equals("status"))
						continue;
					if(attr.getDataType().getTypeType() == IType.COLLECTION)
						continue;
					if(attr.getDataType().getTypeType() == IType.REF || attr.getDataType().getTypeType() == IType.ENTITY){
						IBusinessEntity targetBean = ((IBusinessEntity)attr.getAssociation().getEndBean());
						String showName = LfwMdUtil.getMdItfAttr(targetBean, IBDOBJECT_ITF, "name");
						String firstField = null;
						String matchWriteField = null;
						while(showName != null){
							if(showName != null && showName.indexOf(".") != -1){
								String[] splitFields = showName.split("\\.");
								firstField = splitFields[0];
								FieldRelation fr = new FieldRelation();
								fr.setId(firstField + "_rel");
								String fullName = attr.getAssociation().getEndBean().getFullName();
								String refDsId = "$refds_" + fullName.replaceAll("\\.", "_");
								fr.setRefDataset(refDsId);
								String targetPk = targetBean.getKeyAttribute().getColumn().getName();
								MatchField mf = new MatchField();
								mf.setReadField(firstField);
								//设置matchField字段
								matchWriteField = attr.getName() + "_" + firstField;
								mf.setWriteField(matchWriteField);
								fr.addMatchField(mf);
								WhereField whereField = new WhereField();
								whereField.setKey(targetPk);
								whereField.setValue(attr.getName());
								fr.setWhereField(whereField);
								frs.add(fr);
								if(attr.getAssociation() != null){
									attr = attr.getAssociation().getEndBean().getAttributeByName(firstField);
								}
								showName = splitFields[1];
								targetBean = ((IBusinessEntity)((IRefType)attr.getDataType()).getRefType());
							}else{
								FieldRelation fr = new FieldRelation();
								String fullName = attr.getAssociation().getEndBean().getFullName();
								String refDsId = "$refds_" + fullName.replaceAll("\\.", "_");
								fr.setRefDataset(refDsId);
								String targetPk = targetBean.getKeyAttribute().getColumn().getName();
								MatchField mf = new MatchField();
								mf.setReadField(showName);
								fr.addMatchField(mf);
								WhereField whereField = new WhereField();
								whereField.setKey(targetPk);
								if(matchWriteField != null){
									fr.setId(matchWriteField  + "_rel");
									mf.setWriteField(matchWriteField + "_" + showName);
									whereField.setValue(matchWriteField);
								}
								else{
									fr.setId(attr.getName() + "_rel");
									mf.setWriteField(attr.getName() + "_" + showName);
									whereField.setValue(attr.getName());
								}
								fr.setWhereField(whereField);
								frs.add(fr);
								showName = null;
							}
						}
						
//						if(showName != null){
//							FieldRelation fr = new FieldRelation();
//							fr.setId(attr.getName() + "_rel");
//							String fullName = attr.getAssociation().getEndBean().getFullName();
//							String refDsId = "$refds_" + fullName.replaceAll("\\.", "_");
//							fr.setRefDataset(refDsId);
//							String targetPk = targetBean.getKeyAttribute().getColumn().getName();
//	//						MatchField mf = new MatchField();
//	//						mf.setReadField(targetPk);
//	//						mf.setWriteField(attr.getName());
//						//	fr.addMatchField(mf);
//							
//							MatchField mf = new MatchField();
//							mf.setReadField(showName);
//							//mf.setWriteField(attr.getName() + "_mc");
//							mf.setWriteField(attr.getName() + "_" + showName);
//							fr.addMatchField(mf);
//							WhereField whereField = new WhereField();
//							whereField.setKey(targetPk);
//							whereField.setValue(attr.getName());
//							fr.setWhereField(whereField);
//							frs.add(fr);
//						}
					}
				}
				return frs;
	
			}catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new LfwBusinessException(e);
			}
		}
		return null;

	}
	
	
	public String getAggVo(String fullClassName) throws LfwBusinessException{
		IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
		String aggVO = null;
		try {
			IBean bean = qry.getBeanByFullClassName(fullClassName);
			IBeanStyle beanStyle = bean.getBeanStyle();
			if(beanStyle instanceof AggVOStyle){
				AggVOStyle aggVOStyle = (AggVOStyle) beanStyle;
				aggVO = aggVOStyle.getAggVOClassName();
			}
		}catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
		return aggVO;
	}
	
	public MdDataset mergeDataset(MdDataset mdds) throws LfwBusinessException{
		List<ModifyField> list = new ArrayList<ModifyField>();
		FieldSet pubDsFieldSet = mdds.getFieldSet();
		int pubFieldsCount = pubDsFieldSet.getFieldCount();
		for(int i = 0; i < pubFieldsCount; i ++)
		{
			Field f = pubDsFieldSet.getField(i);
			if(f instanceof ModifyField)
				list.add((ModifyField) f);
		}
		
		Iterator<ModifyField> it = list.iterator();
		while(it.hasNext()){
			pubDsFieldSet.removeField(it.next());
		}
		
		//FieldSet mdfs = mdds.getFieldSet();
		Iterator<ModifyField> mfIt = list.iterator();
		while(mfIt.hasNext()){
			ModifyField mField = mfIt.next();
			Field field = mdds.getFieldSet().getField(mField.getId());
			if(field instanceof MDField){
				MDField mdField = (MDField) field;
				
				if(mdField != null)
				{
					if(mField.getI18nName() != null && !mField.getI18nName().equals(""))
						mdField.setI18nName(mField.getI18nName());
					if(mField.getField() != null && !mField.getField().equals(""))
						mdField.setField(mField.getField());
					if(mField.getText() != null && !mField.getText().equals(""))
						mdField.setText(mField.getText());
					if(mField.getDefaultValue() != null && !mField.getDefaultValue().equals(""))
						mdField.setDefaultValue(mField.getDefaultValue());
					if(mField.getEditFormular() != null && !mField.getEditFormular().equals(""))
						mdField.setEditFormular(mField.getEditFormular());	
					if(mField.getValidateFormula() != null && !mField.getValidateFormula().equals(""))
						mdField.setValidateFormula(mField.getValidateFormula());
					if(mField.getFormater() != null && !mField.getFormater().equals(""))
						mdField.setFormater(mField.getFormater());
					if(mField.getMaxValue() != null && !mField.getMaxValue().equals(""))
						mdField.setMaxValue(mField.getMaxValue());
					if(mField.getMinValue() != null && !mField.getMinValue().equals(""))
						mdField.setMinValue(mField.getMinValue());
					if(mField.getPrecision() != null && !mField.getPrecision().equals(""))
						mdField.setPrecision(mField.getPrecision());
					mdField.setNullAble(mField.isNullAble());
				}
				else{
					LfwLogger.error("根据ModifyField id找不到Field," + mField.getId());
				}
			}
		}
		
		return mdds;
	}
	
	
	/**
	 * 设置外键标识
	 * @param targetDs
	 * @param fkField
	 */
	private void  setFKSign(Dataset targetDs, String fkField){
		Field field = targetDs.getFieldSet().getField(fkField);
		if(field == null){
			field = new Field();
			field.setId(fkField);
			field.setNullAble(false);
		}
	}
	
	/**
	 * 得到dsrelation
	 * @param entity
	 * @param widget
	 */
	private void getDsRelations(IBusinessEntity entity, LfwWidget widget){
		List<IAssociation>  associations = entity.getAssociations();
		if(associations == null)
			return;
		//取一对多实体
		List<IBean> entityList = entity.getRelatedEntities(AssociationKind.Composite, ICardinality.ASS_ONE2MANY);
		if(entityList != null){
			for(IBean relEntity : entityList){
//				IBusinessEntity childEntity = null;
//				try {
//					childEntity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(relEntity.getFullName());
//				} catch (MetaDataException e) {
//					// TODO Auto-generated catch block
//					LfwLogger.error(e.getMessage(), e);
//					throw new LfwRuntimeException("查找元数据异常!");
//				}
				IForeignKey fk = relEntity.getTable().getForeignKeyWithEndTable(entity.getTable());
				if(fk == null)
					continue;
				String masterDsId = entity.getName().replaceAll("\\.", WebConstant.DATASET_META_REPLACE_STR);
				//String slaveMetaId = relEntity.getFullName();
				String slaveMetaId = relEntity.getName();
				String slaveDsId = slaveMetaId.replaceAll("\\.", WebConstant.DATASET_META_REPLACE_STR);//"SCM_SalesOrder_orderitem";//
				Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
				if(masterDs == null)
					continue;
				Dataset targetDs = widget.getViewModels().getDataset(slaveDsId);
				if(targetDs == null)
					continue;
				DatasetRelation relation = new DatasetRelation();
				relation.setId(fk.getStartTable().getName() + "_" + fk.getEndTable().getName());
				relation.setMasterDataset(masterDsId);
				relation.setMasterKeyField(fk.getEndColumn().getName());
				relation.setDetailDataset(slaveDsId);
				relation.setDetailForeignKey(fk.getStartColumn().getName());
				relation.setFrom(Dataset.FROM_NC);
				setFKSign(targetDs, fk.getStartColumn().getName());
				if(widget.getViewModels().getDsrelations() == null){
					DatasetRelations dsRelations = new DatasetRelations();
					widget.getViewModels().setDsrelations(dsRelations);
				}
					
				widget.getViewModels().getDsrelations().addDsRelation(relation);
			}
		}
	}
	
	//从组件得到所有的元数据
	public LfwWidget getMdDsFromComponent(LfwWidget widget, String componetId) throws LfwBusinessException{
		IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
		try {
			IComponent component = qry.getComponentByID(componetId);
			List<IBusinessEntity> componentList = component.getBusinessEntities();
			if(componentList == null || componentList.size() == 0)
				return widget;
			for (int i = 0; i < componentList.size(); i++) {
				IBusinessEntity mdbean = componentList.get(i);
				MdDataset mdDs = new MdDataset();
				//替换ID中所有非字母、数字和下划线的符号为'_'
				//mdDs.setId(mdbean.getName().replaceAll("[^0-9_A-Za-z]", "_"));
				mdDs.setId(IdMaskUtil.maskId(mdbean.getName()));
				if(mdDs.getFieldSet().getFieldCount() == 0){
					mdDs.setObjMeta(component.getNamespace() + "." + mdbean.getName());
				}else{
					mdDs.setObjMeta(component.getNamespace() + "." + mdbean.getName());
					mdDs = getMdDataset(mdDs);
				}
				
				//得到元数据Ds的关联FieldRelation
				List fieldRelaltions = getFieldRelations(mdDs);
				for (int j = 0; j < fieldRelaltions.size(); j++) {
					FieldRelation fr = (FieldRelation) fieldRelaltions.get(j);
					//fr.setRefDataset(fr.getRefDataset().replaceAll("[^$0-9_A-Za-z]", "_"));
					fr.setRefDataset(IdMaskUtil.maskId(fr.getRefDataset()));
					mdDs.getFieldRelations().addFieldRelation(fr);
				}
				
				//添加默认的DsListener
				DatasetListener dsListener = new DatasetListener();
				EventHandlerConf eventHandler = dsListener.getOnDataLoadEvent();
				DatasetRule dsRule = new DatasetRule();
				dsRule.setId(mdDs.getId());
				dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
				WidgetRule widgetRule = new WidgetRule();
				widgetRule.setId(widget.getId());
				widgetRule.addDsRule(dsRule);
				eventHandler.getSubmitRule().addWidgetRule(widgetRule);
				eventHandler.setOnserver(true);
				dsListener.addEventHandler(eventHandler);
				dsListener.setId("defaultDsListener");
				dsListener.setServerClazz("nc.uap.lfw.core.event.deft.DefaultDatasetServerListener");
				mdDs.addListener(dsListener);
				
				widget.getViewModels().addDataset(mdDs);
				List<RefNode> refNodes = getNcRefNodeList(mdDs);
				for (int j = 0; j < refNodes.size(); j++) {
					widget.getViewModels().addRefNode(refNodes.get(j));
				}
				List<ComboData> comboDatas = getNcComoboDataList(mdDs);
				for (int j = 0; j < comboDatas.size(); j++) {
					widget.getViewModels().addComboData(comboDatas.get(j));
				}
				//得到引入的数据集
				List<MdDataset> refdsList = getRefMdDatasetList(mdDs);
				for (int j = 0; j < refdsList.size(); j++) {
					MdDataset ds = refdsList.get(j);
					widget.getViewModels().addDataset(ds);
				}
			}
			
			for (int i = 0; i < componentList.size(); i++) {
				IBusinessEntity mdbean = componentList.get(i);
				getDsRelations(mdbean, widget);
			}
		} catch (MetaDataException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException("填充MDDataset时出错", e);
		}
		return widget;
	}
	
	public MdDataset getMdDataset(MdDataset mdds){
		LifeCyclePhase originalPhase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		
//		if(LfwRuntimeEnvironment.getDatasource() == null){
//			IBusiCenterManageService busiCender = NCLocator.getInstance().lookup(IBusiCenterManageService.class);
//			BusiCenterVO[] busicenters = null;
//			try {
//				busicenters = busiCender.getBusiCenterVOs();
//			} catch (BusinessException e1) {
//				// TODO Auto-generated catch block
//				LfwLogger.error(e1.getMessage(), e1);
//			}
//			for (int i = 0; i < busicenters.length; i++) {
//				//设置默认数据源
//				BusiCenterVO busi = busicenters[i];
//				if(busi.getCode() != null && ("develop".equals(busi.getCode())) || "0000".equals(busi.getCode())){
//					continue;
//				}
//				else{
//					LfwRuntimeEnvironment.setDatasource(busi.getDataSourceName());
//					break;
//				}
//			}
//			if(LfwRuntimeEnvironment.getDatasource() == null || "".equals(LfwRuntimeEnvironment.getDatasource()))
//				LfwRuntimeEnvironment.setDatasource(busicenters[0].getDataSourceName());
//		}
//		else
//			LfwRuntimeEnvironment.setDatasource(LfwRuntimeEnvironment.getDatasource());
		String mdPath = mdds.getObjMeta();
		if(mdPath != null && !mdPath.equals("")){
			String[] ids = mdPath.split("\\.");
			if(ids.length != 2)
				throw new LfwRuntimeException("MdDataset的元数据路径有误!");
			IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
			try {
				IBean bean = qry.getBeanByFullName(mdPath);
				if(bean == null){
					LfwLogger.error("根据路径:" + mdPath + ",找不到实体");
					return mdds;
				}
				List<IAttribute> attris = bean.getAttributes();
				FieldSet fieldset = new FieldSet();	
				int attrSize = attris.size();
				for (int j = 0; j < attrSize; j++) {
					IAttribute attr = (IAttribute)attris.get(j);
					if(attr.getDataType().getTypeType() == IType.COLLECTION)
						continue;
//					if(attr.getName().equals("dr") || attr.getName().equals("ts") ||attr.getName().equals("status"))
//						continue;
//					if(attr.getDataType().getTypeType() == IType.COLLECTION)
//						continue;
					LfwLogger.info("attr属性, ID:" + attr.getID() + ";name:" + attr.getName()+ ";displayname:" + attr.getDisplayName());

					MDField mdfield = new MDField();
					mdfield.setId(attr.getName());
			
					//attr.getColumn()表示取attr对应的的数据表中的字段
					if(attr instanceof Attribute){
						//Attribute attribute = (Attribute) attr;
						//if(!attribute.isNotSerialize()){
							if(attr.getColumn() != null){
								mdfield.setField(attr.getColumn().getName());
								mdfield.setPrimaryKey(attr.getColumn().isPkey());
								mdfield.setNullAble(attr.getColumn().isNullable());
							}else if(attr.isCalculation()){
								mdfield.setField(attr.getName());
							}
							
						//}
					}
					if(attr.getName().toLowerCase().equals("dr"))
						mdfield.setDefaultValue(Integer.valueOf(0));
					else
						mdfield.setDefaultValue(attr.getDefaultValue());
					
					mdfield.setText(attr.getDisplayName());
					mdfield.setI18nName(attr.getResID());
					mdfield.setDataType(LfwTypeToITypeTranslator.translateITypeToString(attr.getDataType()));
					mdfield.setMaxValue(attr.getMaxValue());
					mdfield.setMinValue(attr.getMinValue());
				
					
					if(attr.getDataType().equals(IType.TYPE_INT) || attr.getDataType().equals(IType.TYPE_UFDouble) 
						 ||  attr.getDataType().equals(IType.TYPE_DOUBLE) ||
							attr.getDataType().equals(IType.TYPE_FLOAT) || attr.getDataType().equals(IType.TYPE_Double) || 
							attr.getDataType().equals(IType.TYPE_Integer))
						mdfield.setPrecision(String.valueOf(attr.getPrecise()));
					
					fieldset.addField(mdfield);
					//处理参照数据
					if(attr.getDataType().getTypeType() == IType.REF){
						MDField mdrefField = new MDField();
						IBusinessEntity refBusi = ((IBusinessEntity)((IRefType)attr.getDataType()).getRefType());
						String showName = LfwMdUtil.getMdItfAttr(refBusi, IBDOBJECT_ITF, "name");
						while(showName != null){
							mdfield.setExtSource(Field.SOURCE_MD);
							mdfield.setExtSourceAttr(refBusi.getFullName());
							if(showName != null && showName.indexOf(".") != -1){
								String[] splitFields = showName.split("\\.");
								String firstField = splitFields[0];
								mdrefField = generateMdRefField(attr, mdfield , firstField);
								if(attr.getAssociation() != null){
									attr = attr.getAssociation().getEndBean().getAttributeByName(firstField);
									refBusi = ((IBusinessEntity)((IRefType)attr.getDataType()).getRefType());
								}
								fieldset.addField(mdrefField);
								showName = splitFields[1];
								mdfield = mdrefField;
							}else{
								mdrefField = generateMdRefField(attr, mdfield, showName);
								fieldset.addField(mdrefField);
								showName = null;
							}
						}
					}
					
					//处理下拉框数据
					if(attr.getDataType().getTypeType() == IType.ENUM){
						EnumType type = (EnumType) attr.getDataType();
						mdfield.setExtSource(Field.SOURCE_ENUM);
						mdfield.setExtSourceAttr(type.getID());
					}
					
				}
				
				List<IForeignKey> fList = bean.getTable().getForeignKeies();
				if(fList != null){
					int size = fList.size();
					for (int i = 0; i < size; i++) {
						IForeignKey fk = fList.get(i);
						if(containsField(fieldset, fk.getStartColumn().getName()))
							continue;
						MDField mdfield = new MDField();
						mdfield.setId(fk.getStartColumn().getName());
						mdfield.setField(fk.getStartColumn().getName());
						mdfield.setDefaultValue(null);
						mdfield.setText(fk.getStartColumn().getDisplayName());
						mdfield.setPrimaryKey(fk.getStartColumn().isPkey());
						mdfield.setI18nName(fk.getResID());
						mdfield.setDataType(StringDataTypeConst.STRING);
						fieldset.addField(mdfield);
					}
				}
				
				mdds.setFieldSet(fieldset);
				mdds.setVoMeta(bean.getFullClassName());
				mdds.setCaption(bean.getDisplayName());
			}
			catch (Exception e) {
				LfwLogger.error(e);
			}
			RequestLifeCycleContext.get().setPhase(originalPhase);
			return mdds;
		}
		return null;
	}

	private boolean containsField(FieldSet fieldSet, String field){
		Field[] fs = fieldSet.getFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			if(field.equals(f.getField()))
				return true;
		}
		return false;
	}

	/**
	 * 生成需要的参照的额外字段
	 * @param attr
	 * @param mdrefField
	 * @param showName
	 * @return
	 */
	private MDField generateMdRefField(IAttribute attr, MDField mdrefField,
			String showName) {
		MDField mdrefFieldExt = new MDField();
		mdrefFieldExt.setId(mdrefField.getId() + "_" + showName);
		mdrefFieldExt.setSourceField(mdrefField.getId());
		mdrefFieldExt.setField(null);
		mdrefFieldExt.setDefaultValue(null);
		mdrefFieldExt.setText(mdrefField.getText());
		mdrefFieldExt.setI18nName(mdrefField.getI18nName());
		mdrefFieldExt.setDataType(StringDataTypeConst.STRING);
		return mdrefFieldExt;
	}
}

