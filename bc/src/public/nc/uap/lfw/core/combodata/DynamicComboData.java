package nc.uap.lfw.core.combodata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dengjt
 * 
 * 注：原继承自DynamicComboData的类，实现的是getAllCombItems方法要改为现在实现的createCombItems方法
 * 
 */
public abstract class DynamicComboData extends ComboData{
	private static final long serialVersionUID = -8178999031545378833L;
	private List<CombItem> listCombItem;

	public CombItem[] getAllCombItems() {
		if(listCombItem == null){
			CombItem[] items = createCombItems();
			if (items == null) return null;
			listCombItem = new ArrayList<CombItem>();
			for (int i = 0; i < items.length; i++) {
				listCombItem.add(items[i]);
			}
		}
		if(listCombItem!=null && !listCombItem.isEmpty())
			return listCombItem.toArray(new CombItem[0]);
		else 
			return null;
	}
	
	protected abstract CombItem[] createCombItems(); 
	
	public void addCombItem(CombItem item) {
		if(item==null)
			return;
		if(listCombItem==null)
			listCombItem=new ArrayList<CombItem>();
		listCombItem.add(item);
		super.addCombItem(item);
	}
	
	public void removeComboItem(String itemId)
	{
		if(itemId == null)
			return;
		if(this.listCombItem != null)
		{
			for(int i = 0; i < this.listCombItem.size(); i++)
			{
				if(this.listCombItem.get(i) instanceof CombItem)
				{
					CombItem combo = (CombItem)this.listCombItem.get(i);
					if(combo.getValue() != null && combo.getValue().equals(itemId))
						this.listCombItem.remove(i);
				}
			}
		}
		super.removeComboItem(itemId);
	}
	
	public Object clone()
	{
		DynamicComboData combo = (DynamicComboData) super.clone();
		if(this.listCombItem != null){
			combo.listCombItem = new ArrayList<CombItem>();
			for(CombItem item : this.listCombItem)
			{
				combo.listCombItem.add((CombItem)item.clone());
			}
		}
		return combo;
	}
	@Override
	public void removeAllComboItems() {
		if(this.listCombItem != null){
			this.listCombItem.clear();
		}
		super.removeAllComboItems();
	}
	
	public void reLoadComboItems(){
		if (listCombItem == null) 
			listCombItem = new ArrayList<CombItem>();
		else
			this.listCombItem.clear();
		CombItem[] items = createCombItems();
		if (items == null) return;
		for (int i = 0; i < items.length; i++) {
			listCombItem.add(items[i]);
		}
		super.notifyComboChange();
	}
}
