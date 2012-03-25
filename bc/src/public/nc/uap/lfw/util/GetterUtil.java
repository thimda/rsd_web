package nc.uap.lfw.util;

import java.text.DateFormat;
import java.util.Date;

/**
 * 便利的值获取工具
 * @author dengjt
 *
 */
public class GetterUtil {

	private static String[] BOOLEANS = {"true", "t", "y", "on", "1"};
	public static boolean getBoolean(String value) {
		return getBoolean(value, false);
	}

	public static boolean getBoolean(String value, boolean defaultValue) {
		return get(value, defaultValue);
	}

	public static Date getDate(String value, DateFormat df) {
		return getDate(value, df, new Date());
	}

	public static Date getDate(String value, DateFormat df, Date defaultValue) {
		return get(value, df, defaultValue);
	}

	public static double getDouble(String value) {
		return getDouble(value, 0.0);
	}

	public static double getDouble(String value, double defaultValue) {
		return get(value, defaultValue);
	}

	public static float getFloat(String value) {
		return getFloat(value, (float)0);
	}

	public static float getFloat(String value, float defaultValue) {
		return get(value, defaultValue);
	}

	public static int getInteger(String value) {
		return getInteger(value, 0);
	}

	public static int getInteger(String value, int defaultValue) {
		return get(value, defaultValue);
	}

	public static long getLong(String value) {
		return getLong(value, 0);
	}

	public static long getLong(String value, long defaultValue) {
		return get(value, defaultValue);
	}

	public static short getShort(String value) {
		return getShort(value, (short)0);
	}

	public static short getShort(String value, short defaultValue) {
		return get(value, defaultValue);
	}

	public static String getString(String value) {
		return getString(value, StringPool.BLANK);
	}

	public static String getString(String value, String defaultValue) {
		return get(value, defaultValue);
	}

	public static boolean get(String value, boolean defaultValue) {
		if (Validator.isNotNull(value)) {
			try {
				value = value.trim();
				if (value.equalsIgnoreCase(BOOLEANS[0]) ||
					value.equalsIgnoreCase(BOOLEANS[1]) ||
					value.equalsIgnoreCase(BOOLEANS[2]) ||
					value.equalsIgnoreCase(BOOLEANS[3]) ||
					value.equalsIgnoreCase(BOOLEANS[4])) {

					return true;
				}
				else {
					return false;
				}
			}
			catch (Exception e) {
			}
		}

		return defaultValue;
	}

	public static Date get(String value, DateFormat df, Date defaultValue) {
		try {
			Date date = df.parse(value.trim());

			if (date != null) {
				return date;
			}
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static double get(String value, double defaultValue) {
		try {
			return Double.parseDouble(trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static float get(String value, float defaultValue) {
		try {
			return Float.parseFloat(trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static int get(String value, int defaultValue) {
		try {
			return Integer.parseInt(trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static long get(String value, long defaultValue) {
		try {
			return Long.parseLong(trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static short get(String value, short defaultValue) {
		try {
			return Short.parseShort(trim(value));
		}
		catch (Exception e) {
		}

		return defaultValue;
	}

	public static String get(String value, String defaultValue) {
		if (Validator.isNotNull(value)) {
			value = value.trim();
			value = StringUtil.replace(value, "\r\n", "\n");

			return value;
		}

		return defaultValue;
	}

	private static String trim(String value) {
		if (value != null) {
			value = value.trim();
//
//			StringBuffer sb = new StringBuffer();
//
//			char[] charArray = value.toCharArray();
//
//			for (int i = 0; i < charArray.length; i++) {
//				if ((Character.isDigit(charArray[i])) ||
//					(charArray[i] == '-' && i == 0) ||
//					(charArray[i] == '.')) {
//
//					sb.append(charArray[i]);
//				}
//			}
//
//			value = sb.toString();
		}

		return value;
	}

}