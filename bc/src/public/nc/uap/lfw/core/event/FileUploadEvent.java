package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.FileUploadComp;

/**
 * @author guoweic
 *
 */
public class FileUploadEvent extends AbstractServerEvent<FileUploadComp> {

	public FileUploadEvent(FileUploadComp webElement) {
		super(webElement);
	}

}
