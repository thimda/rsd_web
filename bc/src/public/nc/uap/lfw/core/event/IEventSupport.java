package nc.uap.lfw.core.event;

import java.util.List;

import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.JsEventDesc;

/**
 * 事件支持接口，适用于新版APP应用
 * @author dengjt
 *
 */
public interface IEventSupport {
	public void addEventConf(EventConf event);
	public void removeEventConf(String eventName, String method);
	public EventConf[] getEventConfs();
	public List<JsEventDesc> getAcceptEventDescs();
	public String getId();
}
