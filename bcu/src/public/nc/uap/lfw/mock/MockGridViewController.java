package nc.uap.lfw.mock;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.MouseEvent;

/**
 * 表格型自定义参照实现类controller
 * @author zhangxya
 *
 */
public abstract class MockGridViewController {
	public void dataLoad(DataLoadEvent e){
	}
	public void okButtonClick(MouseEvent e){
	}
	
	public void cancelButtonClick(MouseEvent e){
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}
}