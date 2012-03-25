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
 * 记录信息。在类仅存储数据信息，是DataSet的数据载体。它和
 * Field相互配合构成了DataSet的数据和结构信息。
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
	
	/**发生change的列索引*/
	private List<Integer> changedIndices = null;
	
	public Row(String rowId, int size){
		this.rowId = rowId;
		content = new Object[size];
	}
	
	protected Row(String rowId){
		this.rowId = rowId;
	}
	
	/**
	 * 获取rowId
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
	 * 设置parentId
	 * @param parentId
	 */
	public void setParentId(String parentId){
		
		this.parentId = parentId;
		rowChanged = true;
	}
	

	/**
	 * 获取parentId
	 * @return
	 */
	public String getParentId(){
		
		return this.parentId;
	}
	/**
	 * 获取当前记录的状态信息。
	 * @return
	 */
	public int getState(){
		
		return this.state;
	}
	
	/**
	 * 设置当前记录的状态信息。
	 * @param state
	 */
	public void setState(int state){
		if(this.state != state){
			this.state = state;
			rowChanged = true;
		}
	}
	

	/**
	 * 获取当前行有多少个元素
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
	 * 添加指定个数的空列结构，用于动态增加记录结构。
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
	 * 增加一个空列结构。
	 *
	 */
	public void addColumn(){
		this.addColumn(1);
	}
	
		
	/**
	 * 设置某个字段的值
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
	 * 获得记录中某个字段的值对象
	 * @param fieldName
	 * @return
	 */
	public Object getValue(int index){ 
		if(content == null)
			return null;
		return content[index];
	}
	
	/**
	 * 设置BigDecimal的值
	 * @param index
	 * @param value
	 */
	public void setBigDecimal(int index, BigDecimal value){
		setValue(index, value);
	}
	
	/**
	 * 设置int值
	 * @param index
	 * @param value
	 */
	public void setInt(int index, int value){
		setValue(index, Integer.valueOf(value));
	}
	
	/**
	 * 设置Integer值
	 * @param index
	 * @param value
	 */
	public void setInteger(int index, Integer value){
		setValue(index, value);
	}
	
	/**
	 * 设置String 值
	 * @param index
	 * @param value
	 */
	public void setString(int index,String value){
		setValue(index, value);
	}
	
	/**
	 * 设置boolean值
	 * @param index
	 * @param value
	 */
	public void setBoolean(int index,boolean value){
		setValue(index, new Boolean(value));
	}
	
	/**
	 * 设置UFBoolean类型的值 
	 * @param index
	 * @param value
	 */
	public void setUFBoolean(int index, boolean value){
		setValue(index, new UFBoolean(value));
	}
	
	
	/**
	 * 设置byte类型值
	 * @param index
	 * @param value
	 */
	public void setByte(int index,byte value){
		setValue(index, Byte.valueOf(value));
	}
	
	/**
	 * 设置Date类型数据
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
	 * 设置double类型值
	 * @param index
	 * @param value
	 */
	public void setDouble(int index,double value){
		setValue(index, new Double(value));
	}
	
	/**
	 * 设置float类型值
	 * @param index
	 * @param value
	 */
	public void setFloat(int index,float value){
		setValue(index, new Float(value));
	}
	
	/**
	 * 设置long类型值
	 * @param index
	 * @param value
	 */
	public void setLong(int index,long value){
		setValue(index, Long.valueOf(value));
	}
	
	/**
	 * 设置char类型值
	 * @param index
	 * @param value
	 */
	public void setChar(int index,char value){
		setValue(index, Character.valueOf(value));
	}
	
	/**
	 * 设置字段为NULL;
	 * @param index
	 */
	public void setNull(int index){
		setValue(index, null);
	}

	
	/**
	 * 获取BigDecimal类型值
	 * @param index
	 * @return
	 */
	public BigDecimal getBigDecimal(int index){
		
		return (BigDecimal)content[index];
	}
	
	/**
	 * 获取int值
	 * @param index
	 * @return
	 */
	public int getInt(int index){
		
		return ((Integer)content[index]).intValue();
	}
	
	/**
	 * 获取String类型值
	 * @param index
	 * @return
	 */
	public String getString(int index){
		
		return (String)content[index];
	}
	
	/**
	 * 获取boolean类型值
	 * @param index
	 * @return
	 */
	public boolean getBoolean(int index){
		
		return ((Boolean)content[index]).booleanValue();
	}
	
	/**
	 * 获取UFBoolean类型的值
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
	 * 获取byte类型值
	 * @param index
	 * @return
	 */
	public byte getByte(int index){
		
		return ((Byte)content[index]).byteValue();		
	}
	
	/**
	 * 获取Date类型值
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
	 * 获取double类型值
	 * @param index
	 * @return
	 */
	public double getDouble(int index){
		
		return ((Double)content[index]).doubleValue();
	}
	
	/**
	 * 获取float类型值
	 * @param index
	 * @return
	 */
	public float getFloat(int index){
		
		return ((Float)content[index]).floatValue();
	}
	
	/**
	 * 获取long类型值
	 * @param index
	 * @return
	 */
	public long getLong(int index){
		
		return ((Long)content[index]).longValue();
	}
	
	/**
	 * 获取char类型值
	 * @param index
	 * @return
	 */
	public char getChar(int index){
		
		return ((Character)content[index]).charValue();
	}
	
	/**
	 * 判断某个字段的值是否为空
	 * @param index
	 * @return
	 */
	public boolean isNull(int index){
		
		if(index < 0 || index > content.length - 1)
			throw new IllegalArgumentException("index:" + index + "越界");
		return (content[index] == null);
	}
	
	/**
	 * 清空数据，而不清空状态。
	 *
	 */
	public void clearContent() {
		this.content = new Object[size()];
		rowChanged = true;
	}
	
	/**
	 * 实现对记录对象的深度拷贝,因为Row对象属于DataSet的数据信息，所以
	 * 需要实现深度拷贝，避免相同结构的DataSet数据之间的相互关联。
	 */
	public Object clone(){
		
		try {
			Row row = (Row)super.clone();
		    row.content = content.clone(); //此内容只会出现String,Integer等类型，所以此处已经足够。
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

