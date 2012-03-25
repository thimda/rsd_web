package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.event.DatasetColSingleEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.RowInsertEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

public abstract class DatasetServerListener extends AbstractServerListener {

	public DatasetServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onBeforeRowSelect(DatasetEvent e){}
	public void onAfterRowSelect(DatasetEvent e){};
	public void onAfterRowUnSelect(DatasetEvent e){};
	public void onBeforeRowInsert(DatasetEvent e){};
	public void onAfterRowInsert(RowInsertEvent e){};
	public void onBeforeDataChange(DatasetCellEvent e){};
	public void onAfterDataChange(DatasetCellEvent e){};
	public void onBeforeRowDelete(DatasetEvent e){};
	public void onAfterRowDelete(DatasetEvent e){};
	public void onBeforePageChange(DatasetEvent e){};
	public void onAfterPageChange(DatasetEvent e){};
	public void onDataLoad(DataLoadEvent e){};
	//�������޸�ʱ������DataChange�¼�,Ĭ�ϵ���onAfterDataChange(DatasetCellEvent e)��ͳһ����
	public void onAfterDataChange(DatasetColSingleEvent e){
		onAfterDataChange((DatasetCellEvent)e);
	};
}
