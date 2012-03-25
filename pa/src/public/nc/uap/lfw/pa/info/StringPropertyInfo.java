/**
 * 
 */
package nc.uap.lfw.pa.info;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public class StringPropertyInfo extends BasePropertyInfo {

	private static final long serialVersionUID = 1L;

	@Override
	public String getType() {
		return StringDataTypeConst.STRING;
	}

	@Override
	public boolean editable() {
		// TODO Auto-generated method stub
		return false;
	}
}
