package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.AutoFormContext;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.FormElementContext;
import nc.uap.lfw.core.event.conf.AutoformListener;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwPluginException;

/**
 * Form控件配置
 */
public class FormComp extends WebComponent implements IDataBinding , IContainerComp<FormElement>{

	private static final long serialVersionUID = 7651132623606479940L;
	public static final int DEFAULT_WIDTH = 120;
	public static final int FIXED_LAYOUT = 1;
	public static final int FLOW_LAYOUT = 2;
	public static final int FIXED_HINT_LAYOUT = 3;

	private List<FormElement> elementList = new ArrayList<FormElement>();
	// 自动表单列数,默认两列
	private Integer columnCount = Integer.valueOf(2);
	private String dataset;
	private int rowHeight = 22;
	private int eleWidth = 0;
	// 最小标签宽度
	private int labelMinWidth = 0;

	private String caption;

	private boolean withForm = false;
	// form背景色
	private String backgroundColor = null;
	// 默认渲染方式，以类NC的卡片渲染器进行渲染, 1 table渲染方式， 2NC卡片渲染方式
	private int renderType = FLOW_LAYOUT;
//	// 是否渲染隐藏的表单元素,默认不渲染.
//	private boolean renderHiddenEle = false;
	private boolean readOnly = false;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}


	public int getLabelMinWidth() {
		return labelMinWidth;
	}

	public void setLabelMinWidth(int labelMinWidth) {
		this.labelMinWidth = labelMinWidth;
	}

	public FormComp() {
		super();
	}

	public FormComp(String id) {
		super(id);
	}

	public int getEleWidth() {
		if (this.eleWidth == 0)
			return DEFAULT_WIDTH;
		return eleWidth;
	}

	public void setEleWidth(int eleWidth) {
		this.eleWidth = eleWidth;
	}

	public List<FormElement> getElementList() {
		return elementList;
	}

	public int getElementCountWithoutHidden() {
		if (elementList == null)
			return 0;
		int count = 0;
		for (FormElement ele : elementList) {
			if (ele.isVisible())
				count++;
		}
		return count;
	}

	public FormElement getElementById(String id) {
		Iterator<FormElement> it = elementList.iterator();
		while (it.hasNext()) {
			FormElement ele = it.next();
			if (ele.getId().equals(id))
				return ele;
		}
		return null;
	}

	public void setElementList(List<FormElement> elementList) {
		this.elementList = elementList;
		if (this.elementList != null) {
			for (FormElement formE : this.elementList) {
				formE.setParent(this);
			}
		}
	}

	public void addElement(FormElement ele) {
		ele.setParent(this);
		this.elementList.add(ele);
	}

	public void removeElement(FormElement ele) {
		if (this.elementList != null && this.elementList.size() > 0)
			this.elementList.remove(ele);
	}
	
	public void removeElementById(String eleId){
		if(this.elementList != null && this.elementList.size() > 0){
			for(int i = 0; i < elementList.size(); i++){
				FormElement formEle = elementList.get(i);
				if(formEle.getId().equals(eleId)){
					this.removeElement(formEle);
					break;
				}
			}
		}
		
	}

	public Integer getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(Integer columnCount) {
		this.columnCount = columnCount;
	}

	public String getDataset() {
		return dataset;
	}

	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(AutoformListener.class);
		list.add(FocusListener.class);
		return list;
	}

	public List<FormElement> filterElements(String name) {
		List<FormElement> eleList = new ArrayList<FormElement>();
		Iterator<FormElement> it = elementList.iterator();
		while (it.hasNext()) {
			FormElement ele = it.next();
			if (ele.getId().startsWith(name))
				eleList.add(ele);
		}
		return eleList;
	}

	public void setDataset(String datasetid) {
		this.dataset = datasetid;
	}

	public Object clone() {
		FormComp comp = (FormComp) super.clone();
		if (this.elementList != null) {
			comp.elementList = new ArrayList<FormElement>();
			Iterator<FormElement> it = this.elementList.iterator();
			while (it.hasNext()) {
				comp.addElement((FormElement) it.next().clone());
			}
		}
		return comp;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		if (readOnly != this.readOnly) {
			this.readOnly = readOnly;
			setCtxChanged(true);
			addCtxChangedProperty("readOnly");
		}
	}

	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public boolean isWithForm() {
		return withForm;
	}

	public void setWithForm(boolean withForm) {
		this.withForm = withForm;
	}

	public int getRenderType() {
		return this.renderType;
	}

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	public void mergeProperties(WebElement ele) {
		super.mergeProperties(ele);
		// throw new LfwRuntimeException("not implemented");
	}

//	public boolean isRenderHiddenEle() {
//		return renderHiddenEle;
//	}
//
//	public void setRenderHiddenEle(boolean renderHiddenEle) {
//		this.renderHiddenEle = renderHiddenEle;
//	}

	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			buffer.append("表单的ID不能为空!\r\n");
		}
		if (this.getDataset() == null || this.getDataset().equals("")) {
			buffer.append("表单引用的数据集不能为空!\r\n");
		}
		if (buffer.length() > 0)
			throw new LfwPluginException(buffer.toString());
	}

	public boolean isCtxChanged() {
		if (this.elementList.size() > 0) {
			Iterator<FormElement> it = elementList.iterator();
			while (it.hasNext()) {
				FormElement item = it.next();
				if (item.isCtxChanged()) {
					return true;
				}
			}
		}
		return super.isCtxChanged();
	}

	@Override
	public BaseContext getContext() {
		AutoFormContext ctx = new AutoFormContext();
		ctx.setId(this.getId());
		if (checkCtxPropertyChanged("enabled"))
			ctx.setEnabled(this.enabled);
		if (checkCtxPropertyChanged("readOnly"))
			ctx.setReadOnly(this.readOnly);
		if (this.elementList.size() > 0) {
			List<BaseContext> ctxList = new ArrayList<BaseContext>();
			Iterator<FormElement> it = elementList.iterator();
			while (it.hasNext()) {
				FormElement item = it.next();
				if (item.isCtxChanged()) {
					ctxList.add((BaseContext) item.getContext());
				}
			}
			ctx.setElementContexts(ctxList.toArray(new FormElementContext[0]));
		}

		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		AutoFormContext formCtx = (AutoFormContext) ctx;
		this.setEnabled(formCtx.isEnabled());
		this.setReadOnly(formCtx.isReadOnly());
		BaseContext[] itemContexts = formCtx.getElementContexts();
		if (itemContexts != null) {
			for (int i = 0, n = itemContexts.length; i < n; i++) {
				BaseContext itemCtx = itemContexts[i];
				for (int j = 0, m = elementList.size(); j < m; j++) {
					FormElement ele = elementList.get(j);
					// TODO 这里不需要写，检查bug
					ele.setCtxChanged(false);
					if(itemCtx instanceof FormElementContext){
						ele.setValue(((FormElementContext)itemCtx).getValue());
						ele.setSizeLimit(((FormElementContext)itemCtx).getSizeLimit());
					}
					if (ele.getId().equals(itemCtx.getId())) {
						ele.setContext(itemCtx);
						break;
					}
				}
			}
		}
		this.setCtxChanged(false);
	}

	/**
	 * 设置可编辑状态
	 */
	public void setEnabled(boolean enabled) {
		if (enabled != this.enabled) {
			this.enabled = enabled;
			// List<FormElement> list = this.getElementList();
			// for (int i = 0, n = list.size(); i < n; i++) {
			// list.get(i).setEnabled(enabled);
			// }
			setCtxChanged(true);
			addCtxChangedProperty("enabled");
		}
	}

}
