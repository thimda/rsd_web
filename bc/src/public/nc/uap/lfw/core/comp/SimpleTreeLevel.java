package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 简单树Level定义
 * 简单树Level即此Level对应的dataset行数据间没有父子关系
 * @author dengjt
 *
 */
public class SimpleTreeLevel extends TreeLevel {

	private static final long serialVersionUID = -8998177771506215135L;

	public String getType() {
		return TREE_LEVEL_TYPE_SIMPLE;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
}
