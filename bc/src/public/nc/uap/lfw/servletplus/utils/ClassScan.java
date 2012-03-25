package nc.uap.lfw.servletplus.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;

/**
 * ɨ����µ�java��
 * 
 * @author licza 
 * 
 */
public class ClassScan {
	
	/** �ļ�ϵͳ */
	public static final String URL_PROTOCOL_FILE = "file";

	/** jar */
	public static final String URL_PROTOCOL_JAR = "jar";
	/** WAS JarЭ�� */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	/** WLS JarЭ�� */
	public static final String URL_PROTOCOL_ZIP = "zip";
 


	/**
	 * ���ݱ�����ȡ�������е���
	 * 
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClasses(String packageName) {
		// ��һ��class��ļ���
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		//was�¼�һ��/
		if(RuntimeEnv.isRunningInWebSphere()){
			if(!packageDirName.endsWith("/")){
				packageDirName = packageDirName + "/";
			}
		}
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			LfwLogger.debug(LfwRuntimeEnvironment.getCorePath() + "=== " +packageDirName + "hasElements" +dirs.hasMoreElements());
			// ѭ��������ȥ
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				// �õ�Э�������
				String protocol = url.getProtocol();
				
				/**
				 * Class�ļ�
				 */
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath,recursive, classes);
				}else{
					/**
					 * Jar�е�Class
					 */
					Enumeration<JarEntry> entries = null;
					JarFile  jar = null;
					boolean isNewJar = false;
					try {
						LfwLogger.debug("====ClassScan==== protocol:"+ protocol +" Jar URL : " + url.getFile());
						/**
						 * ������weblogic��was���޷���ȡjar����bug
						 */
						if(URL_PROTOCOL_JAR.equals(protocol) ||URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol)){
							String filepath = url.getFile();
							int idx = filepath.indexOf("!");
							/**
							 * ȥ������Ϣ  ����Weblogic�ᱨFileNotFoundException
							 */
							if(idx > 0)
								filepath = filepath.substring(0,filepath.indexOf("!"));
							LfwLogger.debug("====ClassScan==== new Jar URL : " + filepath);
							jar = getJarFile(filepath);
							isNewJar = true;
						}

						if(jar != null){
							entries = jar.entries();
							
							// ---Modify by licza end---
							if(entries != null){
								while (entries.hasMoreElements()) {
									// ��ȡjar���һ��ʵ�� ������Ŀ¼ ��һЩjar����������ļ� ��META-INF���ļ�
									JarEntry entry = entries.nextElement();
									String name = entry.getName();
									// �������/��ͷ��
									if (name.charAt(0) == '/') {
										// ��ȡ������ַ���
										name = name.substring(1);
									}
									// ���ǰ�벿�ֺͶ���İ�����ͬ
									if (name.startsWith(packageDirName)) {
										LfwLogger.debug("====ClassScan==== ǰ�벿�ֺͶ���İ�����ͬ�İ� : "+ (packageDirName));
										int idx = name.lastIndexOf('/');
										// �����"/"��β ��һ����
										if (idx != -1) {
											// ��ȡ���� ��"/"�滻��"."
											packageName = name.substring(0, idx).replace('/', '.');
										}
										// ������Ե�����ȥ ������һ����
										if ((idx != -1) || recursive) {
											// �����һ��.class�ļ� ���Ҳ���Ŀ¼
											if (name.endsWith(".class")&& !entry.isDirectory()) {
												// ȥ�������".class" ��ȡ����������
												String className = name.substring(packageName.length() + 1, name.length() - 6);
												try {
													// ��ӵ�classes
													LfwLogger.info("====ClassScan====" + packageName + '.'+ className);
													classes.add(Class.forName(packageName + '.'+ className, true, Thread.currentThread().getContextClassLoader()));
													LfwLogger.info("====ClassScan====" + packageName + '.'+ className);
												} catch (Exception e) {
													LfwLogger.error("====ClassScan====" + e.getMessage(), e);
												}
											}
										}
									}
								}
							}
						}
					} catch (IOException e) {
						LfwLogger.error(e.getMessage(), e);
					}finally{
						if(isNewJar){
							jar.close();
						}
					}
				}
			}
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return classes;
	}

	/**
	 * 
	 * 
	 * ���ļ�����ʽ����ȡ���µ�����Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<Class<?>> classes) {
		// ��ȡ�˰���Ŀ¼ ����һ��File
		File dir = new File(packagePath);
		// ��������ڻ��� Ҳ����Ŀ¼��ֱ�ӷ���
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// ������� �ͻ�ȡ���µ������ļ� ����Ŀ¼
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// �Զ�����˹��� �������ѭ��(������Ŀ¼) ��������.class��β���ļ�(����õ�java���ļ�)
			public boolean accept(File file) {

				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});

		for (File file : dirfiles) {
			// �����Ŀ¼ �����ɨ��
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "."
						+ file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					classes.add(Class.forName(packageName + '.' + className, true, Thread.currentThread().getContextClassLoader()));
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * ��̬�����
	 * 
	 * @param cls
	 * @return
	 */
	public static Class<?> dynamicClass(Class<?> cls) {
		if(RuntimeEnv.isRunningInWebSphere() || RuntimeEnv.isRunningInWeblogic()){
			return cls;
		}
		String clsName = cls.getCanonicalName();
		ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
		URL ctxURL = ctxLoader.getResource(clsName.replace(".", "/") + ".class");
		String clazzFile = ctxURL.getFile();
		// Jar���� ���ٶ�̬���� ֱ�ӷ��ء�
		if (clazzFile != null && clazzFile.indexOf("jar!") != -1) {
			return cls;
		}
		/**
		 * �ж��Ƿ��ڵ�ǰ��classloader��
		 */
		if(new File(clazzFile).exists()){
			try {
				ClassLoader cl = new DynamicClassLoader(ctxLoader, clsName,ctxURL);
				return Class.forName(clsName, false, cl);
			} catch (Exception e) {
				LfwLogger.error("loader class error!");
			}
		}
		/**
		 * ����classloader
		 */
		String clsUrl = null;
		URL url;
		try {
			Enumeration<URL> dirs = ctxLoader.getResources(cls.getPackage().getName().replaceAll("\\.", "/"));
			while (dirs.hasMoreElements()) {
				URL urla = dirs.nextElement();
				String classFile = urla.getPath() + "/" + cls.getSimpleName()	+ ".class";
				clsUrl = "file://" + classFile;
				if(new File(classFile).exists()){
					try {
						url = new URL(clsUrl);
						ClassLoader cl = new DynamicClassLoader(ctxLoader, clsName,url);
						return Class.forName(clsName, false, cl);
					} catch (Exception e2) {
						LfwLogger.debug(e2.getMessage());
					}
				}
			}
		} catch (Exception e2) {
			LfwLogger.debug(e2.getMessage());
			
		}
		return cls;
	}
	
	public static final String FILE_URL_PREFIX = "file:";
	
	public static JarFile getJarFile(String jarFileUrl) throws IOException {
		JarFile ufj = null;
		try {
			LfwLogger.debug("====ClassScan==== getJarFile:"+jarFileUrl);
			if(jarFileUrl.startsWith(FILE_URL_PREFIX)){
				jarFileUrl = jarFileUrl.substring(FILE_URL_PREFIX.length());
				LfwLogger.debug("====ClassScan==== trueJarFile:"+jarFileUrl);
			}
			ufj = new JarFile(new File(jarFileUrl));
			LfwLogger.debug("====ClassScan==== JarFile:"+ jarFileUrl +" is null:" + (ufj==null));
			return ufj;
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			return null;
		}
	}
 	/**
	 * ���JarEntry
	 * @param filepath Jar·��
	 * @return
	 */
	public static Enumeration<JarEntry>  getEntriesFromJar(String filepath){
		JarFile ufj = null;
		try {
			ufj = new JarFile(new File(filepath));
			return ufj.entries();
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			return null;
		} 
	}

}
