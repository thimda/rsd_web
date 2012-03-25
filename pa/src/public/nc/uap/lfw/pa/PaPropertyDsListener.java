/**
 * 
 */
package nc.uap.lfw.pa;

import java.lang.reflect.Method;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.MouseServerListener;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.pa.info.BaseInfo;
import nc.uap.lfw.pa.info.ComboPropertyInfo;
import nc.uap.lfw.pa.info.IPropertyInfo;
import nc.uap.lfw.pa.info.InfoCategory;
import nc.uap.lfw.pa.itf.IPaPersistService;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.lang.StringUtils;

/**
 * @author wupeng1
 * @version 6.0 2011-8-9
 * @since 1.6
 */
public class PaPropertyDsListener extends MouseServerListener {

	public PaPropertyDsListener(LfwPageContext pageCtx, String widgetId) {
		super(pageCtx, widgetId);
	}
	
	
	@Override
	public void onclick(MouseEvent event) {
		WebElement ele = (WebElement) event.getSource();
		String type = getElementType(ele);
		
		
		SuperVO vo = null;

		WebSession session = getGlobalContext().getParentGlobalContext().getWebSession();
		session = LfwRuntimeEnvironment.getWebContext().getParentSession();
		String pk_template = session.getOriginalParameter("pk_template");
		
		/**
		 * �õ���ǰҳ���settings Widget���Լ����ݼ��Ϳؼ�
		 */
		LfwWidget widget = getGlobalContext().getParentGlobalContext().getWidgetContext("settings").getWidget();
		
		Dataset dss = widget.getViewModels().getDataset("ds_middle");
		Field[] fss = dss.getFieldSet().getFields();
		
		
		FormComp form = (FormComp) widget.getViewComponents().getComponent("adhintform");
		RowSet dd = dss.getRowSet(Dataset.MASTER_KEY);
		if(dd == null)
			dss.setCurrentKey(Dataset.MASTER_KEY);
		
		//����ҳ��ؼ����ͣ�������ӦVO
		try {
			Class VOclazz = PaConstantMap.mappingTable.get(type);
			vo = (SuperVO)VOclazz.newInstance();
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}

		//�������ͣ��õ���Ӧ���Ե���Ϣ
		BaseInfo bi = InfoCategory.getInfo(type);
		
		IPropertyInfo[] pi = bi.getPropertyInfos();
		
		
		//��һ�α�������������FormElementΪ���ɼ�
		for(FormElement fc: form.getElementList()){
			fc.setVisible(false);
		}
		
		
		//�ڶ��α���������Property�е�ֵ����FormElement�Ŀɼ�
		for(int i = 0; i < pi.length; i++){
			
			IPropertyInfo pinfo = pi[i];
			
			if(pinfo.isVisible()){
				String filedName = pinfo.getDsField();
				FormElement fe = form.getElementById(filedName);
				ComboData scd = null;
				if(pinfo instanceof ComboPropertyInfo){
					scd = widget.getViewModels().getComboData(pinfo.getDsField());
					scd.removeAllComboItems();
					String[] keys = ((ComboPropertyInfo) pinfo).getKeys();
					String[] values = ((ComboPropertyInfo) pinfo).getValues();
					
					for(int j = 0; j < keys.length; j++){
						CombItem item = new CombItem();
						item.setText(keys[j]);
						item.setValue(values[j]);
						scd.addCombItem(item);
					}
				}
				
				fe.setLabel(pinfo.getLabel());
				fe.setVisible(true);
			}
		}
		
		//�����α���������ele�е�ֵ����vo��Ӧֵ
		for(int i = 0; i < pi.length; i++){
			IPropertyInfo pinfo = pi[i];
			Object value = getValue(ele, pinfo.getId());
			Object newValue = null;
			
			if(value instanceof Boolean)
				newValue = UFBoolean.valueOf((Boolean) value);
			else
				newValue = value;
			vo.setAttributeValue(pinfo.getVoField(), newValue);
		}
		//��ID����ΪΨһ����
		String uniqueId = widget.getId() + ele.getId();
		
	//	vo.setAttributeValue("comptype", type);
		vo.setAttributeValue("id", uniqueId);
		
		/**
		 * �����ݿ��еõ���Ӧ���ݲ���ֵ��vo
		 */
		IPaPersistService service = NCLocator.getInstance().lookup(IPaPersistService.class);
		
		String condition = "pk_parent = '" + pk_template + "' and id = '" + uniqueId +"'";
		SuperVO newVO = null;
		try {
			newVO = service.getCompVOByClause(vo.getClass(), condition);
		} catch (PaBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		if(newVO != null){
			vo = newVO;
		}
		
		
		//�������ݼ��е������Ƿ�Ϊ�����ģ�����Ϊ-1�����򷵻����ݼ��е�rowIndex
		int rowIndex = rowExist(dss, uniqueId);
		
		Row row = null;
		
		if(rowIndex == -1){
			row = dss.getEmptyRow();
			
			for(int i = 0; i < fss.length; i++){
				Field fdd = fss[i];
				String id = fdd.getId();
				FormElement formele = form.getElementById(id);
				
				if(!formele.isVisible() || formele == null)
					continue;
				
				Object value = null;
				
				for(int j=0; j<pi.length; j++)
					if(id.equals(pi[j].getDsField()))
						value = vo.getAttributeValue(pi[j].getVoField());
				
				row.setValue(i, value);
			}
			
			int typeIndex = dss.nameToIndex("comptype");
			int idIndex = dss.nameToIndex("string_ext1");
			row.setValue(idIndex, uniqueId);
			row.setValue(typeIndex, type);
			
			dss.addRow(row);
			rowIndex = dss.getRowCount() - 1;
			dss.setRowSelectIndex(rowIndex);
		}

		dss.setRowSelectIndex(rowIndex);

	}	
			
			



	private String getElementType(WebElement ele) {
		String type = null;
		/**
		 * ͨ�����յ���eleȷ������������ӦVO��
		 */
		if(ele instanceof ButtonComp){
			type = PaConstant.COMP_BUTTON;
		}
		else if(ele instanceof GridComp){
			type = PaConstant.COMP_GRID;
		}
		else if(ele instanceof FormComp){
			type = PaConstant.COMP_FORM;
		}
		else if(ele instanceof FormElement){
			type = PaConstant.COMP_FORMELE;
		}
		return type;
	}

	/**
	 * ��ds���ҳ��ؼ�������
	 */
	private int rowExist(Dataset ds, String uniqueId) {
		Row[] rows = ds.getCurrentRowData().getRows();
		
		LfwLogger.debug("---------"+rows.length);
		
		//�ӵ�ǰ�������ҳ��Ƿ�����ؼ���������
		int rowIndex = -1;
		int idIndex = ds.nameToIndex("string_ext1");
		
		if(rows != null && rows.length > 0){
			for(int i = 0; i < rows.length; i ++){
				Row r = rows[i];
				if(r.size() <= 0){
					continue;
				}
				String id = (String) r.getValue(idIndex);
				if(uniqueId.equals(id)){
					rowIndex = i;
					break;
				}
			}
		}
		return rowIndex;
	}


	public static final Object getValue(Object o , String fieldName){
		Method m = null;
		String upCaseFieldName = StringUtils.upperCase(fieldName.substring(0,1))+fieldName.substring(1);
		try {
			m = o.getClass().getMethod("get" + upCaseFieldName, null);
		} catch (SecurityException e) {
			LfwLogger.error(e.getMessage(),e);
		} catch (NoSuchMethodException e) {
			try {
				m = o.getClass().getMethod("is" + upCaseFieldName, null);
			} catch (SecurityException e1) {
				LfwLogger.error(e.getMessage(),e);
			} catch (NoSuchMethodException e1) {
				LfwLogger.error(e1.getMessage(),e1);
				throw new IllegalArgumentException(e1.getMessage());
			}
		}
		if(m != null){
			try {
				return m.invoke(o, null);
			} catch ( Exception e) {
				LfwLogger.error(e.getMessage(),e);
				throw new IllegalArgumentException(e.getMessage());
			} 
		}
		return null;
	}
}
