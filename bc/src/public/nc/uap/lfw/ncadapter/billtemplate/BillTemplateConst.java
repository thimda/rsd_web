package nc.uap.lfw.ncadapter.billtemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 单据模版常量类
 * 
 * @author gd 2007-9-20
 */
public final class BillTemplateConst
{
	public static int HEAD = 0; // 表头
    public static int BODY = 1; // 表体
    public static int TAIL = 2; // 表尾

    public static int COLUMNWIDTH_DEFAULT = 120;
    
    public static final int LIST_PANEL = 0;
    public static final int CARD_PANEL = 1;

    
    public static String SELFDEFPRE = "lfw_";
    public static String ELE_SELFDEF = SELFDEFPRE + "selfDefEle"; //form自定义元素标识
    public static String ELE_ROWSPAN = SELFDEFPRE+ "rowSpan"; //form ele单据模板配置所跨行数键值
//    public static String formEle_colSpan = selfDefinePre +"colSpan"; //form ele单据模板配置所跨列数键值
    
    public static String GRID_MULTISELECT = SELFDEFPRE + "multiSelect"; //grid 是否多选
    public static String GRID_SHOWCOLINFO = SELFDEFPRE + "showColInfo"; //grid 是否产生列操作菜单
    
    public static String ELE_TIP = SELFDEFPRE + "tip"; //form 元素的输入提示信息
    
    public static String ELE_EDITORTYPE = SELFDEFPRE + "editorType"; //grid column单据模板配置编辑器类型键值
    public static String ELE_RENDERTYPE = SELFDEFPRE + "renderType"; //grid column单据模板配置渲染器类型键值
    public static String COL_SORTABLE = SELFDEFPRE + "sortable";	//grid column列是否可排序
    public static String ELE_AUTOEXPAND = SELFDEFPRE + "autoExpand"; //grid column宽度自动扩展列
    public static String ELE_SELLEAFONLY = SELFDEFPRE + "selLeafOnly"; // 是否只能选中叶子节点
    public static String REF_MULTISELECT = SELFDEFPRE + "refMultiselect"; // 是否只能选中叶子节点
    public static String FORMCOLUMNCOUNT = SELFDEFPRE + "formColumnCount"; // form表单显示列数
    // 客户端服务器端分页大小设置
    public static String DS_PAGESIZE = SELFDEFPRE + "pageSize"; // grid后台分页大小
    public static String DS_LAZYLOAD = SELFDEFPRE + "lazyLoad"; //dataset是否缓加载
    
    /**
     * 格式校验扩展项
     */
    public static String FORMAT = SELFDEFPRE + "format";
    
  
    private BillTemplateConst(){}
    
    public static final String REF_MASTER_DS = "masterDs";
    
    /**
     * 获取单据模板设置的"显示颜色"
     * @param color
     * @return
     */
    
    public static Map<Integer, String> foreground = new HashMap<Integer, String>();
    static{
    	foreground.put(-1, "#000000");//默认
    	foreground.put(0, "#000000");//黑色
    	foreground.put(1, "#FFFFFF");//白色
    	foreground.put(2, "#C0C0C0");//浅灰色
    	foreground.put(3, "#808080");//灰色
    	foreground.put(4, "#404040");//深灰色
    	foreground.put(5, "#FF0000");//红色
    	foreground.put(6, "#FFAFAF");//粉色
    	foreground.put(7, "#FFC800");//橘色
    	foreground.put(8, "#FFFF00");//黄色
    	foreground.put(9, "#00FF00");//绿色
    	foreground.put(10, "#FF00FF");//紫红色
    	foreground.put(11, "#00FFFF");//蓝绿色
    	foreground.put(12, "#0000FF");//兰色
    	foreground.put(13, "#346699");//深蓝色
    };	
}
