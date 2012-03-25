package nc.lfw.core.md.util;

import java.util.Map;

import nc.md.model.IBusinessEntity;

public final class LfwMdUtil {
	public static String getMdItfAttr(IBusinessEntity entity, String itfname, String key){
		Map<String,String> interMap = entity.getBizInterfaceMapInfo(itfname);
		if(interMap != null && !(interMap.isEmpty())){
			return interMap.get(key);
		}
		return null;
	}
}
