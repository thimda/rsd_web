package nc.uap.lfw.core.model.parser;

import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.base.ExtendAttributeSupport;

import org.apache.commons.digester.Digester;

/**
 * 扩展属性通用解析器
 * @author gd 2010-1-22
 * @version NC6.0
 * @since NC6.0
 */
public class AttributesParser 
{
	public static void parseAttributes(Digester digester, String base, Class<? extends ExtendAttributeSupport> eleClazz) {
		String path = base + "/Attributes/Attribute";
		String attrClazz = ExtAttribute.class.getName();
		digester.addObjectCreate(path, attrClazz);
		digester.addCallMethod(path + "/Key", "setKey", 0);
		digester.addCallMethod(path + "/Value", "setValue", 0);
		digester.addCallMethod(path + "/Desc", "setDesc", 0);
		digester.addSetNext(path, "addAttribute", attrClazz);
	}
}
