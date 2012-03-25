package nc.uap.lfw.core.cmd;
import java.util.ArrayList;
import java.util.List;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
/**
 * "新增"菜单逻辑处理
 * 
 * @author gd 2010-3-26
 * 
 */
public class UifAddCmd extends UifCommand {
	private String dsId;
	private String navDatasetId;
	private String navStr;
	public UifAddCmd(String dsId) {
		this.dsId = dsId;
	}
	public UifAddCmd(String dsId, String navDatasetId, String navStr) {
		this.dsId = dsId;
		this.navDatasetId = navDatasetId;
		this.navStr = navStr;
	}
	public void execute() {
		ViewContext widgetctx = getLifeCycleContext().getViewContext();
		boolean pageRecordUndo = false;
		boolean widgetRecordUndo = false;
		LfwWidget widget = widgetctx.getView();
		if (this.dsId == null)
			throw new LfwRuntimeException("未指定数据集id!");
		Dataset ds = widget.getViewModels().getDataset(dsId);
		if (ds == null)
			throw new LfwRuntimeException("数据集为空,数据集id=" + dsId + "!");
		String currKey = ds.getCurrentKey();
		if (currKey == null || currKey.equals("")) {
			if (this.navDatasetId != null)
				throw new LfwRuntimeException(navStr);
			ds.getRowSet(Dataset.MASTER_KEY, true);
			ds.setCurrentKey(Dataset.MASTER_KEY);
		}
		if (ds.isControlwidgetopeStatus())
			widgetRecordUndo = true;
		List<String> idList = new ArrayList<String>();
		idList.add(dsId);
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation[] rels = dsRels.getDsRelations(dsId);
			if (rels != null) {
				for (int i = 0; i < rels.length; i++) {
					String detailDsId = rels[i].getDetailDataset();
					idList.add(detailDsId);
					Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
					detailDs.setEnabled(true);
					if (detailDs.isControlwidgetopeStatus())
						widgetRecordUndo = true;
				}
			}
		}
		if (pageRecordUndo)
			getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.recordUndo();\n");
		if (widgetRecordUndo)
			getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').recordUndo();\n");
		{
			for (int i = 0; i < idList.size(); i++) {
				getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').getDataset('" + idList.get(i) + "').recordUndo();\n");
			}
		}
		// 新增行并选中
		Row row = ds.getEmptyRow();
		setNavPkToRow(row, this.navDatasetId, ds);
		onBeforeRowAdd(row);
		ds.addRow(row);
		ds.setRowSelectIndex(ds.getRowIndex(row));
		ds.setEnabled(true);
		onAfterRowAdd(row);
	}
	protected void setNavPkToRow(Row row, String navId, Dataset slaveDs) {
		if (navId == null)
			return;
		LfwWidget widget = AppLifeCycleContext.current().getViewContext().getView();
		// 缓存子表
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if (dsRels != null) {
			DatasetRelation rel = dsRels.getDsRelation(navId, slaveDs.getId());
			Dataset ds = widget.getViewModels().getDataset(navId);
			Row navrow = ds.getSelectedRow();
			if (navrow == null) {
				throw new LfwRuntimeException("请选择导航数据集");
			}
			Object value = navrow.getValue(ds.nameToIndex(rel.getMasterKeyField()));
			row.setValue(slaveDs.nameToIndex(rel.getDetailForeignKey()), value);
		}
	}
	protected void onAfterRowAdd(Row row) {
		updateButtons();
	}
	protected void onBeforeRowAdd(Row row) {}
}
