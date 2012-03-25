package nc.uap.lfw.core.cache;

import java.io.Serializable;

public class TableMeta implements Serializable {
	private static final long serialVersionUID = -780972173383704511L;
	private String[] metas;
	public String[] getMetas() {
		return metas;
	}
	public void setMetas(String[] metas) {
		this.metas = metas;
	}
	public Object toResponse() {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < metas.length; i++) {
			buf.append("\"")
			   .append(metas[i])
			   .append("\"");
			if(i != metas.length - 1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}
}
