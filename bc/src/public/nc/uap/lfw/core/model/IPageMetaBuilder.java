package nc.uap.lfw.core.model;

import java.util.Map;

import nc.uap.lfw.core.page.PageMeta;
/**
 * 页面元数据构造器
 * @author dengjt
 *
 */
public interface IPageMetaBuilder {
	/**
	 * 构造页面对象
	 * @param paramMap  扩展信息参数
	 * @return
	 */
	public PageMeta buildPageMeta(Map<String, Object> paramMap);
}
