package nc.uap.lfw.design.itf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.log.LfwLogger;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class MenifestParser {

	public static void initMenifestRules(Digester digester){
		digester.addObjectCreate("Manifest", BCPManifest.class.getName());
		digester.addSetProperties("Manifest");
		
		String modelClazz = BusinessComponent.class.getName();
		digester.addObjectCreate("Manifest/BusinessComponet", modelClazz);
		digester.addSetProperties("Manifest/BusinessComponet");
		digester.addSetNext("Manifest/BusinessComponet", "addBusinessComponent", modelClazz);
	}
	
	
	public static BCPManifest parse(InputStream input, String dirPath) {
		if(input == null)
			return null;
		Digester digester = new Digester();
		digester.setValidating(false);
		initMenifestRules(digester);
		try {
			BCPManifest menifest = (BCPManifest)digester.parse(input);
			return menifest;
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		} catch (SAXException e) {
			LfwLogger.error(e.getMessage(), e);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		finally{
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	public static BCPManifest getMenifest(String confPath){
		InputStream input = null;
		try {
			input = new FileInputStream(confPath);
			BCPManifest manifest = parse(input, null);
			return manifest;
		} 
		catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		finally{
			if(input != null)
				try {
					input.close();
				} 
				catch (IOException e) {
					Logger.error(e);
				}
		}
		return null;
	}
	
}
