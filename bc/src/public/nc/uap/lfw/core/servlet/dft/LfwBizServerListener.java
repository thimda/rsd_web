package nc.uap.lfw.core.servlet.dft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.servlet.ServletContext;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.LfwServerConfig;
import nc.uap.lfw.core.LfwTheme;
import nc.uap.lfw.core.LfwThemeManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.ControlPluginParser;
import nc.uap.lfw.core.ctrlfrm.plugin.ControlPluginConfig;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.LfwServerListener;
import nc.uap.lfw.jscompression.itf.IJsCompressionService;
import nc.uap.lfw.jsutil.jstools.JsDataUtil;

import nc.uap.lfw.util.StringUtil;

/**
 * lfw启动时执行。仅lfw模块本身使用
 * @author dengjt
 *
 */
public final class LfwBizServerListener extends LfwServerListener {
	public LfwBizServerListener(ServletContext ctx) {
		super(ctx);
	}
	
	protected void doAfterStarted() {
		try{
			ServletContext ctx = getCtx();
			LfwRuntimeEnvironment.setLfwServletContext(ctx);
			LfwServerConfig serverConf = new LfwServerConfig(ctx);
			LfwRuntimeEnvironment.setServerConfig(serverConf);
			
			String appPath = ContextResourceUtil.getCurrentAppPath();
			//控件扫描
			scanControlPlugin((String) getCtx().getAttribute(WebConstant.ROOT_PATH), appPath);
			
			//控件预压缩
			preCompress();
//			JsCodeUtil.refresh();
		}
		catch(Exception e){
			LfwLogger.error("初始化context出现异常" + e.getMessage(), e);
		}
	}
	
	/**
	 * 控件预压缩
	 */
	private void preCompress() {
		IControlPlugin[] plugins = ControlFramework.getInstance().getAllControlPlugins();
		String basePath = "/frame/device_pc/";
		//压缩个体控件
		for (int i = 0; i < plugins.length; i++) {
			IControlPlugin plugin = plugins[i];
			try{
				String[] imps = plugin.getImports(false);
				if(imps != null && imps.length >= 0){
					String result = compressJsFile(basePath + "script/", imps);
					LfwLogger.debug("预压缩控件,ID:" + plugin.getId());
					addCompressedJs(plugin.getId(), result);
				}
				
				String[] cssImps = plugin.getCssImports(false);
				if(cssImps != null && cssImps.length >= 0){
					LfwTheme[] themes = LfwThemeManager.getAllThemes();
					for (int j = 0; j < themes.length; j++) {
						LfwTheme theme = themes[j];
						String cssPath = theme.getAbsPath();
						String result = compressCssFile(cssPath, cssImps);
						LfwLogger.debug("预压缩控件CSS,ID:" + plugin.getId());
						addCompressedCss(plugin.getId(), theme.getId(), result);
					}
				}
			}
			catch(Throwable e){
				LfwLogger.error(e);
			}
		}
		//压缩依赖控件依赖体系
		for (int i = 0; i < plugins.length; i++) {
			IControlPlugin plugin = plugins[i];
			String[] dps = plugin.getDependences();
			if(dps != null && dps.length > 0){
				StringBuffer buf = new StringBuffer();
				for (int j = 0; j < dps.length; j++) {
					String js = getCompressedJs(dps[j]);
					if(js != null){
						buf.append(js);
						buf.append("\n");
					}
				}
				String fullId = plugin.getId() + "_" + StringUtil.merge(dps, "_");
				LfwLogger.debug("预压缩控件依赖,ID:" + fullId);
//				ControlFramework.getInstance().addCompressedJs(fullId, buf.toString());
				addCompressedJs(fullId, buf.toString());
			}
			
			String[] cssDps = plugin.getDependences();
			if(cssDps != null && cssDps.length > 0){
				LfwTheme[] themes = LfwThemeManager.getAllThemes();
				for (int k = 0; k < themes.length; k++) {
					LfwTheme theme = themes[k];
					String themeId = theme.getId();
					StringBuffer buf = new StringBuffer();
					for (int j = 0; j < cssDps.length; j++) {
						String css = getCompressedCss(cssDps[j], themeId);
						if(css != null){
							buf.append(css);
							buf.append("\n");
						}
						
					}
					String fullId = plugin.getId() + "_" + StringUtil.merge(cssDps, "_");
					LfwLogger.debug("预压缩控件CSS,ID:" + plugin.getId());
					addCompressedCss(fullId, themeId, buf.toString());
				}
			}
		}
		//压缩预压缩控件组合
	}
	
