package nc.uap.lfw.core;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.server.ServerConfiguration;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
/**
 * ��ȡ��������Դ�����ࡣͨ��ThreadLocal�е�ServletContext���в���,�����������β�ͬӦ�÷�����������̬�뿪��̬�Ĳ���
 * 
 * @author dengjt
 * @modified 2008-12-03
 *           ���websphereͨ��context��weblogic��ӳ��Ŀ¼����ʽ���޷��õ�resource�������������
 *           ��Ŀǰֻ�ܲ��þ���·��������
 */
public class ContextResourceUtil {
	/**
	 * ��ȡ��ǰӦ��·��,��һ�������ϵ�ServletContext.getRealPath
	 * 
	 * @return
	 */
	public static String getCurrentAppPath() {
		String absPath = null;
		if (RuntimeEnv.isRunningInWebSphere() || RuntimeEnv.isRunningInWeblogic()) {
			String rootPath = LfwRuntimeEnvironment.getRootPath();
			absPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs/" + rootPath + "/";
		} else {
			absPath = LfwRuntimeEnvironment.getServletContext().getRealPath("/");
			absPath = absPath.replaceAll("\\\\", "/");
		}
		return absPath;
	}
	/**
	 * �õ���ǰӦ��ָ��·���µ�������Դ·��
	 * 
	 * @param dirPath
	 * @return
	 */
	@SuppressWarnings("unchecked") public static Set<String> getResourcePaths(String dirPath) {
		if (LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN) || RuntimeEnv.isRunningInWebSphere() || RuntimeEnv.isRunningInWeblogic()) {
			if (LfwLogger.isDebugEnabled())
				LfwLogger.debug("����Ŀ¼:" + dirPath);
			String ctxPath = LfwRuntimeEnvironment.getRootPath();
			if (ctxPath == null) {
				ctxPath = getContextPath();
				if (LfwLogger.isDebugEnabled())
					LfwLogger.debug("��ȡcontext path:" + ctxPath);
			}
			String absPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs" + ctxPath + "/" + dirPath;
			File f = new File(absPath);
			String[] files = f.list();
			Set<String> fileSet = new HashSet<String>();
			if (files != null && files.length != 0) {
				for (int i = 0; i < files.length; i++) {
					fileSet.add(dirPath + "/" + files[i]);
					if (LfwLogger.isDebugEnabled())
						LfwLogger.debug("file path is:" + dirPath + "/" + files[i]);
				}
			}
			if (LfwLogger.isDebugEnabled())
				LfwLogger.debug("context is:" + ctxPath + ",files is:" + files);
			return fileSet;
		} else
			return LfwRuntimeEnvironment.getServletContext().getResourcePaths(dirPath);
	}
	/**
	 * �õ�һ����Դ��������������������ڳ�ʼ��ʱ�õ���Ҳ����������ʱ�õ������ڳ�ʼ��ʱ��ȡ����ctx�������Ȼ��ctx��ȡ��
	 * ����was��Ŀ¼ӳ�������Ҳ��������⡣ ����Ŀǰ��ʼ�����ô˷����ģ�ֻ�м��ع�����Դʱ�����Կ��Ա�֤��Щ��Դ����was�£�������Ŀ¼ӳ��·���С�
	 * 
	 * @param filePath
	 * @return
	 */
	public static InputStream getResourceAsStream(String filePath) {
		if (RuntimeEnv.isRunningInWebSphere() || RuntimeEnv.isRunningInWeblogic()) {
			try {
				String ctxPath = getContextPath();
				String absPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs" + ctxPath + "/" + filePath;
				InputStream input = new FileInputStream(absPath);
				LfwLogger.info("was:get resource == null:false,for file:" + filePath);
				return input;
			} catch (FileNotFoundException e) {
				throw new LfwRuntimeException(e);
			}
		} else {
			InputStream input = null;
			ServletContext servletCtx = LfwRuntimeEnvironment.getServletContext();
			if (servletCtx != null) {
				input = servletCtx.getResourceAsStream(filePath);
			} else {
				try {
					String absPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs" + getContextPath() + "/" + filePath;
					input = new FileInputStream(absPath);
				} catch (FileNotFoundException e) {
					throw new LfwRuntimeException(e);
				}
			}
			return input;
		}
	}
	/**
	 * �õ���ǰӦ��·��
	 * 
	 * @return
	 */
	private static String getContextPath() {
		return LfwRuntimeEnvironment.getRootPath();
	}
	/**
	 * ���������ֻ������ServletContext���ҵ�
	 * 
	 * @param absFilePath
	 * @param fullPath
	 *            true��ʾ��ȫ·������ļ�
	 * @return
	 */
	public static Writer getOutputStream(String filePath) {
		return getOutputStream(filePath, "UTF-8");
	}
	/**
	 * ���ض�����������
	 * 
	 * @param filePath
	 * @param rootPath
	 * @param charset
	 * @return
	 */
	public static Writer getOutputStream(String filePath, String charset) {
		File f = null;
		String absPath = getCurrentAppPath() + filePath;
		LfwLogger.debug("===ContextResourceUtil��getOutputStream(String filePath, String rootPath)����:absPath=" + absPath);
		f = new File(absPath);
		try {
			if (!f.exists())
				f.createNewFile();
			return new OutputStreamWriter(new FileOutputStream(f), charset);
		} catch (FileNotFoundException e) {
			LfwLogger.error(e.getMessage(), e);
			return null;
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			return null;
		}
	}
	/**
	 * �ж���Դ�ļ��Ƿ����,Ŀǰ���þ���·��
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean resourceExist(String filePath) {
		if (absolutePathMode()) {
			String absPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs/" + LfwRuntimeEnvironment.getRootPath() + "/" + filePath;
			File f = new File(absPath);
			if (LfwLogger.isDebugEnabled())
				LfwLogger.debug("===ContextResourceUtil��resourceExist����:������was��weblogic,absPath=" + absPath + ",�ļ�" + (f.exists() ? "����!" : "������!"));
			return f.exists();
		} else {
			try {
				if (LfwLogger.isDebugEnabled())
					LfwLogger.debug("===ContextResourceUtil��resourceExist����:filePath=" + filePath + ",�ļ�" + (LfwRuntimeEnvironment.getServletContext().getResource(filePath) == null ? "������!" : "����!"));
				// ͨ������������ܼ�ʱ�ҵ��ļ���ԭ��δ֪�����þ���·����ʽ��Ŀǰ���õĽ������Ϊ���жϾ���·���Ƿ���ڣ��������·�����ڣ�����Դ���ʲ����ڣ���һֱ�ȴ���ֱ����Դ·���������
				String absPath = LfwRuntimeEnvironment.getServletContext().getRealPath("/") + filePath;
				File f = new File(absPath);
				boolean absExist = f.exists();
				boolean exist = LfwRuntimeEnvironment.getServletContext().getResource(filePath) != null;
				// �ȴ�
				while (absExist != exist) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						LfwLogger.error(e);
					}
					exist = LfwRuntimeEnvironment.getServletContext().getResource(filePath) != null;
				}
				return exist;
			} catch (MalformedURLException e) {
				LfwLogger.error(e.getMessage(), e);
				return false;
			}
		}
	}
	/**
	 * �˷���������ȡLfw�µ�·����Դ
	 * 
	 * @param filePath
	 * @return
	 */
	public static File getFile(String filePath) {
		String path = getAbsPath(filePath);
		return new File(path);
	}
	public static String getAbsPath(String filePath) {
		String path = null;
		if (absolutePathMode()) {
			path = RuntimeEnv.getInstance().getNCHome() + "/hotwebs" + LfwRuntimeEnvironment.getRootPath() + "/" + filePath;
		} else
			path = LfwRuntimeEnvironment.getServletContext().getRealPath("/") + filePath;
		return path;
	}
	/**
	 * �˷���������ȡLfw�µ�·����Դ
	 * 
	 * @param filePath
	 * @return
	 */
	public static File getLfwFile(String filePath) {
		String path = getLfwPath(filePath);
		return new File(path);
	}
	public static String getLfwPath(String filePath) {
		String path = null;
		if (absolutePathMode()) {
			path = RuntimeEnv.getInstance().getNCHome() + "/hotwebs" + LfwRuntimeEnvironment.getLfwCtx() + "/" + filePath;
		} else
			path = LfwRuntimeEnvironment.getLfwServletContext().getRealPath("/") + filePath;
		return path;
	}
	/**
	 * �˷��������ж��Ƿ���Ҫʹ�þ���·����ʽ��������Դ��һ������£�websphere��weblogic��nc��Ⱥ�����̬��
	 * ����ͨ��ServletContext�޷�ȡ�ö�Ӧ��Դ�����Բ��þ���·����ʽ
	 * 
	 * @return
	 */
	private static boolean absolutePathMode() {
		return LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN) || !isSingle() || RuntimeEnv.isRunningInWebSphere() || RuntimeEnv.isRunningInWeblogic();
	}
	/**
	 * �ж��Ƿ�Ⱥ
	 * 
	 * @return
	 */
	private static boolean isSingle() {
		ServerConfiguration sc = ServerConfiguration.getServerConfiguration();
		// ��������Master�ϣ����е���ı�Ҫ��ʼ��
		return sc.isSingle();
	}
}
