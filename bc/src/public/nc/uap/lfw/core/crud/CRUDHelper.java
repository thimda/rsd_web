package nc.uap.lfw.core.crud;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.ncadapter.crud.NCCrudServiceImpl;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class CRUDHelper {
	private static Map<String, ILfwCRUDService<?, ?>> serviceMap = new HashMap<String, ILfwCRUDService<?, ?>>();
	private static ILfwCRUDService<?, ?> getCRUDService(String clazz) {
		if(serviceMap.get(clazz) == null){
			ILfwCRUDService<?, ?> crud = (ILfwCRUDService<?, ?>) LfwClassUtil.newInstance(clazz);
			serviceMap.put(clazz, crud);
		}
		return serviceMap.get(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static ILfwCRUDService<SuperVO, AggregatedValueObject> getCRUDService() {
		return (ILfwCRUDService<SuperVO, AggregatedValueObject>) getCRUDService(NCCrudServiceImpl.class.getName());
	}
}
