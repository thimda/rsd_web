/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-8-24
 * @since 1.6
 */
public class GridCompInfo extends ControlInfo implements IBaseInfo {

	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	private static final String[] KEYS = new String[]{"��", "��"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	static{
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setVisible(false);
//		left.setEditable(true);
//		left.setDsField("string_ext4");
//		left.setLabel("��߾�");
//		left.setVoField("ileft");
//		list.add(left);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext5");
		width.setLabel("���");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext6");
		height.setLabel("�߶�");
		height.setVoField("height");
		list.add(height);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setKeys(KEYS);
		ena.setValues(VALUES);
		ena.setDsField("combo_ext1");
		ena.setLabel("�Ƿ����");
		ena.setVoField("enableds");
		list.add(ena);
		
//		ComboPropertyInfo pos = new ComboPropertyInfo();
//		pos.setId("position");
//		pos.setVisible(true);
//		pos.setEditable(true);
//		pos.setType(StringDataTypeConst.STRING);
//		pos.setKeys(new String[]{"��Ե�", "���Ե�"});
//		pos.setValues(new String[]{"relative", "positive"});
//		pos.setDsField("combo_ext2");
//		pos.setLabel("λ��");
//		pos.setVoField("positions");
//		list.add(pos);
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(KEYS);
		vis.setValues(VALUES);
		vis.setDsField("combo_ext3");
		vis.setLabel("�Ƿ�ɼ�");
		vis.setVoField("visibles");
		list.add(vis);
		
		ComboPropertyInfo edit = new ComboPropertyInfo();
		edit.setId("editable");
		edit.setVisible(true);
		edit.setEditable(true);
		edit.setType(StringDataTypeConst.bOOLEAN);
		edit.setKeys(KEYS);
		edit.setValues(VALUES);
		edit.setDsField("combo_ext4");
		edit.setLabel("�Ƿ�ɱ༭");
		edit.setVoField("editables");
		list.add(edit);
		
		ComboPropertyInfo multi = new ComboPropertyInfo();
		multi.setId("multiSelect");
		multi.setVisible(true);
		multi.setEditable(true);
		multi.setType(StringDataTypeConst.bOOLEAN);
		multi.setKeys(KEYS);
		multi.setValues(VALUES);
		multi.setDsField("combo_ext5");
		multi.setLabel("�Ƿ��ѡ");
		multi.setVoField("multiselects");
		list.add(multi);
		
		ComboPropertyInfo pgtop = new ComboPropertyInfo();
		pgtop.setId("pagenationTop");
		pgtop.setVisible(true);
		pgtop.setEditable(true);
		pgtop.setType(StringDataTypeConst.bOOLEAN);
		pgtop.setKeys(KEYS);
		pgtop.setValues(VALUES);
		pgtop.setDsField("combo_ext6");
		pgtop.setLabel("��ҳ�����ڶ���");
		pgtop.setVoField("pagenationtops");
		list.add(pgtop);
		
		ComboPropertyInfo scinfo = new ComboPropertyInfo();
		scinfo.setId("showColInfo");
		scinfo.setVisible(true);
		scinfo.setEditable(true);
		scinfo.setType(StringDataTypeConst.bOOLEAN);
		scinfo.setKeys(KEYS);
		scinfo.setValues(VALUES);
		scinfo.setDsField("combo_ext7");
		scinfo.setLabel("��ʾ�п��Ʋ˵�");
		scinfo.setVoField("showcolinfos");
		list.add(scinfo);
		
		ComboPropertyInfo showh = new ComboPropertyInfo();
		showh.setId("showHeader");
		showh.setVisible(true);
		showh.setEditable(true);
		showh.setType(StringDataTypeConst.bOOLEAN);
		showh.setKeys(KEYS);
		showh.setValues(VALUES);
		showh.setDsField("combo_ext8");
		showh.setLabel("��ʾ��ͷ");
		showh.setVoField("showheaders");
		list.add(showh);
		
		ComboPropertyInfo snoc = new ComboPropertyInfo();
		snoc.setId("showNumCol");
		snoc.setVisible(true);
		snoc.setEditable(true);
		snoc.setType(StringDataTypeConst.bOOLEAN);
		snoc.setKeys(KEYS);
		snoc.setValues(VALUES);
		snoc.setDsField("combo_ext9");
		snoc.setLabel("��ʾ������");
		snoc.setVoField("shownumcols");
		list.add(snoc);
		
		ComboPropertyInfo showsr = new ComboPropertyInfo();
		showsr.setId("showSumRow");
		showsr.setVisible(true);
		showsr.setEditable(true);
		showsr.setType(StringDataTypeConst.bOOLEAN);
		showsr.setKeys(KEYS);
		showsr.setValues(VALUES);
		showsr.setDsField("combo_ext10");
		showsr.setLabel("��ʾ�ϼ���");
		showsr.setVoField("showsumrows");
		list.add(showsr);
		
		ComboPropertyInfo sort = new ComboPropertyInfo();
		sort.setId("sortable");
		sort.setVisible(true);
		sort.setEditable(true);
		sort.setType(StringDataTypeConst.bOOLEAN);
		sort.setKeys(KEYS);
		sort.setValues(VALUES);
		sort.setDsField("combo_ext11");
		sort.setLabel("���������");
		sort.setVoField("sortables");
		list.add(sort);
		
//		ComboPropertyInfo smppgbar = new ComboPropertyInfo();
//		smppgbar.setId("simplePageBar");
//		smppgbar.setVisible(true);
//		smppgbar.setEditable(true);
//		smppgbar.setType(StringDataTypeConst.bOOLEAN);
//		smppgbar.setKeys(KEYS);
//		smppgbar.setValues(VALUES);
//		smppgbar.setDsField("combo_ext12");
//		smppgbar.setLabel("���׷�ҳ��ʾ��");
//		smppgbar.setVoField("simplepagebars");
//		list.add(smppgbar);
		
		
		StringPropertyInfo ds = new StringPropertyInfo();
		ds.setId("dataset");
		ds.setVisible(true);
		ds.setEditable(true);
		ds.setDsField("string_ext7");
		ds.setLabel("���ݼ�");
		ds.setVoField("dataset");
		list.add(ds);
		
		StringPropertyInfo rowh = new StringPropertyInfo();
		rowh.setId("rowHeight");
		rowh.setVisible(true);
		rowh.setEditable(true);
		rowh.setDsField("string_ext8");
		rowh.setLabel("���и߶�");
		rowh.setVoField("rowheight");
		list.add(rowh);
		
		StringPropertyInfo hrh = new StringPropertyInfo();
		hrh.setId("headerRowHeight");
		hrh.setVisible(true);
		hrh.setEditable(true);
		hrh.setDsField("string_ext9");
		hrh.setLabel("��ͷ�и߶�");
		hrh.setVoField("headerrowheight");
		list.add(hrh);
		
		StringPropertyInfo cap = new StringPropertyInfo();
		cap.setId("caption");
		cap.setVisible(true);
		cap.setEditable(true);
		cap.setDsField("string_ext10");
		cap.setLabel("����");
		cap.setVoField("caption");
		list.add(cap);
		
		StringPropertyInfo oddtype = new StringPropertyInfo();
		oddtype.setId("oddType");
		oddtype.setVisible(true);
		oddtype.setEditable(true);
		oddtype.setDsField("string_ext11");
		oddtype.setLabel("��˫������");
		oddtype.setVoField("oddtype");
		list.add(oddtype);
		
//		StringPropertyInfo showcolumns = new StringPropertyInfo();
//		showcolumns.setId("showColumns");
//		showcolumns.setVisible(true);
//		showcolumns.setEditable(true);
//		showcolumns.setDsField("string_ext12");
//		showcolumns.setLabel("ShowColumns");
//		showcolumns.setVoField("showcolumns");
//		list.add(showcolumns);
//		
//		StringPropertyInfo pagesize = new StringPropertyInfo();
//		pagesize.setId("pageSize");
//		pagesize.setVisible(true);
//		pagesize.setEditable(true);
//		pagesize.setDsField("string_ext13");
//		pagesize.setLabel("ÿҳ��Ŀ");
//		pagesize.setVoField("pagesize");
//		list.add(pagesize);
		
		StringPropertyInfo groupcolumns = new StringPropertyInfo();
		groupcolumns.setId("groupColumns");
		groupcolumns.setVisible(true);
		groupcolumns.setEditable(true);
		groupcolumns.setDsField("string_ext14");
		groupcolumns.setLabel("������");
		groupcolumns.setVoField("groupColumns");
		list.add(groupcolumns);
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext15");
		className.setLabel("�Զ�������");
		className.setVoField("classname");
		list.add(className);
		
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("��ID");
//		parentid.setVoField("parentid");
//		list.add(parentid);
		
	}

	@Override
	public IPropertyInfo[] getPropertyInfos() {
		IPropertyInfo[] pinfo = super.getPropertyInfos();
		if(pinfo == null)
			return list.toArray(new IPropertyInfo[0]);
		else{
			int lenth = pinfo.length;
			IPropertyInfo[] newList = new IPropertyInfo[lenth + list.size()];
			
			System.arraycopy(list.toArray(), 0, newList, 0, list.size());
			System.arraycopy(pinfo, 0, newList, list.size(), lenth);
			
			return newList;
			
		}
	}

}
