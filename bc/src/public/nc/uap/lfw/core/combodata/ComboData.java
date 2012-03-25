package nc.uap.lfw.core.combodata;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WidgetElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.jsp.uimeta.UIElement;

/**
 * �ۺ�����.ʵ����ֻ���ڵ���getAllCombItemsʱ������ȷ�����ݼ���
 * @author dengjt
 *
 */
public abstract class ComboData extends WidgetElement{
	private String caption;
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	private static final long serialVersionUID = 6716603920828925202L;
	public abstract CombItem[] getAllCombItems();
	public void removeComboItem(String itemId){
		notifyComboChange();
	}
	public void removeAllComboItems(){
		notifyComboChange();
	}
	public void addCombItem(CombItem item){
		//ÿ�ε����滻�������⣬����ֻʣ���һ�
		notifyComboChange();
	}
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	
	protected void notifyComboChange(){
		if(LifeCyclePhase.ajax.equals(getPhase())){			
			Map<String,Object> map = new HashMap<String,Object>();
			if(this.getWidget() != null){
				String widgetId = this.getWidget().getId();
				map.put("widgetId", widgetId);
				map.put("comboDataId", this.getId());
				map.put("type", "comboChange");
				this.getWidget().notifyChange(UIElement.UPDATE, map);
			}
		}
	}
}
