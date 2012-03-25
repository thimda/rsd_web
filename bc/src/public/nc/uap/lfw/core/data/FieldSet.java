package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.jsp.uimeta.UIElement;

/**
 * 字段集合信息，它作为<code>DataSet</code>的Meta信息存在。 其中每个Field是DataSet的一个字段描述。 
 *
 * create on 2007-2-6 上午10:27:21
 * 
 * @author lkp
 * 
 */

public class FieldSet implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	private List<Field> fieldList = new ArrayList<Field>();
	private Dataset dataSet;
	
	

	/**
	 * 获取指定位置的字段信息
	 * 
	 * @param index
	 * @return
	 */
	public Field getField(int index) {

		return fieldList.get(index);
	}

	/**
	 * 获取指定标识（Id）的字段信息
	 * 
	 * @param s
	 * @return
	 */
	public Field getField(String id) {

		for (int i = 0; i < fieldList.size(); i++) {

			if (fieldList.get(i).getId().trim().equals(id))
				return fieldList.get(i);
		}
		return null;
	}
	
	public Field[] getFields()
	{
		if(fieldList == null || fieldList.size() == 0)
			return null;
		else
			return fieldList.toArray(new Field[0]);
	}
	

	
	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}
	
	public Object notifyChange(Field field){
		
		if(LifeCyclePhase.ajax.equals(getPhase())){			
			try{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("datasetId", this.getDataSet().getId());
				map.put("field", field);
				map.put("type", "adjustField");
				map.put("widgetId", this.getDataSet().getWidget().getId());
				this.getDataSet().getWidget().notifyChange(UIElement.UPDATE, map);
//				String widgetId = this.getDataSet().getWidget().getId();
//				UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
//				UIElement widget = null;//uiMeta.findChildById(widgetId);
//				Class c = Class.forName("nc.uap.lfw.ra.render.observer.UIWidgetObserver");
//				Class c = Class.forName("nc.uap.lfw.ra.render.pc.PCWidgetRender");
//				Object instance = c.newInstance();
//				Method m = c.getMethod("notifyAddDsField", new Class[]{UIMeta.class, PageMeta.class, Object.class});
//				m.invoke(instance, new Object[]{null, null, map});
//				Method m = c.getMethod("notifyMethod", new Class[]{String.class, UIElement.class, Object.class});
//				return m.invoke(instance, new Object[]{"notifyAddDsField", widget, map});
			}
			//因此类在插件中用到，插件中无法包含日志工具。
			catch(Throwable e){
				throw new LfwRuntimeException(e.getMessage());
			}
		}
		
		return null;
	}

	/**
	 * 添加一个字段信息
	 * 
	 * @param field
	 */
	public void addField(Field field) {
		fieldList.add(field);
		this.notifyChange(field);
	}

	/**
	 * 删除某个字段对象
	 * 
	 * @param field
	 */
	public void removeField(Field field) {

		fieldList.remove(field);
	}

	/**
	 * 获取当前具有的字段数目
	 * 
	 * @return
	 */
	public int getFieldCount() {

		return fieldList.size();
	}

	/**
	 * 获取指定位置的字段名称
	 * 如果不存在，则返回null
	 * 
	 * @param index
	 * @return
	 */
	public String indexToName(int index) {

		Field field = fieldList.get(index);
		if (field != null)
			return field.getId();
		else
			return null;
	}

	/**
	 * 获取指定名称的字段所在位置。
	 * 如果不存在则返回-1。
	 * 
	 * @return
	 */
	public int nameToIndex(String id) {
		int size = fieldList.size();
		for (int i = 0; i < size; i++) {
			if (fieldList.get(i).getId().trim().equals(id))
				return i;
		}
		return -1;
	}
	
	public int fieldToIndex(String field) {
		int size = fieldList.size();
		for (int i = 0; i < size; i++) {
			if (field.equals(fieldList.get(i).getField().trim()))
				return i;
		}
		return -1;
	}
	
	/**
	 * 通过Field的id获取其field属性
	 * @param id
	 * @return
	 */
	public String getFieldById(String id)
	{
		Field field = getField(id);
		if(field != null)
			return field.getField();
		else
			return null;
		
	}
	
	public Object clone()
	{
		try {
			FieldSet fs = (FieldSet)super.clone();
			fs.fieldList = new ArrayList<Field>();
			for(int i = 0;i < this.getFieldCount(); i++)
			{
				fs.fieldList.add((Field)getField(i).clone());
			}
			return fs;
		} catch (CloneNotSupportedException e) {
			LfwLogger.error(e.getMessage(), e);;
		}		
		return null;
	}

	public Dataset getDataSet() {
		return dataSet;
	}

	public void setDataSet(Dataset dataSet) {
		this.dataSet = dataSet;
	}

	public List<Field> getFieldList() {
		return fieldList;
	}

}
