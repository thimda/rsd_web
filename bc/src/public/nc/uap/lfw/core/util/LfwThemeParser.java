package nc.uap.lfw.core.util;

import java.io.File;
import java.io.IOException;

import nc.uap.lfw.core.LfwTheme;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public final class LfwThemeParser {
	public static LfwTheme parse(File f) throws IOException, SAXException{
		Digester digester = new Digester();
		digester.setValidating(false);
		initRules(digester);
		return (LfwTheme) digester.parse(f);
	}

	private static void initRules(Digester digester) {
		String themeClass = LfwTheme.class.getName();
		digester.addObjectCreate("Theme", themeClass);
		digester.addSetProperties("Theme");
		
		digester.addCallMethod("Theme/Element", "setThemeElement", 2);
		digester.addCallParam("Theme/Element", 0, "id");
		digester.addCallParam("Theme/Element", 1, "value");
	}
}
