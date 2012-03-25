package nc.uap.lfw.core.uif.delegator;

import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.lfw.core.md.util.LfwMdUtil;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * 联查审批单据Delegator
 * 
 * @author zhangxya
 * 
 */
public class UifQueryPfinfoDelegator extends AbstractUifDelegator implements
		IUifDeletator {

	private static final String IFLOW_BIZ_ITF = "nc.itf.uap.pf.metadata.IFlowBizItf";

	private String widgetId;
	private String masterDsId;
	private String pageId;

	public void execute() {
		if (this.pageId == null)
			throw new LfwRuntimeException("未指定页面id!");
		if (this.widgetId == null)
			throw new LfwRuntimeException("未指定片段id!");
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget(
				this.widgetId);
		if (widget == null)
			throw new LfwRuntimeException("片段为空,widgetId=" + widgetId + "!");
		Dataset ds = getMasterDs(widget);
		Row row = ds.getSelectedRow();
		if(row == null)
			throw new LfwRuntimeException("单据没有数据");
		try {
			String metaId = ds.getExtendAttributeValue(ExtAttrConstants.DATASET_METAID).toString();
			IBusinessEntity entity = MDQueryService.lookupMDQueryService()
					.getBusinessEntityByFullName(metaId);
			String pk_billtype = LfwMdUtil.getMdItfAttr(entity, IFLOW_BIZ_ITF, IFlowBizItf.ATTRIBUTE_BILLTYPE);
			String pk_bill = LfwMdUtil.getMdItfAttr(entity, IFLOW_BIZ_ITF, IFlowBizItf.ATTRIBUTE_BILLID);
			if (null != pk_billtype && null != pk_bill) {
				String pk_billtypeValue = (String) row.getValue(ds.nameToIndex(pk_billtype));
				String billValue = (String) row.getValue(ds.nameToIndex(pk_bill));
				String url = LfwRuntimeEnvironment.getCorePath()
						+ "/uimeta.um?pageId=pfinfo&billId="
						+ billValue
						+ "&billType="
						+ pk_billtypeValue
						+ "&model=nc.uap.lfw.billtemplate.workflow.WfInfoPageModel";
				getGlobalContext().showModalDialog(url, "联查审批情况", getWidth(),	getHeight(), "pfinfo", true, false);
			}
		} catch (MetaDataException e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}

	public UifQueryPfinfoDelegator(String pageId, String widgetId,
			String masterdsId) {
		super();
		this.pageId = pageId;
		this.widgetId = widgetId;
		this.masterDsId = masterdsId;
	}

	protected Dataset getMasterDs(LfwWidget widget) {
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		return masterDs;
	}

	protected String getWidth() {
		return "800";
	}

	protected String getHeight() {
		return "500";
	}
}
