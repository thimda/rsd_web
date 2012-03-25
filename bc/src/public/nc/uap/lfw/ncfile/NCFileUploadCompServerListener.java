package nc.uap.lfw.ncfile;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.file.event.UploadEvent;
import nc.uap.lfw.file.listener.UploadCompleteServerListener;
import nc.uap.lfw.file.vo.LfwFileVO;

public class NCFileUploadCompServerListener extends
		UploadCompleteServerListener {

	public NCFileUploadCompServerListener(LfwPageContext pagemeta,
			String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onComplete(UploadEvent event) {
		LfwFileVO file = event.getFile();
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget("main");
		Dataset ds = widget.getViewModels().getDataset("ds_ncfile");
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("pk"), file.getPk_lfwfile());
		row.setValue(ds.nameToIndex("filepath"), file.getFilename());
		ds.addRow(row);
	}

}
