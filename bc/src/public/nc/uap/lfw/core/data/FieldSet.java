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
 * �ֶμ�����Ϣ������Ϊ<code>DataSet</code>��Meta��Ϣ���ڡ� ����ÿ��Field��DataSet��һ���ֶ������� 
 *
 * create on 2007-2-6 ����10:27:21
 * 
 * @author lkp
 * 
 */

public class FieldSet implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	private List<Field> fieldList = new ArrayList<Field>();
	private Dataset dataSet;
	
	

	/**
	 * ��ȡָ��λ�õ��ֶ���Ϣ
	 * 
	 * @param index
	 * @return
	 */
	public Field getField(int index) {

		return fieldList.get(index);
	}

	/**
	 * ��ȡָ����ʶ��Id�����ֶ���Ϣ
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
			//������ڲ�����õ���������޷�������־���ߡ�
			catch(Throwable e){
				throw new LfwRuntimeException(e.getMessage());
			}
		}
		
		return null;
	}

	/**
	 * ���һ���ֶ���Ϣ
	 * 
	 * @param field
	 */
	public void addField(Field field) {
		fieldList.add(field);
		this.notifyChange(field);
	}

	/**
	 * ɾ��ĳ���ֶζ���
	 * 
	 * @param field
	 */
	public void removeField(Field field) {

		fieldList.remove(field);
	}

	/**
	 * ��ȡ��ǰ���е��ֶ���Ŀ
	 * 
	 * @return
	 */
	public int getFieldCount() {

		return fieldList.size();
	}

	/**
	 * ��ȡָ��λ�õ��ֶ�����
	 * ��������ڣ��򷵻�null
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
	 * ��ȡָ�����Ƶ��ֶ�����λ�á�
	 * ����������򷵻�-1��
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
	 * ͨ��Field��id��ȡ��field����
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
