package nc.uap.lfw.file.helper;

/**
 * ���ļ��������ص�ַд��ָ�����ݼ���ѡ����
 * @author licza
 *
 */
public class WriteToSelectRow extends Scene{
	/**
	 * ���ļ��������ص�ַд��ָ�����ݼ���ѡ����
	 * @param widget Ҫд������ݼ�
	 * @param dataset Ҫд���dataset
	 * @param filename Ҫд����ļ����ֶ���
	 * @param fileurl Ҫд������ص�ַ�ֶ���
	 */
	public WriteToSelectRow(String widget ,String dataset,String filename,String fileurl) {
		arg.put(WIDGET, widget);
		arg.put(DATASET, dataset);
		arg.put(FILENAME, filename);
		arg.put(FILEURL, fileurl); 
	}

}
