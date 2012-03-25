package nc.uap.lfw.file.helper;

/**
 * 将文件名及下载地址写入指定数据集的选中行
 * @author licza
 *
 */
public class WriteToSelectRow extends Scene{
	/**
	 * 将文件名及下载地址写入指定数据集的选中行
	 * @param widget 要写入的数据集
	 * @param dataset 要写入的dataset
	 * @param filename 要写入的文件名字段名
	 * @param fileurl 要写入的下载地址字段名
	 */
	public WriteToSelectRow(String widget ,String dataset,String filename,String fileurl) {
		arg.put(WIDGET, widget);
		arg.put(DATASET, dataset);
		arg.put(FILENAME, filename);
		arg.put(FILEURL, fileurl); 
	}

}
