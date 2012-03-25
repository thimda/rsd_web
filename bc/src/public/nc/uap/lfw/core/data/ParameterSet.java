package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * <code>Parameter</code>的集合操作
 *
 * create on 2007-2-6 上午10:19:01
 *
 * @author lkp 
 */

public class ParameterSet implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	private List<Parameter> paramList = new Vector<Parameter>();

	/**
	 * 在参数集中增加一个参数对象,如果该参数已经存在，则替换。
	 * 这样做是为了防止DataSet中出现多个同名的参数，保证参数值的一致性。
	 * @param param
	 */
	public void addParameter(Parameter param) {
		if(paramList!= null && paramList.size() > 0 && paramList.contains(param)){
			int index = paramList.indexOf(param);
			paramList.get(index).setValue(param.getValue());
		}else{
			paramList.add(param);
		}
	}
	
	/**
	 * 
	 * @param paramList
	 */
	public void addParameters(List<Parameter> paramList)
	{
		for(Parameter param : paramList)
			this.addParameter(param);
	}

	/**
	 * 删除某个参数对象
	 * @param param
	 */
	public void removeParameter(Parameter param) {

		paramList.remove(param);
	}

	/**
	 * 删除某个参数对应的对象
	 * @param paramName
	 */
	public void removeParameter(String paramName) {
		
		for(Parameter param : paramList)
		{
			if(param.getName().trim().equals(paramName))
			{
				paramList.remove(param);
				break;
			}
		}
	}

	/**
	 * 获取某个指定的参数对象
	 * @param paramName
	 * @return
	 */
	public Parameter getParameter(String paramName) {

		for(Parameter param : paramList)
		{
			if(param.getName().trim().equals(paramName))
				return param;
		}

		return null;
	}
	
	public String getParameterValue(String paramName){
		for(Parameter param : paramList)
		{
			if(param.getName().trim().equals(paramName))
				return param.getValue();
		}

		return null;
	}

	/**
	 * 获取所有的参数信息
	 * @return
	 */
	public Parameter[] getParameters() {

		return (Parameter[]) paramList.toArray(new Parameter[paramList.size()]);
	}

	/**
	 * 清除所有参数记录
	 */
	public void clear() {

		this.paramList.clear();
	}
	
	/**
	 * 获取参数/值对个数
	 * @return
	 */
	public int size(){
		
		return paramList.size();
	}
	
	/**
	 * 获取指定位置参数，用于遍历参数集
	 * @param index
	 * @return
	 */
	public Parameter getParameter(int index){
		
		return paramList.get(index);
	}	
	
	/**
	 * 实现克隆方法
	 */
	public Object clone()
	{
		try {
			ParameterSet ps = (ParameterSet)super.clone();
			ps.paramList = new ArrayList<Parameter>();			
			for(Parameter param : paramList)
			{
				ps.paramList.add((Parameter)param.clone());
			}
			return ps;
		} catch (CloneNotSupportedException e) {
			throw new LfwRuntimeException(e.getMessage());
		}
	}
}
