package nc.uap.lfw.core.model.util;

import nc.uap.lfw.core.data.Dataset;

public interface ICodeRuleDsUtil {
	public void buildCodeRuleDataset(Dataset ds, String codeRule, int codeIndex, int pkIndex, int ppkIndex);
}
