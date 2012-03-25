package nc.uap.lfw.core.data;

import java.io.Serializable;

/**
* 根据解析需要由内部类提成外部类，主要描述配置文件中wherefield元素。
 *
 * create on 2007-3-5 下午03:31:07
 *
 * @author lkp 
  */

public class WhereField implements Serializable,Cloneable{
	
	private static final long serialVersionUID = 1L;

	/*条件字段名称*/
	private String key = null;

	/*条件字段取值来源字段名称*/
	private String value = null;

	/**
	 * 缺省构造
	 *
	 */
	public WhereField() {
	}

	/**
	 * 带参构造
	 * @param key
	 * @param value
	 */
	public WhereField(String key, String value) {

		this.key = key;
		this.value = value;
	}

	/**
	 * 获取条件字段名称
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 设置条件字段名称
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取条件字段取值来源字段名称
	 * @return
	 */
	public String getValue() {
		return value;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * 设置条件字段取值来源字段名称
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}

