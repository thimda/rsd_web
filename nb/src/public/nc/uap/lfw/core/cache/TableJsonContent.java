package nc.uap.lfw.core.cache;

import java.io.Serializable;

public class TableJsonContent implements Serializable {
	private static final long serialVersionUID = 5155073536139189667L;
	private TableMeta meta;
	private TableData data;
	private DeleteData delete;
	public TableMeta getMeta() {
		return meta;
	}
	public void setMeta(TableMeta meta) {
		this.meta = meta;
	}
	public TableData getData() {
		return data;
	}
	public void setData(TableData data) {
		this.data = data;
	}
	public DeleteData getDelete() {
		return delete;
	}
	public void setDelete(DeleteData delete) {
		this.delete = delete;
	}
	public Object toResponse() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"meta\":");
		if(meta != null)
			buf.append(meta.toResponse());
		else
			buf.append("[]");
		
		buf.append(",\"data\":");
		if(data != null)
			buf.append(data.toResponse());
		else
			buf.append("[]");
	   buf.append(",\"delete\":");
	   if(delete != null)
		   buf.append(delete.toResponse());
	   else
		   buf.append("[]");
		return buf.toString();
	}
}
