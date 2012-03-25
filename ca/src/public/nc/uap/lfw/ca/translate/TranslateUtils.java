/**
 * 
 */
package nc.uap.lfw.ca.translate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import nc.uap.lfw.ca.ast.Symbol;
import nc.uap.lfw.ca.dom.JField;
import nc.uap.lfw.ca.dom.JMethod;
import nc.uap.lfw.ca.dom.JParam;
import nc.uap.lfw.ca.dom.JStatement;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.AbstractTypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.CompilationUnit;
import nc.uap.lfw.ca.jdt.core.dom.Expression;
import nc.uap.lfw.ca.jdt.core.dom.FieldDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.MethodDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.SingleVariableDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.TypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.VariableDeclarationFragment;
import nc.uap.lfw.ca.log.CaLogger;
import nc.uap.lfw.ca.util.CompilationJavaUtils;

/**
 * @author chouhl
 *
 */
public class TranslateUtils {
	
	private static boolean compareParamTypes(String[] paramTypes,String[] pts){
		boolean temp = false;
		if(paramTypes == null && (pts == null || pts.length == 0)){
			temp = true;
		}else if(pts == null && (paramTypes == null || paramTypes.length == 0)){
			temp = true;
		}else if(paramTypes != null && pts != null && paramTypes.length == pts.length){
			for(int i=0;i<paramTypes.length;i++){
				if(!paramTypes[i].equals(pts[i])){
					break;
				}
			}
			temp = true;
		}
		return temp;
	}
	
	private static Symbol translate(Symbol s,JudgmentCondition judgmentCondition){
		if(s != null){
			switch(s.getType()){
				case Symbol.NORMAL:
					s.setTargetValue(s.getOriginalValue());
					break;
				case Symbol.METHOD_NAME:
					if(judgmentCondition != null){
						if("getGlobalContext".equals(s.getOriginalValue()) && "nc.uap.lfw.core.event.listener.AbstractServerListener".equals(judgmentCondition.getWholeClassName()) && (judgmentCondition.getParamTypeNames() == null || judgmentCondition.getParamTypeNames().length == 0)){
							s.setTargetValue("pageUI~");
						}else if("getWidgetContext".equals(s.getOriginalValue()) && "nc.uap.lfw.core.event.ctx.LfwPageContext".equals(judgmentCondition.getWholeClassName()) && compareParamTypes(new String[]{"StringLiteral"}, judgmentCondition.getParamTypeNames())){
							s.setTargetValue("getWidget");
						}else if("getWidget".equals(s.getOriginalValue()) && "nc.uap.lfw.core.event.ctx.WidgetContext".equals(judgmentCondition.getWholeClassName()) && (judgmentCondition.getParamTypeNames() == null || judgmentCondition.getParamTypeNames().length == 0)){
							s.setTargetValue("");
						}else{
							s.setTargetValue(s.getOriginalValue());
						}
					}else{
						s.setTargetValue(s.getOriginalValue());
					}
					break;
				case Symbol.TYPE_NAME:
					s.setTargetValue("var");
					break;
				default:
					s.setTargetValue(s.getOriginalValue());
					break;
			}
		}
		return s;
	}
	
