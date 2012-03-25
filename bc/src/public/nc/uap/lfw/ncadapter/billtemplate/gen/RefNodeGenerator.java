package nc.uap.lfw.ncadapter.billtemplate.gen;

import nc.uap.lfw.core.common.CompIdGenerator;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.ncadapter.billtemplate.ref.LfwNCRefUtil;
import nc.ui.bd.ref.IRefConst;


/**
 * 
 * 为单据模版生成参照节点元素
 * 
 */
public class RefNodeGenerator{

	//数据权限标识，根据数据权限过滤参照数据
	public static final  String DataPowerOperation_Code = "dataPowerOperation_Code";
	public NCRefNode createRefNode(Dataset writeDs, boolean usePower, String refKey, String displayName, String refCode, String readFields, String writeFields, boolean multiSel, String dataPowerOperation_Code, String attrTokens)
	{
		int refPageType = LfwNCRefUtil.getRefType(refCode);
		String refNodeId = CompIdGenerator.generateRefCompId(writeDs.getId(), refKey);
		
		NCRefNode refNode = new NCRefNode();
		refNode.setId(refNodeId);
		refNode.setFrom(Dataset.FROM_NC);
		refNode.setWriteDs(writeDs.getId());
		refNode.setPath(getRefNodePath(refPageType));
		if(attrTokens != null){
			refNode.setRefcode(attrTokens);
			refNode.setI18nName(attrTokens);
		}
		else{
			refNode.setRefcode(refCode);
			refNode.setI18nName(displayName);
		}
		
		refNode.setPagemeta("reference");
		refNode.setReadDs("masterDs");
		
		refNode.setReadFields(readFields);
		refNode.setWriteFields(writeFields);
		
		refNode.setUsePower(usePower);
		//此参照存在数据权限
		if(dataPowerOperation_Code != null)
			refNode.setExtendAttribute(DataPowerOperation_Code, dataPowerOperation_Code);
		//可多选
		refNode.setMultiSel(multiSel);
		return refNode;
	}
	
	/**
	 * 生成参照的PageMetaID,此ID的生成是需要规则的，该信息必须标明所在的单据模版以及所在的Dataset,FieldRelation的信息，
	 * 因为在参照的PageModel中，需要根据该信息对参照进行缓存以及根据该pageMetaId生成真正的参照信息，因此需要通过该标识查询
	 * 需要的信息。
	 * 
	 * 格式: 被参照DatasetId...页面类型...pageMeta
	 * 
	 * @param meta
	 * @param fieldRelationId
	 * @param ds
	 * @return
	 */
	public static String getReferencePageMetaId(Dataset ds, String fieldRelationId, String refCode, int refPageType)
	{
		String refDsId = getRefDatasetId(ds, fieldRelationId);
		return refDsId; // + WebConstant.QUERY_SPLIT_STR + refPageType + WebConstant.QUERY_SPLIT_STR + "pageMeta";	 	
	}
	
    /**
     * 获取被参照的dataset的ID，暂时使该ID与参照中真实使用的DatasetID相同。
     * 
     * @param ds
     * @param relationId
     * @return
     */	
	public static String getRefDatasetId(Dataset ds, String relationId)
	{
		return null;
//		FieldRelation fr = ds.getFieldRelation(relationId);
//		return fr.getRefDataset();
	}
	
	private static String getRefNodePath(int pageType){
		switch(pageType)
		{
			case IRefConst.TREE:
				return "reference/reftree.jsp";
			case IRefConst.GRIDTREE:
				return "reference/refgridtree.jsp";
			case IRefConst.GRID:
			default:
				return "reference/refgrid.jsp";
		}
	}
}
