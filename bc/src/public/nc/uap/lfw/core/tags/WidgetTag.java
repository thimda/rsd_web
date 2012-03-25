package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;

import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.IDataBinding;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.MasterFieldInfo;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.uap.lfw.core.serializer.impl.SingleDataset2XmlSerializer;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.StringUtil;

public class WidgetTag extends ContainerElementTag{

	private String id;
	private boolean renderDiv = true;
	private String width;
	private String height;
	public WidgetTag(){
	}
	
	protected String getVarShowId() {
		return WIDGET_PRE + id;
	} 
	@Override
	protected void doRender() throws JspException, IOException {
		LfwWidget widget = getPageModel().getPageMeta().getWidget(id);
		if(widget.isDialog()){
			if(renderDiv == false){
				throw new LfwRuntimeException("对话框片段必须整体渲染");
			}
		}
		super.doRender();
		
	}
	
	@Override
	public String generateHead() {
		if(renderDiv == false)
			return null;
		return "<div id=\"" + DIV_PRE + getId() + "\" style=\"width:100%;height:100%;top:0px;left:0px;position:relative;\">\n";
	}

	@Override
	public String generateHeadScript() {
		LfwWidget widget = getPageModel().getPageMeta().getWidget(id);
//		if(widget.isRendered())
//			return "";
		widget.setRendered(true);
		
		String script = createWidgetUI(widget);
		addToBodyScript(script);
		
		if(widget.isDialog()){
			StringBuffer buf = new StringBuffer();
			buf.append(getVarShowId())
			   .append(".lazyInit = function(){\n");
			addToBodyScript(buf.toString());
		}
//		renderSlotScript(widget);
		
		//初始化所有dataset.Ds数据在初始化PageModel时已经获取
		renderDatasetScript(widget);
		//初始化所有RefNode信息到页面中
		renderRefNodeScript(widget);
		
		//初始化所有聚合数据
		renderComboDataScript(widget);
		
		//记录Dataset客户端关系对象
		renderDsRelations(widget);
		
		//记录RefNode客户端关系对象
		renderRefNodeRelations(widget);
		
//		if(getJspBody() != null)
//			getJspBody().invoke(getJspContext().getOut());
		return "";
		
	}

	@Override
	public String generateTail() {
		if(renderDiv == false)
			return null;
		return "</div>\n";
	}

	@Override
	public String generateTailScript() {
		LfwWidget widget = getPageModel().getPageMeta().getWidget(id);
		if(widget.isDialog()){
			String script = "}\n";
			addToBodyScript(script);
		}
		
//		StringBuffer bufState = new StringBuffer();
//		String showId = getVarShowId();
//		bufState.append(showId + ".setOperateState(" + widget.getOperatorState() + ");\n");
		
//		String busiState = widget.getBusiState();
//		if(busiState != null && !"".equals(busiState))
//			bufState.append(showId + ".setBusinessState(" + busiState + ");\n");
//		bufState.append(showId + ".updateButtons();\n");
//		addToBodyScript(bufState.toString());
		
		if(widget.isDialog()){
			StringBuffer buf = new StringBuffer();
			buf.append("window.")
			   .append(COMP_PRE)
			   .append(getId())
			   .append(" = new ModalDialogComp(\"")
			   .append(getId())
			   .append("\",\"")
			   .append(translate(widget.getI18nName(), null, widget.getLangDir()))
			   .append("\",")
			   .append(0)
			   .append(",")
			   .append(0)
			   .append(",\"")
			   .append(getWidth())
			   .append("\",\"")
			   .append(getHeight())
			   .append("\");\n");
			
			buf.append(COMP_PRE)
			   .append(getId())
			   .append(".getContentPane().appendChild(document.getElementById(\"")
			   .append(DIV_PRE)
			   .append(getId())
			   .append("\"));\n");
			
			buf.append("window.")
				.append(COMP_PRE)
			   .append(getId())
			   .append(".widget=")
			   .append(getVarShowId())
			   .append(";\n");
			buf.append("window.pageUI.addDialog(")
			   .append("\"" + getId() + "\"," + COMP_PRE + getId())
			   .append(");\n");
			
			//将Dialog事件加入
			buf.append(addEventSupport(widget, widget.getId(), COMP_PRE + getId(), null));
			
			addToBodyScript(buf.toString());
		}
		
		if(renderDiv){
			StringBuffer buf = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
			String script = buf.toString();
			buf.delete(0, buf.length());
			return script;
		}
		return null;
	}
	
