package nc.uap.lfw.file.helper;

/**
 * 上传场景工厂
 * @author licza
 *
 */
public class SceneFactory {
	/**
	 * 将文件名及下载地址写入指定数据集的选中行
	 * @param widget 要写入的数据集
	 * @param dataset 要写入的dataset
	 * @param filename 要写入的文件名字段名
	 * @param fileurl 要写入的下载地址字段名
	 * @return WriteToSelectRow
	 * @deprecated
	 */
	public static Scene useWriteToSelectRowScene(String widget ,String dataset,String filename,String fileurl){
		return new WriteToSelectRow( widget, dataset, filename,fileurl);
	}
	/**
	 * 场景:在上传完成后调用自定义listener
	 * @param listenerClass listener类 ,nc.uap.lfw.file.listener.UploadCompleteServerListener的实现
	 * @param parentwidget 要提交的父页面widget
	 * @param parentdataset 要提交的父页面数据集
	 * @return UseCompletListenerScene
	 */
	public static Scene useCompletListenerScene(String listenerClass,String parentwidget,String parentdataset){
		return new UseCompletListenerScene( listenerClass, parentwidget, parentdataset);
	}
	/**
	 * 
	 * 调用前台脚本方法
	 * 
	 * @param method 方法名称
	 * @return UseCallBackMehtod
	 */
	public static Scene useCallBackMethodScene(String method) {
		return new UseCallBackMehtod(method);
	}
	/**
	 * 
	 * 使用单据类型上传
	 * @param billtype 单据类型
	 * @param billitem 单据pk
	 * @return
	 */
	public static Scene useBillItemScene(String billtype, String billitem) {
		return new UploadWithBillItem(billtype, billitem);
	}
	/**
	 * 使用单据类型上传 
	 * @param billtype 单据类型
	 * @param billitem 单据pk
	 * @param override 是否覆盖
	 * @return
	 */
	public static Scene useBillItemScene(String billtype, String billitem, boolean override) {
		return new UploadWithBillItem(billtype, billitem, override);
	}
}
