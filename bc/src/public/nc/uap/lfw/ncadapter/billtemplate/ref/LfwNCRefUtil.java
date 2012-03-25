package nc.uap.lfw.ncadapter.billtemplate.ref;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IRefConst;
import nc.ui.bd.ref.IRefUtilService;


/**
 * �Զ�����յĹ����࣬�û���ȡ�Զ�����յ���ʵ���Լ�����յ�������Ϣ��
 * @author lkp
 * 
 * dengjt �ع� 
 * ���˹�������ΪLfw������ڣ������������Ƿ��Զ��壬���Ᵽ��͸��.ȥ�����淶�׳��쳣����.�����ַ�����Ϊ˽��.�����ɴ����Ż�ʹ�ô���Ĵ����߼�
 */
public class LfwNCRefUtil {

	private static Map<String, AbstractRefModel> m_selfRefModel = new HashMap<String, AbstractRefModel>();
	
	private static Map<String, Integer> m_selfRefType = new HashMap<String, Integer>();
	
	private static IRefUtilService refUtilService;
	
	public static AbstractRefModel getRefModel(String refCode){
		AbstractRefModel refModel;
		if(isSelfDefineRef(refCode))
			refModel = getSelfRefModel(refCode);
		else
			refModel = getRefUtilService().getRefModel(refCode);
		if(refModel == null){
			Logger.error("û���ҵ���Ӧ�Ĳ���model:" + refCode +",����Դ:" + LfwRuntimeEnvironment.getUserDataSource());
		}
		return refModel;
	}
	
	public static int getRefType(String refCode){
		if(isSelfDefineRef(refCode))
			return getSelfRefType(refCode);
		return getRefUtilService().getRefType(getRefUtilService().getRefModel(refCode));
	}
	
	public static int getRefType(AbstractRefModel model) {
		return getRefUtilService().getRefType(model);
	}
	
	private static boolean isSelfDefineRef(String refCode){
		//return refCode.startsWith("self_");
		if(refCode != null)
			return refCode.startsWith("<") && refCode.endsWith(">");
		else 
			return false;
	}
	/**
	 * ��ȡ�Զ�����յ�����ģ���࣬����<>��ʶ���м���༴Ϊ�Զ�����������ơ�
	 * �쳣���׳�������RefWrapperImpl�з��ظ�ǰ̨������ʾ��Ӧ����
	 * 
	 * @param selfRefCode
	 * @return
	 */
	private static AbstractRefModel getSelfRefModel(String selfRefCode)
	{
		//ȥ����ʶΪ�Զ�����յ�<>
		String trueRefCode = selfRefCode.substring(1, selfRefCode.length()-1);//selfRefCode.substring(5, selfRefCode.length());
		m_selfRefModel.remove(trueRefCode);
		if(m_selfRefModel.get(trueRefCode) != null)
			return m_selfRefModel.get(trueRefCode);
		
		if(selfRefCode == null || "".equals(selfRefCode))
			return null;
		
		if(trueRefCode.equals(""))
			return null;
		
		try {
			Class c = Class.forName(trueRefCode);
			Constructor cs = c.getConstructor(new Class[]{String.class});
			AbstractRefModel model = (AbstractRefModel)cs.newInstance(new Object[]{trueRefCode});
			m_selfRefModel.put(trueRefCode, model);
			return model;
		} 
		catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		
		try {
			Class c = Class.forName(trueRefCode);
			Constructor cs = c.getConstructor();
			AbstractRefModel model = (AbstractRefModel)cs.newInstance();
			m_selfRefModel.put(trueRefCode, model);
			return model;
		} 
		catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * ��ȡ�Զ������������ͣ������������͡����ͻ��������͡�
	 * 
	 * @param selfRefCode
	 * @return
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static Integer getSelfRefType(String selfRefCode)
	{
		Integer type = null;
		if(m_selfRefType.get(selfRefCode) == null)
		{
			AbstractRefModel model = getSelfRefModel(selfRefCode);
			if(model instanceof AbstractRefGridTreeModel)
				type = IRefConst.GRIDTREE;
			else if(model instanceof AbstractRefTreeModel)
				type = IRefConst.TREE;
			else 
				type = IRefConst.GRID;
			m_selfRefType.put(selfRefCode, type);
		}
		type = m_selfRefType.get(selfRefCode);
		return type;
	}
	
	public static String getFieldShowName(AbstractRefModel model,
			String fieldCode) {
		int index = model.getFieldIndex(fieldCode);
		if (index < 0 || index >= getAllColumnNames(model).size()) {
			return null;
		}
		return (String) getAllColumnNames(model).elementAt(index);
	}
	
	public static Vector getAllColumnNames(AbstractRefModel refModel) {
		return getRefUtilService().getAllColumnNames(refModel);
	}
	
	private static IRefUtilService getRefUtilService() {
		if(refUtilService == null)
			refUtilService = (IRefUtilService) NCLocator.getInstance().lookup(IRefUtilService.class.getName());
		return refUtilService;
	}
}
