package nc.uap.lfw.core.data;

/**
 * �������Թ������е�ds
 * @author gd 2010-3-3
 *
 */
public class PubDataset extends Dataset{
	private static final long serialVersionUID = -3950557889734697521L;
	private String refId = null;
	// ���п��Ը������ֶ�
	private String[] canModifyFieldIds = {} ;
	
	public String[] getCanModifyFieldIds() {
		return canModifyFieldIds;
	}
	
	public void setCanModifyFieldIds(String[] fieldIds)
	{
		this.canModifyFieldIds = fieldIds;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
}
