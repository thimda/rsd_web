package nc.uap.lfw.jsutil.jstools.vo;

import java.util.ArrayList;

public class CompGroup {
	private String name;
	private String path;
	private ArrayList<CompItem> itemList;
	private boolean checked;
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public ArrayList<CompItem> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<CompItem> items) {
		this.itemList = items;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public void addItem(CompItem item)
	{
		if(itemList == null)
			itemList = new ArrayList<CompItem>();
		itemList.add(item);
	}
}
