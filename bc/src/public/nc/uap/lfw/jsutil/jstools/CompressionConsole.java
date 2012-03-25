package nc.uap.lfw.jsutil.jstools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * javascript优化程序。利用已生成的javascript优化配置文件，进行js优化
 * 
 * @author dengjt
 * 
 */
public class CompressionConsole/* implements IUpdateResource*/ {

	private static final String SPLIT_DELI = ";";
	private static final String CONFFILES = "conffiles";
	private String[] confFiles = null;

	public void doAfterInstall(String ncHome, String version1, String version2) {
		try {
			LfwLogger.info("begin to compress javascript and css");
			initConfFiles(ncHome);
			if (confFiles != null) {

				for (int i = 0; i < confFiles.length; i++) {
					LfwLogger.info("begin to compress group： " + confFiles[i]);
					processOptimize(ncHome, confFiles[i]);
					LfwLogger.info("group " + confFiles[i] + " is successfully compressed");
				}

			}
			LfwLogger.info("all files are successfully compressed!");
		} 
		catch (Exception e) {
			LfwLogger.error(e);
		}
	}

	public void doBeforeInstall(String ncHome, String version1, String version2) {

	}

	private void initConfFiles(String ncHome) throws IOException {
		Properties props = new Properties();
		InputStream inStream = new FileInputStream(ncHome + File.separatorChar
				+ "ierp" + File.separatorChar + "portal" + File.separatorChar
				+ "optimize.properties");
		props.load(inStream);
		String files = props.getProperty(CONFFILES);
		if(files != null)
			confFiles = files.split(SPLIT_DELI);
	}

	private void processOptimize(String ncHome, String filePath) throws IOException {
//		DefaultTreeModel model = JsDataUtil.loadTreeDataFromConf(ncHome + File.separatorChar + filePath);
//		String postFix = null;
//		ITextCompressor compressor = null;
//		//优化应用路径
//		String optimizeAppPath = null;
//		if (((PairObject) ((CheckTreeNode) model.getRoot()).getUserObject())
//				.isScript()) {
//			optimizeAppPath = APPPATH + ((PairObject) ((CheckTreeNode) model.getRoot()).getUserObject()).getPath();
//			compressor = new JsTextCompressor();
//			postFix = "_compressed.js";
//		} else {
//			optimizeAppPath = APPPATH + ((PairObject) ((CheckTreeNode) model.getRoot()).getUserObject()).getPath();
//			compressor = new CssTextCompressor();
//			postFix = "_compressed.css";
//		}
//		Enumeration<CheckTreeNode> em = ((DefaultMutableTreeNode) model
//				.getRoot()).preorderEnumeration();
//		em.nextElement();
//		Writer writer = null;
//		while (em.hasMoreElements()) {
//			CheckTreeNode node = em.nextElement();
//			if (node.getAllowsChildren()) {
//				if (writer != null)
//					writer.close();
//				if (node.isChecked()) {
//					System.out.println("begin to compress group:" + node.toString());
//					writer = new OutputStreamWriter(new FileOutputStream(ncHome + File.separatorChar + optimizeAppPath
//							+ File.separatorChar + "/compression/" + node.getUserObject()
//							+ postFix)/* , "ASCII" */);
//				}
//
//			} else {
//				if (!node.isChecked())
//					continue;
//				System.out.println("begin to compress file:" + node.toString());
//				String content = JsDataUtil.getStrFromFile(ncHome
//						+ File.separatorChar + optimizeAppPath + File.separatorChar + (String) node.getUserObject());
//				String cpContent = compressor.compress(content);
//				writer.write(cpContent);
//				writer.write("\n");
//
//			}
//		}
//		if (writer != null)
//			writer.close();

	}
	
	public static void main(String[] args)
	{
		if(args.length < 1)
			return;
		String ncHome = args[0];
		//String ncHome = "D:/views502/dengjt_NC_UAP_Modules5.02_int/NC5_UAP5.0_VOB/NC_UAP_Modules";
		new CompressionConsole().doAfterInstall(ncHome, null, null);
	}
}
