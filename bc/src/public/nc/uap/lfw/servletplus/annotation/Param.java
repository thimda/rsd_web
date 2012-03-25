package nc.uap.lfw.servletplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nc.uap.lfw.servletplus.constant.Keys;

/**
 * ָ��Action�����Ĳ���. 
 * @author licza.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
/**
* ��������.
*  @return
*/
String name();
/**
* ������.Ĭ����request����ѡsession
*  @return
*/
String scope() default Keys.REQ;
}
