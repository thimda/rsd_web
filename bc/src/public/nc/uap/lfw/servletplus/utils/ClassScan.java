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
 * 扫描包下的java类
 * 
 * @author licza 
 * 
 */
public class ClassScan {
	
	/** 文件系统 */
	public static final String URL_PROTOCOL_FILE = "file";

	/** jar */
	public static final String URL_PROTOCOL_JAR = "jar";
	/** WAS Jar协议 */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	/** WLS Jar协议 */
	public static final String URL_PROTOCOL_ZIP = "zip";
 


	/**
	 * 根据报名获取包中所有的类
	 * 
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClasses(String packageName) {
		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		//was下加一个/
		if(RuntimeEnv.isRunningInWebSphere()){
			if(!packageDirName.endsWith("/")){
				packageDirName = packageDirName + "/";
			}
		}
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			LfwLogger.debug(LfwRuntimeEnvironment.getCorePath() + "=== " +packageDirName + "hasElements" +dirs.hasMoreElements());
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				
				/**
				 * Class文件
				 */
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath,recursive, classes);
				}else{
					/**
					 * Jar中的Class
					 */
					Enumeration<JarEntry> entries = null;
					JarFile  jar = null;
					boolean isNewJar = false;
					try {
						LfwLogger.debug("====ClassScan==== protocol:"+ protocol +" Jar URL : " + url.getFile());
						/**
						 * 修正在weblogic及was下无法获取jar包的bug
						 */
						if(URL_PROTOCOL_JAR.equals(protocol) ||URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol)){
							String filepath = url.getFile();
							int idx = filepath.indexOf("!");
							/**
							 * 去掉包信息  否则Weblogic会报FileNotFoundException
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
									// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
									JarEntry entry = entries.nextElement();
									String name = entry.getName();
									// 如果是以/开头的
									if (name.charAt(0) == '/') {
										// 获取后面的字符串
										name = name.substring(1);
									}
									// 如果前半部分和定义的包名相同
									if (name.startsWith(packageDirName)) {
										LfwLogger.debug("====ClassScan==== 前半部分和定义的包名相同的包 : "+ (packageDirName));
										int idx = name.lastIndexOf('/');
										// 如果以"/"结尾 是一个包
										if (idx != -1) {
											// 获取包名 把"/"替换成"."
											packageName = name.substring(0, idx).replace('/', '.');
										}
										// 如果可以迭代下去 并且是一个包
										if ((idx != -1) || recursive) {
											// 如果是一个.class文件 而且不是目录
											if (name.endsWith(".class")&& !entry.isDirectory()) {
												// 去掉后面的".class" 获取真正的类名
												String className = name.substring(packageName.length() + 1, name.length() - 6);
												try {
													// 添加到classes
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
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {

				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});

		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
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
	 * 动态获得类
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
		// Jar包中 不再动态载入 直接返回。
		if (clazzFile != null && clazzFile.indexOf("jar!") != -1) {
			return cls;
		}
		/**
		 * 判断是否在当前的classloader中
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
		 * 遍历classloader
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
	 * 获得JarEntry
	 * @param filepath Jar路径
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
