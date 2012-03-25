package nc.uap.lfw.servletplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nc.uap.lfw.servletplus.constant.Keys;

/**
 * 指定Action方法的参数. 
 * @author licza.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
/**
* 参数名称.
*  @return
*/
String name();
/**
* 参数域.默认是request，可选session
*  @return
*/
String scope() default Keys.REQ;
}
