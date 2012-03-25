package nc.uap.lfw.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractPresentPlugin implements PresentPlugin {

	

	public static Map<String, Properties> nodeProperties = new HashMap<String, Properties>();
	
	/**
	 * 多语资源的信息
	 */
	public static Map<String, Properties> langResourcesProperties = new HashMap<String, Properties>();
	
	// 页面对应的Uimeta路径
	protected static final Map<String, String> rote = new ConcurrentHashMap<String, String>();
	protected static final Map<String, String> umnameMap = new ConcurrentHashMap<String, String>();
	protected static final Map<String, Boolean> roteType = new ConcurrentHashMap<String, Boolean>();
	// 对应的Model
	protected static final Map<String, String> roteModel = new ConcurrentHashMap<String, String>();

	public static final String WEBTEMP = "webtemp";
	protected static final String MODEL_PROP = "/node.properties";
	protected static final String MODEL = "model";
	protected String targetJsp;
	
	protected String cacheKey;

	public String getTargetJsp() {
		return targetJsp;
	}

	protected String model;

	/**
	 * 得到配置中的PageModel
	 * 
	 * @return
	 */
	public String getModel() {
		return model;
	}

	protected void fetchPublicFiles(String pagePath, String tempPath) throws IOException {
		String basePath = "html/nodes/" + pagePath;
		InputStream input = null;
		try {
			ClassLoader loader = getClass().getClassLoader();
			input = loader.getResourceAsStream("html/nodes/" + pagePath + "/loader_config.properties");
			if (input != null) {
				Properties props = new Properties();
				props.load(input);
				copyTmpFiles(loader, basePath, tempPath, props);
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		} finally {
			if (input != null)
				input.close();
		}
	}

	/**
	 * 根据文件配置
	 * 
	 * @param properties
	 * @return
	 */
	public static Properties loadNodePropertie(String properties) {
		Properties nodeProps = nodeProperties.get(properties);
		if(nodeProps == null){
			InputStream input = null;
			try {
				input = ContextResourceUtil.getResourceAsStream(properties);
				if (input != null) {
					nodeProps = new Properties();
					nodeProps.load(input);
				}
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(input);
			}
			nodeProperties.put(properties, nodeProps);
		}
		return nodeProps;
	}
	
	/**
	 * 加载多语资源信息
	 * @param properties
	 * @return
	 */
	public static Properties loadNodeLangResources(String langResoFilePath) {
//		List<String> nodeLangResources = langResourcesProperties.get(langResoFilePath);
//		if(nodeLangResources == null){
//			File srcFile = new File(langResoFilePath);
//			if(srcFile.exists()){
//				try {
//					nodeLangResources = FileUtils.readLines(srcFile, "UTF-8");
//				} catch (IOException e) {
//					LfwLogger.error(e.getMessage(), e);
//					throw new LfwRuntimeException(e.getMessage());
//				}
//			}
//			langResourcesProperties.put(langResoFilePath, nodeLangResources);
//		}
// 		return nodeLangResources;
// 		
 		Properties nodeLangResources = langResourcesProperties.get(langResoFilePath);
		if(nodeLangResources == null){
			InputStream input = null;
			try {
				input = ContextResourceUtil.getResourceAsStream(langResoFilePath);
				if (input != null) {
					nodeLangResources = new Properties();
					nodeLangResources.load(input);
				}
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(input);
			}
			langResourcesProperties.put(langResoFilePath, nodeLangResources);
		}
		return nodeLangResources;
	}
	

	protected String getWebTempDir() {
		return ContextResourceUtil.getCurrentAppPath() + "/" + WEBTEMP;
	}

	protected void copyTmpFiles(ClassLoader loader, String basePath, String tmpDir, Properties props) {
		File f = new File(tmpDir + "/" + basePath);
		if (!f.exists())
			f.mkdirs();
		for (int i = 1; i < 100; i++) {
			String path = (String) props.get(i + "");
			if (path != null) {
				InputStream input = null;
				OutputStream fout = null;
				try {
					input = loader.getResourceAsStream(basePath + "/" + path);
					if (input != null) {
						String filePath = tmpDir + "/" + basePath + "/" + path;
						String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
						File dir = new File(dirPath);
						if (!dir.exists())
							dir.mkdirs();
						fout = new FileOutputStream(filePath);
						byte[] bytes = new byte[1024 * 4];
						int count = input.read(bytes);
						while (count != -1) {
							fout.write(bytes, 0, count);
							count = input.read(bytes);
						}
					}
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(), e);
					// throw new LfwRuntimeException("拷贝文件时发生错误," +
					// e.getMessage() + "," + basePath);
				} finally {
					if (input != null)
						try {
							input.close();
						} catch (IOException e1) {
							LfwLogger.error(e1.getMessage(), e1);
						}
					if (fout != null)
						try {
							fout.close();
						} catch (IOException e) {
							LfwLogger.error(e.getMessage(), e);
						}
				}
			} else
				break;
		}

	}

	/**
	 * 检查资源是否存在
	 * 
	 * @param um
	 * @param themeID
	 * @param lng
	 * @param templateJsp
	 * @param isDesign
	 * @return
	 * @throws IOException
	 */
	public boolean resCheck(String um, String themeID, String lng, String templateJsp, Boolean isDesign) throws IOException {
		if(WebConstant.DEFAULT_WINDOW_ID.equals(um))
			return true;
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		cacheKey = rootPath + um + themeID + lng + templateJsp;
		if (isDesign || !rote.containsKey(cacheKey)) {
			if(lookup(um, themeID, lng, templateJsp))
				return true;
			
		}
		else {
			targetJsp = rote.get(cacheKey);
			model = roteModel.get(cacheKey);
			String umName = umnameMap.get(cacheKey);
			LfwRuntimeEnvironment.getWebContext().getRequest().setAttribute(WebConstant.PERSONAL_PAGE_ID_KEY, umName);
			LfwRuntimeEnvironment.setFromLfw(roteType.containsKey(cacheKey));
			return true;
		}
		return false;
	}

	private boolean lookup(String um, String themeID, String lng,
			String templateJsp) throws IOException {
		String[] ums = getPosbileName(um, themeID, lng);
		for (String umName : ums) {
			if (resLookUp(umName, templateJsp)) {
				rote.put(cacheKey, getTargetJsp());
				if (getModel() != null)
					roteModel.put(cacheKey, getModel());
				umnameMap.put(cacheKey, umName);
				LfwRuntimeEnvironment.getWebContext().getRequest().setAttribute(WebConstant.PERSONAL_PAGE_ID_KEY, umName);
				return true;
			}
		}
		return false;
	}

//	/**
//	 * 生成缓存键
//	 * 
//	 * @param umName
//	 * @param templateJsp
//	 * @return
//	 */
//	public static String getRoteKey(String umName, String templateJsp) {
//		return LfwRuntimeEnvironment.getRootPath() + "_" + umName + "_" + templateJsp;
//	}

	/**
	 * 资源查找类
	 * 
	 * @param pagePath
	 * @param templateJsp
	 * @return
	 * @throws IOException
	 */
	public boolean resLookUp(String pagePath, String templateJsp) throws IOException {
		return false;
		// 运行态
	}

	/**
	 * 得到混合后的PageId列表
	 * 
	 * @param um
	 * @param themeID
	 * @param lng
	 * @return
	 */
	public static String[] getPosbileName(String um, String themeID, String lng) {
		List<String> posbilePath = new ArrayList<String>();
		posbilePath.add(um + "_" + lng + "_" + themeID);
		posbilePath.add(um + "_" + themeID);
		posbilePath.add(um + "_" + lng);
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		if (request != null) {
			String templatePath = request.getParameter("template");
			if (StringUtils.isNotBlank(templatePath)) {
				posbilePath.add(templatePath);
			}
		}
		posbilePath.add(um);
		return posbilePath.toArray(new String[0]);
	}
}
