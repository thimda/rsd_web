package nc.uap.lfw.design.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * ��������Ĺ�����
 * @author gd 2010-1-7
 * @version NC6.0
 * @since NC6.0
 */
public class ContextResourceUtilForDesign 
{
	/**
	 * 
	 * @param dirPath ����Ŀ¼��ȫ·��
	 * @return
	 */
	public static Set<String> getResourcePaths(String dirPath)
	{
		File f = new File(dirPath);
		String[] files = f.list();
		Set<String> fileSet = new HashSet<String>();
		if(files != null && files.length != 0){
			for(int i = 0; i < files.length; i ++){
				fileSet.add(dirPath + "/" + files[i]);
			}
		}
		return fileSet;
	}
	
	/**
	 * 
	 * @param filePath �ļ��ľ���·��
	 * @return
	 */
	public static InputStream getResourceAsStream(String filePath){
		try {
			InputStream input = new FileInputStream(filePath);
			return input;
		} 
		catch (FileNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * ��������ļ�·��
	 * @param dirPath ����Ŀ¼��ȫ·��
	 * @return
	 */
	public static String[] getConfigFilePaths(String dirPath){
		Set<String> pathSet = getResourcePaths(dirPath);
		if(pathSet == null)
			return null;
		Iterator<String> it = pathSet.iterator();
		List<String> list = new ArrayList<String>();
		while(it.hasNext()){
			String path = it.next();
			int index1 = path.lastIndexOf("/");
			int index2 = path.lastIndexOf(".");
			//ֻ�ܸ���"."���ж��Ƿ��ļ�
			if(index2 > index1){
				list.add(path);
				continue;
			}
			getConfigFiles(path, list);
		}
		return list.toArray(new String[0]);
	}
	
	private static void getConfigFiles(String dir, List<String> list){
		Set<String> pathSet = getResourcePaths(dir);
		Iterator<String> it = pathSet.iterator();
		while(it.hasNext()){
			String path = it.next();
			// ��pagemeta������Ҫ��.xml��β,��ȥ������a.xml.keep �ļ�
			if(path.indexOf(".xml") != -1 && path.indexOf(".xml.") == -1)
				list.add(path);
			// pagemeta������Ҫ��pagemeta.xml��β
			else if(path.indexOf("pagemeta.xml") != -1)
				list.add(path);
			else {
				int index1 = path.lastIndexOf("/");
				int index2 = path.lastIndexOf(".");
				if(index2 < index1)
				getConfigFiles(path, list);
			}
		}
	}
}
