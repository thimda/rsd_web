package nc.uap.lfw.servletplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将方法指定为Action.
 * @author licza.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
/**
* Action名称.
* * @return
*/
String name() default "";
/**
* Action链接.
* * @return
*/
String url() default "";
/**
* Action方法.
* * @return
*/
String method() default "";
}
