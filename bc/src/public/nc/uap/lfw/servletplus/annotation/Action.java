package nc.uap.lfw.servletplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ������ָ��ΪAction.
 * @author licza.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
/**
* Action����.
* * @return
*/
String name() default "";
/**
* Action����.
* * @return
*/
String url() default "";
/**
* Action����.
* * @return
*/
String method() default "";
}
