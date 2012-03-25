package nc.uap.lfw.core.refnode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ���սڵ�֮��Ĺ�ϵ�������󡣸��ݴ������������ṩ����й����Ĳ��յ�ͳһ�������õ����⡣���磬��˾���ձ仯������
 * ������Ҫ�仯����֮����Ҫһ��������������ʽ��
 * 
 * @author dengjt
 *
 */
public class RefNodeRelation implements Cloneable,Serializable{
	private static final long serialVersionUID = 1L;
	
	/*RefNodeRelation�ı�ʶ*/
	private String id;
	
	/*���ֶ���Ϣ*/
//	private MasterFieldInfo[] masterFieldInfos;
	
	private List<MasterFieldInfo> masterFieldInfosList = null;
	
	public void addMasterFieldInfo(MasterFieldInfo masterFieldInfo){
		if(masterFieldInfosList == null)
			masterFieldInfosList = new ArrayList<MasterFieldInfo>();
		masterFieldInfosList.add(masterFieldInfo);
	}
	
	public void removeMasterFieldInfo(String dsId, String fieldId){
		if(masterFieldInfosList != null){
			for (int i = 0; i < masterFieldInfosList.size(); i++) {
				MasterFieldInfo masterFieldInfo = masterFieldInfosList.get(i);
				if(masterFieldInfo.getDsId() != null && masterFieldInfo.getDsId().equals(dsId) 
						&& masterFieldInfo.getFieldId() != null && masterFieldInfo.getFieldId().equals(fieldId)){
					masterFieldInfosList.remove(masterFieldInfo);
				}
			}
		}
	}
	
	public MasterFieldInfo getMasterFieldInfo(String dsId, String fieldId){
		if(masterFieldInfosList != null){
			for (int i = 0; i < masterFieldInfosList.size(); i++) {
				MasterFieldInfo masterFieldInfo = masterFieldInfosList.get(i);
				if(masterFieldInfo.getDsId() != null && masterFieldInfo.getDsId().equals(dsId) 
						&& masterFieldInfo.getFieldId() != null && masterFieldInfo.getFieldId().equals(fieldId)){
					return masterFieldInfo;
				}
			}
		}
		return null;
	}
	
	
	/*��������*/
	private String detailRefNode;
	
	private boolean[] join; 
	/*��Ӱ���Ds�����ڴ�����Ӱ���ds����ͬһ��ʱ������Ҫ���ã�*/
	private String targetDs = null;
	
	//�Ƿ����
	private boolean clearDetail = true;
	
	public String getDetailRefNode() {
		return detailRefNode;
	}

	public void setDetailRefNode(String detailRefNode) {
		this.detailRefNode = detailRefNode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}

	public String getTargetDs() {
		return targetDs;
	}

	public void setTargetDs(String targetDs) {
		this.targetDs = targetDs;
	}

	public boolean isClearDetail() {
		return clearDetail;
	}

	public void setClearDetail(boolean clearDetail) {
		this.clearDetail = clearDetail;
	}

	public List<MasterFieldInfo> getMasterFieldInfos() {
		return masterFieldInfosList;
	}

	public void setMasterFieldInfos(List<MasterFieldInfo> masterFieldInfos) {
		this.masterFieldInfosList = masterFieldInfos;
	}
}

