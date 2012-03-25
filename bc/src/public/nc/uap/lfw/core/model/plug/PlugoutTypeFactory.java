package nc.uap.lfw.core.model.plug;

public final class PlugoutTypeFactory {
	public static IPlugoutType getPlugoutType(String type){
		if (type == null)
			return null;
		String plugoutType = type.split("\\.")[1];
		if(plugoutType.equals(IPlugoutType.TYPE_DATASET_SEL_ROW)){
			return new PlugoutTypeDatasetSelRow();
		}
		else if (plugoutType.equals(IPlugoutType.TYPE_DATASET_MUTL_SEL_ROW)){
			return new PlugoutTypeDatasetMutlSelRow();
		}
		else if (plugoutType.equals(IPlugoutType.TYPE_DATASET_ALL_ROW)){
			return new PlugoutTypeDatasetAllRow();
		}
//		else if (plugoutType.equals(IPlugoutType.TYPE_COMPONENT_VALUE)){
//			return new PlugoutTypeComponentValue();
//		}
		return null;
	}
	
	
}
