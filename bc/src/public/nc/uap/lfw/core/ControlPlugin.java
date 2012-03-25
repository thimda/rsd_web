package nc.uap.lfw.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器Plugin,为各种url提供控制器适配
 * @author dengjt
 *
 */
public interface ControlPlugin {
	public void handle(HttpServletRequest req, HttpServletResponse res, String path) throws Exception;
}
