package nc.uap.lfw.core.data;


/**
 * ������Ϣ����
 */
public class Parameter extends LfwParameter{
	
	private static final long serialVersionUID = 1L;
	
	/*����ֵ*/
	private String value = null;
	
	/**
	 * �������췽��
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
	 * ����ȱʡ���췽��
	 *
	 */
	public Parameter(){
		
	}
	
	/**
	 * ��ȡ����ֵ
	 * @return
	 */
	public String getValue(){
		
		return this.value;
	}
	
	/**
	 * ���ò���ֵ
	 * @param value
	 */
	public void setValue(String value){
		
		this.value = value;
	}
	
	
	/**
	 * ʵ������ж�
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
	 * ʵ�ֿ�¡����
	 */
	public Object clone(){
		return super.clone();
	}


}

