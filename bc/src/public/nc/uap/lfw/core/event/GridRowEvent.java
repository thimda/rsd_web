package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.GridComp;

/**
 * @author guoweic
 *
 */
public class GridRowEvent extends AbstractServerEvent<GridComp> {

	public GridRowEvent(GridComp webElement) {
		super(webElement);
	}

}
