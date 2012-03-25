package nc.uap.lfw.core.cache;

import java.io.Serializable;

public class DeleteData implements Serializable {
	private static final long serialVersionUID = 7935515797552472383L;
	private String[] delPks;
	public String[] getDelPks() {
		return delPks;
	}
	public void setDelPks(String[] delPks) {
		this.delPks = delPks;
	}
	public Object toResponse() {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		if(delPks != null && delPks.length > 0){
			for (int i = 0; i < delPks.length; i++) {
				buf.append("\"")
				   .append(delPks[i])
				   .append("\"");
				if(i != delPks.length - 1)
					buf.append(",");
			}
		}
		buf.append("]");
		return buf.toString();
	}
}
