package nc.uap.lfw.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.log.LfwLogger;
/**
 * String通用操作工具
 * @author dengjt
 * 2006-1-24
 */
public class StringUtil {

	public static String add(String s, String add) {
		return add(s, add, StringPool.COMMA);
	}

	public static String add(String s, String add, String delimiter) {
		return add(s, add, delimiter, false);
	}

	public static String add(
		String s, String add, String delimiter, boolean allowDuplicates) {

		if ((add == null) || (delimiter == null)) {
			return null;
		}

		if (s == null) {
			s = StringPool.BLANK;
		}

		if (allowDuplicates || !contains(s, add, delimiter)) {
			if (Validator.isNull(s) || s.endsWith(delimiter)) {
				s += add + delimiter;
			}
			else {
				s += delimiter + add + delimiter;
			}
		}

		return s;
	}

	public static boolean contains(String s, String text) {
		return contains(s, text, StringPool.COMMA);
	}

	public static boolean contains(String s, String text, String delimiter) {
		if ((s == null) || (text == null) || (delimiter == null)) {
			return false;
		}

		if (!s.endsWith(delimiter)) {
			s += delimiter;
		}

		int pos = s.indexOf(delimiter + text + delimiter);

		if (pos == -1) {
			if (s.startsWith(text + delimiter)) {
				return true;
			}

			return false;
		}

		return true;
	}

	public static int count(String s, String text) {
		if ((s == null) || (text == null)) {
			return 0;
		}

		int count = 0;

		int pos = s.indexOf(text);

		while (pos != -1) {
			pos = s.indexOf(text, pos + text.length());
			count++;
		}

		return count;
	}

	public static boolean endsWith(String s, char end) {
		return startsWith(s, (Character.valueOf(end)).toString());
	}

	public static boolean endsWith(String s, String end) {
		if ((s == null) || (end == null)) {
			return false;
		}

		if (end.length() > s.length()) {
			return false;
		}

		String temp = s.substring(s.length() - end.length(), s.length());

		if (temp.equalsIgnoreCase(end)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static String extractChars(String s) {
		if (s == null) {
			return "";
		}

		char[] c = s.toCharArray();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < c.length; i++) {
			if (Validator.isChar(c[i])) {
				sb.append(c[i]);
			}
		}

		return sb.toString();
	}

	public static String extractDigits(String s) {
		if (s == null) {
			return "";
		}

		char[] c = s.toCharArray();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < c.length; i++) {
			if (Validator.isDigit(c[i])) {
				sb.append(c[i]);
			}
		}

		return sb.toString();
	}

	public static String merge(List<String> list) {
		return merge(list, StringPool.COMMA);
	}
	public static String merge(Set<String> set)
	{
		return merge(set, StringPool.COMMA);
	}
	
	public static String merge(List<String> list, String delimiter) {
		return merge((String[])list.toArray(new String[0]), delimiter);
	}
	public static String merge(Set<String> set, String delimiter) {
		return merge((String[])set.toArray(new String[0]), delimiter);
	}
	
	public static String merge(String[] array) {
		return merge(array, StringPool.COMMA);
	}

	public static String merge(String[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < array.length; i++) {
			sb.append(array[i].trim());

			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}

		return sb.toString();
	}

//	public static String randomize(String s) {
//		Randomizer r = new Randomizer();
//
//		return r.randomize(s);
//	}

	public static String read(ClassLoader classLoader, String name)
		throws IOException {

		return read(classLoader.getResourceAsStream(name));
	}

	public static String read(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		StringBuffer sb = new StringBuffer();
		String line = null;

		while ((line = br.readLine()) != null) {
			sb.append(line).append('\n');
		}

		br.close();

		return sb.toString().trim();
	}

	public static String remove(String s, String remove) {
		return remove(s, remove, StringPool.COMMA);
	}

	public static String remove(String s, String remove, String delimiter) {
		if ((s == null) || (remove == null) || (delimiter == null)) {
			return null;
		}

		if (Validator.isNotNull(s) && !s.endsWith(delimiter)) {
			s += delimiter;
		}

		while (contains(s, remove, delimiter)) {
			int pos = s.indexOf(delimiter + remove + delimiter);

			if (pos == -1) {
				if (s.startsWith(remove + delimiter)) {
					s = s.substring(
							remove.length() + delimiter.length(), s.length());
				}
			}
			else {
				s = s.substring(0, pos) + s.substring(pos + remove.length() +
					delimiter.length(), s.length());
			}
		}

		return s;
	}

	public static String replace(String s, char oldSub, char newSub) {
		return replace(s, oldSub, Character.valueOf(newSub).toString());
	}

	public static String replace(String s, char oldSub, String newSub) {
		if ((s == null) || (newSub == null)) {
			return null;
		}

		char[] c = s.toCharArray();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < c.length; i++) {
			if (c[i] == oldSub) {
				sb.append(newSub);
			}
			else {
				sb.append(c[i]);
			}
		}

