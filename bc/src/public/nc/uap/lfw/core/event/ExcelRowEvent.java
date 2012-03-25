package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.ExcelComp;

/**
 * @author guoweic
 *
 */
public class ExcelRowEvent extends AbstractServerEvent<ExcelComp> {

	public ExcelRowEvent(ExcelComp webElement) {
		super(webElement);
	}

}