	private String createWidgetUI(LfwWidget widget) {
		StringBuffer buf = new StringBuffer();
		String showId = getVarShowId();
		buf.append("window.")
		   .append(showId)
		   .append(" = new Widget('")
		   .append(widget.getId())
		   .append("',");
			boolean visible = widget.isVisible();
			if(widget.isDialog())
				visible = false;
		   buf.append(visible)
		   .append(",")
		   .append(widget.isDialog())
		   .append(");\n");
		
//		if(widget.getMasterDataset() != null){
//			buf.append(getVarShowId())
//			   .append(".setMasterDatasetId('")
//			   .append(widget.getMasterDataset())
//			   .append("');\n");
//		}
//		
//		if(widget.getSlaveDatasets() != null){
//			buf.append(getVarShowId())
//			   .append(".setSlaveDatasetIds([")
//			   .append(StringUtil.mergeScriptArray(widget.getSlaveDatasets()))
//			   .append("]);\n");
//		}
		
		//if(!widget.isDialog()){
		buf.append("window.pageUI.addWidget(")
		   .append(getVarShowId())
		   .append(");\n");
		//}
		
		buf.append(getVarShowId())
		   .append(".beforeWidgetInit();\n");
		
		return buf.toString();
	}
	
	
	/**
	 * 渲染当前控件所对应dataset脚本
	 * @param widget 
	 * @return
	 */
	protected void renderDatasetScript(LfwWidget widget){
		Dataset[] datasets = widget.getViewModels().getDatasets();
		if(datasets != null){
			for (int i = 0; i < datasets.length; i++) {
				Dataset ds = datasets[i];
				if(ds instanceof IRefDataset)
					continue;
				ds.setRendered(true);
				String script = generateDatasetScript(widget, ds);
				addToBodyScript(script);
				
				String dataScript = generateDataScript(widget, ds);
				addToBodyScript(dataScript);
			}
		}
	}
	
	private String generateDataScript(LfwWidget widget, Dataset ds) {
		if(ds.isLazyLoad() == false)
			return null;
		if(ds.getCurrentKey() == null)
			return null;
		String dsXml = new SingleDataset2XmlSerializer().serialize(ds)[0];
		String dsVarId = getDatasetVarShowId(ds.getId(), widget.getId());
		String str = dsVarId + ".setData(decodeURIComponent('" + JsURLEncoder.encode(dsXml, "UTF-8") + "'));";
		return str;
	}

