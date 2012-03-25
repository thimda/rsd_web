package nc.uap.lfw.core.servlet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.LfwTheme;
import nc.uap.lfw.core.LfwThemeManager;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.ControlPluginParser;
import nc.uap.lfw.core.ctrlfrm.plugin.ControlPluginConfig;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.manager.PoolObjectManager;
import nc.uap.lfw.core.util.ApplicationNodeUtil;
import nc.uap.lfw.core.util.LfwThemeParser;
import nc.uap.lfw.core.util.PageNodeUtil;
import nc.uap.lfw.core.util.PreBuildUtil;
import nc.uap.lfw.jscompression.itf.IJsCompressionService;
import nc.uap.lfw.util.StringUtil;
/**
 * 此listener在容器启动后回调，确保相应模块正确初始化。每个模块都会调用到,并且在每个server上执行，包括master
 * 
 * @author dengjt
 */
public class LfwDefaultBizServerListener extends LfwServerListener {
	public LfwDefaultBizServerListener(ServletContext ctx) {
		super(ctx);
	}

	protected void doAfterStarted() {
		try{
			String appPath = ContextResourceUtil.getCurrentAppPath();
			//删除临时缓存目录
			String tempDir = appPath + "/webtemp";
			File f = new File(tempDir);
			if(f.exists() && f.isDirectory()){
				removeDir(f);
			}
			loadPoolObjects(appPath);
			loadThemes(appPath);
			prebuild();
		}
		catch(Exception e){
			LfwLogger.error("初始化context出现异常" + e.getMessage(), e);
		}
	}

	private void loadPoolObjects(String appPath) {
		String nodeDir = appPath + "/html/nodes";
		PageNodeUtil.refresh(nodeDir);
		ApplicationNodeUtil.refresh(appPath + "/html/applications");
		PoolObjectManager.getDatasetsFromPool(LfwRuntimeEnvironment.getRootPath());
		PoolObjectManager.getRefNodes(LfwRuntimeEnvironment.getRootPath());
		//取出公共池里的widget
		PoolObjectManager.getWidgetsFromPool(LfwRuntimeEnvironment.getRootPath());
	}
	

	private void prebuild() throws IOException {
		InputStream input = null;
		try{
			input = getCtx().getResourceAsStream("/WEB-INF/conf/prebuild");
			PreBuildUtil.build(input);
		}
		finally{
			if(input != null)
				input.close();
		}
	}

	private void removeDir(File dir){
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if(files[i].isFile())
				files[i].delete();
			else
				removeDir(files[i]);
		}
		
		dir.delete();
	}
	
	private void loadThemes(String appPath) {
		String themePath = appPath + "/frame/device_pc/themes";
		LfwLogger.debug("开始从" + themePath + "目录下加载主题");
		File dir = new File(themePath);
		if(dir.exists()){
			File[] fs = dir.listFiles();
			if(fs != null && fs.length > 0){
				for (int i = 0; i < fs.length; i++) {
					tryToLoadTheme(fs[i]);
				}
			}
		}
	}

	private void tryToLoadTheme(File dir) {
		File[] fs = dir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.equals("theme.xml");
			}
		});
		if(fs != null && fs.length == 1){
			LfwLogger.debug("发现主题,在目录:" + fs[0].getAbsolutePath());
			try{
				LfwTheme theme = LfwThemeParser.parse(fs[0]);
				theme.setAbsPath(dir.getAbsolutePath());
				String ctx = LfwRuntimeEnvironment.getRootPath();
				theme.setCtxPath(ctx);
				if(ctx.equals(LfwRuntimeEnvironment.getLfwCtx())){
					Logger.debug("加载公共主题:" + theme.getName());
					LfwThemeManager.registerTheme(theme);
				}
				else{
					LfwLogger.debug("加载私有主题:" + theme.getName());
					LfwThemeManager.registerModelTheme(LfwRuntimeEnvironment.getRootPath(), theme);
				}
			}
			catch(Throwable e){
				LfwLogger.error("解析主题时发生异常，路径:" + fs[0].getAbsolutePath(), e);
			}
		}
	}
}
