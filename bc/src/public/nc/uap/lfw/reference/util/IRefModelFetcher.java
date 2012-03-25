package nc.uap.lfw.reference.util;

import java.util.Map;

import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.reference.base.ILfwRefModel;

public interface IRefModelFetcher {
	public ILfwRefModel getRefModel(RefNode refnode, Map<String, Object> param);
}
