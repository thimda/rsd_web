/**
 * 
 */
package nc.uap.lfw.core.comp.ctx;

/**
 * @author zjx
 * 
 */
public class SilverlightWidgetContext extends BaseContext {
	private static final long serialVersionUID = 7452950515877391657L;
	private Boolean _bDataTransfer = false;

	public void setBDataTransfer(Boolean bt) {
		this._bDataTransfer = bt;
	}

	public Boolean getBDataTransfer() {
		return _bDataTransfer;
	}
}