	public static void translate(String wholeClassName,List<JField> fields,List<JMethod> methods){
		
		String[] filenames = CompilationJavaUtils.getFilename(wholeClassName);
		CompilationUnit unit = CompilationJavaUtils.parseJavaToTree(filenames);

		String filename = "d:/" + wholeClassName.replaceAll("\\.", "_") + ".js";
		
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(filename));
			bw.newLine();
			String packageName = unit.getPackage().getName().getFullyQualifiedName();
			List<AbstractTypeDeclaration> atds = unit.types();
			for(AbstractTypeDeclaration node:atds){
				switch(node.getNodeType()){
					case ASTNode.TYPE_DECLARATION:
						TypeDeclaration td = (TypeDeclaration)node;
						
						String wcn = packageName + "." + td.getName().getFullyQualifiedName();
						if(!wcn.equals(wholeClassName)){
							continue;
						}
						
						FieldDeclaration[] fds = td.getFields();
						for(FieldDeclaration fd:fds){
							Symbol s = new Symbol();
							s.setOriginalValue(fd.getType().toString());
							s.setType(Symbol.TYPE_NAME);
							s = translate(s, null);
							bw.write(s.getTargetValue() + " ");
							List<VariableDeclarationFragment> vdfs = fd.fragments();
							if(vdfs != null && vdfs.size() > 0){
								for(int i=0;i<vdfs.size();i++){
									if(i > 0){
										bw.write(",");
									}
									Symbol s1 = new Symbol();
									s1.setOriginalValue(vdfs.get(i).getName().getFullyQualifiedName());
									s1 = translate(s1, null);
									bw.write(s1.getTargetValue() + " ");
									Expression exp = vdfs.get(i).getInitializer();
									if(exp != null){
										Symbol s2 = new Symbol();
										s2.setOriginalValue(exp.toString());
										s2 = translate(s2, null);
										bw.write("= "+s2.getTargetValue());
									}
								}
							}
							bw.write(";");
							bw.newLine();
						}
						
						for(JMethod jm:methods){
							List<JParam> jps = jm.getParamMapToList();
							translate(jm.getMethodName(), null);
							StringBuffer sf = new StringBuffer();
							for(int i=0;i<jps.size();i++){
								if(i > 0){
									sf.append(",");
								}
								sf.append(jps.get(i).getParamname().getOriginalValue());
							}
							bw.write("function " + jm.getMethodName().getTargetValue() + "("+sf.toString()+")");
							bw.newLine();
							bw.write("{");
							bw.newLine();
							List<JStatement> jss = jm.getStatementMapToList();
							for(JStatement js:jss){
								boolean noContent = true;
								boolean temp = false;
								List<JField> jfs = js.getFieldMapToList();
								for(JField jf:jfs){
									if(jf != null && jf.getTypename() != null){
										translate(jf.getTypename(), null);
										if(jf.getFieldvalue() != null && jf.getFieldvalue().getTargetValue().trim().length() > 0){
											bw.write(jf.getTypename().getTargetValue() + " " + jf.getFieldname().getTargetValue() + " = " + jf.getFieldvalue().getTargetValue());
										}else{
											temp = true;
											bw.write(jf.getTypename().getTargetValue() + " " + jf.getFieldname().getTargetValue());
										}
										noContent = false;
									}
								}
								if(temp){
									bw.write(" = ");
								}
								boolean writePoint = false;
								List<JMethod> jms = js.getMethodMapToList();
								for(int i=jms.size()-1;i>=0;i--){
									JudgmentCondition jc = new JudgmentCondition();
									jc.setParamTypeNames(jms.get(i).getParamTypeNames());
									jc.setWholeClassName(jms.get(i).getWholeClassName());
									translate(jms.get(i).getMethodName(), jc);
									if(writePoint){
										bw.write(".");
									}
									if(jms.get(i).getMethodName().getTargetValue().equals("pageUI~")){
										bw.write("pageUI");
										writePoint = true;
									}else{
										if(jms.get(i).getMethodName().getTargetValue().trim().length() > 0){
											List<JParam> jpas = jms.get(i).getParamMapToList();
											StringBuffer sjp = new StringBuffer();
											for(int j=0;j<jpas.size();j++){
												if(j > 0){
													sjp.append(",");
												}
												sjp.append(jpas.get(j).getParamValue().getOriginalValue());
											}
											if(jms.get(i).getObjname() != null && jms.get(i).getObjname().getOriginalValue() != null && jms.get(i).getObjname().getOriginalValue().indexOf("(") == -1){
												bw.write(jms.get(i).getObjname().getOriginalValue() + "." + jms.get(i).getMethodName().getTargetValue()+"("+sjp.toString()+")");
											}else{
												bw.write(jms.get(i).getMethodName().getTargetValue()+"("+sjp.toString()+")");
											}
											writePoint = true;
										}else{
											writePoint = false;
										}
									}
									noContent = false;
								}
								if(noContent){
									bw.write(js.getStatement());
								}else{
									bw.write(";");
									bw.newLine();
								}
							}
							bw.write("}");
							bw.newLine();
						}
						break;
					case ASTNode.ENUM_DECLARATION:
						break;
					case ASTNode.ANNOTATION_TYPE_DECLARATION:
						break;
					default:
						break;
				}
			}
		}catch (IOException e) {
			CaLogger.error(filename + "文件写入失败");
		}finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					CaLogger.error("写入流关闭失败");
				}
			}
		}
	}

}