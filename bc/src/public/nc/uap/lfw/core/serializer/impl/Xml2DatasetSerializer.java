package nc.uap.lfw.core.serializer.impl;

import static nc.uap.lfw.core.common.IntDataTypeConst.BigDecimal_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.Byte_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.Character_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.Date_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.Double_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.Float_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.Integer_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.Long_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.String_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.UFBoolean_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.UFDateTime_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.UFDate_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.UFDate_begin;
import static nc.uap.lfw.core.common.IntDataTypeConst.UFDate_end;
import static nc.uap.lfw.core.common.IntDataTypeConst.UFDouble_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.UFLiteralDate;
import static nc.uap.lfw.core.common.IntDataTypeConst.UFTime_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.boolean_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.byte_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.char_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.double_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.float_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.int_TYPE;
import static nc.uap.lfw.core.common.IntDataTypeConst.long_TYPE;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.DataTypeTranslator;
import nc.uap.lfw.core.common.EventContextConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.core.serializer.IXml2ObjectSerializer;
import nc.uap.lfw.util.LanguageUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pub.lang.UFTime;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class Xml2DatasetSerializer implements IXml2ObjectSerializer<Dataset[]> {

	public static final String PAGEMETA_KEY = "pagemeta";
	
	public Dataset[] serialize(String xml, Map<String, Object> paramMap) {
		try {
			ArrayList<Dataset> dss = new ArrayList<Dataset>();
			Document doc = XmlUtilPatch.getDocumentBuilder().parse(new InputSource(new StringReader(xml)));
			NodeList datasetList = doc.getElementsByTagName("dataset");
			if (datasetList != null) 
			{
				Map<Dataset, Element> dsEleMap = new HashMap<Dataset, Element>();
				for (int i = 0; i < datasetList.getLength(); i++) 
				{
					Element dsEle = (Element) datasetList.item(i);
					String dsId = dsEle.getAttribute("id");
					String[] ids = dsId.split("\\."); 
					String widgetId = ids[0];
					String datasetId = ids[1];
					
					PageMeta pagemeta = null;
					if(paramMap != null && paramMap.get(PAGEMETA_KEY) != null){
						pagemeta = (PageMeta) paramMap.get(PAGEMETA_KEY);
					}
					else
						pagemeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
					Dataset ds = pagemeta.getWidget(widgetId).getViewModels().getDataset(datasetId);
					
					this.processDataset(ds, dsEle);
					dsEleMap.put(ds, dsEle);
					
//					Parameter param = ds.getReqParameters().getParameter(DatasetConstant.QUERY_PARAM_KEYS);
//					if (param != null) 
//					{
//						String[] keys = param.getValue().split(",");
//						Parameter valueParam = ds.getReqParameters().getParameter(DatasetConstant.QUERY_PARAM_VALUES);
//						if (valueParam != null) 
//						{
//							String[] values = valueParam.getValue().split(",");
//							String fieldName = null;
//							for (int j = 0; j < keys.length && j < values.length; j++) 
//							{
//								fieldName = ds.getFieldSet().getFieldById(keys[j].trim());
//								if(fieldName != null && !fieldName.equals(""))
//									ds.getReqParameters().addParameter(new Parameter(fieldName, values[j]));
//								else
//									ds.getReqParameters().addParameter(new Parameter(keys[j].trim(),values[j]));
//							}
//						}
//					}
					dss.add(ds);
				}
				
//				if(datasetList.getLength() == 1){
//					Element dsEle = (Element) datasetList.item(0);
//					NodeList recordList = dsEle.getElementsByTagName(EventContextConstant.records);
//					if(recordList.getLength() == 0)
//						return null;
//				}
				fillDataset(dsEleMap);
			}
			Dataset[] dsArr = dss.toArray(new Dataset[0]);
			for (int i = 0; i < dsArr.length; i++) {
				dsArr[i].setCtxChanged(false);
				RowSet[] rss = dsArr[i].getRowSets();
				if(rss != null){
					for (int j = 0; j < rss.length; j++) {
						RowSet rs = rss[j];
						RowData[] rds = rs.getRowDatas();
						if(rds != null){
							for (int k = 0; k < rds.length; k++) {
								rds[k].setRowDataSelfChanged(false);
							}
						}
					}
				}
			}
			return dsArr;
		} 
		catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException("序列化Dataset时出错");
		} 
		
	}
	
	protected void fillDataset(Map<Dataset, Element> dsEleMap) {
		// 此步中已经将请求参数和records记录设入了ds中
		Iterator<Entry<Dataset, Element>> it = dsEleMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Dataset, Element> entry = it.next();
			Element dsEle = entry.getValue();
			Dataset ds = entry.getKey();
			NodeList rowsets = dsEle.getElementsByTagName("rowsets");
			if(rowsets != null && rowsets.getLength() > 0){
				NodeList rowsetsList = ((Element)rowsets.item(0)).getElementsByTagName("rowset");
				processRowsets(rowsetsList, ds);
			}
			//processRecords(recordsList, entry.getKey());
			String randomRowIndex = dsEle.getAttribute("randomRowIndex");
			ds.setRandomRowIndex(Integer.parseInt(randomRowIndex));
		}
	}
	
	protected void processRowsets(NodeList rowsetsList, Dataset ds) {
		int size = rowsetsList.getLength();
		for(int i = 0; i < size; i ++){
			Element rowset = (Element) rowsetsList.item(i);
			String pageindex = rowset.getAttribute("pageindex");
			String pagesize = rowset.getAttribute("pagesize");
			String recordcount = rowset.getAttribute("recordcount");
			String keyvalue = rowset.getAttribute("keyvalue");
			if(keyvalue == null || keyvalue.equals(""))
				continue;
			RowSet rowSet = ds.getRowSet(keyvalue, true);
			PaginationInfo pInfo = new PaginationInfo();
			pInfo.setPageSize(Integer.parseInt(pagesize));
			pInfo.setRecordsCount(Integer.parseInt(recordcount));
			pInfo.setPageIndex(Integer.parseInt(pageindex));
			rowSet.setPaginationInfo(pInfo);
			
			NodeList recordsList = rowset.getElementsByTagName(EventContextConstant.records);
			
			processRecords(recordsList, rowSet, ds);
			rowSet.setRowSetChanged(false);
		}
		
		// TODO Auto-generated method stub
	}

	private void processRecords(NodeList recordsList,RowSet rowSet, Dataset ds)
	{
		if (recordsList != null && recordsList.getLength() != 0) {
			int rsize = recordsList.getLength();
			for (int m = 0; m < rsize; m++) {
				Element recordsNode = (Element) recordsList.item(m);
				
				String pageindex = recordsNode.getAttribute("pageindex");
				RowData rowData = new RowData(Integer.parseInt(pageindex));
				
				rowSet.addRowData(rowData.getPageindex(), rowData);
				NodeList recordList = recordsNode.getChildNodes();
				int size = recordList.getLength();
				for (int i = 0; i < size; i++) {
					Node node = (Node) recordList.item(i);
					if(node instanceof Text)
						continue;
					Element recordEle = (Element) node;
					if(recordEle.getNodeName().equals("selected")){
						String selected = recordEle.getFirstChild() == null? null : recordEle.getFirstChild().getNodeValue();
						if(selected != null && !selected.equals("")){
							String[] sels = selected.split(",");
							Integer[] selectedIndices = new Integer[sels.length];
							for (int k = 0; k < selectedIndices.length; k++) {
								selectedIndices[k] = Integer.valueOf(sels[k]);
							}
							rowData.setRowSelectIndices(selectedIndices);
						}
						continue;
					}
					if(recordEle.getNodeName().equals("focus")){
						String focusIndex = recordEle.getFirstChild() == null? null : recordEle.getFirstChild().getNodeValue();
						if(focusIndex != null && !focusIndex.equals("")){
							ds.setFocusIndex(Integer.valueOf(focusIndex));
						}
						continue;
					}
	//				if(recordEle instanceof)
					String rowId = recordEle.getAttribute("id");
					String parentId = recordEle.getAttribute("parentid");
	
					if(recordEle.getNodeName().equals(EventContextConstant.erecord)){
						Row emptyRow = new EmptyRow(rowId);
						rowData.addRow(emptyRow);
						emptyRow.setRowChanged(false);
					}
					else{
						Row row = ds.getEmptyRow();
						rowData.addRow(row);
						row.setRowId(rowId);
						
						if (!isNull(parentId))
							row.setParentId(parentId);
		
						NodeList stateRecords = recordEle.getChildNodes();
						int j = 0;
						while(stateRecords.item(j) instanceof Text){
							j ++;
						}
						
						Element stateEle = (Element) stateRecords.item(j);
						String stateName = stateEle.getNodeName();
						row.setState(getState(stateName));
		
		//				dataSet.getRowSet().addRow(row);
						if(stateEle.getFirstChild() != null){
							String recordValue = stateEle.getFirstChild().getNodeValue();
							// 处理ajax请求的记录内容并设置Row对象的各个字段值
			                processXmlToContentRow(ds,recordValue, row);
						}
		                row.setRowChanged(false);
					}
				}
				
				rowData.setRowDataChanged(false);
				rowData.setRowDataSelfChanged(false);
			}
		}
	}
	
	/**
	 * 根据前台ajax发送的记录请求内容，设置Row对象的各个字段值。
	 * 即将<upd>,,,,,,</upd>中逗号分割的部分根据其类型进行DataSet中
	 * 某个Row的设置。
	 * 
	 * @param ds
	 * @param content
	 * @param row
	 */
	public void processXmlToContentRow(Dataset ds,String content,Row row)
	{
		FieldSet fieldSet = ds.getFieldSet();
		//将""值也转换为数组元素 dengjt
		String[] fieldValue = content.split(",", -1);
		try {
			int fieldCount = fieldSet.getFieldCount();
			for(int i = 0;i < fieldCount && i < fieldValue.length; i++)
			{
				String value = fieldValue[i];
		       	Field currentField = fieldSet.getField(i);
		       	String dataTypeStr = currentField.getDataType();
		       	int dataType = DataTypeTranslator.translateString2Int(dataTypeStr);
		       	 //恢复空值
		       	if(value.equals(EventContextConstant.NULL))
		       		value = null;
		       	else
		       		value = URLDecoder.decode(value, "UTF-8");
		       	processRowContent(currentField, dataType, value, row,i);	 
			}
		}
		catch(UnsupportedEncodingException e) {
			Logger.error("DefaultDatasetSerializer.processXmlToContentRow:"+ e.getMessage(), e);
		}
	}
	
	protected void processDataset(Dataset ds, Element dsEle) {
		ds.clear();
		
		String currentkey = dsEle.getAttribute("currentkey");
		ds.setCurrentKey(currentkey);
		String editable = dsEle.getAttribute("editable");
		ds.setEnabled(Boolean.parseBoolean(editable));
		
		NodeList reqParamList = dsEle.getElementsByTagName(EventContextConstant.req_parameters);
		if (reqParamList != null && reqParamList.getLength() != 0) {
			Node reqParamNode = reqParamList.item(0);
			NodeList paramList = reqParamNode.getChildNodes();
			for (int i = 0; i < paramList.getLength(); i++) {

				Element pElement = (Element) paramList.item(i);
				String name = pElement.getAttribute("name");
				String value = "";
				if(pElement.getFirstChild() != null)
					value = pElement.getFirstChild().getNodeValue();

				Parameter param = new Parameter(name, value);
				ds.getReqParameters().addParameter(param);
			}
		}
		//if (pagecount != null && !pagecount.trim().equals(""))
			//ds.getPaginationInfo().setPageCount(Integer.parseInt(pagecount));
	}
	
	public static void processRowContent(Field field, int dataType,String content,Row row,int index)
	{
		// 后台框架级校验暂时屏蔽,前台负责了校验,如果需要校验则在自己的handler代码中进行!!!
		/*进行规则校验*/
		//if(row.getState() != Row.STATE_DELETED)
			//ruleCheck(field, content, index);
		
		if(content == null){
			row.setNull(index);
			return ;
		}
		String con = content.toLowerCase();
		
		try{
			switch(dataType){
			
			   case Integer_TYPE:
				   row.setInteger(index, content.equals("") ? null: Integer.parseInt(content));
				   break;
			   case UFDate_begin:
				   row.setValue(index, content.equals("")? null: new UFDate(Long.parseLong(content)));
				   break;
			   case UFLiteralDate:
				   row.setValue(index, content.equals("")? null: new UFLiteralDate(Long.parseLong(content)));
				   break;
			   case UFDate_end:
				   row.setValue(index, content.equals("")? null: new UFDate(Long.parseLong(content)));
				   break;
			   case int_TYPE:  
				   row.setInt(index, content.equals("")? 0: Integer.parseInt(content));
			       break;
			   case String_TYPE:
				   row.setString(index, content);
				   break;
			   case boolean_TYPE:
				   con = con.toLowerCase();
				   boolean value = con.equals("true")?true:(con.equals("1")?true:(con.equals("y")?true:false));
			       row.setBoolean(index, value);
			       break;
			   case UFBoolean_TYPE:
				   con = con.toLowerCase();
				   value = con.equals("true")?true:(con.equals("1")?true:(con.equals("y")?true:false));
			       row.setUFBoolean(index, value);
			       break;
			   case double_TYPE:
			   case Double_TYPE:
				   row.setDouble(index, content.equals("")? 0 : Double.parseDouble(content));
				   break;
			   case UFDouble_TYPE:
				   row.setValue(index, content.equals("")? null : new UFDouble(content));
				   break;
			   case float_TYPE :
			   case Float_TYPE :
				   row.setFloat(index, content.equals("")? 0 : Float.parseFloat(content));
				   break;
			   case byte_TYPE:
			   case Byte_TYPE:
				   row.setFloat(index, content.equals("")? 0 : Byte.parseByte(content));
				   break;
			   case Date_TYPE:
				   if(content.length() == 10)
					   row.setDate(index, java.sql.Date.valueOf(content));//--此处可能有问题，待改正.
				   break;
			   case BigDecimal_TYPE:
				   row.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(content)));
				   break;
			   case long_TYPE:
			   case Long_TYPE:
				   row.setLong(index, Long.parseLong(content));
				   break;
			   case char_TYPE:
			   case Character_TYPE:
				   row.setChar(index, content.charAt(0));
				   break;
			   case UFDate_TYPE:
//				   if (content.matches("[0-9]+"))
				   if(!"".equals(content))
					   row.setValue(index, content.equals("")? null: new UFDate(Long.parseLong(content)));
//				   else
//					   row.setValue(index, content.equals("")? null: new UFDate(content));
				   break;
			   case UFDateTime_TYPE:
				   if(content != null && !content.equals("")){
//					   if (content.matches("[0-9]+"))
					   row.setValue(index, new UFDateTime(Long.parseLong(content)));
//					   else{
//						   if(content.length() == 10)
//							   content += " 00:00:00";
//						   row.setValue(index, new UFDateTime(content));
//					   }
				   }
				   break;
			   case UFTime_TYPE:
				   row.setValue(index, UFTime.getValidUFTimeString(content));
				   break;
			   default:
				   row.setValue(index, content);			   
			}
		}
		catch(Exception e)
		{
			String value = field.getI18nName();
			if(isEmpty(value))
				value = field.getId();
			String i18nName = LanguageUtil.getWithDefaultByProductCode(LfwRuntimeEnvironment.getLangDir(), value, value);
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), "字段：" + i18nName + " 类型错误!");
		}
	}
	
	private static boolean isNull(String s) {
		if (s == null || s.trim().equals(""))
			return true;
		else
			return false;
	}
	
	private static boolean isEmpty(String content)
	{
		if(content == null || content.equals(""))
			return true;
		return false;
	}
	
	/**
	 * 获取行状态
	 * @param stateName
	 * @return
	 */
	private static int getState(String stateName) {
		if (stateName.trim().equals("add"))
			return Row.STATE_ADD;
		else if (stateName.trim().equals("upd")) 
			return Row.STATE_UPDATE;
		else if (stateName.trim().equals("del"))
			return Row.STATE_DELETED;
		//假删除的行，在序列化回来时将状态置为删除标志
		else if (stateName.trim().equals("fdel"))
			return Row.STATE_FALSE_DELETED;
		else
			return Row.STATE_NORMAL;
	}
}
