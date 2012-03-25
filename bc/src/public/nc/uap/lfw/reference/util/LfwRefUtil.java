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
 * �Զ�����յĹ����࣬�û���ȡ�Զ�����յ���ʵ���Լ�����յ�������Ϣ��
 * @author lkp
 * 
 * dengjt �ع� 
 * ���˹�������ΪLfw������ڣ������������Ƿ��Զ��壬���Ᵽ��͸��.ȥ�����淶�׳��쳣����.�����ַ�����Ϊ˽��.�����ɴ����Ż�ʹ�ô���Ĵ����߼�
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
	 * ����key�����ն�Ӧ����ʾֵ
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
