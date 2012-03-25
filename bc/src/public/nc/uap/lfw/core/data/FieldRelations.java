package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldRelations implements Serializable {
	private static final long serialVersionUID = 7251502658543636597L;
	private Map<String, FieldRelation> fieldRelations = new LinkedHashMap<String, FieldRelation>();
	public void addFieldRelation(FieldRelation rel){
		this.fieldRelations.put(rel.getId(), rel);
	}
	
	public void removeFieldRelation(String relId){
		fieldRelations.remove(relId);
	}
	
	public FieldRelation getFieldRelation(String relationId) {
		return fieldRelations.get(relationId);
	}
	public FieldRelation[] getFieldRelations(){
			return fieldRelations.values().toArray(new FieldRelation[0]);
	}
}
