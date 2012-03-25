package nc.uap.lfw.core.model.parser;

import java.io.IOException;
import java.io.InputStream;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNode;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;
/**
 * 参照节点解析
 * @author dengjt
 *
 */
public class RefNodePoolParser {
	
	
	public static IRefNode parse(InputStream input) {

		Digester digester = new Digester();
		digester.setValidating(false);
		initRules(digester);
		try {
			return (IRefNode) digester.parse(input);
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} catch (SAXException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		finally{
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				LfwLogger.error(e);
			}
		}
	}

	private static void initRules(Digester digester) {
		String refNodeConfClassName = RefNode.class.getName();
		digester.addObjectCreate("RefNode", refNodeConfClassName);
		digester.addSetProperties("RefNode");
	
//		String paramRefNodeConfClassName = ParamRefNode.class.getName();
//		digester.addObjectCreate("ParamRefNode", paramRefNodeConfClassName);
//		digester.addSetProperties("ParamRefNode");
	}

}
