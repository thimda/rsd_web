package nc.uap.lfw.core.common;
/**
 * 请求参数常量Key
 * @author dengjt
 *
 */
public interface ParamConstant {
	public static final String PAGE_ID = "pageId";
	public static final String PAGE_UNIQUE_ID = "pageUniqueId";
	public static final String APP_UNIQUE_ID = "appUniqueId";
	
	/*与上一参数配合使用，表明请求装载那个页面的数据。*/
	public static String OTHER_PAGE_ID = "otherPageId";	 
	public static String OTHER_PAGE_UNIQUE_ID = "otherPageUniqueId";
}
