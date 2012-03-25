package nc.uap.lfw.core.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WidgetElement;

/**
 * DataSetRelation的集合对象
 * 
 * @author dengjt
 *
 */
public class DatasetRelations extends WidgetElement{
	
	private static final long serialVersionUID = 1L;
	private List<DatasetRelation> relationList = new ArrayList<DatasetRelation>();
	
	/**
	 * 判断某个DataSet是否是作为子DataSet出现的。
	 * @param id
	 * @return
	 */
	public boolean isChildDataset(String id)
	{
		for(Iterator<DatasetRelation> iter = relationList.iterator();iter.hasNext();)
		{
			if(iter.next().getDetailDataset().trim().equals(id))
			   return true;
		}
		return false;
	}
	
	/**
	 * 根据子表获取主表dsId
	 * @param detailDsId
	 * @return
	 */
	public String getMasterDsByDetailDs(String detailDsId)
	{
		for(Iterator<DatasetRelation> iter = relationList.iterator();iter.hasNext();)
		{
			DatasetRelation dr = iter.next();
			if(dr.getDetailDataset().trim().equals(detailDsId))
			   return dr.getMasterDataset();
		}
		return null;
	}
//
//	/**
//	 * 获取数据集关系列表
//	 * @return
//	 */
//	priva List<DatasetRelation> getRelationList() {
//		return relationList;
//	}
	
	public DatasetRelation[] getDsRelations() {
		return relationList == null? new DatasetRelation[0] : relationList.toArray(new DatasetRelation[0]);
	}

	public void removeDsRelation(DatasetRelation rel){
		relationList.remove(rel);
	}
	/**
	 * 设置数据集关系列表
	 * @param relationList
	 */
	public void setRelationList(List<DatasetRelation> relationList) {
		this.relationList = relationList;
	}
	
	/**
	 * 添加数据集关系对象
	 * @param relation
	 */
	public void addDsRelation(DatasetRelation relation)
	{
		relationList.add(relation);
	}
	
	/**
	 * 获取这些dsRelation中的所有主表Dataset id数组
	 * @return
	 */
	public String[] getMasterDatasetIds()
	{
		Set<String> set = new HashSet<String>();
		/*将一主多子的情况记录为一个主记录*/
		for(int i = 0;i < relationList.size(); i++)
		{
			set.add(relationList.get(i).getMasterDataset());
		}
		
		return set.toArray(new String[0]);		
	}
	
	/**
	 * 判断一个dsId 是否是主表
	 * @param dsId
	 * @return
	 */
	public boolean isMasterDataset(String dsId)
	{
		for(Iterator<DatasetRelation> iter = relationList.iterator(); iter.hasNext();)
		{
			if(iter.next().getMasterDataset().trim().equals(dsId.trim()))
				return true;			
		}
		
		return false;
	}

	/**
	 * 挑出给定的ds中，处于主表位置的Dataset id
	 * @param dsIds
	 * @return
	 */
	public String getMasterDataset(String[] dsIds) {
		if(dsIds == null || dsIds.length == 0)
			return null;
		String[] masterDsIds = getMasterDatasetIds();
		if(masterDsIds == null || masterDsIds.length == 0)
			return null;
		List<String> masterDsList = new ArrayList<String>();
		for (int i = 0; i < dsIds.length; i++) {
			for (int j = 0; j < masterDsIds.length; j++) {
				if(dsIds[i].equals(masterDsIds[j]))
					masterDsList.add(dsIds[i]);
			}
		}
		if(masterDsList.size() == 0)
			return null;
		else if(masterDsList.size() == 1)
			return masterDsList.get(0);
		else{
			String temp1 = masterDsList.get(0);
			String temp2 = masterDsList.get(1);
			DatasetRelation dsRelation = getDsRelation(temp1, temp2);
			if(dsRelation == null)
				return temp2;
			else
				return temp1;
		}
	}
	/**
	 * 获取某个主表的所有子表dataset id
	 * @param masterId
	 * @return
	 */
	public List<String> getAllChildIds(String masterId)
	{
		List<String> list = new ArrayList<String>();
		DatasetRelation dsRelation;
		for(Iterator<DatasetRelation> iter = relationList.iterator(); iter.hasNext();)
		{
			dsRelation = iter.next();
			if(dsRelation.getMasterDataset().trim().equals(masterId.trim()))
				list.add(dsRelation.getDetailDataset());					
		}
		return list;
	}
	
	/**
	 * 根据主子Dataset id获取对应的主子关系对象。
	 * @param masterDsId
	 * @param detailDsId
	 * @return
	 */
	public DatasetRelation getDsRelation(String masterDsId, String detailDsId)
	{
		String ds1,ds2;
		for(int i = 0;i < relationList.size(); i++)
		{
			ds1 = relationList.get(i).getMasterDataset();
			ds2 = relationList.get(i).getDetailDataset();
			if(ds1.trim().equals(masterDsId.trim()) && ds2.trim().equals(detailDsId.trim()))
				return relationList.get(i);
		}
		return null;
	} 
	
	/**
	 * 获取该主表的所有dsRelation
	 * @param masterDsId
	 * @return
	 */
	public DatasetRelation[] getDsRelations(String masterDsId)
	{
		List<DatasetRelation> list = new ArrayList<DatasetRelation>();
		for(DatasetRelation dsRelation : relationList)
			if(dsRelation.getMasterDataset().trim().equals(masterDsId.trim()))
				list.add(dsRelation);
		return list.toArray(new DatasetRelation[0]);
	}
	
	public Object clone()
	{
		DatasetRelations dsRelation = (DatasetRelations)super.clone();
		dsRelation.relationList = new ArrayList<DatasetRelation>();
		for(DatasetRelation relation : this.relationList)
		{
			dsRelation.relationList.add((DatasetRelation)relation.clone());
		}
		return dsRelation;
	}

	public void addDsRelations(DatasetRelations dsrelations) {
		if(dsrelations != null){
			this.relationList.addAll(dsrelations.relationList);
		}
	}

	@Override
	public void mergeProperties(WebElement ele) {
		
	}
}

