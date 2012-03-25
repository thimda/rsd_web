package nc.uap.lfw.core.page.validate;

import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.page.PageMeta;

public interface IPageMetaValidator {
	public void validate(PageMeta pageMeta) throws LfwValidateException; 
}
