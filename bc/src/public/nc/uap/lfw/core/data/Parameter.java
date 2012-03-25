package nc.uap.lfw.core.data;


/**
 * 参数信息对象
 */
public class Parameter extends LfwParameter{
	
	private static final long serialVersionUID = 1L;
	
	/*参数值*/
	private String value = null;
	
	/**
	 * 参数构造方法
	 * @param name
	 * @param value
	 */
	public Parameter(String name, String value){
		this(name, value, null);
	}
	
	public Parameter(String name, String value, String desc){
		super(name, desc);
		this.value = value;
	}
	
	
	/**
	 * 参数缺省构造方法
	 *
	 */
	public Parameter(){
		
	}
	
	/**
	 * 获取参数值
	 * @return
	 */
	public String getValue(){
		
		return this.value;
	}
	
	/**
	 * 设置参数值
	 * @param value
	 */
	public void setValue(String value){
		
		this.value = value;
	}
	
	
	/**
	 * 实现相等判断
	 */
	public boolean equals(Object obj) {
		   
		if(obj == null || !(obj instanceof Parameter))
			return false;
		if(obj == this)
			return true;
		if(((Parameter)obj).getName().trim().equals(this.getName()))
			return true;
		else
			return false;
	} 
	
	/**
	 * 实现克隆方法
	 */
	public Object clone(){
		return super.clone();
	}


}

