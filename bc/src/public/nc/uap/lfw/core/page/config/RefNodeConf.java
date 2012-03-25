package nc.uap.lfw.core.page.config;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.refnode.BaseRefNode;

/**
 * ≤Œ’’≈‰÷√¿‡
 * @author dengjt
 *
 */
public class RefNodeConf extends BaseRefNode{

	private static final long serialVersionUID = -122517061682893211L;
	private String pageMeta;
	private String readDs;
	private boolean multiSel;
	private boolean selLeafOnly = false;
	private boolean clientCache = true;
	private String refModel;
	private String pageModel;// = "nc.uap.lfw.reference.model.DefaultReferencePageModel";
	private String dataListener;
	private boolean allowInput = false;
	
	public boolean isAllowInput() {
		return allowInput;
	}
	public void setAllowInput(boolean allowInput) {
		this.allowInput = allowInput;
	}
	public String getReadDs() {
		return readDs;
	}
	public void setReadDs(String readDs) {
		this.readDs = readDs;
	}
	
	public String getRefModel() {
		return refModel;
	}
	public void setRefModel(String refModel) {
		this.refModel = refModel;
	}

	public String getPagemeta() {
		return pageMeta;
	}
	public void setPagemeta(String pageMeta) {
		this.pageMeta = pageMeta;
	}

	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	public boolean isMultiSel() {
		return multiSel;
	}
	public void setMultiSel(boolean multiSel) {
		this.multiSel = multiSel;
	}

	public boolean isSelLeafOnly() {
		return selLeafOnly;
	}
	public void setSelLeafOnly(boolean selLeafOnly) {
		this.selLeafOnly = selLeafOnly;
	}
	
	public boolean isClientCache() {
		return clientCache;
	}
	public void setClientCache(boolean clientCache) {
		this.clientCache = clientCache;
	}
	
	public String getPageModel() {
		return pageModel;
	}
	public void setPageModel(String pageModel) {
		this.pageModel = pageModel;
	}
	public String getDataListener() {
		return dataListener;
	}
	public void setDataListener(String dataListener) {
		this.dataListener = dataListener;
	}
}

