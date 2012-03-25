package nc.uap.lfw.core.refnode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 参照节点之间的关系描述对象。根据此描述，可以提供针对有关联的参照的统一条件设置的问题。比如，公司参照变化，部门
 * 的数据要变化。这之间需要一个公共的描述方式。
 * 
 * @author dengjt
 *
 */
public class RefNodeRelation implements Cloneable,Serializable{
	private static final long serialVersionUID = 1L;
	
	/*RefNodeRelation的标识*/
	private String id;
	
	/*主字段信息*/
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
	
	
	/*从属参照*/
	private String detailRefNode;
	
	private boolean[] join; 
	/*被影响的Ds（仅在触发和影响的ds不是同一个时，才需要设置）*/
	private String targetDs = null;
	
	//是否清空
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

