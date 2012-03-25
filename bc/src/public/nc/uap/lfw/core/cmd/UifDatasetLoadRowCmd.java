package nc.uap.lfw.core.cmd;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;

/**
 * ���ݼ��������ݼ�������
 *
 */
public class UifDatasetLoadRowCmd extends UifCommand {

	private String datasetId;
	private String billId;
	
	/**
	 * 
	 * @param dsId ���ݼ�ID
	 * @param billId  ����VO ��ID
	 */
	public UifDatasetLoadRowCmd(String dsId, String billId) {
		this.datasetId = dsId;
		this.billId = billId;
	}
	
	/**
	 * ���ݼ���Ĭ��ʵ�֣�֧�����Ӳ�ѯ�������ݲ�ѯ����ҳ�ȡ�
	 */
	public void execute() {
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(datasetId);
		String clazz = ds.getVoMeta();
		if(clazz == null)
			return;
		SuperVO vo = (SuperVO) LfwClassUtil.newInstance(clazz);

		
		//���ǰ̨���ý��������������ʾ���ش˵��ݣ�����������������ǰ̨�������ģ�
		String pk = billId;
		if(pk != null){
			vo.setPrimaryKey(pk);
		}
		try {
			SuperVO result = queryVO(vo);
			if(result != null){
				new SuperVO2DatasetSerializer().serialize(new SuperVO[]{result}, ds, Row.STATE_NORMAL);
			}
			postProcessRowSelect(ds);
		} 
		catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException("��ѯ�������," + e.getMessage() + ",ds id:" + ds.getId(), "��ѯ���̳��ִ���");
		}
	}
	/**
	 * ����ִ�в�ѯ�����ڱ�Ҫʱ��д�˷���
	 * @param pinfo
	 * @param vo
	 * @param wherePart
	 * @return
	 * @throws LfwBusinessException
	 */
	protected SuperVO queryVO(SuperVO vo) throws LfwBusinessException {
		SuperVO[] vos = CRUDHelper.getCRUDService().queryVOs(vo, null, null);
		return (vos != null && vos.length == 1) ? vos[0] : null;
	}


	/**
	 * ���ò�ѯ����ѡ�У�������治������Ĭ��ѡ���踲��,Ĭ�ϲ�ѡ��
	 * @param ds
	 */
	protected void postProcessRowSelect(Dataset ds) {
		if(ds.getCurrentRowCount() > 0){
			ds.setRowSelectIndex(0);
		}
		ds.setEnabled(false);
	}
}
