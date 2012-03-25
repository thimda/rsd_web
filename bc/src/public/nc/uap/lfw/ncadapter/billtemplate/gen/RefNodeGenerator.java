package nc.uap.lfw.ncadapter.billtemplate.gen;

import nc.uap.lfw.core.common.CompIdGenerator;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.ncadapter.billtemplate.ref.LfwNCRefUtil;
import nc.ui.bd.ref.IRefConst;


/**
 * 
 * Ϊ����ģ�����ɲ��սڵ�Ԫ��
 * 
 */
public class RefNodeGenerator{

	//����Ȩ�ޱ�ʶ����������Ȩ�޹��˲�������
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
		//�˲��մ�������Ȩ��
		if(dataPowerOperation_Code != null)
			refNode.setExtendAttribute(DataPowerOperation_Code, dataPowerOperation_Code);
		//�ɶ�ѡ
		refNode.setMultiSel(multiSel);
		return refNode;
	}
	
	/**
	 * ���ɲ��յ�PageMetaID,��ID����������Ҫ����ģ�����Ϣ����������ڵĵ���ģ���Լ����ڵ�Dataset,FieldRelation����Ϣ��
	 * ��Ϊ�ڲ��յ�PageModel�У���Ҫ���ݸ���Ϣ�Բ��ս��л����Լ����ݸ�pageMetaId���������Ĳ�����Ϣ�������Ҫͨ���ñ�ʶ��ѯ
	 * ��Ҫ����Ϣ��
	 * 
	 * ��ʽ: ������DatasetId...ҳ������...pageMeta
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
     * ��ȡ�����յ�dataset��ID����ʱʹ��ID���������ʵʹ�õ�DatasetID��ͬ��
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
