/**
 * 
 */
package nc.uap.lfw.core.uif.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gd
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UifMethodAnnotation {
	public boolean isWidget() default false;
	public boolean isDataset() default false;
	public boolean isMultiSel() default false;
	public boolean isPageState() default false;
	public boolean isMust() default false;
	public boolean isMap() default false;
	public boolean isQry() default false;
//	public boolean isTab() default false;
//	public boolean isCard() default false;
	public boolean isBoolean() default false;
	public boolean booleanValue() default false;
	public String defaultValue() default "";
	public String tip();
	public String name();
	public boolean isChangeCard() default false;
	public boolean isChangeCardItem() default false;
}
