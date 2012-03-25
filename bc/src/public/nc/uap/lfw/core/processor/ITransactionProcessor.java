package nc.uap.lfw.core.processor;




/**
 * 
 * 该类主要起到代理的作用，为processor的处理添加事务的支持。
 * 
 */
public interface ITransactionProcessor {
	/**
	 * 执行有事务的通用操作
	 * @param trueProcessor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	Object doIt(AbstractRequestProcessor trueProcessor);
}
