package nc.uap.lfw.core.model.util;

import java.util.Iterator;
import java.util.Map;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.WidgetRule;

/**
 * 合并EventSubmitRule
 * 
 * @author dingrf
 *
 */
public class LfwSubmitRuleMergeUtil {
	public static void mergeSubmitRule(EventSubmitRule targetSubmitRule, EventSubmitRule sourceSubmitRule){
		if (targetSubmitRule == null || sourceSubmitRule == null)
			return;
		//处理父提交规
		if (targetSubmitRule.getParentSubmitRule() == null && sourceSubmitRule.getParentSubmitRule() != null)
			targetSubmitRule.setParentSubmitRule(sourceSubmitRule.getParentSubmitRule());
		else if (targetSubmitRule.getParentSubmitRule() != null && sourceSubmitRule.getParentSubmitRule() != null)	
			LfwSubmitRuleMergeUtil.mergeSubmitRule(targetSubmitRule.getParentSubmitRule(), sourceSubmitRule.getParentSubmitRule());
		
		//处理plugout的提交规则参数
		if (targetSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_SIGN) == null && sourceSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_SIGN) != null){
			targetSubmitRule.addParam(sourceSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_SIGN));
			if (sourceSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW) != null)
				targetSubmitRule.addParam(sourceSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW));
			if (sourceSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_SOURCE) != null)
				targetSubmitRule.addParam(sourceSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_SOURCE));
			if (sourceSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_ID) != null)
				targetSubmitRule.addParam(sourceSubmitRule.getParam(AppLifeCycleContext.PLUGOUT_ID));
		}
		mergeWidgetRules(targetSubmitRule,sourceSubmitRule);
	}

	/**
	 * 合并SubmitRule中的所有widgetRule
	 * 
	 * @param targetSubmitRule
	 * @param sourceSubmitRule
	 */
	private static void mergeWidgetRules(EventSubmitRule targetSubmitRule, EventSubmitRule sourceSubmitRule){
		Map<String, WidgetRule> sourceWidgetRuleMap = sourceSubmitRule.getWidgetRules();
		if (sourceWidgetRuleMap.size() == 0)
			return;
		Iterator<WidgetRule> it = sourceWidgetRuleMap.values().iterator();
		while(it.hasNext()){
			WidgetRule sourceWr = it.next();
			String widgetId = sourceWr.getId();
			WidgetRule targetWr = targetSubmitRule.getWidgetRule(widgetId);
			if (targetWr == null)
				targetSubmitRule.addWidgetRule(sourceWr);
			else
				mergeWidgetRule(targetWr, sourceWr);
		}
	}

	/**
	 *合并同id的widgetRule
	 *
	 * @param targetWr
	 * @param sourceWr
	 */
	private static void mergeWidgetRule(WidgetRule targetWidgetRule, WidgetRule sourceWidgetRule) {
		mergetDatasetRules(targetWidgetRule, sourceWidgetRule);
		
	}

	/**
	 * 合并widgetRule中的所有datasetRule
	 * @param targetWidgetRule
	 * @param sourceWidgetRule
	 */
	private static void mergetDatasetRules(WidgetRule targetWidgetRule,
			WidgetRule sourceWidgetRule) {
		DatasetRule[]  sourceDatasetRules = sourceWidgetRule.getDatasetRules();
		if (sourceDatasetRules == null || sourceDatasetRules.length == 0)
			return;
		for (int i = 0; i < sourceDatasetRules.length; i++) {
			DatasetRule sourceDr = sourceDatasetRules[i];
			String datasetId = sourceDr.getId();
			DatasetRule targetDr = targetWidgetRule.getDatasetRule(datasetId);
			if (targetDr == null)
				targetWidgetRule.addDsRule(sourceDr);
			else
				mergeDatasetRule(targetDr, sourceDr);
		}
	}

	/**
	 * 合并DatasetRule
	 * 
	 * @param targetDatasetRule
	 * @param sourceDatasetRule
	 */
	private static void mergeDatasetRule(DatasetRule targetDatasetRule,
			DatasetRule sourceDatasetRule) {
		String targetDrType = targetDatasetRule.getType();
		String sourceDrType = sourceDatasetRule.getType();
		if (targetDrType.equals(DatasetRule.TYPE_ALL_LINE) || sourceDrType.equals(DatasetRule.TYPE_ALL_LINE))
			targetDatasetRule.setType(DatasetRule.TYPE_ALL_LINE);
		else if (targetDrType.equals(DatasetRule.TYPE_CURRENT_PAGE) || sourceDrType.equals(DatasetRule.TYPE_CURRENT_PAGE))
			targetDatasetRule.setType(DatasetRule.TYPE_CURRENT_PAGE);
	}
}
