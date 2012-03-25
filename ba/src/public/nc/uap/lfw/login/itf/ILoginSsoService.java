package nc.uap.lfw.login.itf;

import nc.uap.lfw.login.vo.LfwSessionBean;

/**
 * ���ڽ�������Ϣ��ŵ������ɷ���λ�ã�һ��������ͬctx��webӦ�ÿɹ������Ϣ���Դﵽ�����¼��Ŀ�ġ� һ����˵��λ��ͨ��cookie��ʵ�֡�
 * 
 * @author dengjt
 */
public interface ILoginSsoService<T extends LfwSessionBean> {
	/**
	 * ����¼��Ϣ��ŵ���������
	 * 
	 * @param ssoInfo
	 *            ,��¼��Ϣ
	 * @param systype
	 *            ,�໥�ܵ����¼��ϵͳԼ����ϵͳ����
	 */
	public void addSsoSign(T ssoInfo, String systype);

	/**
	 * �ظ���¼��Ϣ
	 * 
	 * @param systype
	 *            ������ϵͳ���ͻظ���¼��Ϣ
	 */
	public T restoreSsoSign(String systype);

	public void afterLogin(LfwSessionBean userVO);

}
