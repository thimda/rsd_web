package nc.uap.lfw.jscompression.itf;


/**
 * Js����ѹ��������
 * @author gd 2010-4-23
 * @version NC6.0
 */
public interface IJsCompressionService {
//	/**
//	 * ѹ��JS�ļ�
//	 */
//	public String compressJsFiles();
//	
//	/**
//	 * ѹ��css�ļ�
//	 * @return
//	 */
//	public Map<LfwTheme, String> compressCssFiles();
	
	public String compressJs(String[] jsFiles);
	public String compressCss(String[] cssFiles);
}
