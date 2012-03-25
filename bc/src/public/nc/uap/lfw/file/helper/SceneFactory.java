package nc.uap.lfw.file.helper;

/**
 * �ϴ���������
 * @author licza
 *
 */
public class SceneFactory {
	/**
	 * ���ļ��������ص�ַд��ָ�����ݼ���ѡ����
	 * @param widget Ҫд������ݼ�
	 * @param dataset Ҫд���dataset
	 * @param filename Ҫд����ļ����ֶ���
	 * @param fileurl Ҫд������ص�ַ�ֶ���
	 * @return WriteToSelectRow
	 * @deprecated
	 */
	public static Scene useWriteToSelectRowScene(String widget ,String dataset,String filename,String fileurl){
		return new WriteToSelectRow( widget, dataset, filename,fileurl);
	}
	/**
	 * ����:���ϴ���ɺ�����Զ���listener
	 * @param listenerClass listener�� ,nc.uap.lfw.file.listener.UploadCompleteServerListener��ʵ��
	 * @param parentwidget Ҫ�ύ�ĸ�ҳ��widget
	 * @param parentdataset Ҫ�ύ�ĸ�ҳ�����ݼ�
	 * @return UseCompletListenerScene
	 */
	public static Scene useCompletListenerScene(String listenerClass,String parentwidget,String parentdataset){
		return new UseCompletListenerScene( listenerClass, parentwidget, parentdataset);
	}
	/**
	 * 
	 * ����ǰ̨�ű�����
	 * 
	 * @param method ��������
	 * @return UseCallBackMehtod
	 */
	public static Scene useCallBackMethodScene(String method) {
		return new UseCallBackMehtod(method);
	}
	/**
	 * 
	 * ʹ�õ��������ϴ�
	 * @param billtype ��������
	 * @param billitem ����pk
	 * @return
	 */
	public static Scene useBillItemScene(String billtype, String billitem) {
		return new UploadWithBillItem(billtype, billitem);
	}
	/**
	 * ʹ�õ��������ϴ� 
	 * @param billtype ��������
	 * @param billitem ����pk
	 * @param override �Ƿ񸲸�
	 * @return
	 */
	public static Scene useBillItemScene(String billtype, String billitem, boolean override) {
		return new UploadWithBillItem(billtype, billitem, override);
	}
}
