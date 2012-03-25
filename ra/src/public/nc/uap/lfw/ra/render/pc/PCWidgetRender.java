package nc.uap.lfw.ra.render.pc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.IDataBinding;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.AppTypeUtil;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.MasterFieldInfo;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.uap.lfw.core.refnode.SelfDefRefNode;
import nc.uap.lfw.core.serializer.impl.SingleDataset2XmlSerializer;
import nc.uap.lfw.core.tags.AppDynamicCompUtil;
import nc.uap.lfw.core.tags.DatasetMetaUtil;
import nc.uap.lfw.jsp.uimeta.UIDialog;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.render.UIRender;
import nc.uap.lfw.ra.render.UIWidgetRender;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.StringUtil;

/**
 * @author renxh 片段渲染器
 * 
 */
public class PCWidgetRender extends UIWidgetRender<UIWidget, WebElement> {
	
	public static final String OBS_IN = "OBS_IN";
	
	private boolean renderDiv = true;

	public PCWidgetRender(UIWidget uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UIWidget widget = this.getUiElement();
		this.id = widget.getId();
		this.divId = DIV_PRE + this.id;
		this.varId = WIDGET_PRE + this.id;
		LfwWidget lfwWidget = this.getPageMeta().getWidget(id);
		if (lfwWidget.isDialog()) {
			if (renderDiv == false) {
				throw new LfwRuntimeException("对话框片段必须整体渲染");
			}
		}
	}

