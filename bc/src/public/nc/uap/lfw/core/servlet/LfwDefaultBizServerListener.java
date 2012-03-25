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
 * ��listener������������ص���ȷ����Ӧģ����ȷ��ʼ����ÿ��ģ�鶼����õ�,������ÿ��server��ִ�У�����master
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
			//ɾ����ʱ����Ŀ¼
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
			LfwLogger.error("��ʼ��context�����쳣" + e.getMessage(), e);
		}
	}

	private void loadPoolObjects(String appPath) {
		String nodeDir = appPath + "/html/nodes";
		PageNodeUtil.refresh(nodeDir);
		ApplicationNodeUtil.refresh(appPath + "/html/applications");
		PoolObjectManager.getDatasetsFromPool(LfwRuntimeEnvironment.getRootPath());
		PoolObjectManager.getRefNodes(LfwRuntimeEnvironment.getRootPath());
		//ȡ�����������widget
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
		LfwLogger.debug("��ʼ��" + themePath + "Ŀ¼�¼�������");
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
			LfwLogger.debug("��������,��Ŀ¼:" + fs[0].getAbsolutePath());
			try{
				LfwTheme theme = LfwThemeParser.parse(fs[0]);
				theme.setAbsPath(dir.getAbsolutePath());
				String ctx = LfwRuntimeEnvironment.getRootPath();
				theme.setCtxPath(ctx);
				if(ctx.equals(LfwRuntimeEnvironment.getLfwCtx())){
					Logger.debug("���ع�������:" + theme.getName());
					LfwThemeManager.registerTheme(theme);
				}
				else{
					LfwLogger.debug("����˽������:" + theme.getName());
					LfwThemeManager.registerModelTheme(LfwRuntimeEnvironment.getRootPath(), theme);
				}
			}
			catch(Throwable e){
				LfwLogger.error("��������ʱ�����쳣��·��:" + fs[0].getAbsolutePath(), e);
			}
		}
	}
}
