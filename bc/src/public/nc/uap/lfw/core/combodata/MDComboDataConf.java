package nc.uap.lfw.core.combodata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.md.MDBaseQueryFacade;
import nc.md.model.IEnumValue;
import nc.md.model.type.IType;
import nc.md.model.type.impl.EnumType;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.ui.pub.beans.constenum.IConstEnum;

public class MDComboDataConf extends ComboData{
	private static final long serialVersionUID = 1L;
	private String fullclassName;
	private List<CombItem> listCombItem;
	
	public String getFullclassName() {
		return fullclassName;
	}

	public void setFullclassName(String fullclassName) {
		this.fullclassName = fullclassName;
	}

	@Override
	public CombItem[] getAllCombItems() {
		if(this.listCombItem == null){
			if(this.fullclassName == null)
				return null;
			try {
				IType type = MDBaseQueryFacade.getInstance().getTypeByID(fullclassName, IType.STYLE_SINGLE);
				listCombItem = new ArrayList<CombItem>();
				if(type != null){
					EnumType enumtype = (EnumType) type;
					//优先自定义提供者
					if(enumtype.getCustomEnumValueFactory() != null){
						IConstEnum[] ems = enumtype.getCustomEnumValueFactory().getAllConstEnums();
						if(ems != null && ems.length > 0){
							for (int i = 0; i < ems.length; i++) {
								IConstEnum ev = ems[i];
								CombItem item = new CombItem();
								item.setText(ev.getName());
								Object evObj = ev.getValue();
								item.setValue(evObj == null ? null : evObj.toString());
								//item.setI18nName(ev.);
								listCombItem.add(item);
							}
						}
					}
					else{
						List<IEnumValue> em = enumtype.getEnumValues();
						if(em != null){
							Iterator<IEnumValue> it = em.iterator();
							while(it.hasNext()){
								IEnumValue ev = it.next();
								CombItem item = new CombItem();
								item.setText(ev.getName());
								item.setValue(ev.getValue());
								item.setI18nName(ev.getResID());
								listCombItem.add(item);
							}
						}
					}
				}
				return listCombItem.toArray(new CombItem[0]);
				
			} catch (Exception e) {
				throw new LfwRuntimeException(e.getMessage(), e);
			}
		}
		return this.listCombItem.toArray(new CombItem[0]);
	}
	
	public void removeComboItem(String itemId)
	{
		if(itemId == null)
			return;
		if(this.listCombItem == null)
			getAllCombItems();
		Iterator<CombItem> it = this.listCombItem.iterator();
		while(it.hasNext()){
			CombItem combo = it.next();
			if(combo.getValue() != null && combo.getValue().equals(itemId)){
				it.remove();
				super.removeComboItem(itemId);
				break;
			}
		}
	}

	@Override
	public void removeAllComboItems() {
		if(listCombItem == null){
			listCombItem = new ArrayList<CombItem>();
			return;
		}
		listCombItem.clear();
		super.removeAllComboItems();
	}

	@Override
	public void addCombItem(CombItem item) {
		if(this.listCombItem == null)
			getAllCombItems();
		this.listCombItem.add(item);
		super.addCombItem(item);
	}
	public Object clone()
	{
		MDComboDataConf combo = (MDComboDataConf) super.clone();
		if(this.listCombItem != null){
			combo.listCombItem = new ArrayList<CombItem>();
			for(CombItem item : this.listCombItem)
			{
				combo.listCombItem.add((CombItem)item.clone());
			}
		}
		return combo;
	}

}

