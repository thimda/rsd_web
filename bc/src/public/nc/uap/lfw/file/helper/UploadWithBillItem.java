package nc.uap.lfw.file.helper;

/**
 * 使用单据类型上传
 * 
 * @author licza
 * 
 */
public class UploadWithBillItem extends Scene{
	/**
	 * 使用单据类型上传
	 * @param billtype 单据类型
	 * @param billitem 单据pk
	 */
	public UploadWithBillItem(String billtype, String billitem) {
		arg.put(BILLTYPE, billtype);
		arg.put(BILLITEM, billitem);
	}
	/**
	 * 使用单据类型上传 
	 * @param billtype 单据类型
	 * @param billitem 单据pk
	 * @param iscover 是否覆盖
	 */
	public UploadWithBillItem(String billtype, String billitem,boolean iscover) {
		arg.put(BILLTYPE, billtype);
		arg.put(BILLITEM, billitem);
		if(iscover)
			arg.put(ISCOVER, Boolean.TRUE.toString());
	}
}
