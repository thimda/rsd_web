package nc.uap.lfw.ncfile;

import javax.swing.tree.TreeNode;

import org.apache.xerces.dom.ChildNode;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.deft.DefaultDatasetServerListener;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.action.FileSystemAction;
import nc.vo.pub.BusinessException;
import nc.vo.pub.filesystem.NCFileNode;

/**
 * 加载nc上传的文件
 * @author zhangxya
 *
 */
public class NCFileDatasetLoadListener extends DefaultDatasetServerListener {

	@Override
	public void onDataLoad(DataLoadEvent se) {
		// TODO Auto-generated method stub
		Dataset ds = se.getSource();
		String nodePath = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(FileSystemAction.BILLITEM);
		IFileSystemService fileSysService = NCLocator.getInstance().lookup(IFileSystemService.class);
		NCFileNode fileNode = null;
		try {
			 fileNode = fileSysService.queryNCFileNodeTree(nodePath);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			LfwLogger.error(e.getMessage(), e);
		}
		if(fileNode != null){
			int childCount = fileNode.getChildCount();
			for (int i = 0; i < childCount; i++) {
				TreeNode child = fileNode.getChildAt(i);
				if(child instanceof NCFileNode ){
					NCFileNode ncChildNode = (NCFileNode)child;
					if(!ncChildNode.getAllowsChildren()){
						Row row = ds.getEmptyRow();
						ds.addRow(row);
						row.setValue(0, ncChildNode.getUserObject());
						row.setValue(1, ncChildNode.getFullPath());
					}
					else{
						int count = ncChildNode.getChildCount();
						for (int j = 0; j < count; j++) {
							TreeNode ncTreeNode = ncChildNode.getChildAt(j);
							if(ncTreeNode instanceof NCFileNode){
								Row row = ds.getEmptyRow();
								ds.addRow(row);
								row.setValue(0, ((NCFileNode)ncTreeNode).getUserObject());
								row.setValue(1, ((NCFileNode)ncTreeNode).getFullPath());
							}
						}
					}
				}
			}
		}
	}

	public NCFileDatasetLoadListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
		// TODO Auto-generated constructor stub
	}

}
