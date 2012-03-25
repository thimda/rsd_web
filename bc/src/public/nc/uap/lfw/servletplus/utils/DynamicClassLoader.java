package nc.uap.lfw.servletplus.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * ��̬�������
 * 
 * @author licza.
 * 
 */
public class DynamicClassLoader extends ClassLoader {

	private InputStream classFile = null;

	private String name = null;

	/**
	 * ��̬������
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
		// �� URL ָ������Դ
		URLConnection con = url.openConnection();
		InputStream classIs = con.getInputStream();
		this.classFile = classIs;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		// ��ȡ�ļ���
		for (int i = 0; (i = classIs.read(buf)) != -1;) {
			baos.write(buf, 0, i);
		}
		classIs.close();
		baos.close();
		// �����µ������
		byte[] data = baos.toByteArray();
		defineClass(name, data, 0, data.length);
	}

	/**
	 * 
	 * ���� getResourceAsStream() ��Ϊ�˷��ظ�����ļ�����
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
