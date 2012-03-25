package nc.uap.lfw.reference.base;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import nc.jdbc.framework.processor.BaseProcessor;

/**
 * 
 * 参照数据类型处理器
 */
public class RefDataProcessor extends BaseProcessor {
	private static final long serialVersionUID = -9014034558712046046L;

		int beginIndex = -1;

		int endIndex = -1;

		public RefDataProcessor(int beginIndex, int endIndex) {
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;

		}

		public Object processResultSet(ResultSet rs) throws SQLException {
			
			RefResult result = new RefResult();
			List<List<Object>> vecs = new ArrayList<List<Object>>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int nColumnCount = rsmd.getColumnCount();
			int count = 0;
			boolean isRollOverResult = !(beginIndex == -1 && endIndex == -1);
			while (rs.next()) {
				// 滚动结果集
				if (isRollOverResult) {
					if(count >= beginIndex && count < endIndex){
						List<Object> record = getARecord(rs, rsmd, nColumnCount);
						vecs.add(record);
					}
					count ++;
//					count++;
//					if (count >= endIndex) {
//						break;
//					}

				}
				else {
					List<Object> record = getARecord(rs, rsmd, nColumnCount);
					vecs.add(record);
					count ++;
				}

			}
			
			result.setTotalCount(count);
			result.setData(vecs);
			return result;
		}

		private List<Object> getARecord(ResultSet rs, ResultSetMetaData rsmd,
				int nColumnCount) throws SQLException {
			List<Object> tmpV = new ArrayList<Object>();
			for (int j = 1; j <= nColumnCount; j++) {
				Object o = null;
				String strObj = null;
				Object value = null;
				if (rsmd.getColumnType(j) == Types.CHAR
						|| rsmd.getColumnType(j) == Types.VARCHAR) {
					o = rs.getString(j);
					if (o != null) {
						strObj = o.toString().trim();
						value = strObj;
					}
				} else {
					o = rs.getObject(j);
					value = o;
				}
				tmpV.add(value);
			}

			return tmpV;

		}
	}