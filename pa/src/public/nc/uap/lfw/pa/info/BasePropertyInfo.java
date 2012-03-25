/**
 * 
 */
package nc.uap.lfw.pa.info;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public abstract class BasePropertyInfo implements IPropertyInfo{
	private static final long serialVersionUID = -7273347104924710780L;
	private String defaultValue;
	private String id;
	private String value;
	private String dsField;
	private String label;
	private boolean visible;
	private boolean editable;
	private String type;
	
	private String voField;
	
	@Override
	public String getDefaultValue() {
		return defaultValue;
	}
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getValue() {
		return value;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDsField() {
		return dsField;
	}

	public void setDsField(String dsField) {
		this.dsField = dsField;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getLabel() {
		return label == null ? id : label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getVoField() {
		return voField;
	}

	public void setVoField(String voField) {
		this.voField = voField;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	
}
