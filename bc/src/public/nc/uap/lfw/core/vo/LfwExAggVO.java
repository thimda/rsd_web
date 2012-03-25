package nc.uap.lfw.core.vo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IExAggVO;
/**
 * Lfw通用聚合VO，在LFW中，所有聚合VO都看做是一主多子，为了适配NC遗留模型，继续继承原有接口
 * 
 * @author dengjt
 * 
 */
public class LfwExAggVO extends AggregatedValueObject implements IExAggVO {
	private static final long serialVersionUID = -1027173950850626238L;
	private SuperVO parentVO;
	private Map<String, SuperVO[]> childrenMap = new HashMap<String, SuperVO[]>();
	public SuperVO getParentVO() {
		return parentVO;
	}
	public void setParentVO(SuperVO parentVO) {
		this.parentVO = parentVO;
	}
	public void setChildrenVOs(String code, SuperVO[] children) {
		childrenMap.put(code, children);
	}
	public SuperVO[] getChildrenVOs(String code) {
		return childrenMap.get(code);
	}
	public String[] getChildrenCodes() {
		return childrenMap.keySet().toArray(new String[0]);
	}
	@Override public CircularlyAccessibleValueObject[] getChildrenVO() {
		return getTableVO(getDefaultTableCode());
	}
	@Override public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		setTableVO(getDefaultTableCode(), children);
	}
	@Override public void setParentVO(CircularlyAccessibleValueObject parent) {
		this.parentVO = (SuperVO) parent;
	}
	public CircularlyAccessibleValueObject[] getAllChildrenVO() {
		List<SuperVO> list = new ArrayList<SuperVO>();
		Iterator<Entry<String, SuperVO[]>> it = childrenMap.entrySet().iterator();
		while (it.hasNext()) {
			SuperVO[] vos = it.next().getValue();
			if (vos == null)
				continue;
			list.addAll(Arrays.asList(vos));
		}
		return list.toArray(new SuperVO[0]);
	}
	public SuperVO[] getChildVOsByParentId(String tableCode, String parentid) {
		throw new LfwRuntimeException("no need to implement");
	}
	public String getDefaultTableCode() {
		return getTableCodes()[0];
	}
	@SuppressWarnings("unchecked") public HashMap getHmEditingVOs() throws Exception {
		throw new LfwRuntimeException("no need to implement");
	}
	public String getParentId(SuperVO item) {
		throw new LfwRuntimeException("no need to implement");
	}
	public String[] getTableCodes() {
		return childrenMap.keySet().toArray(new String[0]);
	}
	public String[] getTableNames() {
		return null;
	}
	public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
		return childrenMap.get(tableCode);
	}
	public void setParentId(SuperVO item, String id) {
		throw new LfwRuntimeException("no need to implement");
	}
	public void setTableVO(String tableCode, CircularlyAccessibleValueObject[] values) {
		childrenMap.put(tableCode, (SuperVO[]) values);
	}
}
