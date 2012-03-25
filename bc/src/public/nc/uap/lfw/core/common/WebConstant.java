package nc.uap.lfw.core.common;



/**
 * Web框架用到的常量定义
 * @author dengjt
 *
 */
public class WebConstant implements ParamConstant, ApplicationConstant, SessionConstant, CookieConstant{
	
    public static final String LANG_KEY = "LANGUAGE_KEY";
    
    /*Lfw Domain key*/
    public static final String DOMAIN_KEY = "DOMAIN_KEY";
    
    //以毫秒为计数的单位时间
	public static final long SECOND = 1000;

	public static final long MINUTE = SECOND * 60;

	public static final long HOUR = MINUTE * 60;

	public static final long DAY = HOUR * 24;

	public static final long WEEK = DAY * 7;
	
	
//	public static final String NC_RUNTIME_DS = "sys_datasource";
	
//	public static final String NC_RUNTIME_LANG = "sys_lang";
	
	public static final String MULTILANGE_PRODUCT_CODE = "multilanage_product_code";
	
	//调试状态常量key
	public static String DEBUG_MODE = "DEBUG_MODE";
	
	public static final String MODE_DESIGN = "design";
	public static final String MODE_PRODUCTION = "production";
	public static final String MODE_DEBUG = "debug";
	public static final String MODE_PERSONALIZATION = "pa";
	
	//存储到session的key
	public static final String SESSION_PAGEMETA = "pagemeta";
	public static final String SESSION_UIMETA = "uimeta";
	
	public static final String SESSION_PAGEMETA_ORI = "_pagemeta_ori";
	public static final String SESSION_UIMETA_ORI = "_uimeta_ori";
	public static final String SESSION_PAGEMETA_OUT = "_pagemeta_out";
	public static final String SESSION_UIMETA_OUT = "_uimeta_out";
	
	// 客户端模式（是否为离线应用）
	public static final String CLIENT_MODE = "cmode";
	
	//离线模式下缓存所在路径
	public static final String OFFLINE_CACHEPATH = "offlineCachePath";
	
	public static final String CONVERT_DIRECTLY = "convertdir";
	
	public static final String QUERY_SPLIT_STR = "...";
	
	public static final String QUERY_SPLIT_STR_REG = "\\.\\.\\.";
	
	public static final String FIELD_ID_REPLACE_STR = "_";
	
	public static final String DATASET_META_REPLACE_STR = "_";
	
	
	public static final String CACHE_REF_DATASETS = "$refdatasets_";
	
	public static final String CACHE_PAGEMETA = "$pagemeta_";
	
//	// session级用户个性化theme id
//	public static final String SESSION_THEMEID_KEY = "SESSION_THEMEID_KEY";
//	// session级用户多语lang id
//	public static final String SESSION_LANGUAGE_KEY = "SESSION_DEFAULT_LANGUAGE";
	
	public static final String REPLACE_FUNNODE = "REPLACE_FUNNODE";
	
	/**运行时的DatasetMap*/
	public static final String DATASET_MAP = "DATASET_MAP";
	
	public static final String REF_SUFFIX = "_mc";
	
	/** web框架自己用的缓存名称*/
	public static final String LFW_CORE_CACHE = "LFW_CORE_CACHE";
	
	public static final String PAGEMETA_POOL_KEY = "PAGEMETA_POOL_KEY";
	
	public static final String WIDGET_POOL_KEY = "WIDGET_POOL_KEY";
	
	public static final String TEMP_FROM_LFW_PATH = "/webtemp";
	
	public static final String PAGE_ID_KEY = "PAGE_ID_KEY";
	public static final String PERSONAL_PAGE_ID_KEY = "PERSONAL_PAGE_ID_KEY";
	
	public static final String PARENT_PAGE_ID_KEY = "PARENT_PAGE_ID_KEY";
	// 项目的绝对路径
	public static final String PROJECT_PATH_KEY = "PROJECT_PATH_KEY";

	public static final Object CACHE_PAGENODES = "CACHE_PAGENODES";
	
	public static final Object CACHE_APPLICATIONNODES = "CACHE_APPLICATIONNODES";

	//标明请求非页面初始请求
	public static final String AJAX_REQ = "AJAX_REQ";

	//编辑态参数
	public static final String EDIT_MODE_PARAM = "emode";
	public static final String WINDOW_MODE_PARAM = "wmodel";
	//编辑态KEY
	public static final String EDIT_MODE_KEY = "EDIT_MODE";
	public static final String WINDOW_MODE_KEY = "WINDOW_MODE";
	//自定义页面的页面widgetId
	public static final String WIDGET_ID = "widgetId";
	//公共view设计页面window ID
	public static final String DEFAULT_WINDOW_ID = "defaultWindowId";
}
