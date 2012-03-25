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
 * 获取上下文资源工具类。通过ThreadLocal中的ServletContext进行操作,此类用于屏蔽不同应用服务器及运行态与开发态的差异
 * 
 * @author dengjt
 * @modified 2008-12-03
 *           针对websphere通过context和weblogic的映射目录部署方式，无法得到resource的问题进行修正
 *           ，目前只能采用绝对路径来查找
 */
public class ContextResourceUtil {
	/**
	 * 获取当前应用路径,即一般意义上的ServletContext.getRealPath
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
	 * 得到当前应用指定路径下的所有资源路径
	 * 
	 * @param dirPath
	 * @return
	 */
	@SuppressWarnings("unchecked") public static Set<String> getResourcePaths(String dirPath) {
		if (LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN) || RuntimeEnv.isRunningInWebSphere() || RuntimeEnv.isRunningInWeblogic()) {
			if (LfwLogger.isDebugEnabled())
				LfwLogger.debug("加载目录:" + dirPath);
			String ctxPath = LfwRuntimeEnvironment.getRootPath();
			if (ctxPath == null) {
				ctxPath = getContextPath();
				if (LfwLogger.isDebugEnabled())
					LfwLogger.debug("获取context path:" + ctxPath);
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
	 * 得到一个资源输入流，这个方法可能在初始化时用到，也可能在运行时用到。由于初始化时获取不到ctx，因此仍然从ctx中取。
	 * 对于was做目录映射会出现找不到的问题。 但是目前初始化调用此方法的，只有加载公共资源时，所以可以保证这些资源放在was下，而不是目录映射路径中。
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
	 * 得到当前应用路径
	 * 
	 * @return
	 */
	private static String getContextPath() {
		return LfwRuntimeEnvironment.getRootPath();
	}
	/**
	 * 打开输出流，只限于用ServletContext能找到
	 * 
	 * @param absFilePath
	 * @param fullPath
	 *            true表示从全路径获得文件
	 * @return
	 */
	public static Writer getOutputStream(String filePath) {
		return getOutputStream(filePath, "UTF-8");
	}
	/**
	 * 以特定编码打开输出流
	 * 
	 * @param filePath
	 * @param rootPath
	 * @param charset
	 * @return
	 */
	public static Writer getOutputStream(String filePath, String charset) {
		File f = null;
		String absPath = getCurrentAppPath() + filePath;
		LfwLogger.debug("===ContextResourceUtil类getOutputStream(String filePath, String rootPath)方法:absPath=" + absPath);
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
	 * 判断资源文件是否存在,目前采用绝对路径
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean resourceExist(String filePath) {
		if (absolutePathMode()) {
			String absPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs/" + LfwRuntimeEnvironment.getRootPath() + "/" + filePath;
			File f = new File(absPath);
			if (LfwLogger.isDebugEnabled())
				LfwLogger.debug("===ContextResourceUtil类resourceExist方法:运行于was或weblogic,absPath=" + absPath + ",文件" + (f.exists() ? "存在!" : "不存在!"));
			return f.exists();
		} else {
			try {
				if (LfwLogger.isDebugEnabled())
					LfwLogger.debug("===ContextResourceUtil类resourceExist方法:filePath=" + filePath + ",文件" + (LfwRuntimeEnvironment.getServletContext().getResource(filePath) == null ? "不存在!" : "存在!"));
				// 通过这个方法不能即时找到文件，原因未知。采用绝对路径方式。目前采用的解决方法为，判断绝对路径是否存在，如果绝对路径存在，而资源访问不存在，则一直等待，直到资源路径编译完成
				String absPath = LfwRuntimeEnvironment.getServletContext().getRealPath("/") + filePath;
				File f = new File(absPath);
				boolean absExist = f.exists();
				boolean exist = LfwRuntimeEnvironment.getServletContext().getResource(filePath) != null;
				// 等待
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
	 * 此方法用来获取Lfw下的路径资源
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
	 * 此方法用来获取Lfw下的路径资源
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
	 * 此方法用来判断是否需要使用绝对路径方式来访问资源。一般情况下，websphere，weblogic，nc集群及设计态，
	 * 由于通过ServletContext无法取得对应资源，所以采用绝对路径方式
	 * 
	 * @return
	 */
	private static boolean absolutePathMode() {
		return LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN) || !isSingle() || RuntimeEnv.isRunningInWebSphere() || RuntimeEnv.isRunningInWeblogic();
	}
	/**
	 * 判断是否集群
	 * 
	 * @return
	 */
	private static boolean isSingle() {
		ServerConfiguration sc = ServerConfiguration.getServerConfiguration();
		// 单机或者Master上，进行单点的必要初始化
		return sc.isSingle();
	}
}
