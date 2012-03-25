package nc.uap.lfw.core.model;

import nc.uap.lfw.util.LfwClassUtil;

public class RuntimeAdjusterFactory {

	public static IRuntimeAdjuster getRuntimeAdjuster() {
		IRuntimeAdjuster adjuster = null;
		if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
			adjuster = (IRuntimeAdjuster) LfwClassUtil.newInstance("nc.uap.lfw.core.event.AppTypeRuntimeAdjuster");
		}
		else if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.PAGE_TYPE)){
			adjuster = (IRuntimeAdjuster) LfwClassUtil.newInstance("nc.uap.lfw.core.page.PageTypeRuntimeAdjuster");
		}
		return adjuster;
	}

}
