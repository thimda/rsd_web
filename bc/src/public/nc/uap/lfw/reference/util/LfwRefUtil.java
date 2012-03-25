package nc.uap.lfw.reference.util;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.reference.base.ILfwRefModel;
import nc.uap.lfw.util.LfwClassUtil;


/**
 * 自定义参照的工具类，用户获取自定义参照的类实例以及类参照的类型信息。
 * @author lkp
 * 
 * dengjt 重构 
 * 将此工具类作为Lfw参照入口，由它来区分是否自定义，对外保持透明.去掉不规范抛出异常方法.将部分方法变为私有.这样可大量优化使用此类的代码逻辑
 */
public class LfwRefUtil {
	
	public static ILfwRefModel getRefModel(RefNode refnode){
		IRefModelFetcher fetcher = null;
		if(refnode instanceof NCRefNode)
			fetcher = (IRefModelFetcher) LfwClassUtil.newInstance(LfwRuntimeEnvironment.getServerConfig().get("REFMODEL_NC"));
		else
			fetcher = new DefaultRefModelFetcher();
		
		return fetcher.getRefModel(refnode, null);
	}
	
//	public static int getRefType(String refCode){
//		if(isSelfDefineRef(refCode))
//			return getSelfRefType(refCode);
//		return getRefUtilService().getRefType(getRefUtilService().getRefModel(refCode));
//	}
	
	public static int getRefType(ILfwRefModel model) {
		return model.getRefType().intValue();
	}
	
	
//	public static String getFieldShowName(AbstractRefModel model,
//			String fieldCode) {
//		int index = model.getFieldIndex(fieldCode);
//		if (index < 0 || index >= getAllColumnNames(model).size()) {
//			return null;
//		}
//		return (String) getAllColumnNames(model).elementAt(index);
//	}
	
//	public static Vector getAllColumnNames(AbstractRefModel refModel) {
//		return getRefUtilService().getAllColumnNames(refModel);
//	}
	
	/**
	 * 根据key填充参照对应的显示值
	 * @param widget
	 * @param ref
	 */
	public static void fetchRefShowValue(LfwWidget widget, ReferenceComp ref){
		String value = ref.getValue();
		if(value == null || value.equals("")){
			ref.setShowValue(null);
			return;
		}
		String showValue = ref.getShowValue();
		if(showValue != null && !showValue.equals(""))
			return;
		if(showValue == null || showValue.equals("")){
			String refCode = ref.getRefcode();
			if(refCode != null){
				IRefNode refNo = (IRefNode) widget.getViewModels().getRefNode(refCode);
				if(refNo instanceof RefNode){
					RefNode refnode = (RefNode) widget.getViewModels().getRefNode(refCode);
					ILfwRefModel refmodel = LfwRefUtil.getRefModel(refnode);
					String name = refmodel.matchRefName(value);
					ref.setShowValue(name);
				}
			}
		}
	}
}
