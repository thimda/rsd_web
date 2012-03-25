package nc.uap.lfw.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.BitSet;

import nc.uap.lfw.core.exception.LfwRuntimeException;
/**
 * URL编码类。来自于java.net.URLEncoder.此类和java.net.URLEncoder的编码方式区别在于将' '转换为%20而不是
 * '+'以便于前台js能够通过decodeURLComponent正确转换回来
 * @author dengjt
 *
 */
public class JsURLEncoder {
	private static BitSet dontNeedEncoding;

	private static final int caseDiff = ('a' - 'A');

	static {
		dontNeedEncoding = new BitSet(256);
		int i;
		for (i = 'a'; i <= 'z'; i++) {
			dontNeedEncoding.set(i);
		}
		for (i = 'A'; i <= 'Z'; i++) {
			dontNeedEncoding.set(i);
		}
		for (i = '0'; i <= '9'; i++) {
			dontNeedEncoding.set(i);
		}
		dontNeedEncoding.set('-');
		dontNeedEncoding.set('_');
		dontNeedEncoding.set('.');
		dontNeedEncoding.set('*');
	}

	/**
	 * You can't call the constructor.
	 */
	private JsURLEncoder() {
	}

	
	public static String encode(String s, String enc){
		try{
			boolean needToChange = false;
			boolean wroteUnencodedChar = false;
			int maxBytesPerChar = 10; // rather arbitrary limit, but safe for now
			StringBuffer out = new StringBuffer(s.length());
			ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
	
			OutputStreamWriter writer = new OutputStreamWriter(buf, enc);
	
			for (int i = 0; i < s.length(); i++) {
				int c = (int) s.charAt(i);
				if (dontNeedEncoding.get(c)) {
					out.append((char) c);
					wroteUnencodedChar = true;
				} else {
					// convert to external encoding before hex conversion
					try {
						if (wroteUnencodedChar) { // Fix for 4407610
							writer = new OutputStreamWriter(buf, enc);
							wroteUnencodedChar = false;
						}
						writer.write(c);
						/*
						 * If this character represents the start of a Unicode
						 * surrogate pair, then pass in two characters. It's not
						 * clear what should be done if a bytes reserved in the
						 * surrogate pairs range occurs outside of a legal surrogate
						 * pair. For now, just treat it as if it were any other
						 * character.
						 */
						if (c >= 0xD800 && c <= 0xDBFF) {
							/*
							 * System.out.println(Integer.toHexString(c) + " is high
							 * surrogate");
							 */
							if ((i + 1) < s.length()) {
								int d = (int) s.charAt(i + 1);
								/*
								 * System.out.println("\tExamining " +
								 * Integer.toHexString(d));
								 */
								if (d >= 0xDC00 && d <= 0xDFFF) {
									/*
									 * System.out.println("\t" +
									 * Integer.toHexString(d) + " is low
									 * surrogate");
									 */
									writer.write(d);
									i++;
								}
							}
						}
						writer.flush();
					} catch (IOException e) {
						buf.reset();
						continue;
					}
					byte[] ba = buf.toByteArray();
					for (int j = 0; j < ba.length; j++) {
						out.append('%');
						char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
						// converting to use uppercase letter as part of
						// the hex value if ch is a letter.
						if (Character.isLetter(ch)) {
							ch -= caseDiff;
						}
						out.append(ch);
						ch = Character.forDigit(ba[j] & 0xF, 16);
						if (Character.isLetter(ch)) {
							ch -= caseDiff;
						}
						out.append(ch);
					}
					buf.reset();
					needToChange = true;
				}
			}
	
			return (needToChange ? out.toString() : s);
		}
		catch(Exception e){
			throw new LfwRuntimeException(e);
		}
	}
}
