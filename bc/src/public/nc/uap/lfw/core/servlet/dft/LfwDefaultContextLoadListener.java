package nc.uap.lfw.core.servlet.dft;

import javax.servlet.ServletContextEvent;

import nc.uap.lfw.core.servlet.LfwContextLoaderListenerBase;

/**
 * Lfw context ���ؼ�����,��ģ��ɴ����̳�
 * 
 * @author
 * 
 */
public class LfwDefaultContextLoadListener extends LfwContextLoaderListenerBase {

	public void contextDestroyed(ServletContextEvent ctxEvent) {
		super.contextDestroyed(ctxEvent);
	}

	public void contextInitialized(ServletContextEvent ctxEvent) {
		super.contextInitialized(ctxEvent);
	}

}
