package nc.uap.lfw.reference.nc;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * NC参照接口标识
 *
 */
public interface INcRefModel {
	/**
	 * 获得NC参照实现类
	 * @return
	 */
	public AbstractRefModel getNcModel();
}
