package nc.uap.lfw.core.model.parser;

import java.io.IOException;
import java.io.InputStream;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.design.itf.IDatasetProvider;
import nc.uap.lfw.util.JsURLDecoder;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;


/**
 *
 */
public class WidgetParser {

	public static LfwWidget parse(InputStream input) {
		Digester digester = new Digester();
		digester.setValidating(false);
		ParserRules.initWidgetRules(digester);
		try {
			LfwWidget conf = (LfwWidget)digester.parse(input);
			Dataset[] datasets = conf.getViewModels().getDatasets();
			for (int i = 0; i < datasets.length; i++) {
				Dataset ds = datasets[i];
				if(ds instanceof MdDataset){
					MdDataset mdds = (MdDataset) ds;
					IDatasetProvider provider = NCLocator.getInstance().lookup(IDatasetProvider.class);
					provider.mergeDataset(mdds);
				}
				Field[] fields = ds.getFieldSet().getFields();
				if(fields != null){
					for (int j = 0; j < fields.length; j++) {
						Field field = fields[j];
						if(field.getEditFormular() != null && !"".equals(field.getEditFormular()))
							field.setEditFormular(JsURLDecoder.decode(field.getEditFormular(), "UTF-8"));
						if(field.getValidateFormula() != null && !"".equals(field.getValidateFormula()))
							field.setValidateFormula(JsURLDecoder.decode(field.getValidateFormula(), "UTF-8"));
						if(field.getFormater() != null && !"".equals(field.getFormater()))
							field.setFormater(JsURLDecoder.decode(field.getFormater(), "UTF-8"));
						field.setCtxChanged(false);
					}
				}
			}
			return conf;
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} catch (SAXException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		finally{
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		}
	}
}
