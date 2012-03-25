package nc.uap.lfw.core.ctrlfrm;

import java.io.File;

import nc.uap.lfw.core.ctrlfrm.plugin.ControlPluginConfig;
import nc.uap.lfw.core.log.LfwLogger;

import org.apache.commons.digester.Digester;

public final class ControlPluginParser {
	public static ControlPluginConfig parse(File file) {
		try {
			Digester digester = new Digester();
			digester.setValidating(false);
			
			String ctrlPluginClazz = ControlPluginConfig.class.getName();
			digester.addObjectCreate("plugin", ctrlPluginClazz);
			digester.addSetProperties("plugin");
			digester.addCallMethod("plugin/id", "setId", 0);
			digester.addCallMethod("plugin/fullname", "setFullname", 0);
			digester.addCallMethod("plugin/cssfullname", "setCssfullname", 0);
			digester.addCallMethod("plugin/dependences", "setDependences", 0);
			digester.addCallMethod("plugin/javaclazz", "setJavaclazz", 0);
			digester.addCallMethod("plugin/uijavaclazz", "setUijavaclazz", 0);
			digester.addCallMethod("plugin/serializer", "setSerializer", 0);
			digester.addCallMethod("plugin/uiserializer", "setUiserializer", 0);
			digester.addCallMethod("plugin/uirender", "setUirender", 0);
			digester.addCallMethod("plugin/zone", "setZone", 0);
			digester.addCallMethod("plugin/compress", "setCompress", 0);
			return (ControlPluginConfig) digester.parse(file);
		} 
		catch (Exception e) {
			LfwLogger.error(e);
		}
		return null;
	}
}
