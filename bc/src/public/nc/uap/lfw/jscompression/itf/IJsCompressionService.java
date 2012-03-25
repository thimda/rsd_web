package nc.uap.lfw.jscompression.itf;


/**
 * Js代码压缩服务类
 * @author gd 2010-4-23
 * @version NC6.0
 */
public interface IJsCompressionService {
//	/**
//	 * 压缩JS文件
//	 */
//	public String compressJsFiles();
//	
//	/**
//	 * 压缩css文件
//	 * @return
//	 */
//	public Map<LfwTheme, String> compressCssFiles();
	
	public String compressJs(String[] jsFiles);
	public String compressCss(String[] cssFiles);
}
