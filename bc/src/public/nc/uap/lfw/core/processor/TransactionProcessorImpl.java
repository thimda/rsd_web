package nc.uap.lfw.core.processor;



/**
 * 用于事务处理的处理器类实现。
 * 
 */
public class TransactionProcessorImpl implements ITransactionProcessor {

	@SuppressWarnings("unchecked")
	public Object doIt(AbstractRequestProcessor trueProcessor) {
		return trueProcessor.doIt();
	}
}
