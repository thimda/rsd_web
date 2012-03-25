package nc.uap.lfw.file.helper;

/**
 * ����:���ϴ���ɺ�����Զ���listener
 * @author licza
 *
 */
public class UseCompletListenerScene extends Scene {
	
	/**
	 * ����:���ϴ���ɺ�����Զ���listener
	 * @param listenerClass listener��
	 * @param parentwidget Ҫ�ύ�ĸ�ҳ��widget
	 * @param parentdataset Ҫ�ύ�ĸ�ҳ�����ݼ�
	 */
	public UseCompletListenerScene(String listenerClass,String parentwidget,String parentdataset){
		arg.put(UPLOADLISTENER, listenerClass);
		arg.put(PARENTWIDGET, parentwidget);
		arg.put(PARENTDATASET, parentdataset);
	}
}
