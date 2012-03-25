package nc.uap.lfw.design.noexport;

import java.util.Map;

import nc.uap.lfw.core.page.PageMeta;

public interface IPageMetaBuidlerForDesign {
	public PageMeta buildPageMeta(Map<String, Object> paramMap);
}
