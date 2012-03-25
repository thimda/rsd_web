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
public class FormCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		
		StringPropertyInfo ds = new StringPropertyInfo();
		ds.setId("dataset");
		ds.setDsField("string_ext4");
		ds.setVisible(true);
		ds.setEditable(false);
		ds.setLabel("数据集");
		ds.setVoField("dataset");
		list.add(ds);
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setDsField("combo_ext1");
		vis.setKeys(new String[]{"是", "否"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setLabel("是否可见");
		vis.setVoField("visibles");
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setKeys(new String[]{"是", "否"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setDsField("combo_ext2");
		ena.setLabel("是否可编辑");
		ena.setVoField("enableds");
		list.add(ena);
		
//		ComboPropertyInfo pos = new ComboPropertyInfo();
//		pos.setId("position");
//		pos.setKeys(new String[]{"相对的", "绝对的"});
//		pos.setValues(new String[]{"relative", "positive"});
//		pos.setDsField("combo_ext3");
//		pos.setType(StringDataTypeConst.STRING);
//		pos.setVisible(true);
//		pos.setEditable(true);
//		pos.setVoField("positions");
//		pos.setLabel("位置");
//		list.add(pos);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext9");
		width.setLabel("宽度");
		width.setVoField("width");
		list.add(width);
		
//		StringPropertyInfo top = new StringPropertyInfo();
//		top.setId("top");
//		top.setVisible(false);
//		top.setEditable(true);
//		top.setDsField("string_ext10");
//		top.setLabel("顶层距");
//		top.setVoField("itop");
//		list.add(top);
//		
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setVisible(false);
//		left.setEditable(true);
//		left.setDsField("string_ext11");
//		left.setLabel("左边距");
//		left.setVoField("ileft");
//		list.add(left);
		
		IntegerPropertyInfo rowht = new IntegerPropertyInfo();
		rowht.setId("rowHeight");
		rowht.setVisible(true);
		rowht.setEditable(true);
		rowht.setDsField("integer_ext4");
		rowht.setType(StringDataTypeConst.INT);
		rowht.setLabel("行高");
		rowht.setVoField("rowheight");
		list.add(rowht);
		
		IntegerPropertyInfo colcount = new IntegerPropertyInfo();
		colcount.setId("columnCount");
		colcount.setVisible(true);
		colcount.setEditable(true);
		colcount.setDsField("integer_ext5");
		colcount.setType(StringDataTypeConst.INTEGER);
		colcount.setLabel("行数");
		colcount.setVoField("columncount");
		list.add(colcount);
		
		IntegerPropertyInfo rendertype = new IntegerPropertyInfo();
		rendertype.setId("renderType");
		rendertype.setVisible(true);
		rendertype.setEditable(true);
		rendertype.setDsField("integer_ext6");
		rendertype.setType(StringDataTypeConst.INT);
		rendertype.setLabel("渲染类型");
		rendertype.setVoField("rendertype");
		list.add(rendertype);
		
		IntegerPropertyInfo lmw = new IntegerPropertyInfo();
		lmw.setId("labelMinWidth");
		lmw.setVisible(true);
		lmw.setEditable(true);
		lmw.setType(StringDataTypeConst.INT);
		lmw.setDsField("integer_ext7");
		lmw.setLabel("最小标签宽度");
		lmw.setVoField("labelminwidth");
		list.add(lmw);
		
		StringPropertyInfo caption = new StringPropertyInfo();
		caption.setId("caption");
		caption.setVisible(false);
		caption.setEditable(true);
		caption.setDsField("string_ext5");
		caption.setLabel("标题");
		caption.setVoField("caption");
		list.add(caption);
		
		StringPropertyInfo cm = new StringPropertyInfo();
		cm.setId("contextMenu");
		cm.setVisible(false);
		cm.setEditable(true);
		cm.setDsField("string_ext6");
		cm.setLabel("弹出菜单");
		cm.setVoField("contextmenu");
		list.add(cm);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setVisible(false);
		classname.setEditable(true);
		classname.setDsField("string_ext7");
		classname.setLabel("自定义主题");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo bkgrdcolor = new StringPropertyInfo();
		bkgrdcolor.setId("backgroundColor");
		bkgrdcolor.setVisible(false);
		bkgrdcolor.setEditable(true);
		bkgrdcolor.setDsField("string_ext8");
		bkgrdcolor.setLabel("表单背景");
		bkgrdcolor.setVoField("backgroundcolor");
		list.add(bkgrdcolor);

		IntegerPropertyInfo elewidth = new IntegerPropertyInfo();
		elewidth.setId("eleWidth");
		elewidth.setVisible(true);
		elewidth.setEditable(true);
		elewidth.setType(StringDataTypeConst.INT);
		elewidth.setDsField("integer_ext8");
		elewidth.setLabel("行宽");
		elewidth.setVoField("elewidth");
		list.add(elewidth);
		
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("父ID");
//		parentid.setVoField("parentid");
//		list.add(parentid);
		
		ComboPropertyInfo labelPosition = new ComboPropertyInfo();
		labelPosition.setId("labelPosition");
		labelPosition.setKeys(new String[]{"左", "上"});
		labelPosition.setValues(new String[]{"left", "top"});
		labelPosition.setVisible(true);
		labelPosition.setEditable(true);
		labelPosition.setDsField("combo_ext4");
		labelPosition.setType(StringDataTypeConst.STRING);
		labelPosition.setDefaultValue("left");
		labelPosition.setVoField("labelposition");
		labelPosition.setLabel("显示值位置");
		list.add(labelPosition);
	}

	@Override
	public IPropertyInfo[] getPropertyInfos() {
		IPropertyInfo[] pinfo = super.getPropertyInfos();
		
		if(pinfo == null)
			return list.toArray(new IPropertyInfo[0]);
		else{
			int lenth = pinfo.length;
			IPropertyInfo[] newList = new IPropertyInfo[list.size()+lenth];
			
			System.arraycopy(list.toArray(), 0, newList, 0, list.size());
			System.arraycopy(pinfo, 0, newList, list.size(), lenth);

			return newList;
		}
		
	}
	
	

}
