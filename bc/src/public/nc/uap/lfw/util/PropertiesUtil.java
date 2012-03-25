package nc.uap.lfw.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Properties操作工具类
 * @author dengjt 2006-1-24
 */
public class PropertiesUtil {

	public static void copyProperties(Properties from, Properties to) {
		Iterator itr = from.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			to.setProperty((String) entry.getKey(), (String) entry.getValue());
		}
	}

	public static Properties fromMap(Map map) {
		Properties p = new Properties();
		Iterator itr = map.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			p.setProperty((String) entry.getKey(), (String) entry.getValue());
		}
		return p;
	}

	public static void fromProperties(Properties p, Map<String, String> map) {
		map.clear();
		Iterator itr = p.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			map.put((String)entry.getKey(), (String)entry.getValue());
		}
	}

	public static Properties load(String s) throws IOException {
		Properties p = new Properties();
		load(p, s);
		return p;
	}

	public static void load(Properties p, String s) throws IOException {
		if (Validator.isNotNull(s)) {
			s = UnicodeFormatter.toString(s);
			s = StringUtil.replace(s, "\\u003d", "=");
			s = StringUtil.replace(s, "\\u000a", "\n");
			p.load(new ByteArrayInputStream(s.getBytes()));
		}
	}

	public static void merge(Properties p1, Properties p2) {
		Enumeration enu = p2.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			String value = p1.getProperty(key);
			p1.setProperty(key, value);
		}
	}

	public static String toString(Properties p) {
		StringBuffer sb = new StringBuffer();
		Enumeration enu = p.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			sb.append(key);
			sb.append(StringPool.EQUAL);
			sb.append(p.getProperty(key));
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void trimKeys(Properties p) {
		Enumeration enu = p.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			String value = p.getProperty(key);
			String trimmedKey = key.trim();
			if (!key.equals(trimmedKey)) {
				p.remove(key);
				p.setProperty(trimmedKey, value);
			}
		}
	}
}
