package nc.uap.lfw.core.cmd;
import java.util.ArrayList;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.crud.ILfwCRUDService;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.Datasets2AggVOSerializer;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
public class UifDelCmd extends UifCommand {
	private String masterDsId;
	private String aggVoClazz;
	public UifDelCmd(String masterDsId, String aggVoClazz) {
		this.masterDsId = masterDsId;
		this.aggVoClazz = aggVoClazz;
	}
	public void execute() {
		AppInteractionUtil.showConfirmDialog("ȷ�϶Ի���", "ȷ��ɾ����?");
		if (!AppInteractionUtil.getConfirmDialogResult().booleanValue())
			return;
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		if (widget == null)
			throw new LfwRuntimeException("Ƭ��Ϊ��!");
		if (this.masterDsId == null)
			throw new LfwRuntimeException("δָ�����ݼ�id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LfwRuntimeException("���ݼ�Ϊ��,���ݼ�id=" + masterDsId + "!");
		// δָ��aggVo,���ds���õ�Ԫ�����л��
		// Ҫɾ��������������
		Row[] delRows = masterDs.getSelectedRows();
		if (delRows != null && delRows.length > 0) {
			ArrayList<Dataset> detailDss = null;
			DatasetRelation[] rels = null;
			if (widget.getViewModels().getDsrelations() != null) {
				rels = widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			}
			if (rels != null) {
				// �����ӱ�
				detailDss = new ArrayList<Dataset>();
				for (int i = 0; i < rels.length; i++) {
					int index = masterDs.getFieldSet().nameToIndex(rels[i].getMasterKeyField());
					Dataset detailDs = widget.getViewModels().getDataset(rels[i].getDetailDataset());
					detailDs.setExtendAttribute("parent_index", index);
					if (detailDs != null) {
						detailDss.add(detailDs);
					}
				}
			}
			for (int i = 0; i < delRows.length; i++) {
				ArrayList<AggregatedValueObject> vos = new ArrayList<AggregatedValueObject>();
				Row masterRow = delRows[i];
				// �־û�
				Datasets2AggVOSerializer ser = new Datasets2AggVOSerializer();
				vos.add(ser.serialize(masterDs, masterRow, detailDss == null ? null : detailDss.toArray(new Dataset[0]), aggVoClazz));
				onDeleteVO(vos, true);
				// ɾ����������
				masterDs.removeRow(masterDs.getRowIndex(delRows[i]));
				if (detailDss != null) {
					int size = detailDss.size();
					if (size > 0) {
						for (int j = 0; j < size; j++) {
							Dataset detailDs = detailDss.get(j);
							// ɾ���ӱ�����
							for (int k = 0; k < delRows.length; k++) {
								Integer index = (Integer) detailDs.getExtendAttributeValue("parent_index");
								// �Ƴ��ӱ��Ӧ������Ƭ��
								if (delRows[i].getValue(index) != null)
									detailDs.removeRowSet((String) delRows[i].getValue(index));
							}
						}
					}
				}
			}
		}
		updateButtons();
	}
	protected void onDeleteVO(ArrayList<AggregatedValueObject> vos, boolean trueDel) {
		try {
			getCrudService().deleteVo(vos.get(0), trueDel);
		} catch (LfwBusinessException e) {
			Logger.error(e, e);
			throw new LfwRuntimeException(e);
		}
	}
	protected ILfwCRUDService<SuperVO, AggregatedValueObject> getCrudService() {
		return CRUDHelper.getCRUDService();
	}
}
