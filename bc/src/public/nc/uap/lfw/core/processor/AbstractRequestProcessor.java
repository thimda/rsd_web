package nc.uap.lfw.core.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.IObject2XmlSerializer;
import nc.uap.lfw.core.serializer.IXml2ObjectSerializer;
import nc.uap.lfw.core.serializer.impl.Dataset2XmlSerializer;
import nc.uap.lfw.core.serializer.impl.SingleDataset2XmlSerializer;
import nc.vo.ml.NCLangRes4VoTransl;
/**
 * ���V6ǰ�󽻻�Э����Ϣ��������ģ�͵Ľ�һ����װ
 * @author dengjt
 * @param <T>
 *
 */
public abstract class AbstractRequestProcessor<T> extends RequestProcessorBase<T> {

	private String requestXml = null;
	private String requestType = null;

	@Override
	protected Object process() {
		Object result = null;
		WebContext ctx = getWebContext();
		//�ɷ�����
		String type = ctx.getParameter("type");
		if(type == null)
			throw new LfwRuntimeException("û��ָ�����õķ���");
		
		String trans = ctx.getParameter("needTransaction");
		//Ĭ�ϼ�����
		boolean needTrans = (trans != null && trans.equals("1"));
		if(!needTrans)
			result = doIt();
		else{
			//ͨ��ejb�ص����Ա��������֧��
			result = getTransactionSupport().doIt(this);
		}
		return result;
	}	
	
	
	@SuppressWarnings("unchecked")
	protected Object doIt() {
		requestType = getWebContext().getParameter("type");
		if (requestType == null || requestType.trim().equals(""))
			return null;
		Method method = null;
		
		try {
			method = this.getClass().getMethod(requestType, new Class[] {});
			return (T) method.invoke(this, new Object[] {});
		} catch (NoSuchMethodException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "AbstractRequestProcessor-000001", null, new String[]{this.getClass().getName(), requestType})/*{0}��û���ҵ�������{1}*/, e);
		} catch (IllegalArgumentException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);

		} catch (IllegalAccessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);

		} catch (InvocationTargetException e) {
			LfwLogger.error(e.getMessage(), e);
			Throwable exp = e.getTargetException();
			if(exp instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e.getTargetException();
			if(exp instanceof LfwInteractionException)//�����쳣
				throw (LfwInteractionException)e.getTargetException();
			throw new LfwRuntimeException(e.getTargetException());
		}
	}
	

	@Override
	protected T processModel() {
		requestXml = getWebContext().getParameter("xml");
        if(requestXml == null || requestXml.trim().equals(""))
			return null;
        return getRequestSerializer().serialize(requestXml, null);
	}
	
	protected abstract IXml2ObjectSerializer<T> getRequestSerializer();
	
	@SuppressWarnings("unchecked")
	@Override
	protected String[] generateReturnString(Object result) {
		if(result == null)
			return null;
		
		if(getModelObject() != null && result.getClass().equals(getModelObject().getClass()))
		{	
			IObject2XmlSerializer<T> serializer = getResponseSerializer();
			if(serializer != null)
				return serializer.serialize((T)result);
		}	
		if(result instanceof String)
			return new String[]{result.toString()};
		if(result instanceof String[])
			return (String[]) result;
		if(result instanceof Dataset){
			Dataset ds = (Dataset) result;
			return new SingleDataset2XmlSerializer().serialize(ds);
		}
		if(result instanceof Dataset[]){
			Dataset[] dss = (Dataset[]) result;
			return new Dataset2XmlSerializer().serialize(dss);
		}
		return new String[]{result.toString()};
	}
	
	/**
	 * �����xml�����л���
	 * @return
	 */
	protected IObject2XmlSerializer<T> getResponseSerializer(){
		return null;
	}
	
	private ITransactionProcessor getTransactionSupport()
	{
		return (ITransactionProcessor)NCLocator.getInstance().lookup(ITransactionProcessor.class.getName());
	}
	
}
