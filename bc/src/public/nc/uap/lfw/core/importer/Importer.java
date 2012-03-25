package nc.uap.lfw.core.importer;
/**
 * “¿¿µ“˝»Î√Ë ˆ
 *
 */
public final class Importer {
	public static final String DEFAULT_CTX = "lfw";
	private String ctx = DEFAULT_CTX;
	private String fullname;
	public Importer(String fullname) {
		setFullname(fullname);
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getCtx() {
		return ctx;
	}
	public void setCtx(String ctx) {
		this.ctx = ctx;
	}
	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	@Override
	public String toString() {
		return ctx + "/" + fullname;
	}
}
