package nc.uap.lfw.design.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.uif.listener.UifLineOperatorMouseListener;
import nc.uap.lfw.core.uif.listener.UifMethodAnnotation;
import nc.uap.lfw.design.itf.GenRefCodeConstant;
import nc.uap.lfw.design.itf.IGeneratorCode;
import nc.uap.lfw.util.StringPool;
import nc.uap.lfw.util.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFNumberFormat;
import nc.vo.pub.lang.UFTime;

/**
 * 产生代码
 * @author zhangxya
 *
 */
public class GeneratorCodeImpl implements IGeneratorCode{
	
	public List<String> packageList = new ArrayList<String>();
	private static final List<String> excludePackList = new ArrayList<String>();
	static{
		excludePackList.add(String.class.getName());
		excludePackList.add(int.class.getName());
		excludePackList.add(void.class.getName());
		excludePackList.add(String[].class.getName());
		excludePackList.add(int[].class.getName());
		excludePackList.add(Integer.class.getName());
		excludePackList.add(boolean.class.getName());
		excludePackList.add(Boolean.class.getName());
	}
	
	private void addPackageByType(String type){
		if(type.equals(StringDataTypeConst.DOUBLE) || type.equals(StringDataTypeConst.dOUBLE)){
			if(!packageList.contains(Double.class.getName()))
				packageList.add(Double.class.getName());
		}else if(type.equals(StringDataTypeConst.UFDOUBLE)){
			if(!packageList.contains(UFDouble.class.getName()))
				packageList.add(UFDouble.class.getName());
		}
		else if(type.equals(StringDataTypeConst.FLOATE) || type.equals(StringDataTypeConst.fLOATE)){
			if(!packageList.contains(Float.class.getName()))
				packageList.add(Float.class.getName());
		}
		else if(type.equals(StringDataTypeConst.bYTE) || type.equals(StringDataTypeConst.BYTE)){
			if(!packageList.contains(Byte.class.getName()))
				packageList.add(Byte.class.getName());
		}
		else if(type.equals(StringDataTypeConst.UFBOOLEAN)){
			if(!packageList.contains(UFBoolean.class.getName()))
				packageList.add(UFBoolean.class.getName());
		}
		else if(type.equals(StringDataTypeConst.DATE)){
			if(!packageList.contains(Date.class.getName()))
				packageList.add(Date.class.getName());
		}
		else if(type.equals(StringDataTypeConst.BIGDECIMAL)){
			if(!packageList.contains(BigDecimal.class.getName()))
				packageList.add(BigDecimal.class.getName());
		}
		else if(type.equals(StringDataTypeConst.lONG) || type.equals(StringDataTypeConst.lONG)){
			if(!packageList.contains(Long.class.getName()))
				packageList.add(Long.class.getName());
		}
		else if(type.equals(StringDataTypeConst.UFDATE)){
			if(!packageList.contains(UFDate.class.getName()))
				packageList.add(UFDate.class.getName());
		}
		else if(type.equals(StringDataTypeConst.UFTIME)){
			if(!packageList.contains(UFTime.class.getName()))
				packageList.add(UFTime.class.getName());
		}
		else if(type.equals(StringDataTypeConst.UFNUMBERFORMAT)){
			if(!packageList.contains(UFNumberFormat.class.getName()))
				packageList.add(UFNumberFormat.class.getName());
		}
		else if(type.equals(StringDataTypeConst.OBJECT)){
			if(!packageList.contains(Object.class.getName()))
				packageList.add(Object.class.getName());
		}
	}
	
	public String generatorPackageCode(String extendClass, String fullPath){
		StringBuffer packageBuffer =  new StringBuffer();
		packageBuffer.append("package " + fullPath + ";\n");
		if (packageList.size() > 0) {
			for (int j = 0; j < packageList.size(); j++) {
				String pname = packageList.get(j);
				if(excludePackList.contains(pname))
					continue;
				packageBuffer.append("import " + pname + ";\n");
			}
		}
		packageBuffer.append("\n");
		return packageBuffer.toString();
	}
	
	public void generatorCodeByContructor(String sourceName, Class extendClass, StringBuffer buf){
		//构造函数
		Constructor<?>[] contructors = extendClass.getConstructors();
		for (int i = 0; i < contructors.length; i++) {
			Constructor con = contructors[i];
			String className = con.getName();
			buf.append("\tpublic " + sourceName);
			buf.append("(");
			
			Class<?>[] pamTypes = con.getParameterTypes();
			for (int j = 0; j < pamTypes.length; j++) {
				Class pam = pamTypes[j];
				String name = pam.getName();
				if(!packageList.contains(name))
					packageList.add(name);
				buf.append(pam.getSimpleName());
				buf.append(" arg" + j);
				if(j != pamTypes.length - 1)
					buf.append(",");
			}
			buf.append(")");
			buf.append("{\n");
			buf.append("\t\tsuper(");
			for (int j = 0; j < pamTypes.length; j++) {
				buf.append("arg" + j);
				if(j != pamTypes.length - 1)
					buf.append(",");
			}
			buf.append(");\n");
			buf.append("\t}\n");
		}
		
	}
	
