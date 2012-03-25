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
 * 自定义参照的工具类，用户获取自定义参照的类实例以及类参照的类型信息。
 * @author lkp
 * 
 * dengjt 重构 
 * 将此工具类作为Lfw参照入口，由它来区分是否自定义，对外保持透明.去掉不规范抛出异常方法.将部分方法变为私有.这样可大量优化使用此类的代码逻辑
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
			Logger.error("没有找到对应的参照model:" + refCode +",数据源:" + LfwRuntimeEnvironment.getUserDataSource());
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
	 * 获取自定义参照的数据模型类，它以<>标识，中间的类即为自定义参照类名称。
	 * 异常被抛出，会在RefWrapperImpl中返回给前台参照显示相应错误。
	 * 
	 * @param selfRefCode
	 * @return
	 */
	private static AbstractRefModel getSelfRefModel(String selfRefCode)
	{
		//去除标识为自定义参照的<>
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
	 * 获取自定义参照类的类型，即是属于树型、表型还是树表型。
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
