package nc.uap.lfw.core.processor;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.log.LfwLogger;
/**
 * 抽象化的请求处理基类。将一般的业务处理分为三步：
 * 1.处理请求模型
 * 2.业务逻辑调用
 * 3.处理返回模型
 * @author dengjt
 *
 */
public abstract class RequestProcessorBase<T> implements IRequestProcessor{
	//模型对象
	private T modelObject;
	//业务处理返回对象,可以返回任何可序列化对象
	private Object result;
	public String[] doProcess() {
		if(LfwLogger.isDebugEnabled()){
			LfwLogger.debug("进入Processor方法：" + this.getClass().getName());
			LfwLogger.debug("开始数据处理");
		}
		//处理数据模型
		modelObject = processModel();
		if(LfwLogger.isDebugEnabled()){
			LfwLogger.debug("结束数据处理");
			LfwLogger.debug("开始业务处理");
		}
		result = process();
		if(LfwLogger.isDebugEnabled()){
			LfwLogger.debug("结束业务处理处理");
			LfwLogger.debug("开始返回值序列化");
		}
		String[] returnStr = generateReturnString(result);
		
		if(LfwLogger.isDebugEnabled()){
			LfwLogger.debug("结束返回值序列化");
		}
		return returnStr;
	}
	
	protected T getModelObject() {
		return modelObject;
	}
	
	/**
	 * 根据业务返回结果，构造返回值
	 * @param result
	 * @return
	 */
	protected abstract String[] generateReturnString(Object result);

	/**
	 * 构造请求模型对象
	 * @return
	 */
	protected abstract T processModel();
	
	/**
	 * 业务处理调用
	 * @return
	 */
	protected abstract Object process();

	/**
	 * 获取当前请求信息
	 * @return
	 */
	protected WebContext getWebContext() {
		return LfwRuntimeEnvironment.getWebContext();
	}
	
}
