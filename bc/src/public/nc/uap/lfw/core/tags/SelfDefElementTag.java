package nc.uap.lfw.core.tags;

import javax.servlet.jsp.tagext.JspTag;

import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ���Զ���Ԫ����Ⱦtag,��ele�������οؼ�,
 * @author gd 2008-01-10
 */
public class SelfDefElementTag extends ContainerElementTag {
	
	private FormComp formComp = null;
	
	public String generateHead() {
		// �����жϸ�selfDefEle�Ƿ���form����ж����,�����������jsp������
//		FormElement sefEle = (FormElement)getFormComp().getElementById(getId());
//		List<FormElement> list = getFormComp().getElementList();
//		if(list.indexOf(sefEle) == -1)
//			throw new LfwRuntimeException(this.getClass().getName() + ":can not find the component by id:" + getId());
//		
//		StringBuffer buf = new StringBuffer();
//		buf.append("<div id=\"")
//		   .append(getDivShowId())
//		   .append("\" style=\"width:")
//		   .append(sefEle.getWidth())
//		   .append(";height:")
//		   .append(sefEle.getHeight())
//		   .append(";top:")
//		   .append(sefEle.getTop())
//		   .append(";left:")
//		   .append(sefEle.getLeft())
//		   .append(";position:")
//		   .append(sefEle.getPosition())
//		   .append(";\">");
//		return buf.toString();
		return super.generateHead();
	}
	
	public String generateHeadScript() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ele = getComponent('" + getFormComp().getId() + "').getElement('" + this.getId() + "');ele.Div_gen.appendChild(")
		   .append("document.getElementById('")
		   .append(getDivShowId())
		   .append("'));\n");
		   
		return buf.toString();
	}
	
	public String generateTail() {
		return "</div>";
	}
	
	private FormComp getFormComp()
	{
		if(formComp == null)
		{
			JspTag parentTag = findAncestorWithClass(this, FormCompTag.class);
			if(parentTag == null)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "SelDefElement-000000", null, new String[]{getId()})/*�Զ���Ԫ��{0}����formԪ�ص�ֱ����Ԫ��,jspҳ��д������!*/);
			
			FormCompTag formTag = (FormCompTag)parentTag;
			// ��ȡform�����
			//formComp = (FormComp)getCurrWidget().getViewComponents().getComponent(formTag.getId());
		}
		return formComp;
	}
}
