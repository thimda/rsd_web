package nc.uap.lfw.core.tags;


import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import nc.uap.lfw.core.comp.WebComponent;
/**
 * ��ͨ�ؼ���ȾTag ����
 * @author dengjt
 *
 */
public abstract class NormalComponentTag extends WebComponentTag implements INormalComponentTag{

	private String left = "0";
	private String top = "0";
	private String width = "100%";
	private String height = "100%";
	private String position = "relative";
	private String className = "";
	public void doTag() throws JspException, IOException {
		super.doTag();
	}
	
	protected void doRender()  throws JspException, IOException
	{
		
		WebComponent comp = getComponent();
		if(comp.isRendered())
			return;
		
		getJspContext().setAttribute(getId(), comp);
		
		JspWriter out = getJspContext().getOut();
		
		// ����<div></div>�ؼ�ռλ��
		String bodyDiv = generateBody();
		if(bodyDiv != null && !bodyDiv.equals(""))
			out.write(bodyDiv);
		
		// generateBodyScript()����Ҫ���ǵķ���
		String script = generateBodyScript();
		addToBodyScript(script);
		
		// ��ӿؼ��¼���
		script = addEventSupport(comp, getCurrWidget() == null?null:getCurrWidget().getId(), getVarShowId(), null);
		addToBodyScript(script);
		
		if(comp.getContextMenu() != null && !comp.getContextMenu().equals("")){
			script = addContextMenu(comp.getContextMenu(), getVarShowId());
			addToBodyScript(script);
		}
		
		script = generateDsBindingScript();
		StringBuffer dsScript = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
		if(script != null)
			dsScript.append(script);
		
		comp.setRendered(true);
		
		//logger.info("component:" + getId() + " is  completely rendered");
	}
	
	
	/**
	 * ͳһ��Ⱦ�ؼ�ռλ��
	 * modified by guoweic: ����"px"
	 */
	public String generateBody() {
		StringBuffer buf = new StringBuffer();
		WebComponent comp = getComponent();
		
		String width = getWidth();
		String height = getHeight();
		buf.append("<div style=\"width:")
		   //.append(comp.getWidth())
		   .append(width.indexOf("%") != -1 ? width : (width + "px"))
		   .append(";height:")
		   //.append(comp.getHeight())
		   .append(height.indexOf("%") != -1 ? height : (height + "px"))
		   .append(";top:")
		   //.append(comp.getTop())
		   .append(getTop() + "px")
		   .append(";left:")
		   //.append(comp.getLeft())
		   .append(getLeft() + "px")
		   .append(";position:")
		   .append(getPosition())
		   .append(";overflow:hidden");
	    buf.append("\" id=\"")
	    	.append(getDivShowId())
	    	.append("\"></div>");
	
		return buf.toString();
	}
	
	public String getWidth(){
		return width;
	}
	
	public void setWidth(String width){
		this.width = width;
	}
	
	public String getHeight(){
		return height;
	}
	
	public void setHeight(String height){
		this.height = height;
	}
	
	protected String getTop(){
		return top;
	}
	
	protected String getLeft(){
		return left;
	}
	
	protected String getPosition() {
		return position;
	}
	
	/**
	 * �ṩ�����า�ǵķ���
	 */
	public String generateDsBindingScript(){
		return "";
	}

	/**
	 * ��ȡͼƬȫ·��
	 * @param refImg
	 * @return
	protected String getRealImgPath(String refImg) {
		if (refImg == null || refImg.trim().equals(""))
			refImg = "";
		else if (refImg.contains("${t}")) {
			String themeId = LfwRuntimeEnvironment.getThemeId();
			refImg = LfwRuntimeEnvironment.getLfwCtx() + refImg.replace("${t}", themeId);
		}
		else {
			if (null == nodeImgPath)
				nodeImgPath = (String) getPageContext().getRequest().getAttribute(WebConstant.NODE_IMAGE_PATH);
			if (!refImg.startsWith("/"))
				refImg = nodeImgPath + "/" + refImg;
			else
				refImg = LfwRuntimeEnvironment.getRootPath() + refImg;
		}
		return refImg;
	}
	 */

	/**
	 * ��ȡͼƬȫ·��
	 * @param refImg
	 * @return
	protected String getRealImgPath(String refImg) {
		WebComponent comp = getComponent();
		if (null == nodeImgPath)
			nodeImgPath = (String) getPageContext().getRequest().getAttribute(WebConstant.NODE_IMAGE_PATH);
		refImg = comp.getRealImgPath(refImg, nodeImgPath);
		return refImg;
	}
	 */
	
	/**
	 * ��ȡ��ȷ��λ���ַ���
	 * @param str
	 * @return
	 */
	protected String getFormatString(String str) {
		if (str.indexOf("%") != -1 || str.indexOf("px") != -1)
			return str;
		return str + "px";
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
