package nc.uap.lfw.file.helper;

/**
 * 场景:在上传完成后调用自定义listener
 * @author licza
 *
 */
public class UseCompletListenerScene extends Scene {
	
	/**
	 * 场景:在上传完成后调用自定义listener
	 * @param listenerClass listener类
	 * @param parentwidget 要提交的父页面widget
	 * @param parentdataset 要提交的父页面数据集
	 */
	public UseCompletListenerScene(String listenerClass,String parentwidget,String parentdataset){
		arg.put(UPLOADLISTENER, listenerClass);
		arg.put(PARENTWIDGET, parentwidget);
		arg.put(PARENTDATASET, parentdataset);
	}
}