		return sb.toString();
	}

	public static String replace(String s, String oldSub, String newSub) {
		if ((s == null) || (oldSub == null) || (newSub == null)) {
			return null;
		}

		int y = s.indexOf(oldSub);

		if (y >= 0) {
			StringBuffer sb = new StringBuffer();

			int length = oldSub.length();
			int x = 0;

			while (x <= y) {
				sb.append(s.substring(x, y));
				sb.append(newSub);
				x = y + length;
				y = s.indexOf(oldSub, x);
			}

			sb.append(s.substring(x));

			return sb.toString();
		}
		else {
			return s;
		}
	}

	public static String replace(String s, String[] oldSubs, String[] newSubs) {
		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		for (int i = 0; i < oldSubs.length; i++) {
			s = replace(s, oldSubs[i], newSubs[i]);
		}

		return s;
	}

	public static String reverse(String s) {
		if (s == null) {
			return null;
		}

		char[] c = s.toCharArray();
		char[] reverse = new char[c.length];

		for (int i = 0; i < c.length; i++) {
			reverse[i] = c[c.length - i - 1];
		}

		return new String(reverse);
	}

	public static String shorten(String s) {
		return shorten(s, 20);
	}

	public static String shorten(String s, int length) {
		return shorten(s, length, "..");
	}

	public static String shorten(String s, String suffix) {
		return shorten(s, 20, suffix);
	}

	public static String shorten(String s, int length, String suffix) {
		if (s == null || suffix == null)  {
			return null;
		}

		if (s.length() > length) {
			s = s.substring(0, length) + suffix;
		}

		return s;
	}

	public static String[] split(String s) {
		return split(s, StringPool.COMMA);
	}

	public static String[] split(String s, String delimiter) {
		if (s == null || delimiter == null) {
			return new String[0];
		}

		s = s.trim();

		if (!s.endsWith(delimiter)) {
			s += delimiter;
		}

		if (s.equals(delimiter)) {
			return new String[0];
		}

		List<String> nodeValues = new ArrayList<String>();

		if (delimiter.equals("\n") || delimiter.equals("\r")) {
			try {
				BufferedReader br = new BufferedReader(new StringReader(s));

				String line = null;

				while ((line = br.readLine()) != null) {
					nodeValues.add(line);
				}

				br.close();
			}
			catch (IOException ioe) {
				LfwLogger.error(ioe.getMessage(), ioe);
			}
		}
		else {
			int offset = 0;
			int pos = s.indexOf(delimiter, offset);

			while (pos != -1) {
				nodeValues.add(s.substring(offset, pos));

				offset = pos + delimiter.length();
				pos = s.indexOf(delimiter, offset);
			}
		}

		return (String[])nodeValues.toArray(new String[0]);
	}

	public static boolean[] split(String s, String delimiter, boolean x) {
		String[] array = split(s, delimiter);
		boolean[] newArray = new boolean[array.length];

		for (int i = 0; i < array.length; i++) {
			boolean value = x;

			try {
				value = Boolean.valueOf(array[i]).booleanValue();
			}
			catch (Exception e) {
			}

			newArray[i] = value;
		}

		return newArray;
	}

	public static double[] split(String s, String delimiter, double x) {
		String[] array = split(s, delimiter);
		double[] newArray = new double[array.length];

		for (int i = 0; i < array.length; i++) {
			double value = x;

			try {
				value = Double.parseDouble(array[i]);
			}
			catch (Exception e) {
			}

			newArray[i] = value;
		}

		return newArray;
	}

	public static float[] split(String s, String delimiter, float x) {
		String[] array = split(s, delimiter);
		float[] newArray = new float[array.length];

		for (int i = 0; i < array.length; i++) {
			float value = x;

			try {
				value = Float.parseFloat(array[i]);
			}
			catch (Exception e) {
			}

			newArray[i] = value;
		}

		return newArray;
	}

	public static int[] split(String s, String delimiter, int x) {
		String[] array = split(s, delimiter);
		int[] newArray = new int[array.length];

		for (int i = 0; i < array.length; i++) {
			int value = x;

			try {
				value = Integer.parseInt(array[i]);
			}
			catch (Exception e) {
			}

			newArray[i] = value;
		}

		return newArray;
	}

	public static long[] split(String s, String delimiter, long x) {
		String[] array = split(s, delimiter);
		long[] newArray = new long[array.length];

		for (int i = 0; i < array.length; i++) {
			long value = x;

			try {
				value = Long.parseLong(array[i]);
			}
			catch (Exception e) {
			}

			newArray[i] = value;
		}

		return newArray;
	}

	public static short[] split(String s, String delimiter, short x) {
		String[] array = split(s, delimiter);
		short[] newArray = new short[array.length];

		for (int i = 0; i < array.length; i++) {
			short value = x;

			try {
				value = Short.parseShort(array[i]);
			}
			catch (Exception e) {
			}

			newArray[i] = value;
		}

		return newArray;
	}

    public static final String stackTrace(Throwable t) {
		String s = null;

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			t.printStackTrace(new PrintWriter(baos, true));
			s = baos.toString();
		}
		catch (Exception e) {
		}

		return s;
    }

	public static boolean startsWith(String s, char begin) {
		return startsWith(s, (Character.valueOf(begin)).toString());
	}

	public static boolean startsWith(String s, String start) {
		if ((s == null) || (start == null)) {
			return false;
		}

		if (start.length() > s.length()) {
			return false;
		}

		String temp = s.substring(0, start.length());

		if (temp.equalsIgnoreCase(start)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static String trimLeading(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return s.substring(i, s.length());
			}
		}

		return StringPool.BLANK;
	}

	public static String trimTrailing(String s) {
		for (int i = s.length() - 1; i >= 0; i--) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return s.substring(0, i + 1);
			}
		}

		return StringPool.BLANK;
	}

	public static String wrap(String text) {
		return wrap(text, 80, "\n");
	}

	public static String wrap(String text, int width, String lineSeparator) {
		if (text == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		try {
			BufferedReader br = new BufferedReader(new StringReader(text));

			String s = StringPool.BLANK;

			while ((s = br.readLine()) != null) {
				if (s.length() == 0) {
					sb.append(lineSeparator);
				}
				else {
					String[] tokens = s.split(StringPool.SPACE);
					boolean firstWord = true;
					int curLineLength = 0;

					for (int i = 0; i < tokens.length; i++) {
						if (!firstWord) {
							sb.append(StringPool.SPACE);
							curLineLength++;
						}

						if (firstWord) {
							sb.append(lineSeparator);
						}

						sb.append(tokens[i]);

						curLineLength += tokens[i].length();

						if (curLineLength >= width) {
							firstWord = true;
							curLineLength = 0;
						}
						else {
							firstWord = false;
						}
					}
				}
			}
		}
		catch (IOException ioe) {
			LfwLogger.error(ioe.getMessage(), ioe);
		}

		return sb.toString();
	}
	
	/**
	 * 合并成逗号分隔的形式，即"a", "b"
	 * @param strArr
	 * @return
	 */
	public static String mergeScriptArray(String[] strArr)
	{
		if(strArr == null)
			return null;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < strArr.length; i++) {
			buf.append("\"");
			buf.append(strArr[i]);
			buf.append("\"");
			if(i != strArr.length - 1)
				buf.append(",");
		}
		return buf.toString();
	}
	
	/**
	 * 合并成逗号分隔的形式，即a, b
	 * @param strArr
	 * @return
	 */
	public static String mergeScriptArrayForInteger(int[] strArr)
	{
		if(strArr == null)
			return null;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < strArr.length; i++) {
			buf.append(strArr[i]);
			if(i != strArr.length - 1)
				buf.append(",");
		}
		return buf.toString();
	}
	
	/**
	 * 合并成javascript的数组样式，即["a", "b"]
	 * @param str 以逗号分隔的字符串
	 * @return
	 */
	public static String mergeScriptArray(String str)
	{
		if(str == null)
			return null;
		String[] strArr = str.split(StringPool.COMMA);
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < strArr.length; i++) {
			buf.append("\"");
			buf.append(strArr[i]);
			buf.append("\"");
			if(i != strArr.length - 1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}
	
	
	/**
	 * 合并成javascript的数组样式，即["a", "b"]
	 * @param str 以逗号分隔的字符串
	 * @return
	 */
	public static String mergeScriptArrayStr(String[] strArr){
		if(strArr == null)
			return null;
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < strArr.length; i++) {
			buf.append("\"");
			buf.append(strArr[i]);
			buf.append("\"");
			if(i != strArr.length - 1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}
	
	/**
	 * 合并成javascript的数组样式，即[a, b]
	 * @param strArr 以逗号分隔的字符串
	 * @return
	 */
	public static String mergeScriptArrayStrForInteger(String str)
	{
		if(str == null)
			return null;
		String[] strArr = str.split(StringPool.COMMA);
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < strArr.length; i++) {
			buf.append(strArr[i]);
			if(i != strArr.length - 1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}
	
	/**
	 * 合并成javascript的数组样式，即["a", "b"]
	 * @param strList
	 * @return
	 */
	public static String mergeScriptArray(Collection<String> strList){
		if(strList == null)
			return null;
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		Iterator<String> it = strList.iterator();
		while(it.hasNext()){
			String str = it.next();
			buf.append("\"");
			buf.append(str);
			buf.append("\"");
			if(it.hasNext())
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}
	
	public static String convertToCorrectEncoding(String oriString)
	{
		//RuntimeEnv.getInstance().isRunningInWebSphere() || 
		//weblogic无需转换
		if(oriString == null)
			return null;
		return convertToCorrentEncoding(new String[]{oriString})[0];
	}
	
	public static String[] convertToCorrentEncoding(String[] oriStrs){
		if(oriStrs == null || oriStrs.length == 0)
			return null;
		if(RuntimeEnv.isRunningInWeblogic())
			return oriStrs;
		try{
			for(int i = 0; i < oriStrs.length; i ++){
				oriStrs[i] = new String(oriStrs[i].getBytes("ISO-8859-1"), "UTF-8");
			}
		}
		catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
		}
		return oriStrs;
	}
}

