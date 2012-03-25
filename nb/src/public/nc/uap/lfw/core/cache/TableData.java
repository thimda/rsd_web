package nc.uap.lfw.core.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableData implements Serializable {

	private static final String NULL = "$NULL$";
	private static final long serialVersionUID = -3352826964979714252L;
	private List<Object[]> list;
	public void setData(List<Object[]> list) {
		this.list = list;
	}
	
	public void addData(Object[] objs){
		if(this.list == null)
			this.list = new ArrayList<Object[]>();
		this.list.add(objs);
	}

	public List<Object[]> getList() {
		return list;
	}

	public void setList(List<Object[]> list) {
		this.list = list;
	}

	public Object toResponse() {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object[] datas = list.get(i);
			buf.append("[");
			for (int j = 0; j < datas.length; j++) {
				buf.append("\"");
				if(datas[j] == null)
					datas[j] = NULL;
				buf.append(datas[j])
				   .append("\"");
				if(j != datas.length -1)
					buf.append(",");
			}
			buf.append("]");
			if(i != size -1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}

}
