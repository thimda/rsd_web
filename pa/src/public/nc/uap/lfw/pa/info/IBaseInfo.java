/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.io.Serializable;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public interface IBaseInfo extends Serializable{
	public IPropertyInfo[] getPropertyInfos();
}
