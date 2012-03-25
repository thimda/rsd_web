package nc.uap.lfw.servletplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

/**
 * ָ��һ��������servlet
 * 
 * @author licza
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Servlet {
	/**
	 * ServLet����.
	 */
	String name() default StringUtils.EMPTY;

	/**
	 * ServLet·�� example: /user.
	 */
	String path();

	/**
	 * ����Ŀ¼
	 * 
	 * @return
	 */
	String langdir() default StringUtils.EMPTY;
}
