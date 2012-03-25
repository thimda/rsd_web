package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.GridComp;

/**
 * @author guoweic
 *
 */
public class GridEvent extends AbstractServerEvent<GridComp> {

	public GridEvent(GridComp webElement) {
		super(webElement);
	}

}
