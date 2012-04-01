package nc.uap.lfw.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * �����������һ��������ٰ���һ������ʱ�����ɰ���0���������
 * @author dengjt
 *
 */
public class PlugoutDesc implements Serializable, Cloneable {
	private static final long serialVersionUID = 6615952170085829290L;
	//��ͨ����
	public static String PLUGOUTNORMAL = "normal";
	
	//widget��������
	public static String PLUGOUTWIDGET = "widget";

	//plugout����
	private String plugoutType = PLUGOUTNORMAL;
	
	//submitRule
	private EventSubmitRule submitRule = null;
	
	
	public String getPlugoutType() {
		return plugoutType;
	}
	public void setPlugoutType(String plugoutType) {
		this.plugoutType = plugoutType;
	}
	private List<PlugoutDescItem> descItemList;
	private List<PlugoutEmitItem> emitList;
	private String id;
	public List<PlugoutDescItem> getDescItemList() {
		return descItemList;
	}
	public void setDescItemList(List<PlugoutDescItem> descItemList) {
		this.descItemList = descItemList;
	}
	
	public void addDescItem(PlugoutDescItem descItem){
		if(descItemList == null)
			descItemList = new ArrayList<PlugoutDescItem>();
		this.descItemList.add(descItem);
	}
	
	public List<PlugoutEmitItem> getEmitList() {
		return emitList;
	}
	public void setEmitList(List<PlugoutEmitItem> emitList) {
		this.emitList = emitList;
	}
	
	public void addEmitItem(PlugoutEmitItem item) {
		if(emitList == null)
			emitList = new ArrayList<PlugoutEmitItem>();
		emitList.add(item);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public EventSubmitRule getSubmitRule() {
		return submitRule;
	}
	public void setSubmitRule(EventSubmitRule submitRule) {
		this.submitRule = submitRule;
	}
	public Object clone(){
		try {
			PlugoutDesc plugoutDesc = (PlugoutDesc)super.clone();
			plugoutDesc.descItemList = new ArrayList<PlugoutDescItem>();
			plugoutDesc.emitList = new ArrayList<PlugoutEmitItem>();
			
			if (this.descItemList != null){
				for (PlugoutDescItem item : this.descItemList){
					plugoutDesc.addDescItem(item);
				}
			}
			if (this.emitList != null){
				for (PlugoutEmitItem item : this.emitList){
					plugoutDesc.addEmitItem(item);
				}
			}
			if (this.submitRule != null){
				plugoutDesc.submitRule = (EventSubmitRule)this.submitRule.clone(); 
			}
			return plugoutDesc;
		} catch (CloneNotSupportedException e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
}
