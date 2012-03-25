package nc.uap.lfw.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 输出描述器，一个输出至少包含一个出发时机，可包含0到多个参数
 * @author dengjt
 *
 */
public class PlugoutDesc implements Serializable, Cloneable {
	private static final long serialVersionUID = 6615952170085829290L;
	//普通类型
	public static String PLUGOUTNORMAL = "normal";
	
	//widget弹出类型
	public static String PLUGOUTWIDGET = "widget";

	//plugout类型
	private String plugoutType = PLUGOUTNORMAL;
	
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
			return plugoutDesc;
		} catch (CloneNotSupportedException e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
}