	private String getCompressedCss(String id, String themeId) {
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String jsdir = appPath  + "/frame/device_pc/themes/" + themeId + "/compressed/";
		String jspath = jsdir + id + ".css";
		File f = new File(jspath);
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(f);
			return JsDataUtil.getStrFromFile(fin);
		} 
		catch (Exception e) {
			LfwLogger.error(e);
			return null;
		}
		finally{
			if(fin != null){
				try {
					fin.close();
				} catch (IOException e) {
					LfwLogger.error(e);
				}
			}
		}
	}
	
	private String getCompressedJs(String id) {
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String jsdir = appPath  + "/frame/device_pc/script/compressed/";
		String jspath = jsdir + id + ".css";
		File f = new File(jspath);
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(f);
			return JsDataUtil.getStrFromFile(fin);
		} 
		catch (Exception e) {
			LfwLogger.error(e);
			return null;
		}
		finally{
			if(fin != null){
				try {
					fin.close();
				} catch (IOException e) {
					LfwLogger.error(e);
				}
			}
		}
	}

	private void addCompressedCss(String id, String themeId, String result) {
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String jsdir = appPath  + "/frame/device_pc/themes/" + themeId + "/compressed/";
		File dir = new File(jsdir);
		if(!dir.exists())
			dir.mkdir();
		String jspath = jsdir + id + ".css";
		File f = new File(jspath);
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(f);
			fout.write(result.getBytes("UTF-8"));
		} 
		catch (Exception e) {
			LfwLogger.error(e);
		}
		finally{
			if(fout != null){
				try {
					fout.close();
				} catch (IOException e) {
					LfwLogger.error(e);
				}
			}
		}
	}

	private void addCompressedJs(String id, String result) {
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String jsdir = appPath  + "/frame/device_pc/script/compressed/";
		File dir = new File(jsdir);
		if(!dir.exists())
			dir.mkdir();
		String jspath = jsdir + id + ".js";
		File f = new File(jspath);
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(f);
			fout.write(result.getBytes("UTF-8"));
		} 
		catch (Exception e) {
			LfwLogger.error(e);
		}
		finally{
			if(fout != null){
				try {
					fout.close();
				} catch (IOException e) {
					LfwLogger.error(e);
				}
			}
		}
	}

	private String compressJsFile(String basePath, String[] files){
		String[] fullPaths = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			fullPaths[i] = basePath + files[i] + ".js";
		}
		IJsCompressionService cs = NCLocator.getInstance().lookup(IJsCompressionService.class);
		return cs.compressJs(fullPaths);
	}
	

	private String compressCssFile(String basePath, String[] files){
		String[] fullPaths = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			fullPaths[i] = basePath + "/" + files[i] + ".css";
		}
		IJsCompressionService cs = NCLocator.getInstance().lookup(IJsCompressionService.class);
		return cs.compressCss(fullPaths);
	}
	
	/**
	 * 进行控件扫描
	 * @param ctxPath
	 * @param appPath
	 */
	private void scanControlPlugin(String ctxPath, String appPath) {
		String configDir = appPath + "/frame/device_pc/config";
		File f = new File(configDir);
		if(f.exists() && f.isDirectory()){
			File[] fs = f.listFiles();
			if(fs != null){
				for (int i = 0; i < fs.length; i++) {
					String name = fs[i].getName();
					if(name.equals(ControlPluginConfig.CF_CONTAINER) || name.equals(ControlPluginConfig.CF_CONTROL) || name.equals(ControlPluginConfig.CF_MODEL) || name.equals(ControlPluginConfig.CF_BASIC)){
						if(fs[i].isDirectory()){
							File[] cfs = fs[i].listFiles(new FilenameFilter(){
								@Override
								public boolean accept(File dir, String name) {
									return name.endsWith(".xml");
								}
							});
							for (int j = 0; j < cfs.length; j++) {
								ControlPluginConfig config = ControlPluginParser.parse(cfs[j]);
								if(config != null){
									config.setCtx(ctxPath);
									ControlFramework.getInstance().addControlPluginConfig(config);
								}
							}
						}
					}
				}
			}
		}
	}
}
