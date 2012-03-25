package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * <code>Parameter</code>�ļ��ϲ���
 *
 * create on 2007-2-6 ����10:19:01
 *
 * @author lkp 
 */

public class ParameterSet implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	private List<Parameter> paramList = new Vector<Parameter>();

	/**
	 * �ڲ�����������һ����������,����ò����Ѿ����ڣ����滻��
	 * ��������Ϊ�˷�ֹDataSet�г��ֶ��ͬ���Ĳ�������֤����ֵ��һ���ԡ�
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
	 * ɾ��ĳ����������
	 * @param param
	 */
	public void removeParameter(Parameter param) {

		paramList.remove(param);
	}

	/**
	 * ɾ��ĳ��������Ӧ�Ķ���
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
	 * ��ȡĳ��ָ���Ĳ�������
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
	 * ��ȡ���еĲ�����Ϣ
	 * @return
	 */
	public Parameter[] getParameters() {

		return (Parameter[]) paramList.toArray(new Parameter[paramList.size()]);
	}

	/**
	 * ������в�����¼
	 */
	public void clear() {

		this.paramList.clear();
	}
	
	/**
	 * ��ȡ����/ֵ�Ը���
	 * @return
	 */
	public int size(){
		
		return paramList.size();
	}
	
	/**
	 * ��ȡָ��λ�ò��������ڱ���������
	 * @param index
	 * @return
	 */
	public Parameter getParameter(int index){
		
		return paramList.get(index);
	}	
	
	/**
	 * ʵ�ֿ�¡����
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
