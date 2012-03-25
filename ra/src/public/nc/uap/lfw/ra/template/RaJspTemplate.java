package nc.uap.lfw.ra.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.jsp.uimeta.UIMeta;

import org.apache.commons.lang.StringUtils;

/**
 * @author renxh
 * 生成jsp模板的工具类
 *
 */
public class RaJspTemplate {
	private UIMeta uimeta;
	
	public RaJspTemplate(UIMeta uimeta){
		this.uimeta = uimeta;
	}


	/* (non-Javadoc)
	 * @see nc.uap.lfw.jsp.parser.Convert2JspUtil#convert2Jsp(java.lang.String)
	 */
	public String convert2Jsp() {
		String source = convertMeta2Jsp(uimeta);
		ClassLoader loader = getClass().getClassLoader();
		InputStream input = loader.getResourceAsStream("html/nodes/template/template.jsp");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			StringBuffer result = new StringBuffer();
			String str = reader.readLine();
			while (str != null) {
				result.append(str);
				str = reader.readLine();
			}
			String rt = result.toString();
			// 增加import标签
			String importStr = "<lfw:import ";
			if (uimeta != null) {
				if (uimeta.isJquery() == 1)
					importStr += " jquery = 'true' ";

//				if (uimeta.isJsEditor() == 1)
//					importStr += " jsEditor = 'true' ";

				// excel属性
//				if (uimeta.isJsExcel() == 1)
//					importStr += " jsExcel = 'true' ";

				if (StringUtils.isNotEmpty(uimeta.getIncudecss())) {
					importStr += " includeCss = '" + uimeta.getIncudecss() + "' ";
				}
				if (StringUtils.isNotEmpty(uimeta.getIncudejs())) {
					importStr += " includeJs = '" + uimeta.getIncudejs() + "' ";
				}
				
				importStr += " type='App' ";
			}
			importStr += "/>";
			rt = rt.replace("$#IMPORT#$", importStr);
			rt = rt.replace("$#BODY#$", source);
			return rt;
		} catch (UnsupportedEncodingException e) {
			throw new LfwParseException(e.getMessage(), e);
		} catch (IOException e) {
			throw new LfwParseException(e.getMessage(), e);
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					LfwLogger.error(e.getMessage(), e);
				}
		}
	}

	/**
	 * 2011-8-2 下午08:05:07 renxh
	 * des：将元数据生成标签
	 * @param meta
	 * @return
	 */
	private String convertMeta2Jsp(UIMeta meta) {
		StringBuffer buf = new StringBuffer();
		buf.append(" <lfw:pageView />\n");
		return buf.toString();
	}

}
