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
public interface IPropertyInfo extends Serializable{
	
	public String getDefaultValue();
	public String getType();
	public String getValue();
	public String getId();
	public String getDsField();
	public String getLabel();
	public String getVoField();
	public boolean isVisible();
	public boolean editable();
	
}
