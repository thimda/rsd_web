package nc.uap.lfw.core.serializer;

import java.util.Map;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.serializer.impl.Xml2DatasetSerializer;

public class Xml2SingleDatasetSerializer implements IXml2ObjectSerializer<Dataset> {

	public Dataset serialize(String xml, Map<String, Object> paramMap) {
		Dataset[] dss = new Xml2DatasetSerializer().serialize(xml, paramMap);
		return dss[0];
	}

}
