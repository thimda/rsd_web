package nc.uap.lfw.core.comp;

import javax.servlet.http.HttpServlet;

import com.meterware.pseudoserver.HttpRequest;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.SilverlightWidgetContext;

/**
 * @author zjx Silverlight island abstract control
 */

public class WebSilverlightWidget extends WebComponent {
	private static final long serialVersionUID = -7707560363856298325L;
	private String _strurl =  "/v61test/xap/Zune3D.xap"; // set 
														 									// a
																							// default
																							// value
																							// for
																							// example
	private String _strInstanceName = "PhoneInstance";

	/**
	 * @param istancename
	 * ����silverlight ʵ����
	 */
	public void setInstanceName(String istancename) {
		this._strInstanceName = istancename;
	}

	/**
	 * @return
	 * ��ȡsilverlight ʵ����
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
		
		return  this._strurl;
	}

	@Override
	public BaseContext getContext() {
		// TODO Auto-generated method stub
		SilverlightWidgetContext c = new SilverlightWidgetContext();
		return c;
	}
}
