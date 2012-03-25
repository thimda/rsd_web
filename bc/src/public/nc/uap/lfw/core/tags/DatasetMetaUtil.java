package nc.uap.lfw.core.tags;

import java.util.Calendar;

import nc.bs.pub.formulaparse.FormulaParse;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.LanguageUtil;
import nc.vo.pub.formulaset.core.ASTConstant;
import nc.vo.pub.formulaset.core.ASTFunNode;
import nc.vo.pub.formulaset.core.ASTStart;
import nc.vo.pub.formulaset.core.ASTVarNode;
import nc.vo.pub.formulaset.core.Node;
import nc.vo.pub.formulaset.jep.JEPExpression;
import nc.vo.pub.lang.UFDate;

public class DatasetMetaUtil {
	private static FormulaParse fp = new FormulaParse();
	public static String generateMeta(Dataset ds) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		
		int count = ds.getFieldSet().getFieldCount();
		for(int i = 0; i < count; i ++)
		{
			Field field = ds.getFieldSet().getField(i);
			buf.append(generateField(field));
			if(i != count - 1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}

	/**
	 * ����field
	 * @param buf
	 * @param field
	 */
	public static String generateField(Field field) {
		StringBuffer buf = new StringBuffer();
		buf.append("{key:\"")
		   .append(field.getId())
		   .append("\",value:\"")
		   .append(translate(field.getI18nName(), field.getI18nName(),  field.getLangDir()))
		   .append("\",dftValue:");
			Object defValue = field.getDefaultValue();
			if(defValue != null){
				if("Memo".equals(field.getDataType()) || "String".equals(field.getDataType()))
					defValue = JsURLEncoder.encode((String)defValue,"UTF-8");
				if(defValue instanceof String){
					if(((String)defValue).toUpperCase().equals(field.DEFAULT_VALUE_SYSDATE)) {
						UFDate currentDate = new UFDate(Calendar.getInstance().getTime());
						defValue = currentDate.toString();
					}
				}
			}
		buf.append(defValue == null? null : "\"" + defValue + "\"")
		   .append(",nullAble:")
		   .append(field.isNullAble())
		   .append(",dataType:\"")
		   .append(field.getDataType())
		   .append("\"");
		if(field.getField() != null)
		   buf.append(",field:\"")
			   .append(field.getField()) 
			   .append("\"");
		else
			buf.append(",field:null");
		//�ǲ�����(�ֹ�������)
//			if(field.getProperty("refField") != null){
//			   buf.append(",refField:")
//			   	  .append("\"")
//			   	  .append(field.getProperty("refField").getValue())
//			   	  .append("\"");
//			   buf.append(",field:")
//			   	  .append("\"")
//			   	  .append(field.getField())
//			   	  .append("\""); 
//			}
		
//			�ǲ�����
//			if(field.getProperty("refField1") != null){
//			   buf.append(",refField1:")
//			   	  .append("\"")
//			   	  .append(field.getProperty("refField1").getValue())
//			   	  .append("\"");
//			   buf.append(",field:")
//			   	  .append("\"")
//			   	  .append(field.getField())
//			   	  .append("\""); 
//			}
		
		//�ǲ��մ�����
//			else if(field.getProperty("sourceRefField") != null){
//				buf.append(",sourceRefField:")
//					.append("\"")
//					.append(field.getPropertyValueByName("sourceRefField"))
//					.append("\"");
//			}
//			
		//add foreign key
//			if(field.getProperty("isForeignKey") != null){
//				buf.append(",isForeignKey:")
//					.append(field.getPropertyValueByName("isForeignKey"));
//			} 
		// �Ƿ�����
		if(field.isPrimaryKey()){
			buf.append(",isPrimaryKey:true");
		}
		// ����
		if(field.getPrecision() != null){
			buf.append(",precision:")
				.append(field.getPrecision());
		}
		//��ʽ��У������
		if(field.getFormater() != null)
			buf.append(",formater:'"+field.getFormater()+"'");
		else
			buf.append(",formater:null");

		if(field.getMaxValue() != null)
		   buf.append(",maxValue:")
			   .append(field.getMaxValue());
		else
			buf.append(",maxValue:null");

		if(field.getMinValue() != null)
		   buf.append(",minValue:")
			   .append(field.getMinValue());
		else
			buf.append(",minValue:null");
		
		if(field.isLock() == true)
			buf.append(",isLock:true");
		else
			buf.append(",isLock:false");
		//�Զ���༭��ʽ
		if(field.getDefEditFormular() != null)
			buf.append(",defEditFormular:\"" + field.getDefEditFormular() + "\"");
		//��֤��ʽ
		if(field.getValidateFormula() != null){
			String vf = field.getValidateFormula();
			vf = JsURLEncoder.encode(vf,"UTF-8"); 
			buf.append(",validateFormular:\"" + vf + "\"");
		}
		
		/**�Ƿ�༭��ʽֻ�Ǽ򵥵ļӼ��˳����㣿����ǣ����̨��������ʽ��ǰ̨�����㣻������ǣ�����ǰ��ʽ*/
		String editformular = field.getEditFormular();
		String afterEditFormular = "";//����༭��ʽ�еķǼ���ʽ
		if(!"".equals(editformular) && editformular != null){
			String deEditFormular = editformular;

			String[] exps = deEditFormular.split("\\;");
			fp.setExpressArray(exps);
			StringBuffer clientSb = new StringBuffer();
			for(int k=0; k < exps.length; k++){
				StringBuffer sb = new StringBuffer("{");
				JEPExpression jep = fp.getExpressionFromCache(exps[k]);
				if(!(jep.getTopNode() instanceof ASTFunNode)){
					afterEditFormular += exps[k]+ ";";
					continue;
				}
				//������ǼӼ��˳����򷵻�
				nc.vo.pub.formulaset.core.Operator op = ((ASTFunNode)jep.getTopNode()).getOperator(); 
//					if(op != "+" && op!="-" && op!="*" && op!="/")
				if(op == null){
					afterEditFormular += exps[k]+";";
					continue;
				}
				sb.append("key:'" + jep.getLeftName() + "',")
				  .append("formular:[");
				if(getNodeArray(jep.getTopNode(),sb)){
					afterEditFormular += exps[k] + ";";
					continue;
				}
				if(sb == null){
					continue;
				}
				sb.append("]");
				if(k != exps.length-1)
					sb.append("},");
				else
					sb.append("}");
				clientSb.append(sb);
			}
			
//				String clientEditFormular =  getSimpleEditFormularArray(JsURLDecoder.decode(editformular, "UTF-8"));
			String clientEditFormular = null;
			if(clientSb != null && clientSb.length() != 0){
				clientEditFormular = "[" + clientSb.toString()+"]";
				if(clientEditFormular != null){
					/**�ͻ��˱༭��ʽ*/
					if(clientEditFormular != null){
						buf.append(",clientFormular:\"")
						.append(JsURLEncoder.encode(clientEditFormular,"UTF-8"))
						.append("\"");
					}
				}
			}
				
			
		}
		if(!"".equals(afterEditFormular) && afterEditFormular != null ){
			field.setEditFormular(afterEditFormular);
			if(field.getEditFormular() != null){
				buf.append(",formular:\"");
				String encodeStr = JsURLEncoder.encode(field.getEditFormular(),"UTF-8"); 
				buf.append(encodeStr.replace('+', ' '))//��+��ת��Ϊ ����
				.append("\"");
			}
		}
		
		buf.append("}");
		return buf.toString();
	}
	
	//����true ���Ƴ�����
	private static boolean getNodeArray(Node node,StringBuffer sb){
		int nodeNum = node.jjtGetNumChildren();
		//�������
		if(nodeNum == 2){
			String symbol = null;
			if(((ASTFunNode)node).getOperator() != null){
				symbol = ((ASTFunNode)node).getOperator().getSymbol();
			}else if(((ASTFunNode)node).getName() != null){//TODO
				symbol = ((ASTFunNode)node).getName();
			}else{
				sb = null;
				return true;
			}
			
			Node cNode = node.jjtGetChild(0);
			if(getNodeArray(cNode,sb))
				return true;
			cNode = node.jjtGetChild(1);
			if(getNodeArray(cNode, sb))
				return true;
			if(((ASTFunNode)node).jjtGetParent() instanceof ASTStart)
				sb.append("{type:'ASTFunNode',value:'" + symbol + "'}");
			else 
				sb.append("{type:'ASTFunNode',value:'" + symbol + "'},");
		}else if(node instanceof ASTConstant){
			sb.append("{type:'ASTConstant',value:'" + ((ASTConstant)node).getValue() + "'},");
		}else if(node instanceof ASTVarNode){
			sb.append("{type:'ASTVarNode',value:'" + ((ASTVarNode)node).getName() + "'},");
		}else if(node instanceof ASTFunNode)
			return true;
		else if(nodeNum >2)
			return true;
		return false;
	}
	
	/**
	 * ���ж��﷭��,������ܷ���,����ԭi18nName
	 * @param i18nName
	 * @return
	 */
	private static String translate(String i18nName, String defaultI18nName, String langDir){
		if(i18nName == null && defaultI18nName == null)
			return "";
//			return defaultI18nName;
//		return i18nName;
		if(langDir == null)
			langDir = LfwRuntimeEnvironment.getLangDir();
		return LanguageUtil.getWithDefaultByProductCode(langDir, defaultI18nName, i18nName);
	}
}