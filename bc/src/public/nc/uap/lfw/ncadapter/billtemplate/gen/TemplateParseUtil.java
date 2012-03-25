package nc.uap.lfw.ncadapter.billtemplate.gen;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletVO;

public class TemplateParseUtil {
	/**
	 * ����ȥ�����һ��"."��
	 * @param metaProperty
	 * @return
	 */
	public String parseDatasetPartId(String metaProperty){
		return metaProperty.substring(0, metaProperty.lastIndexOf("."));
	}



	/**
	 * ��BodyVO���ҵ�IDColName����ӦVO
	 * @param idColName
	 * @param billVO
	 * @return
	 */
	public BillTempletBodyVO getTargetBodyVO(Integer pos, String idColName, String tabCode, BillTempletVO billVO){
		BillTempletBodyVO[] bodyVOs = billVO.getBodyVO();
		BillTempletBodyVO tempVO = null;
		if(bodyVOs != null){
			for(BillTempletBodyVO bodyVO : bodyVOs){
				if(bodyVO.getItemkey().equals(idColName)  || (bodyVO.getIdcolname() != null && bodyVO.getIdcolname().equals(idColName))){
					if(bodyVO.getPos().equals(pos) && (tabCode == null || bodyVO.getTable_code().equals(tabCode)))
						return bodyVO;
					tempVO = bodyVO;
				}
			}
		}
		if(tempVO == null)
			throw new LfwRuntimeException("����IdColName��ѯ������Ӧ���У���ȷ��ģ���д��ڴ���");
		return tempVO;
	}
}
