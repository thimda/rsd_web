package nc.uap.lfw.core.processor;



/**
 * WebFrame �����������˴���������Ajax�����������Ӧ�߼�����Json��ʽ���󣩡����еĴ������������Դ˽ӿ�.
 * �����ʵ����ÿһ�����󶼻����´��������ؿ���״̬����
 * ������������У�����Ҫ���׳�LfwRuntimeException����ʱ�쳣��ҵ���߼��������¼��־�������ﴦ���꣬�������ཫ��������Lfw����ʱ�쳣����־��¼
 * @author dengjt
 * 2007-1-10
 * @version 6.0
 */
public interface IRequestProcessor {
	/**
	 * �������󲢷��ش��������顣
	 * @param ctx
	 * @return
	 */
	public String[] doProcess(); 
	
}
