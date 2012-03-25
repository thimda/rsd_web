package nc.uap.lfw.file.helper;

/**
 * ʹ�õ��������ϴ�
 * 
 * @author licza
 * 
 */
public class UploadWithBillItem extends Scene{
	/**
	 * ʹ�õ��������ϴ�
	 * @param billtype ��������
	 * @param billitem ����pk
	 */
	public UploadWithBillItem(String billtype, String billitem) {
		arg.put(BILLTYPE, billtype);
		arg.put(BILLITEM, billitem);
	}
	/**
	 * ʹ�õ��������ϴ� 
	 * @param billtype ��������
	 * @param billitem ����pk
	 * @param iscover �Ƿ񸲸�
	 */
	public UploadWithBillItem(String billtype, String billitem,boolean iscover) {
		arg.put(BILLTYPE, billtype);
		arg.put(BILLITEM, billitem);
		if(iscover)
			arg.put(ISCOVER, Boolean.TRUE.toString());
	}
}
