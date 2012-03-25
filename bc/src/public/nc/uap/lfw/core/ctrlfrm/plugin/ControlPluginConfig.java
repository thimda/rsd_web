package nc.uap.lfw.core.ctrlfrm.plugin;

import java.io.Serializable;
/**
 * øÿº˛≈‰÷√
 * @author dengjt
 *
 */
public class ControlPluginConfig implements Serializable {
	private static final long serialVersionUID = 5477147547769253597L;
	public static final String ZONE_VIEW = "view";
	public static final String ZONE_WINDOW = "window";
	public static final String CF_CONTROL = "control";
	public static final String CF_CONTAINER = "container";
	public static final String CF_BASIC = "basic";
	public static final String CF_MODEL = "model";
	private String id;
	private String ctx;
	private String dependences;
	private String fullname;
	private String cssfullname;
	private String javaclazz;
	private String uijavaclazz;
	private String serializer;
	private String uiserializer;
	private String uirender;
	private String zone;
	private String compress;
	private String[] imports;
	private String[] cssImports;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDependences() {
		return dependences;
	}
	public void setDependences(String dependences) {
		this.dependences = dependences;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		if(fullname != null && !fullname.equals("")){
			String[] strs = fullname.split(",");
			this.fullname = strs[0];
			for (int i = 0; i < strs.length; i++) {
				strs[i] = strs[i].replaceAll("\\.", "/");
				strs[i] = strs[i].replaceAll("\\$", ".");
			}
			imports = strs;
		}
	}
	public String getJavaclazz() {
		return javaclazz;
	}
	public void setJavaclazz(String javaclazz) {
		this.javaclazz = javaclazz;
	}
	public String getSerializer() {
		return serializer;
	}
	public void setSerializer(String serializer) {
		this.serializer = serializer;
	}
	public String getUiserializer() {
		return uiserializer;
	}
	public void setUiserializer(String uiserializer) {
		this.uiserializer = uiserializer;
	}
	public String getUirender() {
		return uirender;
	}
	public void setUirender(String uirender) {
		this.uirender = uirender;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getCssfullname() {
		return cssfullname;
	}
	public void setCssfullname(String cssfullname) {
		if(cssfullname != null && !cssfullname.equals("")){
			String[] strs = cssfullname.split(",");
			this.cssfullname = strs[0];
			for (int i = 0; i < strs.length; i++) {
				strs[i] = strs[i].replaceAll("\\.", "/");
				strs[i] = strs[i].replaceAll("\\$", ".");
			}			
			cssImports = strs;
		}
	}
	public String getCtx() {
		return ctx;
	}
	public void setCtx(String ctx) {
		this.ctx = ctx;
	}
	public String getUijavaclazz() {
		return uijavaclazz;
	}
	public void setUijavaclazz(String uijavaclazz) {
		this.uijavaclazz = uijavaclazz;
	}
	public String[] getImports() {
		return imports;
	}
	public String[] getCssImports() {
		return cssImports;
	}
	public String getCompress() {
		return compress;
	}
	public void setCompress(String compress) {
		this.compress = compress;
	}
}
