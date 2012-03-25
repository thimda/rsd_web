package nc.uap.lfw.ncadapter.billtemplate.gen;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.bill.BillTempletVO;
import static nc.uap.lfw.ncadapter.billtemplate.BillTemplateConst.*;

/**
 * 单据模板用户tab自定义项和tabitem自定义项解析工具类
 * @author gd 2008-03-04
 *
 */
public class BillTemplateUserDefInfoUtil {
	
	private Map<String, String> attrMap = null;
	
	public BillTemplateUserDefInfoUtil(String info)
	{
		attrMap = parseLfwUserDefInfo(info);
	}
	
	/**
	 * 传入属性名获取属性值
	 * @see nc.lfw.billtemplate.BillTemplateConst 获取属性名
	 * @param attrName
	 * @return
	 */
	public String getAttributeValue(String attrName)
	{
		if(attrName == null || attrName.equals("") || !attrName.startsWith(SELFDEFPRE))
			return null;
		if(attrMap == null)
			return null;
		return attrMap.get(attrName);
	}
	
	/**
	 * 根据tabcode获取TabVo
	 * @param vo
	 * @param tabCode
	 * @return
	 */
	public static BillTabVO getTabVoByTabCode(BillTempletVO vo, String tabCode, boolean isCard)
	{
		BillTabVO[] tabVOs = vo.getHeadVO().getStructvo().getBillTabVOs();
		if(tabVOs != null)
		{
			for(BillTabVO tabVo:tabVOs)
			{
				Integer pos = tabVo.getPos();
				String prefix;
				if(pos.intValue() == 0)
					prefix = "headTab_";
				else if(pos.intValue() == 1)
					prefix = "bodyTab_";
				else
					prefix = "tailTab_";
				if(isCard)
					prefix += "card_";
				else 
					prefix += "list_";
				String key = prefix + tabVo.getTabcode();
				if(key.equals(tabCode))
					return tabVo;
			}
		}
		return null;
	}
	
	private Map<String, String> parseLfwUserDefInfo(String info)
	{
		if(info == null || info.trim().equals(""))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		// 以逗号分割获取配置的所有名值对信息
		String[] nameValuePairs = info.split(",");
		if(nameValuePairs != null)
		{
			for(String pair:nameValuePairs)
			{
				String[] nameValue = pair.split(":");
				// 以"lfw_"开头则认为是轻量级的设置
				if(nameValue.length == 2 && nameValue[0].startsWith(SELFDEFPRE))
					map.put(nameValue[0], nameValue[1]);
			}
		}
		return map;
	}
}
