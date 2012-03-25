package nc.uap.lfw.design.itf;

import java.io.Serializable;

public class TypeNodeVO  implements Serializable{
	private String pk_typenode;
	private String typename;
	public String getPk_typenode() {
		return pk_typenode;
	}
	public void setPk_typenode(String pk_typenode) {
		this.pk_typenode = pk_typenode;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
}