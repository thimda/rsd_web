package nc.uap.lfw.jsutil.jstools;

import java.io.InputStream;

public final class CompressorUtil {
	public static String compressJs(InputStream[] inputs){
		JsTextCompressor compressor = new JsTextCompressor();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < inputs.length; i++) {
			String content = JsDataUtil.getStrFromFile(inputs[i]);
			String cpContent = compressor.compress(content);
			buf.append(cpContent);
			if(i != inputs.length -1)
				buf.append("\n");
		}
		return buf.toString();
	}
	
	public static String compressCss(InputStream[] inputs){
		CssTextCompressor compressor = new CssTextCompressor();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < inputs.length; i++) {
			String content = JsDataUtil.getStrFromFile(inputs[i]);
			String cpContent = compressor.compress(content);
			buf.append(cpContent);
			if(i != inputs.length -1)
				buf.append("\n");
		}
		return buf.toString();
	}
}
