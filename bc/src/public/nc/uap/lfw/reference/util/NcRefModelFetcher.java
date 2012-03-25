package nc.uap.lfw.reference.util;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.ncadapter.billtemplate.ref.LfwNCRefUtil;
import nc.uap.lfw.reference.base.ILfwRefModel;
import nc.uap.lfw.reference.nc.NcAdapterGridRefModel;
import nc.uap.lfw.reference.nc.NcAdapterTreeGridRefModel;
import nc.uap.lfw.reference.nc.NcAdapterTreeRefModel;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.IRefUtilService;

/**
 * NC参照的获取类
 * @author gd
 *
 */
public class NcRefModelFetcher implements IRefModelFetcher {
	//数据权限标识，根据数据权限过滤参照数据
	private static final  String DataPowerOperation_Code = "dataPowerOperation_Code";
	public ILfwRefModel getRefModel(RefNode refnode, Map<String, Object> param) {
		NCRefNode ncrefNode = (NCRefNode) refnode;
		String refCode = ncrefNode.getRefcode();
		AbstractRefModel refModel = LfwNCRefUtil.getRefModel(refCode);
		ExtAttribute dataPowderCode = refnode.getExtendAttribute(DataPowerOperation_Code);
		if(dataPowderCode != null){
			refModel.setDataPowerOperation_code((String) dataPowderCode.getValue());
		}
		int refType = getRefType(refModel);
		if(refType == IRefConst.GRID){
			NcAdapterGridRefModel gridModel = new NcAdapterGridRefModel();
			gridModel.setNcModel(refModel);
			return gridModel;
		}
		else if(refType == IRefConst.TREE)
		{
			NcAdapterTreeRefModel treeModel = new NcAdapterTreeRefModel();
			treeModel.setNcModel((AbstractRefTreeModel)refModel);
			return treeModel;
		}
		else{			NcAdapterTreeGridRefModel treeGridModel = new NcAdapterTreeGridRefModel();
			treeGridModel.setNcModel((AbstractRefGridTreeModel) refModel);
			return treeGridModel;
		}
	}
	
	private IRefUtilService getRefUtilService() {
		return (IRefUtilService) NCLocator.getInstance().lookup(IRefUtilService.class.getName());
	}
	
	public int getRefType(AbstractRefModel model) {
		return getRefUtilService().getRefType(model);
	}
}
