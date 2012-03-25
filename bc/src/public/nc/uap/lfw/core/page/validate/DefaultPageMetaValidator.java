package nc.uap.lfw.core.page.validate;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.page.PageMeta;

public class DefaultPageMetaValidator implements IPageMetaValidator {

	public void validate(PageMeta pageMeta) throws LfwValidateException {
		//����У���Ƿ������л�������Ⱥ�»��������
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream writer = new ObjectOutputStream(out);
			writer.writeObject(pageMeta);
		}
		catch(Exception e){
			throw new LfwValidateException("��ǰPageMeta�������л�");
		}
	}

}
