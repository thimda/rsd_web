package nc.uap.lfw.mock;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.MouseEvent;

/**
 * �Զ��������Ͳ���ִ����
 * @author zhangxya
 *
 */
public class MockTreeGridController {
	public void dataLoad(DataLoadEvent e){
	}
	public void okButtonClick(MouseEvent e){
	}
	
	public void afterRowSel(DataLoadEvent e){
		
	}
	
	public void cancelButtonClick(MouseEvent e){
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}
}