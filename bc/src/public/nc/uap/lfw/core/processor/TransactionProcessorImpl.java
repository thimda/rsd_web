package nc.uap.lfw.core.processor;



/**
 * ����������Ĵ�������ʵ�֡�
 * 
 */
public class TransactionProcessorImpl implements ITransactionProcessor {

	@SuppressWarnings("unchecked")
	public Object doIt(AbstractRequestProcessor trueProcessor) {
		return trueProcessor.doIt();
	}
}
