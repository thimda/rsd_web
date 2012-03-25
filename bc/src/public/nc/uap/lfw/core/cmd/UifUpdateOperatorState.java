/**
 * 
 */
package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * @author chouhl
 *
 */
public class UifUpdateOperatorState extends UifCommand {

	private Dataset ds;
	
	private LfwWidget widget;
	
	public UifUpdateOperatorState(Dataset ds, LfwWidget widget){
		this.ds = ds;
		this.widget = widget;
	}
	
	@Override
	public void execute() {
//		Row[] rows = ds.getSelectedRows();
////		if(IOperatorState.ADD.equals(widget.getOperatorState()) || IOperatorState.EDIT.equals(widget.getOperatorState()))
////			return;
//		if(rows == null || rows.length == 0){
//			widget.setOperatorState(IOperatorState.INIT);
//		}else if(rows.length == 1){
//			if(Row.STATE_ADD == rows[0].getState()){ 
//				widget.setOperatorState(IOperatorState.ADD);
//			}else if(Row.STATE_UPDATE == rows[0].getState()){ 
//				widget.setOperatorState(IOperatorState.EDIT);
//			}else{
//				widget.setOperatorState(IOperatorState.SINGLESEL);
//			}
//		}else{
//			widget.setOperatorState(IOperatorState.MULTISEL);
//		}
	}

}
