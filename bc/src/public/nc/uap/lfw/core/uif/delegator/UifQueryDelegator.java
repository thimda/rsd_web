package nc.uap.lfw.core.uif.delegator;

import java.util.Properties;

import nc.bs.ml.NCLangResOnserver;
import nc.uap.lfw.core.AbstractPresentPlugin;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.ParamConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.util.PageNodeUtil;


public class UifQueryDelegator extends AbstractUifDelegator implements
		IUifDeletator {
	private String qryPageId;
	private String templateId;
	private String page;
	private String pWidgetId;
	private String pDatasetId;
	public UifQueryDelegator(String qryPageId, String page, String templateId, String pwidgetId, String pdatasetId) {
		super();
		this.qryPageId = qryPageId;
		this.templateId = templateId;
		this.page = page;
		this.pWidgetId = pwidgetId;
		this.pDatasetId = pdatasetId;
	}
	
	public void execute() {
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget(pWidgetId);
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if(dsRels != null){
			DatasetRelation[] rels = dsRels.getDsRelations(pDatasetId);
			if(rels != null){
				for (int i = 0; i < rels.length; i++) {
					String detailDsId = rels[i].getDetailDataset();
					Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
					if(detailDs.getCurrentRowData() != null)
						detailDs.getCurrentRowData().clear();
				}
			}
		}
		
		String pageUniqueId = LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
		String url = LfwRuntimeEnvironment.getCorePath() + "/" + page + "?pageId=" + this.qryPageId + "&" + ParamConstant.OTHER_PAGE_UNIQUE_ID + "=" + pageUniqueId;
		if(templateId != null)
			url += "&template=" + templateId + "&pwid=" + this.pWidgetId + "&pdsid=" + this.pDatasetId;
		
		String pagePath = PageNodeUtil.getPageNodeDir(qryPageId.replace("\\.", "\\"));
		String nodePath = "html/nodes/" + pagePath + "/node.properties";
		Properties props = AbstractPresentPlugin.loadNodePropertie(nodePath);
		String qryPageModel = null;
		if(props != null)
			qryPageModel = (String) props.get("QUERY_PAGEMODEL");
		//处理查询的pagemdoel
		if(qryPageModel != null)
			url += "&model=" + qryPageModel;
		else
			url += "&model=nc.uap.lfw.core.uif.delegator.UifQueryPageModel";
		String queryName = NCLangResOnserver.getInstance().getString("lfw", "查询", "query_query");
		getGlobalContext().showModalDialog(url, queryName, getWidth(), getHeight(), "query", false, false);
	}
	
	protected String getWidth() {
		return "800";
	}
	
	protected String getHeight() {
		return "500";
	}
}
