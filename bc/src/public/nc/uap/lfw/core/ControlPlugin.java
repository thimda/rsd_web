package nc.uap.lfw.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ������Plugin,Ϊ����url�ṩ����������
 * @author dengjt
 *
 */
public interface ControlPlugin {
	public void handle(HttpServletRequest req, HttpServletResponse res, String path) throws Exception;
}
