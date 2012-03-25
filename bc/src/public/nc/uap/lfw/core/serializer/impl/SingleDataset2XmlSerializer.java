package nc.uap.lfw.core.serializer.impl;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.serializer.IObject2XmlSerializer;

public class SingleDataset2XmlSerializer implements IObject2XmlSerializer<Dataset> {

	public String[] serialize(Dataset obj) {
		return new Dataset2XmlSerializer().serialize(new Dataset[]{obj});
	}
}
