package nc.uap.lfw.core.page.validate;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.page.PageMeta;

public class DefaultPageMetaValidator implements IPageMetaValidator {

	public void validate(PageMeta pageMeta) throws LfwValidateException {
		//首先校验是否能序列化，否则集群下会出现问题
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream writer = new ObjectOutputStream(out);
			writer.writeObject(pageMeta);
		}
		catch(Exception e){
			throw new LfwValidateException("当前PageMeta不能序列化");
		}
	}

}
