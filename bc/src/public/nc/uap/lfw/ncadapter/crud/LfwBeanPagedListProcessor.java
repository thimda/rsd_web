package nc.uap.lfw.ncadapter.crud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.mapping.MappingMetaManager;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.util.BeanConvertor;
import nc.jdbc.framework.util.InOutUtil;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.SuperVO;

import org.apache.commons.beanutils.Converter;

public class LfwBeanPagedListProcessor extends BaseProcessor {

	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = -6448041138120901285L;

	private Class type = null;

	private int skip = 0;

	private int size = 10;

	public LfwBeanPagedListProcessor(Class type, int skip, int size) {
		this.type = type;
		this.skip = skip;
		this.size = size;
	}

	public Object processResultSet(ResultSet rs) throws SQLException {
		return toBeanList(rs, this.type, skip, size);
	}
	
	
	@SuppressWarnings("unchecked")
	private PagedResult toBeanList(ResultSet resultSet, Class type, int skip,
			int size) throws SQLException {
		MethodAndTypes methodAndTypes = getBeanInvokeAndTypes(type, resultSet,
				null, null);
		List list = new ArrayList();
		int index = 0;
		PagedResult result = new PagedResult();
		while (index < skip) {
			if(resultSet.next())
				index++;
			else
				break;
		}
		if (index < skip){
			result.setSize(index);
			result.setResult(list);
			return result;
		}
		
		int end = skip + size;
//		int offset = 0;
		while(resultSet.next()) {
			if(index < end){
				//index ++;
				Object target = newInstance(type);
				for (int i = 1; i <= methodAndTypes.types.length; i++) {
					Object value = getColumnValue(methodAndTypes.types[i - 1],
							resultSet, i);
					if (value == null)
						continue;
					Method invoke = methodAndTypes.invokes[i - 1];
					if (invoke == null) {
						// 如果是null则不赋值
						continue;
					}
					Converter converter = methodAndTypes.converters[i - 1];
					if (converter != null)
						value = converter.convert(invoke.getParameterTypes()[0],
								value);
					BeanHelper.invokeMethod(target, invoke, value);
					// populate(target, value, methodAndTypes.invokes[i - 1]);
				}
				list.add(target);
			}
			index ++;
		}

		result.setResult(list);
		result.setSize(index);
		return result;
	}

	
	private class MethodAndTypes {
		public Method[] invokes = null;

		public int[] types = null;

		public Converter[] converters = null;

	}
	
	private MethodAndTypes getBeanInvokeAndTypes(Class type,
			ResultSet resultSet, IMappingMeta meta, String[] columns)
			throws SQLException {
		MethodAndTypes retResult = new MethodAndTypes();

		Object bean = newInstance(type);
		boolean isSuperBean = false;
		if (bean instanceof SuperVO) {
			isSuperBean = true;
		}
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();
		Method[] invokes = new Method[cols];
		String[] colName = new String[cols];
		Converter[] converters = new Converter[cols];
		int[] types = new int[cols];
		for (int i = 0; i < cols; i++) {
			types[i] = metaData.getColumnType(i + 1);
			if (columns != null)
				colName[i] = columns[i].toLowerCase();
			else
				colName[i] = metaData.getColumnName(i + 1).toLowerCase();
			String propName = colName[i];
			if (meta != null) {
				propName = MappingMetaManager.getMapingMeta(meta)
						.getAttributeName(colName[i]);
				if (propName == null)
					continue;
				propName = propName.toLowerCase();
			}

			if (isSuperBean) {
				invokes[i] = getSuperBeanInvokeMethod(bean, propName);
			} else {
				invokes[i] = BeanHelper.getMethod(bean, propName);
			}
			if (invokes[i] != null)
				converters[i] = BeanConvertor.getConVerter(invokes[i]
						.getParameterTypes()[0]);
		}
		retResult.invokes = invokes;
		retResult.types = types;
		retResult.converters = converters;
		return retResult;
	}
	
	private Method getSuperBeanInvokeMethod(Object bean, String colName) {

		Method m = BeanHelper.getMethod(bean, colName);
		if (m != null)
			return m;
		String pkFiledName = ((SuperVO) bean).getPKFieldName();
		if (pkFiledName == null)
			return null;
		pkFiledName = pkFiledName.toLowerCase();
		if (pkFiledName.equals(colName)) {
			return BeanHelper.getMethod(bean, "primarykey");
		}
		return null;

	}
	

	/**
	 * @param c
	 * @return
	 * @throws SQLException
	 */
	private Object newInstance(Class c) throws SQLException {
		try {
			return c.newInstance();

		} catch (InstantiationException e) {
			throw new SQLException("Cannot create " + c.getName() + ": "
					+ e.getMessage());

		} catch (IllegalAccessException e) {
			throw new SQLException("Cannot create " + c.getName() + ": "
					+ e.getMessage());
		}
	}
	
	private Object getColumnValue(int type, ResultSet resultSet, int i) throws SQLException {
		Object value;
		switch (type) {
			case Types.VARCHAR:
			case Types.CHAR:
				value = resultSet.getString(i);
				break;
			// case Types.INTEGER:
			// case Types.DECIMAL:
			// value = new Integer(resultSet.getInt(i));
			// break;
			case Types.BLOB:
			case Types.LONGVARBINARY:
			case Types.VARBINARY:
			case Types.BINARY:
				value = InOutUtil.deserialize(resultSet.getBytes(i));
				break;
			case Types.CLOB:
				value = getClob(resultSet, i);
				break;
			default:
				value = resultSet.getObject(i);
				break;
			}
		return value;
	}

	
	/**
	 * @param rs
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	private String getClob(ResultSet rs, int columnIndex)
			throws SQLException {
		Reader rsReader = rs.getCharacterStream(columnIndex);
		if (rsReader == null)
			return null;
		BufferedReader reader = new BufferedReader(rsReader);
		// Reader reader = rs.getCharacterStream(columnIndex);
		StringBuffer buffer = new StringBuffer();
		try {
			while (true) {
				String c = reader.readLine();
				if (c == null) {
					break;
				}
				buffer.append(c);
			}
			reader.close();
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return buffer.toString();
	}
}
