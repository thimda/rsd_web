package nc.uap.lfw.core.processor;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.log.LfwLogger;
/**
 * ���󻯵���������ࡣ��һ���ҵ�����Ϊ������
 * 1.��������ģ��
 * 2.ҵ���߼�����
 * 3.������ģ��
 * @author dengjt
 *
 */
public abstract class RequestProcessorBase<T> implements IRequestProcessor{
	//ģ�Ͷ���
	private T modelObject;
	//ҵ�����ض���,���Է����κο����л�����
	private Object result;
	public String[] doProcess() {
		if(LfwLogger.isDebugEnabled()){
			LfwLogger.debug("����Processor������" + this.getClass().getName());
			LfwLogger.debug("��ʼ���ݴ���");
		}
		//��������ģ��
		modelObject = processModel();
		if(LfwLogger.isDebugEnabled()){
			LfwLogger.debug("�������ݴ���");
			LfwLogger.debug("��ʼҵ����");
		}
		result = process();
		if(LfwLogger.isDebugEnabled()){
			LfwLogger.debug("����ҵ������");
			LfwLogger.debug("��ʼ����ֵ���л�");
		}
		String[] returnStr = generateReturnString(result);
		
		if(LfwLogger.isDebugEnabled()){
			LfwLogger.debug("��������ֵ���л�");
		}
		return returnStr;
	}
	
	protected T getModelObject() {
		return modelObject;
	}
	
	/**
	 * ����ҵ�񷵻ؽ�������췵��ֵ
	 * @param result
	 * @return
	 */
	protected abstract String[] generateReturnString(Object result);

	/**
	 * ��������ģ�Ͷ���
	 * @return
	 */
	protected abstract T processModel();
	
	/**
	 * ҵ�������
	 * @return
	 */
	protected abstract Object process();

	/**
	 * ��ȡ��ǰ������Ϣ
	 * @return
	 */
	protected WebContext getWebContext() {
		return LfwRuntimeEnvironment.getWebContext();
	}
	
}
