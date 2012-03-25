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
 * 默认提供的注册在bd_billtype.checkclassname审批流检查类
 * @author zhangxya
 *
 */
public class DefaultStatusCallBack implements ICheckStatusCallback{

	public void callCheckStatus(CheckStatusCallbackContext cscc) throws BusinessException {
		NCObject ncObj = NCObject.newInstance(cscc.getBillVo());
		IFlowBizItf itf = ncObj.getBizInterface(IFlowBizItf.class);
		String[] fields = new String[3];
		// 审批人
		itf.setApprover(cscc.getApproveId());
		fields[0] = itf.getColumnName(itf.ATTRIBUTE_APPROVER);
		// 审批时间
		itf.setApproveDate(cscc.getApproveDate() == null ? null : new UFDateTime(cscc.getApproveDate()));
		fields[1] = itf.getColumnName(itf.ATTRIBUTE_APPROVEDATE);
		// 审批状态
		itf.setApproveStatus(cscc.getCheckStatus());
		fields[2] = itf.getColumnName(itf.ATTRIBUTE_APPROVESTATUS);
		// 保存修改后数据
		SuperVO vo = (SuperVO) ((AggregatedValueObject) cscc.getBillVo()).getParentVO();
		BaseDAO  baseDAO = new BaseDAO();
		baseDAO.updateVO(vo, fields);
	}
}