	public String generalHeadHtml() {
		if (renderDiv == false)
			return "";
		String position = "relative";
		UIWidget uiwidget = getUiElement();
		if(uiwidget instanceof UIDialog)
			position = "absolute";
		String height = "height:100%;";
		if(isFlowMode())
			height = "";
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;" + height + "top:0px;left:0px;position:" + position + ";\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	@Override
	public String generalHeadHtmlDynamic() {
		if (renderDiv == false)
			return "";
		StringBuffer buf = new StringBuffer();
		UIWidget uiwidget = getUiElement();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		if(uiwidget instanceof UIDialog){
			UIDialog dialog = (UIDialog) uiwidget;
			buf.append(getNewDivId()).append(".style.width = '" + dialog.getWidth() + "px';\n");
			buf.append(getNewDivId()).append(".style.height = '" + dialog.getHeight() + "px';\n");
		}
		else{
			buf.append(getNewDivId()).append(".style.width = '100%';\n");
			if(!isFlowMode())
				buf.append(getNewDivId()).append(".style.height = '100%';\n");
		}
		
		String position = "relative";
		if(uiwidget instanceof UIDialog)
			position = "absolute";
		
		buf.append(getNewDivId()).append(".style.top = '0px';\n");
		buf.append(getNewDivId()).append(".style.left = '0px';\n");
		buf.append(getNewDivId()).append(".style.position = '" + position + "';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}

		return buf.toString();
	}

	public String generalHeadScript() {
		StringBuffer buf = new StringBuffer();
		LfwWidget widget = getPageMeta().getWidget(id);
		widget.setRendered(true);

		String script = createWidgetUI(widget);
		buf.append(script);

		if (widget.isDialog()) {
			buf.append(varId).append(".lazyInit = function(){\n");
		}

		// 初始化所有dataset.Ds数据在初始化PageModel时已经获取
		buf.append(renderDatasetScript(widget));
		// 初始化所有RefNode信息到页面中
		buf.append(renderRefNodeScript(widget));

		// 初始化所有聚合数据
		buf.append(renderComboDataScript(widget));

		// 记录Dataset客户端关系对象
		buf.append(renderDsRelations(widget));

		// 记录RefNode客户端关系对象
		buf.append(renderRefNodeRelations(widget));

		return buf.toString();
	}

	public String generalTailHtml() {

		if (renderDiv == false)
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	public String generalTailScript() {
		StringBuffer buf = new StringBuffer();
		UIWidget uiWidget = this.getUiElement();
		LfwWidget widget = getPageMeta().getWidget(id);
		if (widget.isDialog()) {
			String script = "}\n";
			buf.append(script);
		}

//		StringBuffer bufState = new StringBuffer();
		String showId = getVarId();
		String cId = COMP_PRE + getId();
		String dId = DIV_PRE + getId();

//		bufState.append(showId + ".setOperateState(" + widget.getOperatorState() + ");\n");
//
//		String busiState = widget.getBusiState();
//		if (busiState != null && !"".equals(busiState))
//			bufState.append(showId + ".setBusinessState(" + busiState + ");\n");
//		bufState.append(showId + ".updateButtons();\n");
//		buf.append(bufState.toString());
		
		if (widget.isDialog() || uiWidget instanceof UIDialog) {
			StringBuffer dialogBuf = new StringBuffer();
			String width = null;
			String height = null;
			String title = null;
			if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
				UIDialog dialog = (UIDialog) uiWidget;
				width = dialog.getWidth();
				height = dialog.getHeight();
				title = dialog.getTitle();
				
			}
			else{
				UIDialog dialog = (UIDialog) uiWidget.getUimeta().getDialog(getId());
				if(dialog == null){
					UIMeta pageUm = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
					dialog = (UIDialog) pageUm.getDialog(getId());
				}
				width = dialog.getWidth();
				height = dialog.getHeight();
				title = widget.getI18nName();
			}
			dialogBuf.append("window.").append(cId).append(" = new ModalDialogComp(\"").append(getId()).append("\",\"");
			dialogBuf.append(title).append("\",").append(0).append(",").append(0).append(",\"");
			dialogBuf.append(width).append("\",\"").append(height).append("\");\n");
			
			Boolean isPopClose = (Boolean) uiWidget.getAttribute("isPopClose");
			
			/**
			 * 关闭时弹出保存并关闭的窗口
			 */
			if(isPopClose){
				dialogBuf.append("var dls = new DialogListener();\n");
				dialogBuf.append("dls.source_id = \'").append(getId()).append("\';\n");
				dialogBuf.append("dls.listener_id = \'onBeforeClose_").append(getId()).append("\';\n");
				dialogBuf.append("dls.beforeClose = function (){");
				
				dialogBuf.append(" return pageUI.showCloseConfirm(").append(cId).append(");}\n");
				dialogBuf.append(cId).append(".addListener(dls);");
			}
			
			dialogBuf.append("var tmpContent = document.getElementById(\"").append(dId).append("\");\n");
			dialogBuf.append(cId).append(".getContentPane().appendChild(tmpContent);\n");
			dialogBuf.append("tmpContent.style.width = '100%';\n");
			if (!isFlowMode())
				dialogBuf.append("tmpContent.style.height = '100%';\n");
			
			
			dialogBuf.append("tmpContent.style.position = 'relative';\n");
			
			dialogBuf.append("if (!(IS_IE7)) {window.onresize();}\n");

			dialogBuf.append("window.").append(cId).append(".widget=").append(showId).append(";\n");

			// 将dialog对象加入
			dialogBuf.append("window.pageUI.addDialog(").append("\"" + getId() + "\"," + cId).append(");\n");
			
			
			buf.append(dialogBuf.toString());
			LfwWidget wdg = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget(this.getWidget());
			String dlgEventScript = addEventSupport(wdg, getWidget(), "pageUI.getWidget(\"" + getId() + "\")", null);
			buf.append(dlgEventScript);
		}
		else{
			LfwWidget wdg = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget(this.getWidget());
			String dlgEventScript = addEventSupport(wdg, getWidget(), "pageUI.getWidget(\"" + getId() + "\")", null);
			buf.append(dlgEventScript);
			buf.append(showId).append(".onBeforeShow();\n");
		}

		
		if (renderDiv) {
			StringBuffer buft = (StringBuffer) this.getContextAttribute(DS_SCRIPT);

			if (buft != null) {
				String script = buft.toString();
				buft.delete(0, buft.length());
				buf.append(script);
			}

		}

		// 加入plugout对象
		List<PlugoutDesc> plugoutDescs = widget.getPlugoutDescs();
		if (plugoutDescs != null && plugoutDescs.size() > 0) {
			for (PlugoutDesc plugout : plugoutDescs) {
				String id = plugout.getId();
				buf.append("var plugout_").append(id).append(" = new PlugOut('").append(id).append("');\n");
				List<PlugoutDescItem> plugoutDescItems = plugout.getDescItemList();
				if (plugoutDescItems != null && plugoutDescItems.size() > 0) {
					for (PlugoutDescItem item : plugoutDescItems) {
						buf.append("var plugoutItem_").append(item.getName()).append(" = new PlugOutItem('").append(item.getName()).append("','").append(item.getType()).append(
								"','").append(item.getSource()).append("','").append(item.getDesc() == null ? "" : item.getDesc()).append("');\n");
						buf.append(" plugout_").append(id).append(".addItem(").append("plugoutItem_").append(item.getName()).append(");\n");
					}
					buf.append(varId).append(".addPlugOut(plugout_").append(id).append(");\n");
				}
			}
		}
		buf.append(wrapByRequired("form", PcFormRenderUtil.getAllFormDsScript(this.id)));//自由表单的数据集的设置
		PcFormRenderUtil.removeFormDsScript(this.id);	
		
		return buf.toString();
	}

	private String createWidgetUI(LfwWidget widget) {
		StringBuffer buf = new StringBuffer();
		String showId = getVarId();
		buf.append("var ").append(showId).append(" = new Widget('").append(widget.getId()).append("',");

		boolean visible = widget.isVisible();
		UIWidget uiWidget = getUiElement();
		boolean dialog = false;
		if (widget.isDialog() || uiWidget instanceof UIDialog){
			visible = false;
			dialog = true;
		}
		buf.append(visible).append(",").append(dialog).append(");\n");

//		buf.append("window.").append(showId).append(" = ").append(showId).append(";\n");

		buf.append("window.pageUI.addWidget(").append(showId).append(");\n");
		// }

		buf.append(showId).append(".beforeWidgetInit();\n");

		return buf.toString();
	}

	/**
	 * 渲染当前控件所对应dataset脚本
	 * 
	 * @param widget
	 * @return
	 */
	protected String renderDatasetScript(LfwWidget widget) {
		StringBuffer buf = new StringBuffer();
		Dataset[] datasets = widget.getViewModels().getDatasets();
		if (datasets != null) {
			for (int i = 0; i < datasets.length; i++) {
				Dataset ds = datasets[i];
				if (ds instanceof IRefDataset)
					continue;
				ds.setRendered(true);
				String script = generateDatasetScript(widget, ds);
				buf.append(script);

				String dataScript = generateDataScript(widget, ds);
				if (dataScript != null) {
					buf.append(dataScript);
				}

			}
		}
		return buf.toString();
	}

	protected String renderRefNodeScript(LfwWidget widget) {
		StringBuffer buf = new StringBuffer();
		IRefNode[] refnodes = widget.getViewModels().getRefNodes();
		if (refnodes != null) {
			for (int i = 0; i < refnodes.length; i++) {
				IRefNode refNode = refnodes[i];
				refNode.setRendered(true);
				String script = generateRefNodeScript(widget, refNode);
				buf.append(script);
			}
		}
		return buf.toString();
	}

	/**
	 * 渲染ComboData到客户端
	 * 
	 * @param widget
	 * @param combodataId
	 * @return
	 */
	protected String renderComboDataScript(LfwWidget widget) {
		StringBuffer sb = new StringBuffer();
		ComboData[] cds = widget.getViewModels().getComboDatas();
		for (int j = 0; j < cds.length; j++) {
			ComboData cd = cds[j];
			StringBuffer buf = renderComboDataScript(widget, cd);
			sb.append(buf);
		}
		return sb.toString();
	}

	/**
	 * 渲染combodata的脚本
	 * @param widget
	 * @param cd
	 * @return
	 */
	private StringBuffer renderComboDataScript(LfwWidget widget, ComboData cd) {
		cd.setRendered(true);
		StringBuffer buf = new StringBuffer();
		String id = COMBO_PRE + widget.getId() + "_" + cd.getId();
		buf.append("window.").append(id).append(" = new ComboData(\"").append(cd.getId()).append("\");\n");
		CombItem[] items = cd.getAllCombItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				buf.append(id).append(".addItem(new ComboItem(\"").append(translate(items[i].getI18nName(), items[i].getText(), items[i].getLangDir())).append("\",\"").append(
						items[i].getValue()).append("\"");
				if(items[i].getImage() != null && items[i].getImage().trim().length() > 0){
					buf.append(",'"+items[i].getImage()+"'");
				}
				buf.append("));\n");
			}
		}

		buf.append("if(!"+varId+")");
		buf.append("var "+varId).append("=pageUI.getWidget('"+widget.getId()+"');\n");
		buf.append(varId).append(".addComboData(").append(id).append(");\n");

		buf.append(id).append(".widget = ").append(varId).append(";\n");
		return buf;
	}

