/**
 * 
 */
package nc.uap.lfw.jsp.uimeta;

/**
 * @author zjx Silverlight island ui meta
 */
public class UISilverlightWidget extends UIComponent {

	private static final long serialVersionUID = 5649352424920442579L;
	private String _strurl = "xap/Zune3D.xap"; // set
	// a
	// default
	// value
	// for
	// example
	private String _strInstanceName = "PhoneInstance";

	/**
	 * @param istancename
	 *            ����silverlight ʵ����
	 */
	public void setInstanceName(String istancename) {
		this._strInstanceName = istancename;
	}

	/**
	 * @return ��ȡsilverlight ʵ����
	 */
	public String getInstanceName() {
		return this._strInstanceName;
	}

	/**
	 * @param strurl
	 *            ���� silverlight xap λ��
	 */
	public void setUrl(String strurl) {
		this._strurl = strurl;
	}

	/**
	 * @return silverlight xap λ��
	 */
	public String getUrl() {

		return this._strurl;
	}

}
