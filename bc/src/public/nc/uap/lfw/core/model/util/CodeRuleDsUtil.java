package nc.uap.lfw.core.model.util;

import java.util.HashMap;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;

public class CodeRuleDsUtil implements ICodeRuleDsUtil{
	
	private HashMap<Row, String> obj_code_map = new HashMap<Row, String>();
	private HashMap<String, Row> code_obj_map = new HashMap<String, Row>();
	private int[] codeSection = null;

	public void buildCodeRuleDataset(Dataset ds, String codeRule, int codeIndex, int pkIndex, int ppkIndex){
		try {
			if (ds != null) {
				parseCodeRule(codeRule);
				boolean addCol = false;
				RowSet[] rss = ds.getRowSets();
				for (int i = 0; i < rss.length; i++) {
					RowSet rs = rss[i];
					RowData[] rds = rs.getRowDatas();
					for (int j = 0; j < rds.length; j++) {
						RowData rd = rds[j];
						int size = rd.getRowCount();
						for (int k = 0; k < size; k++) {
							Row row = rd.getRow(k);
							if(row instanceof EmptyRow)
								continue;
							if(k == 0){
								if(row.size() < ds.getFieldSet().getFieldCount())
									addCol = true;
							}
							if(addCol)
								row.addColumn();
							String codeValue = row.getString(codeIndex);
							obj_code_map.put(row, codeValue);
							code_obj_map.put(codeValue, row);
							
						}
						createTree(rd, pkIndex, ppkIndex);
						
					}
				}
				
			}
		}
		catch (Exception e) {
			Logger.error("数据有错,无法构建成树!(nc.ui.pub.report.treetableex.TreeCreateTool.createTreeByCode(CircularlyAccessibleValueObject [], String, String, String))");
		}
	}
	

	
	private void createTree(RowData rd, int pkIndex, int ppkIndex) {
		int size = rd.getRowCount();
		for (int i = 0; i < size; i++){
			Row row = rd.getRow(i);
			String childCode = obj_code_map.get(row);
			if(childCode == null)
				throw new LfwRuntimeException("Child code is null");
			String parentCode = getParentCode(childCode);
			Row parentNode = code_obj_map.get(parentCode);

			if(parentNode != null){
				
				row.setValue(ppkIndex, parentNode.getString(pkIndex));
			}

		}

	}
	
	/**
	 * 解析编码规则
	 *
	 */
	private void parseCodeRule(String codeRule) {
		java.util.StringTokenizer st = new java.util.StringTokenizer(codeRule, " ,./\\", false);
		int count = st.countTokens();
		codeSection = new int[count];
		int index = 0;
		try{
			while(st.hasMoreTokens()){
				codeSection[index++] = Integer.parseInt(st.nextToken().trim());
			}
		}
		catch(Exception e){
			LfwLogger.error("解析编码规则出错", e);
			codeSection = null;
		}
	}
	
	private String getParentCode(String childCode) {
		String parentCode = "";
		if (codeSection != null) {
			int index = 0;
			int sublength = 0;
			int length = childCode.length();
			while(length > 0 && index < codeSection.length){
				length -= codeSection[index];			
				if(length > 0){
					sublength += codeSection[index];
				}
				index ++;
			}
			if(sublength > 0)
				parentCode = childCode.substring(0, sublength);
		}
		return parentCode;
	}
}
