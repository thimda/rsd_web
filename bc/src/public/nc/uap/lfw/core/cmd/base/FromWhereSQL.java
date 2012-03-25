package nc.uap.lfw.core.cmd.base;

import java.io.Serializable;

/**
 * From-Where SQL对象
 * 
 * 
 * 创建日期：2010-9-21
 */
public interface FromWhereSQL extends Serializable {

	/** 默认属性路径 */
	public static final String DEFAULT_KEY = ".";
	
	/** 默认属性路径，用作主实体表别名的Key */
	public static final String DEFAULT_ATTRPATH = DEFAULT_KEY;
	
	/**
	 * 返回From语句
	 */
	public String getFrom();

	/**
	 * 返回Where语句
	 */
	public String getWhere();

	/**
	 * 根据元数据属性路径返回属性对应表的别名</br>
	 * <p><strong>对于普通属性(即非扩展属性)</strong>
	 * </br>示例如下：
	 * </br>bean(表名 order)
	 * </br>|
	 * </br>|- code(普通属性，表名 order)
	 * </br>|
	 * </br>|- ref(引用属性，表名 table_ref)</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |                           </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |- code(普通属性，表名 table_ref)
	 * </br>         
	 * </br>|- collection(聚合属性，表名 detail)</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |                           </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |- name(普通属性，表名 detail)
	 * <li>
	 * 若想获取主实体order表的表别名，需要传入的attrpath为FromWhereSQL.DEFAULT_ATTRPATH。
	 * <li>
	 * 若想获取子实体detail表的表别名，需要传入的attrpath为collection。
	 * <li>
	 * 若想获取引用实体table_ref表的表别名，需要传入的attrpath为ref。
	 * <p></br><strong>对于扩展属性，attrpath需要遵循规范：</br>
	 * 将扩展属性名替换成扩展表名(如有前缀，前缀保持不变)。</strong>
	 * </br>示例如下：
	 * </br>bean(表名 order)
	 * </br>|
	 * </br>|- ext_code(扩展属性，表名 ext_order)
	 * </br>|
	 * </br>|- collection(聚合属性，表名 detail)</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |                           </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *           |- ext_name(扩展属性，表名 ext_detail)
	 * <li>
	 * 若想获取ext_order表的别名，按照规范，将扩展属性名ext_code替换为扩展表名ext_order，传入ext_order即可。
	 * <li>
	 * 若想得到ext_detail表的别名，按照规范，将扩展属性名ext_name替换为扩展表名ext_detail，传入collection.ext_detail即可。
	 * 
	 * @param attrpath
	 *            元数据属性路径(如：dept.code)
	 */
	public String getTableAliasByAttrpath(String attrpath);
}