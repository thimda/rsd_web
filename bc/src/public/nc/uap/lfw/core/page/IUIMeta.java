package nc.uap.lfw.core.page;

import java.io.Serializable;

/**
 * UIMeta 标识性接口
 * @author dengjt
 *
 */
public interface IUIMeta extends Serializable{
	/**
	 * 进行clone操作
	 * @return
	 */
	public IUIMeta doClone();
	/**
	 * 运行时内部调整
	 */
	public void adjustForRuntime();
}
