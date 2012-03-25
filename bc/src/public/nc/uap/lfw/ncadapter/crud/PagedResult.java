package nc.uap.lfw.ncadapter.crud;

import java.io.Serializable;
import java.util.List;

public class PagedResult implements Serializable {
	private static final long serialVersionUID = 6877399676248172194L;
	private List result;
	private int size;
	public List getResult() {
		return result;
	}
	public void setResult(List result) {
		this.result = result;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
