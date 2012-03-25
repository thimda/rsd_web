package nc.uap.lfw.core.data;

import java.io.Serializable;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * DataSetRelation�Ĳ����ӿڶ���
 *
 * create on 2007-2-6 ����09:19:55
 *
 * @author lkp 
 * 
 * modified by dengjt ȥ����ϼ�֧�֡���ҵ���߼�
 */

public class DatasetRelation  implements Cloneable,Serializable{
	
	private static final long serialVersionUID = 1L;

	/*DataSetRelation�ı�ʶ*/
	private String id;
	
	/*�����ݼ�*/
	private String masterDataSet;
	
	/*���������ֶ�*/
	private String masterKeyField;
	
	/*�����ݼ�*/
	private String detailDataSet;
	
	/*�ӱ�����ֶ�*/
	private String detailForeignKey;
	
	private String listenerClazz;
	
	private String from;
	
	/*��ds�����õģ������ӱ�VO��tablecode*/
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
	//	/*�ӱ���������*/
//	private String detailKeyField;
	/**
	 * ȱʡ����
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
	 * ���ι���
	 * @param id
	 */
	public DatasetRelation(String id)
	{
		this.id = id;
	}
	
	/**
	 * ����dsRelation id
	 * @param id
	 */
	public void setId(String id){
		
		this.id = id;
	}
	
	/**
	 * ��ȡdsRelation id
	 * @return
	 */
	public String getId(){
		
		return this.id;
	}
	
	/**
	 * ��������DataSet��ʶ
	 * @param master
	 */
	public void setMasterDataset(String masterDataset){
		
		this.masterDataSet = masterDataset;
	}

	/**
	 * ��ȡ����DataSet��ʶ
	 * @return
	 */
	public String getMasterDataset(){
		
		return this.masterDataSet;
	}
	
	/**
	 * �������������ֶ�
	 * @param masterKeyFields
	 */
	public void setMasterKeyField(String masterKeyField){
		this.masterKeyField = masterKeyField;
	}
	
	/**
	 * ��ȡ���������ֶ�
	 * @return
	 */
	public String getMasterKeyField(){
		
		return this.masterKeyField;
	}
	
	/**
	 * �����ӱ�DataSet
	 * @param detailDataset
	 */
	public void setDetailDataset(String detailDataset){
		
		this.detailDataSet = detailDataset;
	}
	
	/**
	 * ��ȡ�ӱ�DataSet
	 * @return
	 */
	public String getDetailDataset(){
		
		return this.detailDataSet;
	}
	
	/**
	 * �����ӱ�����ֶ�
	 * @param detailForeignKeys
	 */
	public void setDetailForeignKey(String detailForeignKey){
		
		this.detailForeignKey = detailForeignKey;
	}
	
	/**
	 * ��ȡ�ӱ�����ֶ�
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

