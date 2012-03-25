package nc.uap.lfw.core.refnode;
import nc.uap.lfw.core.exception.LfwPluginException;
import nc.uap.lfw.core.page.config.RefNodeConf;

public class RefNode extends RefNodeConf implements Cloneable{
	private static final long serialVersionUID = 3122374275495533964L;
	public static final String FROM_NC = "NC";
	private String writeDs;
	private String refId;
	private String readFields;
	private String writeFields;
	private String from;
	private String refnodeDelegator;
	public String getRefnodeDelegator() {
		return refnodeDelegator;
	}
	public void setRefnodeDelegator(String refnodeDelegator) {
		this.refnodeDelegator = refnodeDelegator;
	}
	public String getWriteDs() {
		return writeDs;
	}
	public void setWriteDs(String name) {
		this.writeDs = name;
	}

	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	
	/**
	 * 使用全局Pool中的参照节点配置补充当前配置中不足的信息
	 * @param nodeConf
	 */
	public void adjustInfoByRefNodeConf(RefNodeConf nodeConf)
	{
		if(this.getPagemeta() == null)
			this.setPagemeta(nodeConf.getPagemeta());
		if(this.getPath() == null)
			this.setPath(nodeConf.getPath());
		if(this.getI18nName() == null)
			this.setI18nName(nodeConf.getI18nName());
	}
	
	public Object clone()
	{
		return super.clone();
	}
	
	public String getReadFields() {
		return readFields;
	}
	public void setReadFields(String readFields) {
		this.readFields = readFields;
	}
	public String getWriteFields() {
		return writeFields;
	}
	public void setWriteFields(String writeFields) {
		this.writeFields = writeFields;
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	public void validate(){
		StringBuffer buffer = new StringBuffer();
		if(this.getId() == null || this.getId().equals("")){
			buffer.append("参照的ID不能为空!\r\n");
		}
		if(this.getPagemeta() == null || this.getPagemeta().equals("")){
			buffer.append("参照的pagemeta不能为空!\r\n");
		}
		if(buffer.length() > 0)
			throw new  LfwPluginException(buffer.toString());
	}
}

