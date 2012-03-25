package nc.uap.lfw.core.data;

import java.io.Serializable;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * DataSetRelation的操作接口定义
 *
 * create on 2007-2-6 上午09:19:55
 *
 * @author lkp 
 * 
 * modified by dengjt 去掉组合键支持。简化业务逻辑
 */

public class DatasetRelation  implements Cloneable,Serializable{
	
	private static final long serialVersionUID = 1L;

	/*DataSetRelation的标识*/
	private String id;
	
	/*主数据集*/
	private String masterDataSet;
	
	/*主表主键字段*/
	private String masterKeyField;
	
	/*子数据集*/
	private String detailDataSet;
	
	/*子表外键字段*/
	private String detailForeignKey;
	
	private String listenerClazz;
	
	private String from;
	
	/*在ds中设置的，用于子表VO的tablecode*/
//	private String tablecode;
	
//	private boolean isAggVO;
	
//	private String aggVOClassName ;

//	public String getAggVOClassName() {
//		return aggVOClassName;
//	}
//
//	public void setAggVOClassName(String aggVOClassName) {
//		this.aggVOClassName = aggVOClassName;
//	}

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	//	/*子表主键数组*/
//	private String detailKeyField;
	/**
	 * 缺省构造
	 *
	 */
	public DatasetRelation(){}
//	
//	public String getTablecode() {
//		return tablecode;
//	}
//	
//	public void setTablecode(String tablecode) {
//		this.tablecode = tablecode;
//	}
	/**
	 * 带参构造
	 * @param id
	 */
	public DatasetRelation(String id)
	{
		this.id = id;
	}
	
	/**
	 * 设置dsRelation id
	 * @param id
	 */
	public void setId(String id){
		
		this.id = id;
	}
	
	/**
	 * 获取dsRelation id
	 * @return
	 */
	public String getId(){
		
		return this.id;
	}
	
	/**
	 * 设置主表DataSet标识
	 * @param master
	 */
	public void setMasterDataset(String masterDataset){
		
		this.masterDataSet = masterDataset;
	}

	/**
	 * 获取主表DataSet标识
	 * @return
	 */
	public String getMasterDataset(){
		
		return this.masterDataSet;
	}
	
	/**
	 * 设置主表主键字段
	 * @param masterKeyFields
	 */
	public void setMasterKeyField(String masterKeyField){
		this.masterKeyField = masterKeyField;
	}
	
	/**
	 * 获取主表主键字段
	 * @return
	 */
	public String getMasterKeyField(){
		
		return this.masterKeyField;
	}
	
	/**
	 * 设置子表DataSet
	 * @param detailDataset
	 */
	public void setDetailDataset(String detailDataset){
		
		this.detailDataSet = detailDataset;
	}
	
	/**
	 * 获取子表DataSet
	 * @return
	 */
	public String getDetailDataset(){
		
		return this.detailDataSet;
	}
	
	/**
	 * 设置子表外键字段
	 * @param detailForeignKeys
	 */
	public void setDetailForeignKey(String detailForeignKey){
		
		this.detailForeignKey = detailForeignKey;
	}
	
	/**
	 * 获取子表外键字段
	 * @return
	 */
	public String getDetailForeignKey(){
		
		return this.detailForeignKey;
	}

	
	public Object clone()
	{
		
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
	  throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
//
//	public String getDetailKeyField() {
//		return detailKeyField;
//	}
//
//	public void setDetailKeyField(String detailKeyField) {
//		this.detailKeyField = detailKeyField;
//	}
	public String getListenerClazz() {
		return listenerClazz;
	}
	public void setListenerClazz(String listenerClazz) {
		this.listenerClazz = listenerClazz;
	}

//	public boolean isAggVO() {
//		return isAggVO;
//	}
//
//	public void setAggVO(boolean isAggVO) {
//		this.isAggVO = isAggVO;
//	}
// 
	
}

