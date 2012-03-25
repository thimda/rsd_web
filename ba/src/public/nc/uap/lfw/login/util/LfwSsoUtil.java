package nc.uap.lfw.login.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.SessionConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.vo.LfwTokenVO;
import nc.vo.framework.rsa.Encode;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.json.UnmarshallException;

/**
 * LFW�����½������
 * 
 * @author licza
 * 
 */
public class LfwSsoUtil {
	/**����**/
	protected static final String ACCOUNT = "account";
	/**��˾**/
	protected static final String PKCORP = "pkcorp";
	/**��½ʱ��**/
	protected static final String LOGINDATE = "logindate";
	/**ǿ�Ƶ�½**/
	protected static final String FORCE = "force";
	

	/** ����IP��ַ **/
	private static final String LOCALHOST = "127.0.0.1";
	/** SSO������IP��SSO token �ķָ��� **/
	private static final String TOKENSPLITER = "_";
	
	/** ������ **/
	private static final String CTX_PATTERN = "\\[CTX_\\d*\\]";
	/** IP��ַ **/
	private static final String IP_PATTERN = "\\[IP_\\d*\\]";
	/** SSO��½ƾ֤ **/
	private static final String TOKEN_PATTERN = "[SSO_TOKEN]";
	private static final String DATASOURCE = "datasource";
	/**
	 * ����URL�еĺ����</br>
	 * 
	 * @param url
	 * @return
	 */
	public static String processNode(String url) {
		if (url == null)
			return null;
		String token = LfwRuntimeEnvironment.getLfwSessionBean().getSsotoken();
		return replacePattern(replacePattern(url, CTX_PATTERN), IP_PATTERN)
				.replace(TOKEN_PATTERN, token);
	}

	/**
	 * �滻URL�еĺ�
	 * 
	 * @param url
	 * @param patternRule
	 * @return
	 */
	public static String replacePattern(String url, String patternRule) {
		Pattern pattern = Pattern.compile(patternRule);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String result = (matcher.group(i));
				String value = LfwRuntimeEnvironment.getModelServerConfig().getConfigValue(
						result);
				if(value != null)
					url = url.replace(result, value);
			}
		}
		return url;
	}


	/**
	 * ���SSO��������ַ
	 * @return
	 */
	public static String getServerIP() {
		String token = LfwRuntimeEnvironment.getWebContext().getParameter("token");
		// �����Զ����IP
		if (StringUtils.isNotEmpty(token) && token.indexOf(TOKENSPLITER) > 0) {
			return token.split(TOKENSPLITER)[0];
		}
		return LOCALHOST;
	}
	/**
	 * �����֤��Ϣ��
	 * @return
	 */
	public static String getTokenID() {
		String token = LfwRuntimeEnvironment.getWebContext().getParameter("token");
		// Token�а���IP��ַ
		if (StringUtils.isNotEmpty(token) && token.indexOf(TOKENSPLITER) > 0) {
			return token.split(TOKENSPLITER)[1];
		}
		return token;
	}
	/**
	 * �������Դ��ַ
	 * @return
	 */
	public static String getDsId(){
		String token = LfwRuntimeEnvironment.getWebContext().getParameter("token");
		// Token�а���IP��ַ
		if (StringUtils.isNotEmpty(token) && token.indexOf(TOKENSPLITER) > 2) {
			return token.split(TOKENSPLITER)[2];
		}
		return null;
	}

	/**
	 * ����Token��� ���token
	 * 
	 * @param tokenId
	 * @return
	 */
	public static LfwTokenVO getRmtToken(String serverIp, String tokenId,String ds) {
		String url = getRmtSsoUrl(serverIp, tokenId , ds);
		LfwLogger.info("===LfwSsoUtil=== Token����:" + url);
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LfwLogger.error("===LfwSsoUtil===����������");
				return null;
			}
			String jsonStr = getMethod.getResponseBodyAsString();

			LfwLogger.debug("===LfwSsoUtil===���л�VO");
			return (LfwTokenVO) LfwJsonSerializer.getInstance().fromJSON(
					jsonStr);
		} catch (HttpException e) {
			LfwLogger.error("===LfwSsoUtil===�����������쳣", e);
			return null;
		} catch (IOException e) {
			LfwLogger.error("===LfwSsoUtil===���������쳣", e);
			return null;
		} catch (UnmarshallException e) {
			LfwLogger.error("===LfwSsoUtil===JSON���л�����", e);
			return null;
		} finally {
			LfwLogger.debug("===LfwSsoUtil===�ͷ�����");
			getMethod.releaseConnection();
		}
	}

	/**
	 * ����LFWTokenVO���AuthenticationUserVO
	 */
	public static AuthenticationUserVO getAuthUserVO(LfwTokenVO token) {
		AuthenticationUserVO uservo = new AuthenticationUserVO();
		uservo.setUserID(token.getUserid());
		Encode encode=new Encode();
		uservo.setPassword(encode.decode(token.getPasswd()));
		Map<String, String> extinfo = new HashMap<String, String>();
		extinfo.put(ACCOUNT, token.getAccountcode());
		extinfo.put(PKCORP, token.getPkcrop());
		extinfo.put(LOGINDATE, token.getLogindate().getDate().toString());
		extinfo.put(FORCE, "true");
		extinfo.put(DATASOURCE, token.getDatasource());
		uservo.setExtInfo(extinfo);
		return uservo;
	}

	/**
	 * ��õ����������ַ
	 * 
	 * @return
	 */
	protected static String getRmtSsoUrl(String sso_server, String tokenId ,String ds) {
		StringBuffer sb = new StringBuffer("http://");
		sb.append(sso_server);
		sb.append(LfwRuntimeEnvironment.getLfwCtx());
		sb.append("/pt/sso/exchange?id=");
		sb.append(tokenId);
		if(StringUtils.isNotBlank(ds))
			sb.append("&ds="+(ds.replace("~", "_")));
		String url = sb.toString();
		LfwLogger.debug("===LfwSsoUtil#getRmtSsoUrl URL = : " + url);
		return url;
	}
	
	/**
	 * ��鵥���½�Ƿ��Ѿ�����
	 * @return
	 */
	public static boolean isSessionOverdue(){
	String exteriorToken = LfwSsoUtil.getTokenID();
	if(exteriorToken != null){
		HttpSession session = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
		String rootToken = (String) session.getAttribute(SessionConstant.ROOT_TOKEN_ID);
		boolean b = !exteriorToken.equals(rootToken);
		if(b){
			session.invalidate();
		}
		return b;
	} 
	return false;
	}

}
