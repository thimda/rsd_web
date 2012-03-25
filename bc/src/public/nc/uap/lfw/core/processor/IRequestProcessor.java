package nc.uap.lfw.core.processor;



/**
 * WebFrame 请求处理器。此处理器负责Ajax请求的所有相应逻辑（除Json方式请求）。所有的处理器需派生自此接口.
 * 此类的实例在每一次请求都会重新创建。不必考虑状态问题
 * 此类操作过程中，出错要求抛出LfwRuntimeException运行时异常。业务逻辑出错，需记录日志的在这里处理完，外层调用类将不负责处理Lfw运行时异常的日志记录
 * @author dengjt
 * 2007-1-10
 * @version 6.0
 */
public interface IRequestProcessor {
	/**
	 * 处理请求并返回处理结果数组。
	 * @param ctx
	 * @return
	 */
	public String[] doProcess(); 
	
}