	public void generatorMethod(Class extendClass, StringBuffer buf, Map<String, Object> param){
		Method[] methods = extendClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if(Modifier.isAbstract(method.getModifiers())){
				String MethodName = method.getName();
				if(Modifier.isPublic(method.getModifiers())){
					buf.append("\tpublic");
				}
				else
					buf.append("\tprotected");
				
				Class returnType = method.getReturnType();
				String returnTypepackage = returnType.getName();
				if(!packageList.contains(returnTypepackage))
					packageList.add(returnTypepackage);
				//buf.append("package " + returnTypepackage + ";\n");
				buf.append("\t" + returnType.getSimpleName());
				buf.append("\t" + method.getName());
				
				buf.append("(");
				Class<?>[] parameterTypes = method.getParameterTypes();
				for (int j = 0; j < parameterTypes.length; j++) {
					Class paramterType = parameterTypes[i];
					String paramterPackage = paramterType.getClass().getName();
					if(!packageList.contains(paramterPackage))
						packageList.add(paramterPackage);
					buf.append(paramterPackage + "/targ" + j);
					if(j != parameterTypes.length - 1)
						buf.append(",");
				}
				buf.append(")");
				buf.append("{\n");
				
//				if(UifLineAddMouseListener.class.isAssignableFrom(extendClass)){
//					if(method.getName().equals("getItemDsMap")){
//						if(!packageList.contains("java.util.HashMap"))
//							packageList.add("java.util.HashMap");
//						UifMethodAnnotation annta = method.getAnnotation(UifMethodAnnotation.class);
//						String name = annta.name();
//						Map<String, String> map = (Map<String, String>) param.get(name);
//						Iterator<Entry<String, String>> it = map.entrySet().iterator();
//						buf.append("\t\tMap<String, String> map = new HashMap<String, String>();\n");
//						while(it.hasNext()){
//							Entry<String, String> entry = it.next();
//							buf.append("\t\tmap.put(\"" + entry.getKey() + "\", \"" + entry.getValue() + "\");\n");
//						}
//						buf.append("\t\treturn map;\n\t}\n");
//						continue;
//					}
//				}
				
				UifMethodAnnotation annta = method.getAnnotation(UifMethodAnnotation.class);
				if(annta == null) {
					if (!"".equals(returnTypepackage))
						buf.append("\t\t" + "return null;\n");
					buf.append("\t}\n");
					continue;
				}
				String returnValue = annta.name();
				if(param.get(returnValue) != null){
					if (!annta.isMultiSel()) {
						if ("String".equals(returnType.getSimpleName()))
							buf.append("\t\t" + "return \"" + param.get(returnValue) + "\";\n");
						else
							buf.append("\t\t" + "return " + param.get(returnValue) + ";\n");
					} else if(annta.isMultiSel()){
						Object value = param.get(returnValue);
						String[] returnVs = null;
//						if(value instanceof String){
//							returnVs = new String[]{value.toString()};
//						}
//						else if(value instanceof String[])
//							returnVs = (String[]) value;
						returnVs = value.toString().split(",");
						
						buf.append("\t\t" + "return new String[]{" + StringUtil.mergeScriptArray(returnVs) + "};\n");
					}
					
				}
				else
					buf.append("\t\t" + "return null;\n");
				buf.append("\t}\n");
				
			}
		}
		
