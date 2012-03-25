package nc.uap.lfw.core.model.util;
import java.util.Map;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.model.IDatasetBuilder;
import nc.uap.lfw.design.itf.IDatasetProvider;
public class DefaultMdDatasetBuilder implements IDatasetBuilder {
	public Dataset buildDataset(Dataset ds, Map<String, Object> paramMap) {
		MdDataset mdDs = (MdDataset) ds;
		NCLocator.getInstance().lookup(IDatasetProvider.class).getMdDataset(mdDs);
		return mdDs;
	}
}
