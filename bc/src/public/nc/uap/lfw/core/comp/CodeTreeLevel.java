package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.data.Dataset;

/**
 * ������Level
 * ������Levelָ��Level����Ӧ��dataset��������ͨ�����������ι�ϵ���ӱ�������һ�ֵݹ���
 * @author dengjt
 *
 */
public class CodeTreeLevel extends RecursiveTreeLevel {
	
	private static final long serialVersionUID = -2149518856890487561L;
	
	// �����ֶ�
	private String codeField = "2/2/2";
	
	//�����ֶεı������
	private String codeRule;
	
	public String getCodeField() {
		return codeField;
	}
	public void setCodeField(String codeField) {
		this.codeField = codeField;
	}
	
	public String getCodeRule() {
		return codeRule;
	}
	public void setCodeRule(String codeRule) {
		this.codeRule = codeRule;
	}
	
	public String getRecursiveKeyField() {
		if(recursiveKeyField != null)
			return recursiveKeyField;
		return getCodeField();
	}
	@Override
	public String getRecursivePKeyField() {
		if(recursivePKeyField != null)
			return recursivePKeyField;
		return Dataset.PARENT_PK_COLUMN;
	}
	
}
