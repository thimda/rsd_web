package nc.uap.lfw.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.uap.lfw.core.data.Field;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * TODO 用正则重写
 * 
 */
public class Validator {
	/** email验证规则 **/
	protected static String emailRule = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	/** 汉字验证规则 **/
	protected static String chnRule = "[\u4e00-\u9fa5]*";
	/** 日期验证规则 **/
	protected static String dateRule = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29) ";
	/** 电话号码验证规则 **/
	protected static String phoneRule = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
	/** js变量验证规则 **/
	protected static String variableRule = "^[a-zA-Z_$][a-z0-9A-Z_$]*";

	/**
	 * 校验
	 * 
	 * @param field
	 * @param val
	 * @return
	 */
	public static String valid(Field field, String val) {
		StringBuffer errorContent = new StringBuffer();
		
		if ((val == null || "".equals(val)))
			return "";

		if (!Validator.maxValid(field, val)) {
			errorContent.append("[" + field.getText() + "]:数据超过最大值\r\n<br>");
		}
		if (!Validator.minValid(field, val)) {
			errorContent.append("[" + field.getText() + "]:数据低于最小值\r\n<br>");
		}
		if (!Validator.ruleValid(field, val)) {
			errorContent.append("[" + field.getText() + "]:数据不符合校验规则\r\n<br>");
		}
		return errorContent.toString();
	}

	/**
	 * 最大值校验
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static boolean maxValid(Field field, String value) {
		int maxSize = StringUtils.isNumeric(field.getMaxValue()) ? Integer
				.parseInt(field.getMaxValue()) : 0;
		// 需要验证值
		if (maxSize > 0 && value == null)
			return false;
		if (maxSize > 0) {
			// 若输入字节长度大于指定长度,则截去超过指定的最大长度的部分

			if (field.getDataType().toLowerCase().endsWith("double")
					|| field.getDataType().toLowerCase().endsWith("float")
					|| field.getDataType().toLowerCase().endsWith("long")
					|| field.getDataType().toLowerCase().indexOf("int") >= 0) {

				return !(Double.parseDouble(value) > maxSize);
			} else {
				return !(value.length() > maxSize);
			}

		}
		return true;
	}

	/**
	 * 最小值校验
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static boolean minValid(Field field, String value) {
		int minSize = StringUtils.isNumeric(field.getMinValue()) ? Integer
				.parseInt(field.getMinValue()) : 0;
		if (minSize > 0 && value == null)
			return false;
		if (minSize > 0) {
			if (field.getDataType().toLowerCase().endsWith("double")
					|| field.getDataType().toLowerCase().endsWith("float")
					|| field.getDataType().toLowerCase().endsWith("long")
					|| field.getDataType().toLowerCase().indexOf("int") >= 0) {

				return !(Double.parseDouble(value) < minSize);
			} else {
				return !(value.length() < minSize);
			}

		}
		return true;
	}

	/**
	 * 规则校验
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static boolean ruleValid(Field field, String value) {
		String formater = field.getFormater();
		if (StringUtils.isEmpty(formater))
			return true;
		if (formater.equals("email")) {
			return patternFinder(value, emailRule);
		} else if (formater.equals("number")) {
			return StringUtils.isNumeric(value);
		} else if (formater.equals("chn")) {
			return patternFinder(value, chnRule);
		} else if (formater.equals("variable")) {
			return patternFinder(value, variableRule);
		} else if (formater.equals("date")) {
			return patternFinder(value, dateRule);
		} else if (formater.equals("phone")) {
			return patternFinder(value, phoneRule);
		} else {
			return patternFinder(value, formater);
		}
	};

	/**
	 * 正则查找
	 * 
	 * @param value
	 * @param patternRule
	 * @return
	 */
	public static boolean patternFinder(String value, String patternRule) {
		if (value == null)
			return false;
		Pattern pattern = Pattern.compile(patternRule);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	public static boolean isAddress(String address) {
		if (isNull(address)) {
			return false;
		}

		char[] c = address.toCharArray();

		for (int i = 0; i < c.length; i++) {
			if ((!isChar(c[i])) && (!isDigit(c[i]))
					&& (!Character.isWhitespace(c[i]))) {

				return false;
			}
		}

		return true;
	}

	/**
	 * 确定指定字符是否为字母
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChar(char c) {
		return Character.isLetter(c);
	}

	public static boolean isChar(String s) {
		if (isNull(s)) {
			return false;
		}
		char[] c = s.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (!isChar(c[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 确定指定字符是否为数字
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isDigit(char c) {
		int x = (int) c;
		if ((x >= 48) && (x <= 57)) {
			return true;
		}
		return false;
	}

	public static boolean isDigit(String s) {
		if (isNull(s)) {
			return false;
		}
		char[] c = s.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (!isDigit(c[i])) {
				return false;
			}
		}
		return true;
	}

	public static boolean isHex(String s) {
		if (isNull(s)) {
			return false;
		}

		return true;
	}

	public static boolean isHTML(String s) {
		if (isNull(s)) {
			return false;
		}

		if (((s.indexOf("<html>") != -1) || (s.indexOf("<HTML>") != -1))
				&& ((s.indexOf("</html>") != -1) || (s.indexOf("</HTML>") != -1))) {

			return true;
		}

		return false;
	}

	public static boolean isLUHN(String number) {
		if (number == null) {
			return false;
		}

		number = StringUtil.reverse(number);

		int total = 0;

		for (int i = 0; i < number.length(); i++) {
			int x = 0;

			if (((i + 1) % 2) == 0) {
				x = Integer.parseInt(number.substring(i, i + 1)) * 2;

				if (x >= 10) {
					String s = Integer.toString(x);

					x = Integer.parseInt(s.substring(0, 1))
							+ Integer.parseInt(s.substring(1, 2));
				}
			} else {
				x = Integer.parseInt(number.substring(i, i + 1));
			}

			total = total + x;
		}

		if ((total % 10) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isDate(int month, int day, int year) {
		return false;
	}

	public static boolean isGregorianDate(int month, int day, int year) {
		return false;
	}

	public static boolean isJulianDate(int month, int day, int year) {
		return false;
	}

	public static boolean isEmailAddress(String ea) {
		if (isNull(ea)) {
			return false;
		}

		int eaLength = ea.length();

		if (eaLength < 6) {

			// j@j.c

			return false;
		}

		ea = ea.toLowerCase();

		int at = ea.indexOf('@');

		// Unix based email addresses cannot be longer than 24 characters.
		// However, many Windows based email addresses can be longer than 24,
		// so we will set the maximum at 48.

		// int maxEmailLength = 24;
		int maxEmailLength = 48;

		if ((at > maxEmailLength) || (at == -1) || (at == 0)
				|| ((at <= eaLength) && (at > eaLength - 5))) {

			// 123456789012345678901234@joe.com
			// joe.com
			// @joe.com
			// joe@joe
			// joe@jo
			// joe@j

			return false;
		}

		int dot = ea.lastIndexOf('.');

		if ((dot == -1) || (dot < at) || (dot > eaLength - 3)) {

			// joe@joecom
			// joe.@joecom
			// joe@joe.c

			return false;
		}

		if (ea.indexOf("..") != -1) {

			// joe@joe..com

			return false;
		}

		char[] name = ea.substring(0, at).toCharArray();

		for (int i = 0; i < name.length; i++) {
			if ((!isChar(name[i])) && (!isDigit(name[i])) && (name[i] != '.')
					&& (name[i] != '-') && (name[i] != '_')) {

				return false;
			}
		}

		if ((name[0] == '.') || (name[name.length - 1] == '.')
				|| (name[0] == '-') || (name[name.length - 1] == '-')
				|| (name[0] == '_')) { // || (name[name.length - 1] == '_')) {

			// .joe.@joe.com
			// -joe-@joe.com
			// _joe_@joe.com

			return false;
		}

		char[] host = ea.substring(at + 1, ea.length()).toCharArray();

		for (int i = 0; i < host.length; i++) {
			if ((!isChar(host[i])) && (!isDigit(host[i])) && (host[i] != '.')
					&& (host[i] != '-')) {

				return false;
			}
		}

		if ((host[0] == '.') || (host[host.length - 1] == '.')
				|| (host[0] == '-') || (host[host.length - 1] == '-')) {

			// joe@.joe.com.
			// joe@-joe.com-

			return false;
		}

		// postmaster@joe.com

		if (ea.startsWith("postmaster@")) {
			return false;
		}

		// root@.com

		if (ea.startsWith("root@")) {
			return false;
		}

		return true;
	}

	/**
	 * @deprecated Use <code>isEmailAddress</code>.
	 */
	public static boolean isValidEmailAddress(String ea) {
		return isEmailAddress(ea);
	}

	public static boolean isName(String name) {
		if (isNull(name)) {
			return false;
		}

		char[] c = name.trim().toCharArray();

		for (int i = 0; i < c.length; i++) {
			if (((!isChar(c[i])) && (!Character.isWhitespace(c[i])))
					|| (c[i] == ',')) {

				return false;
			}
		}

		return true;
	}

	public static boolean isNumber(String number) {
		if (isNull(number)) {
			return false;
		}

		char[] c = number.toCharArray();

		for (int i = 0; i < c.length; i++) {
			if (!isDigit(c[i])) {
				return false;
			}
		}

		return true;
	}

	public static boolean isNull(String s) {
		if (s == null) {
			return true;
		}

		s = s.trim();

		if ((s.equals(StringPool.NULL)) || (s.equals(StringPool.BLANK))) {
			return true;
		}

		return false;
	}

	public static boolean isNotNull(String s) {
		return !isNull(s);
	}

	public static boolean isPassword(String password) {
		if (isNull(password)) {
			return false;
		}

		if (password.length() < 4) {
			return false;
		}

		char[] c = password.toCharArray();

		for (int i = 0; i < c.length; i++) {
			if ((!isChar(c[i])) && (!isDigit(c[i]))) {

				return false;
			}
		}

		return true;
	}

	public static boolean isPhoneNumber(String phoneNumber) {
		return isNumber(StringUtil.extractDigits(phoneNumber));
	}

}