		if(UifLineOperatorMouseListener.class.isAssignableFrom(extendClass)){
			Method[] superMethods = UifLineOperatorMouseListener.class.getDeclaredMethods();
			String dsId = (String) param.get("BodyInfo子数据集");
			if(dsId != null){
				for (int i = 0; i < superMethods.length; i++) {
					Method method = superMethods[i];
					if(method.getName().equals("getBodyInfo")){
						if(!packageList.contains("nc.uap.lfw.core.uif.listener.IBodyInfo"))
							packageList.add("nc.uap.lfw.core.uif.listener.IBodyInfo");
						if(!packageList.contains("nc.uap.lfw.core.uif.listener.SingleBodyInfo"))
							packageList.add("nc.uap.lfw.core.uif.listener.SingleBodyInfo");
						buf.append("\tpublic IBodyInfo getBodyInfo(){\n");
						buf.append("\t\treturn new SingleBodyInfo(");
						buf.append("\""+ dsId + "\"" + ");\n");
						buf.append("\t}\n");
						break;
					}
				}
			}
		}
		buf.append("}\n");
	}
	
	
	public String generatorCode(String fullPath, String extendClass, Map<String, Object> param)
			throws LfwBusinessException {
		//name="片段" value="id, ids"
		String packagePath = fullPath.substring(0,fullPath.lastIndexOf("."));
		String className = fullPath.substring(fullPath.lastIndexOf(".") + 1);
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("public class " + className + " extends " + extendClass + "{\n");
			Class<?> extendsClass = Class.forName(extendClass);
			//产生构造函数
			generatorCodeByContructor(className, extendsClass, buf);
			//产生方法
			generatorMethod(extendsClass, buf, param);
			//产生package
			String packageString = generatorPackageCode(extendClass, packagePath);
			return packageString + buf.toString();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String generatorVO(String fullPath, String tableName, String primaryKey, Dataset ds) throws LfwBusinessException {
		String packagePath = fullPath.substring(0,fullPath.lastIndexOf("."));
		String className = fullPath.substring(fullPath.lastIndexOf(".") + 1);
		String extendClass = "nc.vo.pub.SuperVO";
		StringBuffer buf = new StringBuffer();
		buf.append("public class " + className + " extends " + extendClass.substring(extendClass.lastIndexOf(".") + 1) + " {\n");
		Field[] fields = ds.getFieldSet().getFields();
		//产生所有的Field字段
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String voField = field.getField();
			if(voField != null && !voField.equals("")){
				String fieldType = field.getDataType();
				if(fieldType == null || fieldType.equals(""))
					fieldType = "String";
				buf.append("	private " + fieldType + " " + voField + ";\n");
				addPackageByType(fieldType);
			}
		}
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String voField = field.getField();
			if(voField != null && !voField.equals("")){
				String fieldType = field.getDataType();
				if(fieldType == null || fieldType.equals(""))
					fieldType = "String";
				//getMethod()
				buf.append("\tpublic " + fieldType + " get"  + voField.substring(0,1).toUpperCase() + voField.substring(1) + "(){" + "\n");
				buf.append("\t\treturn " + voField + ";\n");
				buf.append("\t}\n");
				//setMethod()
				buf.append("\tpublic void set" + voField.substring(0,1).toUpperCase() + voField.substring(1) + "(" + fieldType + " " + voField + ") {\n");
				buf.append("\t\tthis." + voField + "=" + voField + ";\n");
				buf.append("\t}\n");
			}
		}
		buf.append("\tpublic String getPrimaryKey(){\n");
		buf.append("\t\treturn " + primaryKey + ";\n");
		buf.append("\t}\n");
		buf.append("\tpublic String getTableName() {\n");
		buf.append("\t\treturn \"" + tableName + "\";\n");
		buf.append("\t}\n");
		//getPKFieldName
		buf.append("\tpublic String getPKFieldName() {\n");
		buf.append("\t\treturn \"" + primaryKey + "\";\n");
		buf.append("\t}\n");
		//
		buf.append("}");
		//产生package
		if(!packageList.contains(extendClass))
			packageList.add(extendClass);
		String packageString = generatorPackageCode(extendClass, packagePath);
		return packageString + buf.toString();
	}

	public String generateRefNodeClass(String refType, String modelClass, String tableName,
		String refPk, String refCode, String refName, String visibleFields,
		String pfield, String childfield) throws LfwBusinessException {
		StringBuffer buf = new StringBuffer();
		BufferedReader reader = null;
		String cs = null;
		try {
			//this.getClass().getClassLoader().getResourceAsStream("nc/bs/pub/action/N_XXXX_" + action + ".java")
			String path = RuntimeEnv.getInstance().getNCHome() + "/resources/lfw/refnode";
			String templatePath = null;
			if(refType.equals("grid")){
				templatePath = "GridModel.java";
			}
			else{
				templatePath = "TreeModel.java";
			}
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/" + templatePath)));
			String line = reader.readLine();
			while(line != null){
				buf.append(line);
				buf.append("\r\n");
				line = reader.readLine();
			}
			cs = buf.toString();
			String className = modelClass.substring(modelClass.lastIndexOf(".") + 1);
			String packageName = modelClass.substring(0, modelClass.lastIndexOf("."));
			cs = cs.replaceAll(GenRefCodeConstant.PACKAGE, packageName);
			cs = cs.replaceAll(GenRefCodeConstant.MODEL_CLASS, className);
			cs = cs.replaceAll(GenRefCodeConstant.REF_PK, refPk);
			cs = cs.replaceAll(GenRefCodeConstant.REF_NAME, refName);
			cs = cs.replaceAll(GenRefCodeConstant.REF_CODE, refCode);
			cs = cs.replaceAll(GenRefCodeConstant.TABLE_NAME, tableName);
			cs = cs.replaceAll(GenRefCodeConstant.VISIBLE_FIELDS, mergeArray(visibleFields));
			cs = cs.replaceAll(GenRefCodeConstant.PARENT_FIELD, pfield);
			cs = cs.replaceAll(GenRefCodeConstant.CHILD_FIELD, childfield);
		} 
		catch (IOException e) {
			throw new LfwRuntimeException("获取脚本时出错");
		}
		finally{
			if(reader != null){
				try {
					reader.close();
				} 
				catch (IOException e) {
				}
			}
		}
		return cs;
	}

	private String mergeArray(String visibleFields) {
		String[] strArr = visibleFields.split(StringPool.COMMA);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < strArr.length; i++) {
			buf.append("\"");
			buf.append(strArr[i]);
			buf.append("\"");
			if(i != strArr.length - 1)
				buf.append(",");
		}
		return buf.toString();
	}
}
