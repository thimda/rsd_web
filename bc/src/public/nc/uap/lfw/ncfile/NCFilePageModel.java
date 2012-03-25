package nc.uap.lfw.ncfile;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.file.action.FileSystemAction;

public class NCFilePageModel extends PageModel {

	@Override
	protected void initPageMetaStruct() {
		// TODO Auto-generated method stub
		String billItem = LfwRuntimeEnvironment.getWebContext().getParameter(FileSystemAction.BILLITEM);
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute(FileSystemAction.BILLITEM, billItem);
	}

}
