package nc.uap.lfw.servletplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

/**
 * 指定一个类型是servlet
 * 
 * @author licza
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Servlet {
	/**
	 * ServLet名称.
	 */
	String name() default StringUtils.EMPTY;

	/**
	 * ServLet路径 example: /user.
	 */
	String path();

	/**
	 * 多语目录
	 * 
	 * @return
	 */
	String langdir() default StringUtils.EMPTY;
}