	protected String renderDsRelations(LfwWidget widget) {
		StringBuffer scriptBuf = new StringBuffer();
		DatasetRelations dsRelations = widget.getViewModels().getDsrelations();
		if (dsRelations != null) {
			scriptBuf.append(varId + ".setDsRelations(new DsRelations());\n");
			DatasetRelation[] rels = dsRelations.getDsRelations();
			for (int i = 0; i < rels.length; i++) {
				DatasetRelation dsRelation = rels[i];
				scriptBuf.append("var ").append(DS_RELATION_PRE).append(dsRelation.getId()).append(" = new DsRelation(\"").append(dsRelation.getId()).append("\",\"").append(
						dsRelation.getMasterDataset()).append("\",\"").append(dsRelation.getMasterKeyField()).append("\",\"").append(dsRelation.getDetailDataset()).append("\",\"")
						.append(dsRelation.getDetailForeignKey()).append("\");\n");
				scriptBuf.append(varId + ".dsRelations.addRelation(").append(DS_RELATION_PRE).append(dsRelation.getId()).append(");\n");
			}

		}
		return scriptBuf.toString();
	}

	protected String renderRefNodeRelations(LfwWidget widget) {
		StringBuffer scriptBuf = new StringBuffer();
		RefNodeRelations refNodeRelations = widget.getViewModels().getRefNodeRelations();
		if (refNodeRelations != null) {
			scriptBuf.append("window.$refNodeRelations = new RefNodeRelations();\n");
			scriptBuf.append("window.$refNodeRelations").append(".widget = ").append(varId).append(";\n");
			Map<String, RefNodeRelation> map = refNodeRelations.getRefnodeRelations();
			Set<String> keys = map.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String relId = it.next();
				RefNodeRelation rfRelation = map.get(relId);
				scriptBuf.append("window.").append(RF_RELATION_PRE).append(rfRelation.getId()).append(" = new RefNodeRelation(\"").append(rfRelation.getId()).append("\",\"")
				// .append(rfRelation.getMasterRefNode())
						.append(refNodeInfoToObj(rfRelation.getMasterFieldInfos())).append("\",\"").append(rfRelation.getDetailRefNode()).append("\",\"").append(
								rfRelation.getTargetDs()).append("\",").append(rfRelation.isClearDetail()).append(");\n");
				scriptBuf.append("window.$refNodeRelations.addRelation(").append(RF_RELATION_PRE).append(rfRelation.getId()).append(");\n");
			}
			scriptBuf.append("bindRefNode2Dataset();\n");
		}
		return scriptBuf.toString();
	}

	/**
	 * 生成Dataset对应脚本文件
	 * 
	 * @param ds
	 * @return
	 */
	protected String generateDatasetScript(LfwWidget widget, Dataset ds) {
		StringBuffer buf = new StringBuffer();
		String dsVarId = getDatasetVarShowId(ds.getId(), widget.getId());
		// buf.append("var " + dsVarId)
		buf.append(dsVarId).append(" = new Dataset(\"").append(ds.getId()).append("\",");
		// if(!ds.isLazyLoad())
		// buf.append("document.getElementById(\"")
		// .append(ds.getId())
		// .append("\")");

		// else
		// buf.append("null");
		String metaStr = DatasetMetaUtil.generateMeta(ds);
		buf.append(metaStr);
		buf.append(",").append(ds.isLazyLoad()).append(",").append(ds.isEnabled()).append(",").append(ds.getPageSize()).append(");\n");

		// 设置ds的操作状态
//		String operatorStatusArray = ds.getOperatorStatusArray();
//		if (operatorStatusArray != null && !operatorStatusArray.equals(""))
//			buf.append(dsVarId + ".operateStateArray=" + StringUtil.mergeScriptArray(operatorStatusArray) + ";\n");

		String varId = getVarId();
		// buf.append(addEventSupport(ds, ds.getId()));
		buf.append("var ").append(varId).append("= pageUI.getWidget('").append(getId()).append("');\n");
		
		buf.append(getVarId()).append(".addDataset(").append(dsVarId).append(");\n");

		buf.append(dsVarId).append(".widget = ").append(getVarId()).append(";\n");

		WebComponent[] comps = widget.getViewComponents().getComponents();
		for (int i = 0; i < comps.length; i++) {
			if(comps[i] instanceof IDataBinding){
				String dsId = ((IDataBinding)comps[i]).getDataset();
				if(ds.getId().equals(dsId)){
					buf.append(dsVarId)
					   .append(".hasComp = true;\n");
					break;
				}
			}
		}
		
		buf.append(addEventSupport(ds, widget.getId(), getDatasetVarShowId(ds.getId(), widget.getId()), null));
		return buf.toString();
	}
	
	/**
	 * 想dataset中添加field的脚本 renxh
	 * @param widget
	 * @param ds
	 * @param field
	 * @return
	 */
	private String addDatasetFieldScript(LfwWidget widget,Dataset ds,Field field){
		StringBuffer buf = new StringBuffer();
		String dsVarId = getDatasetVarShowId(ds.getId(), widget.getId());
		buf.append("var ").append(dsVarId).append(" = pageUI.getWidget('"+id+"').getDataset('"+ds.getId()+"');\n");
		buf.append("if("+dsVarId+"){");
		buf.append(dsVarId+".addField("+DatasetMetaUtil.generateField(field)+");\n");
		buf.append("};\n");
		return buf.toString();
	}

	private String generateDataScript(LfwWidget widget, Dataset ds) {
		if (ds.isLazyLoad() == false)
			return null;
		if (ds.getCurrentKey() == null)
			return null;
		String dsXml = new SingleDataset2XmlSerializer().serialize(ds)[0];
		String dsVarId = getDatasetVarShowId(ds.getId(), widget.getId());
		String str = dsVarId + ".setData(decodeURIComponent('" + JsURLEncoder.encode(dsXml, "UTF-8") + "'));";
		return str;
	}

	protected String generateRefNodeScript(LfwWidget widget, IRefNode iRefNode) {
		StringBuffer buf = new StringBuffer(1024);
		String refId = RF_PRE + widget.getId() + "_" + iRefNode.getId();
		if(iRefNode instanceof RefNode){
			RefNode refNode = (RefNode) iRefNode;
			// 如果relation为空,则按照fields来取
			String readFields = StringUtil.mergeScriptArray(refNode.getReadFields());
			String writeFields = StringUtil.mergeScriptArray(refNode.getWriteFields());
	
			buf.append("window.").append(refId).append(" = ").append("new RefNodeInfo(\"").append(refNode.getId()).append("\",\"").append(
					translate(refNode.getI18nName(), refNode.getText(), refNode.getLangDir())).append("\",\"").append(refNode.getPagemeta()).append("\",\"").append(refNode.getPath())
					.append("\",\"").append(refNode.getReadDs()).append("\",\"").append(refNode.getWriteDs() == null ? "" : refNode.getWriteDs()).append("\",").append(readFields)
					.append(",").append(writeFields).append(",\"").append("") // filterSql
					.append("\",\"").append(/*
											 * refNode.getUserObj() == null ? "":
											 * refNode.getUserObj()
											 */"").append("\",").append(refNode.isMultiSel()).append(",").append(false) // use
					// power
					.append(",").append(refNode.isSelLeafOnly()).append(",").append(refNode.isRefresh()).append(",").append(refNode.isDialog()).append(",").append(
							refNode.isAllowInput()).append(");\n");
		}
		else if(iRefNode instanceof SelfDefRefNode){
			SelfDefRefNode refNode = (SelfDefRefNode) iRefNode;
			buf.append("window.").append(refId).append(" = ").append("new SelfRefNodeInfo('").append(refNode.getId()).append("','").append(
					translate(refNode.getI18nName(), refNode.getText(), refNode.getLangDir())).append("','").append(refNode.getPath()).append("',").append(refNode.isDialog()).append(");\n");
		}
		buf.append("var "+ getVarId()).append(" = pageUI.getWidget('"+widget.getId()+"');\n");
		buf.append(getVarId()).append(".addRefNode(").append(refId).append(");\n");
		
		buf.append(refId);
		buf.append(".widget = ").append(getVarId()).append(";\n");
		return buf.toString();
	}

	/**
	 * 将RefNodeInfo数组转换为前台JS对象
	 * 
	 * @param refNodeInfos
	 * @return
	 */
	private String refNodeInfoToObj(List<MasterFieldInfo> masterFieldInfos) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		if (masterFieldInfos != null) {
			for (int i = 0, n = masterFieldInfos.size(); i < n; i++) {
				MasterFieldInfo masterFieldInfo = masterFieldInfos.get(i);
				buf.append("{dsId:'").append(masterFieldInfo.getDsId()).append("',fieldId:'").append(masterFieldInfo.getFieldId()).append("',filterSql:'").append(
						masterFieldInfo.getFilterSql()).append("',nullProcess:'").append(masterFieldInfo.getNullProcess()).append("'}");
				if (i != masterFieldInfos.size() - 1)
					buf.append(",");
			}
		}
		buf.append("]");

		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		if (ele instanceof Dataset)
			return LfwPageContext.SOURCE_TYPE_DATASET;
		else if (ele instanceof LfwWidget)
			return LfwPageContext.SOURCE_TYPE_WIDGT;
		return null;
	}

	
	@Override
	protected String getExtendProxyStr(IEventSupport ele, JsListenerConf listener, EventHandlerConf jsEvent) {
		if (ele instanceof Dataset) {
			if (jsEvent.getName().equals("onDataLoad")) {
				StringBuffer buf = new StringBuffer();
				buf.append("if(dataLoadEvent.userObj != null)\n").append("proxy.setReturnArgs(dataLoadEvent.userObj);\n");
				return buf.toString();
			}
		}
		return null;
	}

	protected void addProxyParam(StringBuffer buf, String eventName) {
		super.addProxyParam(buf, eventName);
		if (eventName.equals("onAfterRowInsert")) {
			buf.append("proxy.addParam('row_insert_index', rowInsertEvent.insertedIndex);\n");
		}
	}


	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		if (obj instanceof Map){
			Map<String,Object> map = (Map<String,Object>)obj;
			String widgetId = (String)map.get("widgetId");
			LfwWidget widget = getPageMeta().getWidget(widgetId);
			String type = (String)map.get("type");
			if (type != null && type.equals("adjustField")){
				String datasetId = (String)map.get("datasetId");
				Field field = (Field)map.get("field");
				Dataset dataset = (Dataset)widget.getViewModels().getDataset(datasetId);
				String script = this.adjustFieldScript(dataset, field);
				this.addBeforeExeScript(script);
			}
			else if (type != null && type.equals(LfwWidget.WIDGET_CAPTION)){
				String script = this.captionScript(widget);
				this.addDynamicScript(script);
			}
			else if (type != null && type.equals("comboChange")){
				String comboDataId = (String)map.get("comboDataId");
				ComboData comboData = (ComboData)widget.getViewModels().getComboData(comboDataId);
				this.comboDataChangeScript(widgetId, comboData);
			}
			else if (type != null && type.equals("notifyAddRefNode")){
				IRefNode RefNode = (IRefNode)map.get("iRefNode");
				this.notifyAddRefNode(null, null, RefNode);
			}
			else if (type != null && type.equals("notifyAddComboDataScript")){
				ComboData comboData = (ComboData)map.get("comboData");
				this.notifyAddComboDataScript(null, null, comboData);
			}
		}
		else{
			UIWidget uiWidget = this.getUiElement();
			LfwWidget widget = getPageMeta().getWidget(id);
			if (widget.isDialog() || uiWidget instanceof UIDialog) {
				StringBuffer buf = new StringBuffer();
//				String cId = COMP_PRE + getId();
				buf.append("pageUI.getWidget('").append(id).append("')");
				if (uiWidget != null && !((UIDialog)uiWidget).isVisible()){
					buf.append(".setVisible(false);\n");
				}
				else {
					buf.append(".setVisible(true);\n");		
				}
				addDynamicScript(wrapByRequired("modaldialog", buf.toString()));
			} 
			else {
				super.notifyUpdate(uiMeta, pageMeta, obj);
			}
		}
	}
	
	public void notifyAddDsField(UIMeta uiMeta, PageMeta pageMeta, Object obj){
		LfwWidget widget = getPageMeta().getWidget(id);
		if(obj instanceof Map){
			Map<String,Object> map = (Map<String,Object>)obj;
			String dsId = (String)map.get("dsId");
			Field field = (Field)map.get("field");
			Dataset ds = widget.getViewModels().getDataset(dsId);
			String script = this.addDatasetFieldScript(widget, ds, field);			
			this.addDynamicScript(script);
		}
		
	}
	
	/**
	 * 添加引用脚本
	 * @param uiMeta
	 * @param pageMeta
	 * @param obj
	 */
	public void notifyAddRefNode(UIMeta uiMeta, PageMeta pageMeta, Object obj){
		LfwWidget widget = getPageMeta().getWidget(id);
		IRefNode refNode = (IRefNode)obj;
		String script = this.generateRefNodeScript(widget, refNode);
		this.addDynamicScript(script);
	}
	
	/**
	 * 添加combodata脚本
	 * @param uiMeta
	 * @param pageMeta
	 * @param obj
	 */
	public void notifyAddComboDataScript(UIMeta uiMeta, PageMeta pageMeta, Object obj){
		LfwWidget widget = getPageMeta().getWidget(id);
		String script = this.renderComboDataScript(widget,(ComboData)obj).toString();
		//this.addDynamicScript("debugger;\n");
		this.addDynamicScript(script);
	}

	@Override
	public String createRenderScript() {
		return super.createRenderScript();
	}

	@Override
	public String createRenderScriptDynamic() {
		String str = super.createRenderScriptDynamic();
		StringBuffer buf = new StringBuffer();
		buf.append("pageUI.hasChanged=false;\n").append(str);
		return buf.toString();
		
	}

	
	private String adjustFieldScript(Dataset ds, Field field){
		StringBuffer buf = new StringBuffer();
		LfwWidget widget = ds.getWidget();
		String varDs = "$c_"+ds.getId();
		
//		buf.append("pageUI.hasChanged=false;\n");
		
		if(widget == null){
			buf.append("var "+varDs).append(" = pageUI.getDataset('"+ds.getId()+"');\n");
		}else{
			buf.append("var "+varDs).append(" = pageUI.getWidget('"+widget.getId()+"').getDataset('"+ds.getId()+"');\n");
		}
		String varField = "$c_"+field.getId();
		buf.append("var "+varField).append(" = "+DatasetMetaUtil.generateField(field)+";\n");
		buf.append(varDs).append(".addField("+varField+");\n");
		return buf.toString();
	}
	
	private String captionScript(LfwWidget wg){
		return "pageUI.getDialog('" + wg.getId() + "').titleDiv.innerHTML='" + wg.getCaption() + "'";
	}
	
	private void comboDataChangeScript(String widgetId, ComboData comboData){
		if(comboData.getExtendAttributeValue(OBS_IN) != null)
			return;
		comboData.setExtendAttribute(OBS_IN, "1");
		ApplicationContext appCtx = AppLifeCycleContext.current().getApplicationContext();
		ViewContext viewCtx = appCtx.getCurrentWindowContext().getViewContext(widgetId);
		AppDynamicCompUtil util = new AppDynamicCompUtil(appCtx, viewCtx);
		util.replaceComboData(comboData.getId(), widgetId, (ComboData) comboData.clone());
		comboData.removeExtendAttribute(OBS_IN);
	}
}