	protected void renderRefNodeScript(LfwWidget widget) {
		IRefNode[] refnodes = widget.getViewModels().getRefNodes();
		if(refnodes != null){
			for (int i = 0; i < refnodes.length; i++) {
				IRefNode refNode = refnodes[i];
				refNode.setRendered(true);
				generateRefNodeScript(widget, refNode);
				//addToBodyScript(script);
			}
		}
	}
	
	
	protected void renderDsRelations(LfwWidget widget){
		StringBuffer scriptBuf = new StringBuffer();
		DatasetRelations dsRelations = widget.getViewModels().getDsrelations();
		scriptBuf.append(getVarShowId()+ ".setDsRelations(new DsRelations());\n");
		if(dsRelations != null)
		{
			DatasetRelation[] rels = dsRelations.getDsRelations();
			for (int i = 0; i < rels.length; i++) {
				DatasetRelation dsRelation = rels[i];
				scriptBuf.append("var ")
				         .append(DS_RELATION_PRE)
				         .append(dsRelation.getId())
				         .append(" = new DsRelation(\"")
				         .append(dsRelation.getId())
				         .append("\",\"")
				         .append(dsRelation.getMasterDataset())
				         .append("\",\"")
				         .append(dsRelation.getMasterKeyField())
				         .append("\",\"")
				         .append(dsRelation.getDetailDataset())
				         .append("\",\"")
				         .append(dsRelation.getDetailForeignKey())
//				         .append("\",\"")
//				         .append(dsRelation.getDetailKeyField())
				         .append("\");\n");
				scriptBuf.append(getVarShowId() + ".dsRelations.addRelation(")
						 .append(DS_RELATION_PRE)
						 .append(dsRelation.getId())
						 .append(");\n");
			}
			
		}
		addToBodyScript(scriptBuf.toString());
	}
	
