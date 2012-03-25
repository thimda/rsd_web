package nc.uap.lfw.core.event;

import java.util.List;

import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.JsEventDesc;

/**
 * �¼�֧�ֽӿڣ��������°�APPӦ��
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
