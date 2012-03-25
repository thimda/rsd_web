package nc.uap.lfw.core.uif.listener;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.CheckStatusCallbackContext;
import nc.bs.pub.pf.ICheckStatusCallback;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * Ĭ���ṩ��ע����bd_billtype.checkclassname�����������
 * @author zhangxya
 *
 */
public class DefaultStatusCallBack implements ICheckStatusCallback{

	public void callCheckStatus(CheckStatusCallbackContext cscc) throws BusinessException {
		NCObject ncObj = NCObject.newInstance(cscc.getBillVo());
		IFlowBizItf itf = ncObj.getBizInterface(IFlowBizItf.class);
		String[] fields = new String[3];
		// ������
		itf.setApprover(cscc.getApproveId());
		fields[0] = itf.getColumnName(itf.ATTRIBUTE_APPROVER);
		// ����ʱ��
		itf.setApproveDate(cscc.getApproveDate() == null ? null : new UFDateTime(cscc.getApproveDate()));
		fields[1] = itf.getColumnName(itf.ATTRIBUTE_APPROVEDATE);
		// ����״̬
		itf.setApproveStatus(cscc.getCheckStatus());
		fields[2] = itf.getColumnName(itf.ATTRIBUTE_APPROVESTATUS);
		// �����޸ĺ�����
		SuperVO vo = (SuperVO) ((AggregatedValueObject) cscc.getBillVo()).getParentVO();
		BaseDAO  baseDAO = new BaseDAO();
		baseDAO.updateVO(vo, fields);
	}
}
