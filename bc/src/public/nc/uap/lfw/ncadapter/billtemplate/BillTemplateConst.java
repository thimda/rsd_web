package nc.uap.lfw.ncadapter.billtemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * ����ģ�泣����
 * 
 * @author gd 2007-9-20
 */
public final class BillTemplateConst
{
	public static int HEAD = 0; // ��ͷ
    public static int BODY = 1; // ����
    public static int TAIL = 2; // ��β

    public static int COLUMNWIDTH_DEFAULT = 120;
    
    public static final int LIST_PANEL = 0;
    public static final int CARD_PANEL = 1;

    
    public static String SELFDEFPRE = "lfw_";
    public static String ELE_SELFDEF = SELFDEFPRE + "selfDefEle"; //form�Զ���Ԫ�ر�ʶ
    public static String ELE_ROWSPAN = SELFDEFPRE+ "rowSpan"; //form ele����ģ����������������ֵ
//    public static String formEle_colSpan = selfDefinePre +"colSpan"; //form ele����ģ����������������ֵ
    
    public static String GRID_MULTISELECT = SELFDEFPRE + "multiSelect"; //grid �Ƿ��ѡ
    public static String GRID_SHOWCOLINFO = SELFDEFPRE + "showColInfo"; //grid �Ƿ�����в����˵�
    
    public static String ELE_TIP = SELFDEFPRE + "tip"; //form Ԫ�ص�������ʾ��Ϣ
    
    public static String ELE_EDITORTYPE = SELFDEFPRE + "editorType"; //grid column����ģ�����ñ༭�����ͼ�ֵ
    public static String ELE_RENDERTYPE = SELFDEFPRE + "renderType"; //grid column����ģ��������Ⱦ�����ͼ�ֵ
    public static String COL_SORTABLE = SELFDEFPRE + "sortable";	//grid column���Ƿ������
    public static String ELE_AUTOEXPAND = SELFDEFPRE + "autoExpand"; //grid column����Զ���չ��
    public static String ELE_SELLEAFONLY = SELFDEFPRE + "selLeafOnly"; // �Ƿ�ֻ��ѡ��Ҷ�ӽڵ�
    public static String REF_MULTISELECT = SELFDEFPRE + "refMultiselect"; // �Ƿ�ֻ��ѡ��Ҷ�ӽڵ�
    public static String FORMCOLUMNCOUNT = SELFDEFPRE + "formColumnCount"; // form����ʾ����
    // �ͻ��˷������˷�ҳ��С����
    public static String DS_PAGESIZE = SELFDEFPRE + "pageSize"; // grid��̨��ҳ��С
    public static String DS_LAZYLOAD = SELFDEFPRE + "lazyLoad"; //dataset�Ƿ񻺼���
    
    /**
     * ��ʽУ����չ��
     */
    public static String FORMAT = SELFDEFPRE + "format";
    
  
    private BillTemplateConst(){}
    
    public static final String REF_MASTER_DS = "masterDs";
    
    /**
     * ��ȡ����ģ�����õ�"��ʾ��ɫ"
     * @param color
     * @return
     */
    
    public static Map<Integer, String> foreground = new HashMap<Integer, String>();
    static{
    	foreground.put(-1, "#000000");//Ĭ��
    	foreground.put(0, "#000000");//��ɫ
    	foreground.put(1, "#FFFFFF");//��ɫ
    	foreground.put(2, "#C0C0C0");//ǳ��ɫ
    	foreground.put(3, "#808080");//��ɫ
    	foreground.put(4, "#404040");//���ɫ
    	foreground.put(5, "#FF0000");//��ɫ
    	foreground.put(6, "#FFAFAF");//��ɫ
    	foreground.put(7, "#FFC800");//��ɫ
    	foreground.put(8, "#FFFF00");//��ɫ
    	foreground.put(9, "#00FF00");//��ɫ
    	foreground.put(10, "#FF00FF");//�Ϻ�ɫ
    	foreground.put(11, "#00FFFF");//����ɫ
    	foreground.put(12, "#0000FF");//��ɫ
    	foreground.put(13, "#346699");//����ɫ
    };	
}
