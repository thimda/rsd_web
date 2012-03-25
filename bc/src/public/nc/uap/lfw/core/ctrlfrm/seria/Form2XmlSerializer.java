package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Form2XmlSerializer extends BaseSerializer<FormComp> implements IViewZone{

	@Override
	public void deSerialize(FormComp form, Document doc, Element parentNode) {
		Element formNode = null;
		if(form.getFrom() == null){
			formNode = doc.createElement("FormComp");
			parentNode.appendChild(formNode);
			formNode.setAttribute("id", form.getId());
			formNode.setAttribute("dataset", form.getDataset());
//			if(isNotNullString(form.getWidth()))
//				formNode.setAttribute("width", form.getWidth());
//			if(isNotNullString(form.getPosition()))
//				formNode.setAttribute("position", form.getPosition());
//			if(isNotNullString(form.getTop()))
//				formNode.setAttribute("top", form.getTop());
//			if(isNotNullString(form.getLeft()))
//				formNode.setAttribute("left", form.getLeft());
			if(isNotNullString(form.getCaption()))
				formNode.setAttribute("caption", form.getCaption());
			formNode.setAttribute("rowHeight", String.valueOf(form.getRowHeight()));
			formNode.setAttribute("eleWidth", String.valueOf(form.getEleWidth()));
			if(form.getColumnCount() != null)
				formNode.setAttribute("columnCount", String.valueOf(form.getColumnCount()));
			if(form.getRenderType() > 0)
				formNode.setAttribute("renderType", String.valueOf(form.getRenderType()));
			if(isNotNullString(form.getContextMenu()))
				formNode.setAttribute("contextMenu", form.getContextMenu());
//			if(isNotNullString(form.getClassName()))
//				formNode.setAttribute("className", form.getClassName());
			if(isNotNullString(form.getBackgroundColor()))
				formNode.setAttribute("backgroundColor", form.getBackgroundColor());
			formNode.setAttribute("labelMinWidth", String.valueOf(form.getLabelMinWidth()));
			formNode.setAttribute("enabled", String.valueOf(form.isEnabled()));
			formNode.setAttribute("visible", String.valueOf(form.isVisible()));
			formNode.setAttribute("renderHiddenEle", String.valueOf(form.isRenderHiddenEle()));
			
			List<FormElement> eles = form.getElementList();
			if(eles != null && eles.size() > 0)
			{
				for(int j = 0; j < eles.size(); j++)
				{
					FormElement ele = eles.get(j);
					Element formEleNode = doc.createElement("Element");
					formNode.appendChild(formEleNode);
					
					formEleNode.setAttribute("id", ele.getId());
					if(isNotNullString(ele.getField()))
						formEleNode.setAttribute("field", ele.getField());
					if(ele.getText() != null)
						formEleNode.setAttribute("text", ele.getText());
					if(isNotNullString(ele.getI18nName()))
						formEleNode.setAttribute("i18nName", ele.getI18nName());
					if(isNotNullString(ele.getDescription()))
						formEleNode.setAttribute("description", ele.getDescription());
					if(isNotNullString(ele.getLangDir()))
						formEleNode.setAttribute("langDir", ele.getLangDir());
					if(isNotNullString(ele.getEditorType()))
						formEleNode.setAttribute("editorType", ele.getEditorType());
					if(isNotNullString(ele.getDataType()))
						formEleNode.setAttribute("dataType", ele.getDataType());
					if(isNotNullString(ele.getRefNode()))
						formEleNode.setAttribute("refNode", ele.getRefNode());
					if(isNotNullString(ele.getRefComboData()))
						formEleNode.setAttribute("refComboData", ele.getRefComboData());
					if(isNotNullString(ele.getLabelColor()))
						formEleNode.setAttribute("labelColor", ele.getLabelColor());
					if(isNotNullString(ele.getDefaultValue()))
						formEleNode.setAttribute("defaultValue", ele.getDefaultValue());
					if(isNotNullString(ele.getDataDivHeight()))
						formEleNode.setAttribute("dataDivHeight", ele.getDataDivHeight());
					if(isNotNullString(ele.getRelationField()))
						formEleNode.setAttribute("relationField", ele.getRelationField());
					if(isNotNullString(ele.getMaxLength()))
						formEleNode.setAttribute("maxLength", ele.getMaxLength());
					formEleNode.setAttribute("editable", String.valueOf(ele.isEditable()));
					formEleNode.setAttribute("enabled", String.valueOf(ele.isEnabled()));
					formEleNode.setAttribute("visible", String.valueOf(ele.isVisible()));
					formEleNode.setAttribute("nextLine", String.valueOf(ele.isNextLine()));
					formEleNode.setAttribute("rowSpan", String.valueOf(ele.getRowSpan()));
					formEleNode.setAttribute("colSpan", String.valueOf(ele.getColSpan()));	
					formEleNode.setAttribute("visible", String.valueOf(ele.isVisible()));
					formEleNode.setAttribute("imageOnly", String.valueOf(ele.isImageOnly()));
					formEleNode.setAttribute("selectOnly", String.valueOf(ele.isSelectOnly()));
					formEleNode.setAttribute("nullAble", String.valueOf(ele.isNullAble()));
					formEleNode.setAttribute("attachNext", String.valueOf(ele.isAttachNext()));
					formEleNode.setAttribute("width", ele.getWidth());
					formEleNode.setAttribute("height", ele.getHeight());
					if(isNotNullString(ele.getInputAssistant()))
						formEleNode.setAttribute("inputAssistant", ele.getInputAssistant());
					if(isNotNullString(ele.getMaxValue()))
						formEleNode.setAttribute("maxValue", ele.getMaxValue());
					if(isNotNullString(ele.getBindId()))
						formEleNode.setAttribute("bindId", ele.getBindId());
					if(isNotNullString(ele.getMinValue()))
						formEleNode.setAttribute("minValue", ele.getMinValue());
					if(isNotNullString(ele.getPrecision()))
						formEleNode.setAttribute("precision", ele.getPrecision());
					if(isNotNullString(ele.getHideBarIndices()))
						formEleNode.setAttribute("hideBarIndices", ele.getHideBarIndices());
					if(isNotNullString(ele.getHideImageIndices()))
						formEleNode.setAttribute("hideImageIndices", ele.getHideImageIndices());
					if(isNotNullString(ele.getTip()))
						formEleNode.setAttribute("tip", ele.getTip());
					if(isNotNullString(ele.getPrecision()))
						formEleNode.setAttribute("precision", ele.getPrecision());
					if(isNotNullString(ele.getSizeLimit()))
						formEleNode.setAttribute("sizeLimit", ele.getSizeLimit());
				}
			}
			Map<String, JsListenerConf> jsListeners = form.getListenerMap();
			if(jsListeners != null && jsListeners.size() > 0)
				PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), formNode);
			
			//Events
			AMCUtil.addEvents(doc, form.getEventConfs(), formNode);
		}
		else{
			Map<String, JsListenerConf> jsListeners = form.getListenerMap();
			boolean saveFlag = false;
			for (Iterator<String> itwd = jsListeners.keySet().iterator(); itwd.hasNext();) {
				String listenerId = (String) itwd.next();
				JsListenerConf listener = jsListeners.get(listenerId);
				if(listener.getFrom() == null){
					saveFlag = true;
					break;
				}
				else if(listener.getConfType().equals(WebElement.CONF_DEL)){
					saveFlag = true;
					break;
				}
			}
			if(saveFlag){
				if(jsListeners != null && jsListeners.size() > 0){
					formNode = doc.createElement("FormComp");
					parentNode.appendChild(formNode);
					formNode.setAttribute("id", form.getId());
					if(isNotNullString(form.getConfType()))
						formNode.setAttribute("confType", form.getConfType());
					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), formNode);
					
					//Events
					//AMCUtil.addEvents(doc, form.getEventConfs(), formNode);
				}
			}
		}
	}

	@Override
	public void serialize(Digester digester) {
		String autoFormClassName = FormComp.class.getName();
		//FormComp解析
		digester.addObjectCreate("Widget/Components/FormComp", autoFormClassName);
		digester.addSetProperties("Widget/Components/FormComp");
		
		//兼容AutoForm的解析
		digester.addObjectCreate("Widget/Components/AutoForm", autoFormClassName);
		digester.addSetProperties("Widget/Components/AutoForm");
		

		String elementClassName = FormElement.class.getName();
		//FormComp的Element，Event解析
		digester.addObjectCreate("Widget/Components/FormComp/Element",
				elementClassName);
		digester.addSetProperties("Widget/Components/FormComp/Element");
		digester.addSetNext("Widget/Components/FormComp/Element", "addElement",
				elementClassName);
		digester.addSetNext("Widget/Components/FormComp", "addComponent",
				autoFormClassName);

		ListenersParser.parseListeners(digester, "Widget/Components/FormComp/Listeners", FormComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/FormComp", FormComp.class);
	}

}
