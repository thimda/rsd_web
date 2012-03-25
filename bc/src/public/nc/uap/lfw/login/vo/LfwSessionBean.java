package nc.uap.lfw.login.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public abstract class LfwSessionBean extends ExtendAttributeSupport implements
		Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FEATURE_CLIENT_PRINT = "FEATURE_CLIENT_PRINT";
	public static final String FEATURE_CLIENT_CACHE = "FEATURE_CLIENT_CACHE";
	public static final String CLIENT_PRINTERS = "client_printers";

	/**
	 * ���ڼ�¼�ͻ����Ƿ�֧��ĳ������,����˵�ͻ��˻���
	 */
	private Map<String, UFBoolean> clientSupportMap;
	/**
	 * �����¼����
	 */
	private String ssotoken;
	
	private TimeZone timeZone;

	public byte getSysid() {
		return sysid;
	}

	public void setSysid(byte sysid) {
		this.sysid = sysid;
	}

	/**
	 * �ͻ��˴�ӡ���б�
	 */
	private String[] printers;
	
	private byte sysid;
	/**
	 * masker����� pk_formatdoc + ts
	 */
	private String maskerkey;
	public abstract UFDate getLoginDate();

	public abstract String getPk_user();

	public abstract String getUser_code();

	public abstract String getUser_name();

	public abstract String getPk_unit();

	public abstract String getThemeId();

	public abstract String getLangId();

	public abstract String getDatasource();

	public String[] getClientPrinters() {
		return printers;
	}

	public void setFeatureSupport(String feature, UFBoolean support) {
		if (clientSupportMap == null) {
			clientSupportMap = new HashMap<String, UFBoolean>();
		}
		clientSupportMap.put(feature, support);
	}

	public boolean isFeatureSupport(String feature) {
		if (clientSupportMap == null)
			return false;
		UFBoolean support = clientSupportMap.get(feature);
		return support != null && support.booleanValue();
	}

	/**
	 * ��õ����½ƾ��
	 * 
	 * @param serverIp
	 *          ���������IP
	 * @return
	 */
	public String getSsotoken(String serverIp) {
		return serverIp + "_" + ssotoken + "_" + getDatasource().replace("_", "~");
	}

	/**
	 * ��õ����½ƾ��
	 * 
	 * @return
	 */
	public String getSsotoken() {
		String serverIp = LfwRuntimeEnvironment.getServerAddr();
		if (serverIp == null || serverIp.equals("")) {
			serverIp = LfwRuntimeEnvironment.getWebContext().getRequest()
					.getServerName();
			int port = LfwRuntimeEnvironment.getWebContext().getRequest()
					.getServerPort();
			if (port != 80)
				serverIp = serverIp + ":" + port;
		}
		return serverIp + "_" + ssotoken  + "_" + getDatasource().replace("_", "~");
	}

	public void setSsotoken(String ssotoken) {
		this.ssotoken = ssotoken;
	}

	public void setClientPrinters(String[] printers) {
		this.printers = printers;
	}

	/**
	 * ���±�����Ϣ����Щϵͳ�����Ǵ�LfwRuntimeEnvironment��ȡ�������������������Ҫ������ӳ�䵽���廷���С�
	 * ������ÿ������Դ���󶼻����
	 */
	public void fireLocalEnvironment() {
	}

	public String getMaskerkey() {
		return maskerkey;
	}

	public void setMaskerkey(String maskerkey) {
		this.maskerkey = maskerkey;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
}
