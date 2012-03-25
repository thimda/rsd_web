package nc.uap.lfw.jsutil.jstools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.jsutil.jstools.vo.CompGroup;
import nc.uap.lfw.jsutil.jstools.vo.CompItem;
import nc.uap.lfw.jsutil.jstools.vo.ConfObject;
import nc.uap.lfw.jsutil.jstools.vo.PairObject;

import com.thoughtworks.xstream.XStream;

public class JsDataUtil {
	public static DefaultTreeModel loadTreeDataFromConf(Reader reader) {
		XStream x = new XStream();
		ConfObject conf = (ConfObject) x.fromXML(reader);
		DefaultMutableTreeNode root = null;
		PairObject obj = new PairObject();
		if(!conf.isScript())
		{
			obj.setScript(false);
			obj.setPath(conf.getPath());
			root = new DefaultMutableTreeNode(obj);
//			root.setShowText("待优化CSS组件");
		}
		else
		{
			obj.setScript(true);
			obj.setPath(conf.getPath());
			root = new DefaultMutableTreeNode(obj);
//			root.setShowText("待优化Script组件");
		}
		Iterator<CompGroup> it = conf.getGroupList().iterator();
		while (it.hasNext()) {
			CompGroup group = it.next();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(group.getPath());
			//node.setShowText(group.getName());
			//node.setChecked(group.isChecked());
			root.add(node);
			if (group.getItemList() != null) {
				Iterator<CompItem> it2 = group.getItemList().iterator();
				while (it2.hasNext()) {
					CompItem item = it2.next();
					DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(item.getPath());
					//itemNode.setShowText(item.getName());
					itemNode.setAllowsChildren(false);
					//itemNode.setChecked(item.isChecked());
					node.add(itemNode);
				}
			}
		}
		return new DefaultTreeModel(root);
	}

	public static DefaultTreeModel loadTreeDataFromDir(String path, String[] dirs, final boolean isScript) {
		if (dirs == null)
			return null;
		PairObject obj = new PairObject();
		obj.setPath(path);
		obj.setScript(isScript);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(obj);
//		if(isScript)
//			root.setShowText("待优化SCRIPT组件");
//		else
//			root.setShowText("待优化CSS组件");
		DefaultTreeModel model = new DefaultTreeModel(root);
		FilenameFilter filter = new FilenameFilter() {
			private boolean isJs(String name) {
				if (name.endsWith(".js") && !name.endsWith("_compressed.js"))
					return true;
				return false;
			}

			private boolean isCss(String name) {
				if (name.endsWith(".css") && !name.endsWith("_compressed.css"))
					return true;
				return false;
			}
			public boolean accept(File dir, String name) {
				if(isScript)
					return isJs(name);
				else
					return isCss(name);
			}
		};
		for (int i = 0; i < dirs.length; i++) {
			File f = new File(path + File.separatorChar + dirs[i]);
			if (!f.exists()) {
				LfwLogger.warn("没有找到目录:" + dirs[i] + ",此目录被忽略");
				continue;
			} else if (!f.isDirectory()) {
				LfwLogger.warn("指定的路径:" + dirs[i] + "不是目录,此文件被忽略");
				continue;
			}
			DefaultMutableTreeNode checkNode = new DefaultMutableTreeNode(dirs[i]);
//			checkNode.setShowText(f.getName());
			root.add(checkNode);
			File[] files = f.listFiles(filter);
			if (files != null) {
				for (int j = 0; j < files.length; j++) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(files[j]
							.getAbsolutePath().substring(path.length() + 1));
					//node.setShowText(files[j].getName());
					node.setAllowsChildren(false);
					checkNode.add(node);
				}
			}
		}
		return model;
	}

	public static void storeTreeData(File file, DefaultTreeModel model, String path) {
		Enumeration<DefaultMutableTreeNode> nodeEnum = ((DefaultMutableTreeNode) model.getRoot())
				.preorderEnumeration();
		ConfObject conf = new ConfObject();
		conf.setPath(path);
		// ignore root
		DefaultMutableTreeNode root = nodeEnum.nextElement();
		PairObject obj = (PairObject) root.getUserObject();
		conf.setScript(obj.isScript());
		conf.setPath(obj.getPath());
		CompGroup group = null;
		while (nodeEnum.hasMoreElements()) {
			DefaultMutableTreeNode node = nodeEnum.nextElement();
	
			if (node.getAllowsChildren()) {
				group = new CompGroup();
				group.setName(node.toString());
				group.setPath((String) node.getUserObject());
				//group.setChecked(node.isChecked());
				conf.addCompGroup(group);
			} else {
				CompItem item = new CompItem();
				item.setName(node.toString());
				item.setPath((String) node.getUserObject());
				//item.setChecked(node.isChecked());
				group.addItem(item);
			}

		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			new XStream().toXML(conf, writer);
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
	}

	public static String getStrFromFile(InputStream input) {
		try {
			try {
				return getStrFromFile(input, "UTF-8");
			} 
			catch (IOException e) {
				return getStrFromFile(input, null);
			}
		} 
		catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("读取文件发生异常");
		}
	}
	
	private static String getStrFromFile(InputStream input, String charSet) throws IOException{
		BufferedReader reader = null;
		if(charSet != null)
			reader = new BufferedReader(new InputStreamReader(input, charSet));
		else
			reader = new BufferedReader(new InputStreamReader(input));
		StringBuffer buf = new StringBuffer();
		String str;
		while ((str = reader.readLine()) != null) {
			buf.append(str);
			buf.append("\n");
		}
		return buf.toString();
	}
}
