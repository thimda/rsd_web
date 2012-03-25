package nc.uap.lfw.login.itf;


import javax.servlet.http.Cookie;

import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.vo.LfwSessionBean;

/**
 * ��¼�߼��ṩ�ţ�����¼Э�����
 * @author dengjt
 *
 */
public interface ILoginHandler <K extends LfwSessionBean>{
	/**
	 * ��ȡҪУ����û�VO,���ݾ���ҵ��У����Ҫ������Ҫ��Ϣ���õ���VO��
	 */
	public AuthenticationUserVO getAuthenticateVO() throws LoginInterruptedException;
	
	
	/**
	 * У���û����������û���Ӧ�ĻỰ��Ϣ����
	 * @throws LoginInterruptedException 
	 */
	public K doAuthenticate(AuthenticationUserVO userVO) throws LoginInterruptedException;
	
	/**
	 * ��ȡ������Ϣʵ��
	 */
	public ILoginSsoService<K> getLoginSsoService();
	
	
	/**
	 * ʵ��cookie�����Ը���������ʵ���������cookie��¼���ͻ���
	 */
	public Cookie[] getCookies(AuthenticationUserVO userVO);
	
	/**
	 * ��ȡϵͳ���͡���ϵͳ���Ϳ�����Լ������
	 * @return
	 */
	public String getSysType();
	
}