	protected void renderRefNodeRelations(LfwWidget widget){
		StringBuffer scriptBuf = new StringBuffer();
		RefNodeRelations refNodeRelations = widget.getViewModels().getRefNodeRelations();
		if(refNodeRelations != null)
		{
			scriptBuf.append("window.$refNodeRelations = new RefNodeRelations();\n");
			scriptBuf.append("window.$refNodeRelations")
			.append(".widget = ")
			.append(getVarShowId())
			.append(";\n");
			Map<String, RefNodeRelation> map = refNodeRelations.getRefnodeRelations();
			Set<String> keys = map.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext())
			{
				String relId = it.next();
				RefNodeRelation rfRelation = map.get(relId);
				scriptBuf.append("window.")
				         .append(RF_RELATION_PRE)
				         .append(rfRelation.getId())
				         .append(" = new RefNodeRelation(\"")
				         .append(rfRelation.getId())
				         .append("\",\"")
//				         .append(rfRelation.getMasterRefNode())
				         .append(refNodeInfoToObj(rfRelation.getMasterFieldInfos()))
				         .append("\",\"")
				         .append(rfRelation.getDetailRefNode())
				         .append("\",\"")
				         .append(rfRelation.getTargetDs())
				         .append("\",")
				         .append(rfRelation.isClearDetail())
				         .append(");\n");
				scriptBuf.append("window.$refNodeRelations.addRelation(")
						 .append(RF_RELATION_PRE)
						 .append(rfRelation.getId())
						 .append(");\n");
			}
		}
		scriptBuf.append("bindRefNode2Dataset();\n");
		addToBodyScript(scriptBuf.toString());
	}
	
	/**
	 * 将RefNodeInfo数组转换为前台JS对象
	 * @param refNodeInfos
	 * @return
	 */
	private String refNodeInfoToObj(List<MasterFieldInfo> masterFieldInfos) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		if (masterFieldInfos != null) {
			for (int i = 0, n = masterFieldInfos.size(); i < n; i++) {
				MasterFieldInfo masterFieldInfo = masterFieldInfos.get(i);
				buf.append("{dsId:'")
				   .append(masterFieldInfo.getDsId())
				   .append("',fieldId:'")
				   .append(masterFieldInfo.getFieldId())
				   .append("',filterSql:'")
				   .append(masterFieldInfo.getFilterSql())
				   .append("',nullProcess:'")
				   .append(masterFieldInfo.getNullProcess())
				   .append("'}");
				if (i != masterFieldInfos.size() - 1)
					buf.append(",");
			}
		}
		buf.append("]");
		
		return buf.toString();
	}
	
	
	/**
	 * 渲染ComboData到客户端
	 * @param widget 
	 * @param combodataId
	 * @return
	 */
	protected void renderComboDataScript(LfwWidget widget)
	{
		ComboData[] cds = widget.getViewModels().getComboDatas();
		for (int j = 0; j < cds.length; j++) {
			ComboData cd = cds[j];
			cd.setRendered(true);
			StringBuffer buf = new StringBuffer();
			String id = COMBO_PRE + widget.getId() + "_" + cd.getId();
			buf.append("window.")
			   .append(id)
		       .append(" = new ComboData(\"")
		       .append(cd.getId())
		       .append("\");\n");
			CombItem[] items = cd.getAllCombItems();
			if(items != null){
				for (int i = 0; i < items.length; i++) {
					buf.append(id)
					   .append(".addItem(new ComboItem(\"")
					   .append(translate(items[i].getI18nName(), items[i].getText(), items[i].getLangDir()))
					   .append("\",\"") 
					   .append(items[i].getValue())
					   .append("\"));\n");
				}
			}
			
			buf.append(getVarShowId())
			   .append(".addComboData(")
			   .append(id)
			   .append(");\n");
			
			buf.append(id)
			   .append(".widget = ")
			   .append(getVarShowId())
			   .append(";\n");
			addToBodyScript(buf.toString());
		}
	}
	
	protected String generateRefNodeScript(LfwWidget widget, IRefNode currRefNode){
		RefNode refNode = (RefNode) currRefNode;
		//String whereFields = "";
		// 如果relation为空,则按照fields来取
		String readFields = StringUtil.mergeScriptArray(refNode.getReadFields());
		String writeFields = StringUtil.mergeScriptArray(refNode.getWriteFields());
		//if((readFields == null || readFields.equals("")) && (writeFields == null || writeFields.equals("")))
			//throw new LfwRuntimeException("无法找到refNode:" + refNode.getId() + "指定的relationId:" + refNode.getRelationId());
		
		StringBuffer buf = new StringBuffer(1024);
//		 RefNodeInfo(id, name, pageMeta, path, readDs, writeDs, readFields, 
//					writeFields, filterSql, userObj, multiSel, usePower, selLeafOnly, refreshRefPage, isDialog)
		String refId = RF_PRE + widget.getId() + "_" + refNode.getId();
		buf.append("window.")
		   .append(refId)
		   .append(" = ")
		   .append("new RefNodeInfo(\"")
		   .append(refNode.getId())
		   .append("\",\"")
		   .append(translate(refNode.getI18nName(), refNode.getText(), refNode.getLangDir()))
		   .append("\",\"")
		   .append(refNode.getPagemeta())
		   .append("\",\"")
		   .append(refNode.getPath())
		   .append("\",\"")
		   .append(refNode.getReadDs())
		   .append("\",\"")
		   .append(refNode.getWriteDs() == null ? "" : refNode.getWriteDs())
		   .append("\",")
		   .append(readFields)
		   .append(",")
		   .append(writeFields)
		   .append(",\"")
		   .append("") //filterSql
		   .append("\",\"")
		   .append(/*refNode.getUserObj() == null ? "": refNode.getUserObj()*/"")
		   .append("\",")
		   .append(refNode.isMultiSel())
		   .append(",")
		   .append(false) //use power
		   .append(",")
		   .append(refNode.isSelLeafOnly())
		   .append(",")
		   .append(refNode.isRefresh())
		   .append(",")
		   .append(refNode.isDialog())
		   .append(",")
		   .append(refNode.isAllowInput())
		   .append(")\n");
		
		buf.append(getVarShowId())
		   .append(".addRefNode(")
		   .append(refId)
		   .append(");\n");
		
		buf.append(refId);
		   buf.append(".widget = ")
		   .append(getVarShowId())
		   .append(";\n");
		addToBodyScript(buf.toString());
		return buf.toString();
	}
	
	/**
	 * 生成Dataset对应脚本文件
	 * @param ds
	 * @return
	 */
	protected String generateDatasetScript(LfwWidget widget, Dataset ds)
	{
		StringBuffer buf = new StringBuffer();
		String dsVarId = getDatasetVarShowId(ds.getId(), widget.getId());
//		buf.append("var " + dsVarId)
		buf.append(dsVarId)
		   .append(" = new Dataset(\"")
		   .append(ds.getId())
		   .append("\",");
//		if(!ds.isLazyLoad())
//		   buf.append("document.getElementById(\"")
//		      .append(ds.getId())
//		      .append("\")");
		   	
//		else
//		   buf.append("null");
		String metaStr = DatasetMetaUtil.generateMeta(ds);
		buf.append(metaStr);
		buf.append(",")
			.append(ds.isLazyLoad())
			.append(",")
			.append(ds.isEnabled())
			.append(",")
			.append(ds.getPageSize())
			.append(");\n");
		
		// 设置ds的操作状态
//		String operatorStatusArray = ds.getOperatorStatusArray();
//		if(operatorStatusArray != null && !operatorStatusArray.equals(""))
//			buf.append(dsVarId + ".operateStateArray=" + StringUtil.mergeScriptArray(operatorStatusArray) + ";\n");
		
		//buf.append(addEventSupport(ds, ds.getId()));
		
		buf.append(getVarShowId())
		   .append(".addDataset(")
		   .append(dsVarId)
		   .append(");\n");
		
		buf.append(dsVarId)
		   .append(".widget = ")
		   .append(getVarShowId())
		   .append(";\n");
		
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
	
	
//	/**
//	 * 获取可在客户端运算的编辑公式
//	 * @param exp
//	 * @return
//	 */
//	private String getSimpleEditFormularArray(String args) {
//		if(args == null)
//			return null;
//		String[] exps = args.split("\\;");
//		fp.setExpressArray(exps);
//		StringBuffer outSb = new StringBuffer("[");
//		for(int i=0;i<exps.length;i++){
//			StringBuffer sb = new StringBuffer("{");
//			JEPExpression jep = fp.getExpressionFromCache(exps[i]);
//			if(!(jep.getTopNode() instanceof ASTFunNode))
//				return null;
//			//如果不是加减乘除，则返回
//			nc.vo.pub.formulaset.core.Operator op = ((ASTFunNode)jep.getTopNode()).getOperator(); 
////			if(op != "+" && op!="-" && op!="*" && op!="/")
//			if(op == null)
//				return null;
//			sb.append("key:'" + jep.getLeftName() + "',")
//			  .append("formular:[");
//			if(getNodeArray(jep.getTopNode(),sb)){
//				continue;
//			}
//				
//			if(sb == null)
//				return null;
//			sb.append("]");
//			if(i != exps.length-1)
//				sb.append("},");
//			else
//				sb.append("}");
//			outSb.append(sb);
//		}
//		outSb.append("]");
//		return outSb.toString();
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isRenderDiv() {
		return renderDiv;
	}

	public void setRenderDiv(boolean renderDiv) {
		this.renderDiv = renderDiv;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		if(ele instanceof LfwWidget)
			return LfwPageContext.SOURCE_TYPE_WIDGT;
		else if(ele instanceof Dataset)
			return LfwPageContext.SOURCE_TYPE_DATASET;
		return null;
	}

	@Override
	protected String getExtendProxyStr(WebElement ele, JsListenerConf listener,
			EventHandlerConf jsEvent) {
		if(ele instanceof Dataset){
			if(jsEvent.getName().equals("onDataLoad")){
				StringBuffer buf = new StringBuffer();
				buf.append("if(dataLoadEvent.userObj != null)\n")
				   .append("proxy.setReturnArgs(dataLoadEvent.userObj);\n");
				return buf.toString();
			}
		}
		return null;
	}
	
	@Override
	protected void addProxyParam(StringBuffer buf, String eventName) {
		super.addProxyParam(buf, eventName);
		if(eventName.equals("onAfterRowInsert")){
			buf.append("proxy.addParam('row_insert_index', rowInsertEvent.insertedIndex);\n");
		}
	}
	
	public String getWidth() {
		return this.width;
	}
	
	public String getHeight() {
		return this.height;
	}

}

