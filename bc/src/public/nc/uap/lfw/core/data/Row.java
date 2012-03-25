package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ��¼��Ϣ��������洢������Ϣ����DataSet���������塣����
 * Field�໥��Ϲ�����DataSet�����ݺͽṹ��Ϣ��
 *
 */

 public class Row implements Cloneable,Serializable{
	
	private static final long serialVersionUID = 1L;
	public static final int STATE_NORMAL = 0;
	public static final int STATE_UPDATE = 1;
	public static final int STATE_ADD= 2;
	public static final int STATE_DELETED = 3;
	public static final int STATE_FALSE_DELETED = 4;
	
	
	private String rowId ;
	
	private int state;
	
	private Object[] content = null;
	
	private String parentId;
	
	private boolean rowChanged = true;
	
	/**����change��������*/
	private List<Integer> changedIndices = null;
	
	public Row(String rowId, int size){
		this.rowId = rowId;
		content = new Object[size];
	}
	
	protected Row(String rowId){
		this.rowId = rowId;
	}
	
	/**
	 * ��ȡrowId
	 * @return
	 */
	public String getRowId()
	{
		return rowId;
	}
	
	public void setRowId(String id){
		this.rowId = id;
		rowChanged = true;
	}
	
	/**
	 * ����parentId
	 * @param parentId
	 */
	public void setParentId(String parentId){
		
		this.parentId = parentId;
		rowChanged = true;
	}
	

	/**
	 * ��ȡparentId
	 * @return
	 */
	public String getParentId(){
		
		return this.parentId;
	}
	/**
	 * ��ȡ��ǰ��¼��״̬��Ϣ��
	 * @return
	 */
	public int getState(){
		
		return this.state;
	}
	
	/**
	 * ���õ�ǰ��¼��״̬��Ϣ��
	 * @param state
	 */
	public void setState(int state){
		if(this.state != state){
			this.state = state;
			rowChanged = true;
		}
	}
	

	/**
	 * ��ȡ��ǰ���ж��ٸ�Ԫ��
	 * @return
	 */
	public int size()
	{
		if(content != null)
			return content.length;
		else
			return 0;
	}
	/**
	 * ���ָ�������Ŀ��нṹ�����ڶ�̬���Ӽ�¼�ṹ��
	 *
	 */
	public void addColumn(int number){
		
		int numbers = number + content.length;
		Object[] newContent = new Object[numbers];
		System.arraycopy(content, 0, newContent, 0, content.length);
		this.content = newContent;
		rowChanged = true;
	}
	
	/**
	 * ����һ�����нṹ��
	 *
	 */
	public void addColumn(){
		this.addColumn(1);
	}
	
		
	/**
	 * ����ĳ���ֶε�ֵ
	 * @param index
	 * @param value
	 */
	public void setValue(int index, Object value){
		if(content == null)
			return;
		Object oldValue = content[index]; 
		content[index] = value;
		rowChanged = true;
//		if (!oldValue.equals(value))
		if (oldValue != value)
			addChangedIndex(index);
	}
	
	/**
	 * ��ü�¼��ĳ���ֶε�ֵ����
	 * @param fieldName
	 * @return
	 */
	public Object getValue(int index){ 
		if(content == null)
			return null;
		return content[index];
	}
	
	/**
	 * ����BigDecimal��ֵ
	 * @param index
	 * @param value
	 */
	public void setBigDecimal(int index, BigDecimal value){
		setValue(index, value);
	}
	
	/**
	 * ����intֵ
	 * @param index
	 * @param value
	 */
	public void setInt(int index, int value){
		setValue(index, Integer.valueOf(value));
	}
	
	/**
	 * ����Integerֵ
	 * @param index
	 * @param value
	 */
	public void setInteger(int index, Integer value){
		setValue(index, value);
	}
	
	/**
	 * ����String ֵ
	 * @param index
	 * @param value
	 */
	public void setString(int index,String value){
		setValue(index, value);
	}
	
	/**
	 * ����booleanֵ
	 * @param index
	 * @param value
	 */
	public void setBoolean(int index,boolean value){
		setValue(index, new Boolean(value));
	}
	
	/**
	 * ����UFBoolean���͵�ֵ 
	 * @param index
	 * @param value
	 */
	public void setUFBoolean(int index, boolean value){
		setValue(index, new UFBoolean(value));
	}
	
	
	/**
	 * ����byte����ֵ
	 * @param index
	 * @param value
	 */
	public void setByte(int index,byte value){
		setValue(index, Byte.valueOf(value));
	}
	
	/**
	 * ����Date��������
	 * @param index
	 * @param value
	 */
	public void setDate(int index,Date value){
		setValue(index, value);
	}
	
	public void setUFDate(int index,UFDate value){
		setValue(index, value);
	}
	
	/**
	 * ����double����ֵ
	 * @param index
	 * @param value
	 */
	public void setDouble(int index,double value){
		setValue(index, new Double(value));
	}
	
	/**
	 * ����float����ֵ
	 * @param index
	 * @param value
	 */
	public void setFloat(int index,float value){
		setValue(index, new Float(value));
	}
	
	/**
	 * ����long����ֵ
	 * @param index
	 * @param value
	 */
	public void setLong(int index,long value){
		setValue(index, Long.valueOf(value));
	}
	
	/**
	 * ����char����ֵ
	 * @param index
	 * @param value
	 */
	public void setChar(int index,char value){
		setValue(index, Character.valueOf(value));
	}
	
	/**
	 * �����ֶ�ΪNULL;
	 * @param index
	 */
	public void setNull(int index){
		setValue(index, null);
	}

	
	/**
	 * ��ȡBigDecimal����ֵ
	 * @param index
	 * @return
	 */
	public BigDecimal getBigDecimal(int index){
		
		return (BigDecimal)content[index];
	}
	
	/**
	 * ��ȡintֵ
	 * @param index
	 * @return
	 */
	public int getInt(int index){
		
		return ((Integer)content[index]).intValue();
	}
	
	/**
	 * ��ȡString����ֵ
	 * @param index
	 * @return
	 */
	public String getString(int index){
		
		return (String)content[index];
	}
	
	/**
	 * ��ȡboolean����ֵ
	 * @param index
	 * @return
	 */
	public boolean getBoolean(int index){
		
		return ((Boolean)content[index]).booleanValue();
	}
	
	/**
	 * ��ȡUFBoolean���͵�ֵ
	 * @param index
	 * @return
	 */
	public UFBoolean getUFBoolean(int index){
		
		return (UFBoolean)content[index];
	}
	
	
	public  UFDouble getUFDobule(int index){
		return (UFDouble)content[index];
	}
	
	
	/**
	 * ��ȡbyte����ֵ
	 * @param index
	 * @return
	 */
	public byte getByte(int index){
		
		return ((Byte)content[index]).byteValue();		
	}
	
	/**
	 * ��ȡDate����ֵ
	 * @param index
	 * @return
	 */
	public Date getDate(int index){
		
		return (Date)content[index];
	}
	
	public UFDate getUFData(int index){
		return (UFDate)content[index];
	}
	/**
	 * ��ȡdouble����ֵ
	 * @param index
	 * @return
	 */
	public double getDouble(int index){
		
		return ((Double)content[index]).doubleValue();
	}
	
	/**
	 * ��ȡfloat����ֵ
	 * @param index
	 * @return
	 */
	public float getFloat(int index){
		
		return ((Float)content[index]).floatValue();
	}
	
	/**
	 * ��ȡlong����ֵ
	 * @param index
	 * @return
	 */
	public long getLong(int index){
		
		return ((Long)content[index]).longValue();
	}
	
	/**
	 * ��ȡchar����ֵ
	 * @param index
	 * @return
	 */
	public char getChar(int index){
		
		return ((Character)content[index]).charValue();
	}
	
	/**
	 * �ж�ĳ���ֶε�ֵ�Ƿ�Ϊ��
	 * @param index
	 * @return
	 */
	public boolean isNull(int index){
		
		if(index < 0 || index > content.length - 1)
			throw new IllegalArgumentException("index:" + index + "Խ��");
		return (content[index] == null);
	}
	
	/**
	 * ������ݣ��������״̬��
	 *
	 */
	public void clearContent() {
		this.content = new Object[size()];
		rowChanged = true;
	}
	
	/**
	 * ʵ�ֶԼ�¼�������ȿ���,��ΪRow��������DataSet��������Ϣ������
	 * ��Ҫʵ����ȿ�����������ͬ�ṹ��DataSet����֮����໥������
	 */
	public Object clone(){
		
		try {
			Row row = (Row)super.clone();
		    row.content = content.clone(); //������ֻ�����String,Integer�����ͣ����Դ˴��Ѿ��㹻��
			return row;
		} catch (CloneNotSupportedException e) {
	
			LfwLogger.error(e.getMessage(), e);;
		}
		return null;
	}


	public boolean isRowChanged() {
		return rowChanged;
	}

	public void setRowChanged(boolean rowChanged) {
		this.rowChanged = rowChanged;
		if (rowChanged == false)
			this.getChangedIndices().clear();
	}

	public Object[] getContent() {
		return content;
	}

	public void setContent(Object[] content) {
		this.content = content;
	}

	public List<Integer> getChangedIndices() {
		if (changedIndices == null){
			changedIndices = new ArrayList<Integer>();
		}
		return changedIndices;
	}
	
	private void addChangedIndex(Integer index){
		if (changedIndices == null){
			changedIndices = new ArrayList<Integer>();
		}
		if (changedIndices.indexOf(index) == -1)
			changedIndices.add(index);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < content.length; i++) {
			buf.append(content[i]);
			buf.append(",");
		}
		return buf.toString();
	}
 }

