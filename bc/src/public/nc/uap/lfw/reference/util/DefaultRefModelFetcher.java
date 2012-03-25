package nc.uap.lfw.reference.util;

import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.reference.base.ILfwRefModel;

public class DefaultRefModelFetcher implements IRefModelFetcher {

	public ILfwRefModel getRefModel(RefNode refnode, Map<String, Object> param) {
		if(refnode instanceof RefNode){
			String refModel = refnode.getRefModel();
			try {
				return (ILfwRefModel) Class.forName(refModel).newInstance();
				//return (ILfwRefModel)Class.forName(refModel).getConstructor(RefNode.class).newInstance(refnode);
			} catch (InstantiationException e) {
				Logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				Logger.error(e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				Logger.error(e.getMessage(), e);
			} catch (SecurityException e) {
				Logger.error(e.getMessage(), e);
			} 
//			catch (NoSuchMethodException e) {
//				Logger.error(e.getMessage(), e);
//			} catch (IllegalArgumentException e) {
//				Logger.error(e.getMessage(), e);
//			} catch (InvocationTargetException e) {
//				Logger.error(e.getMessage(), e);
//			};
		}
		return null;
	}

}
