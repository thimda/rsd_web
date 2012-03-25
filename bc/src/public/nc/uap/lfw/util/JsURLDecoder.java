package nc.uap.lfw.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;

public class JsURLDecoder {
	public static String decode(String source, String enc){
		try {
			return URLDecoder.decode(source, enc);
		} catch (UnsupportedEncodingException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
}
