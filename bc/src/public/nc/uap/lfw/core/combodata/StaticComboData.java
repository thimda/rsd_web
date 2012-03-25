package nc.uap.lfw.core.combodata;

import java.util.ArrayList;
import java.util.List;


/**
 * ¾²Ì¬¾ÛºÏÊý¾Ý
 * @author dengjt
 *
 */
public class StaticComboData  extends ComboData{
	private static final long serialVersionUID = 960819024151735416L;
	private List<CombItem> listCombItem;

	public CombItem[] getAllCombItems() {
		if(listCombItem!=null && !listCombItem.isEmpty())
			return listCombItem.toArray(new CombItem[0]);
		return null;
	}
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
		StaticComboData combo = (StaticComboData) super.clone();
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
}
