package nc.uap.lfw.servletplus.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 动态类加载器
 * 
 * @author licza.
 * 
 */
public class DynamicClassLoader extends ClassLoader {

	private InputStream classFile = null;

	private String name = null;

	/**
	 * 动态载入类
	 * 
	 * @param clsLoader
	 * @param name
	 * @param url
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DynamicClassLoader(ClassLoader clsLoader, String name, URL url)
			throws FileNotFoundException, IOException {
		super(clsLoader);
		this.name = name + ".class";
		// 打开 URL 指定的资源
		URLConnection con = url.openConnection();
		InputStream classIs = con.getInputStream();
		this.classFile = classIs;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		// 读取文件流
		for (int i = 0; (i = classIs.read(buf)) != -1;) {
			baos.write(buf, 0, i);
		}
		classIs.close();
		baos.close();
		// 创建新的类对象
		byte[] data = baos.toByteArray();
		defineClass(name, data, 0, data.length);
	}

	/**
	 * 
	 * 重载 getResourceAsStream() 是为了返回该类的文件流。
	 * 
	 * @return an InputStream of the class bytes, or null
	 */
	public InputStream getResourceAsStream(String resourceName) {
		try {
			if (resourceName.equals(name)) {
				return this.classFile;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

}
