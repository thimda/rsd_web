package nc.uap.lfw.servletplus.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.After;
import nc.uap.lfw.servletplus.annotation.Before;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.bind.DataBinder;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.lfw.servletplus.utils.ClassScan;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;

public class ServletForward {
	/**
	 * ת��Servlet����
	 * @param method Action������
	 * @param request HttpRequest
	 * @param response HttpResponse
	 * @throws Throwable 
	 */ 
	@SuppressWarnings("unchecked")
	public static  void forward(Method method, HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		try {
			BaseActionInterface ba =getBaseAction(method);
			Class[] parameterTypes=method.getParameterTypes();
			Object[] param = DataBinder.bind(request,parameterTypes, method.getParameterAnnotations());
			ba.fill(request, response);
			initMutiLang(ba);
			doBeforeMethod(ba);
			MethodUtils.invokeExactMethod(ba, method.getName(), param, parameterTypes);
			doAfterMethod( ba);
			ba.fush();
		}catch (ClassNotFoundException e) {
			response.sendError(404);
			return;
		}catch (NoSuchMethodException e) {
			response.sendError(404);
			return;
		}catch (IllegalAccessException e) {
			LfwLogger.error(e.getMessage(), e);
			response.sendError(404);
			return;
		} catch (InvocationTargetException e) {
			LfwLogger.error(e.getMessage(), e);
			throw	e.getTargetException();
		} 
	}
	/**
	 * ��ʼ������Ŀ¼
	 * @param ba
	 */
	public static void initMutiLang(BaseActionInterface ba){
		Servlet servletConfig = ba.getClass().getAnnotation(Servlet.class);
		if(servletConfig != null){
			String langDir = servletConfig.langdir();
			if(StringUtils.isNotEmpty(langDir))
				LfwRuntimeEnvironment.setLangDir(langDir);
		}
	}
	/**
	 * ִ��ServletActionִ��ǰҪִ�еķ���
	 * @param ba Servletʵ��
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void doBeforeMethod(Object ba) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String beforeMethodName = getBeforeMethodName(ba);
		if (beforeMethodName != null) {
				MethodUtils.invokeMethod(ba, beforeMethodName, null);
		}
	}
	/**
	 * ִ��ServletActionִ�к�Ҫִ�еķ���
	 * @param ba Servletʵ��
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void doAfterMethod(Object ba) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String  AfterMethodName = getAfterMethodName(ba);
		if (AfterMethodName != null) {
				MethodUtils.invokeMethod(ba, AfterMethodName, null);
		}
	}
	/**
	 * ���ServletActionִ�к�Ҫִ�еķ���
	 * @param ba Servletʵ��
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String getBeforeMethodName(Object obj) {
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (Method method : methods) {
			boolean isBefore = method.isAnnotationPresent(Before.class);
			if (isBefore) {
				return method.getName();
			}
		}
		return null;
	}
	/**
	 * ���ServletActionִ��ǰҪִ�еķ���
	 * @param ba Servletʵ��
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String getAfterMethodName(Object obj) {
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (Method method : methods) {
			boolean isAfter = method.isAnnotationPresent(After.class);
			if (isAfter) {
				return method.getName();
			}
		}
		return null;
	}
	/**
	 * ��÷������͵�ʵ��
	 * @param method ����ʵ��
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static BaseAction getBaseAction(Method method) throws ClassNotFoundException{
		BaseAction ba ;
			//���������¶�̬������
			try {
				if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG)){
					ba = (BaseAction)ClassScan.dynamicClass(method.getDeclaringClass()).newInstance();
				}
				else{
					ba = (BaseAction) method.getDeclaringClass().newInstance();
				}
			} 
			catch (Exception e) {
				throw new ClassNotFoundException(method.getDeclaringClass().getName()+" not Found!");
			}  
		
		return ba;
	}
}
