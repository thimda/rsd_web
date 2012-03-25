package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import nc.uap.lfw.core.comp.IDataBinding;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
/**
 * Web控件渲染Tag基类
 * @author dengjt
 *
 */
public abstract class WebComponentTag extends WebElementTag {
	private static final String BEFORE_SCRIPT = "beforeScript";
	private static final String AFTER_SCRIPT = "afterScript";
	protected static final String BEFORE_LOGIC_SCRIPT = "beforeLogicScript";
	//组件所用的外部css，此css根据组件的实现不同，不一定能被接受。
	private String styleClass = "";
	/**
	 * Tag类调用入口
	 */
	public void doTag() throws JspException, IOException {
		LfwWidget wd = getCurrWidget();
		if(wd == null)
			throw new LfwRuntimeException("WebComponentTag找不到指定的Widget,id = " + getWidget());
		if(!wd.isRendered()){
			WidgetTag wt = new WidgetTag();
			wt.setRenderDiv(false);
			wt.setId(wd.getId());
			wt.setJspContext(this.getJspContext());
			wt.doRender();
		}
		String script;
//		if(!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION)){
//			script = addBeforeJsLog();
//			addToBodyScript(script);
//		}
		script = (String) getAttribute(BEFORE_SCRIPT);
		if(script != null)
			addToBodyScript(script);
		
		super.doTag();
		
		script = (String) getAttribute(AFTER_SCRIPT);
		if(script != null)
		{	
			addToBodyScript(script);
		}
		
		// 加入widget
		addToBodyScript(getVarShowId() + ".widget = " + WIDGET_PRE + wd.getId() + ";\n");
		
//		if(!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION)){
//			script = addAfterJsLog();
//			addToBodyScript(script);
//		}
	}
	
//	/**
//	 * 为调试状态下添加log，以便很容易的检查哪个组件渲染出现问题
//	 *
//	 */
//	protected String addBeforeJsLog() {
//		return "log(\"the component " + getId() + " is to be executed\");\n";
//	}
//	
//	/**
//	 * 为调试状态下添加log，以便很容易的检查哪个组件渲染出现问题
//	 *
//	 */
//	protected String addAfterJsLog() {
//		return "log(\"the component " + getId() + " successfully executed\");\n";
//	}

	/**
	 * 获得当前控件所对应Component 
	 * @return
	 */
	protected WebComponent getComponent()
	{
		LfwWidget widget = getCurrWidget();
		WebComponent comp = widget.getViewComponents().getComponent(getId());
		if(comp == null)
			throw new LfwRuntimeException(this.getClass().getName() + ":can not find the component by id:" + getId());
		return comp;
	}
	
	/**
	 * 获得当前控件所绑定Dataset
	 * @return
	 */
	protected Dataset getDataset()
	{
		WebComponent comp = getComponent();
		if(!(comp instanceof IDataBinding))
			throw new LfwRuntimeException("the component is not type of IDataBinding:" + getId());
		Dataset ds = getDatasetById(((IDataBinding)comp).getDataset());
		if(ds == null)
			throw new LfwRuntimeException("can not find dataset by assigned id:" + ((IDataBinding)comp).getDataset());
		return ds;
	}
	
	protected Dataset getDatasetById(String id)
	{
		LfwWidget widget = getCurrWidget();
		return widget.getViewModels().getDataset(id);
	}
	
	protected String getFieldI18nName(String i18nName, String fieldId, String defaultI18nName, String langDir)
	{ 
		if(i18nName != null && !i18nName.equals("")){
			if(i18nName.equals("$NULL$"))
				return "";
			return translate(i18nName, defaultI18nName == null?i18nName:defaultI18nName, langDir);
		}
		Dataset ds = getDataset();
		if(ds == null) 
			return defaultI18nName;
		
		if(fieldId != null){
			int fldIndex = ds.getFieldSet().nameToIndex(fieldId);
			if(fldIndex == -1)
				throw new LfwRuntimeException("can not find the field:" + fieldId + ",dataset:" + ds.getId());
			Field field = ds.getFieldSet().getField(fldIndex);
			i18nName = field.getI18nName();
			String text = field.getText();
			String defaultValue = text == null? i18nName : text;
			if(i18nName == null || i18nName.equals(""))
				return  defaultI18nName == null?defaultValue:defaultI18nName;
			else{
				return translate(i18nName, defaultI18nName == null?defaultValue:defaultI18nName, langDir);
			}
		}
		else return defaultI18nName;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public void setBeforeScript(String beforeScript){
		setAttribute(BEFORE_SCRIPT, beforeScript);
	}
	
	public void setAfterScript(String afterScript){
		setAttribute(AFTER_SCRIPT, afterScript);
	}
	
	public void setBeforeLogicScript(String beforeLogicScript){
		setAttribute(BEFORE_LOGIC_SCRIPT, beforeLogicScript);
	}
}
