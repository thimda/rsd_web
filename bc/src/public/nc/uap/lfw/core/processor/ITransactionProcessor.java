package nc.uap.lfw.core.processor;




/**
 * 
 * ������Ҫ�𵽴�������ã�Ϊprocessor�Ĵ�����������֧�֡�
 * 
 */
public interface ITransactionProcessor {
	/**
	 * ִ���������ͨ�ò���
	 * @param trueProcessor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	Object doIt(AbstractRequestProcessor trueProcessor);
}
