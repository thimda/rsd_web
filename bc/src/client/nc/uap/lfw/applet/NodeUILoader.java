package nc.uap.lfw.applet;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import nc.sfbase.client.ClientToolKit;
import nc.sfbase.client.NCAppletStub;
import nc.uap.lfw.core.log.LfwLogger;
/**
 * NC节点界面加载器
 * @author licza
 *
 */
public class NodeUILoader
{

    public NodeUILoader()
    {
        final String ssokey = ClientToolKit.getAppletParam("ssoKey");
        final String nodeId = ClientToolKit.getAppletParam("nodeId");
        if(ClientToolKit.isEmptyStr(ssokey))
        {
            throw new NullPointerException("单点登陆验证串为空!");
        } else
        {
        	if(nodeId != null && nodeId.length() > 0){
        		Runnable run = new Runnable() {
        			public void run() {
        				(new nc.login.sso.ui.SSOLoader()).ssoLogin(ssokey);
        				initNodeUI();
        			}
        		};
        		SwingUtilities.invokeLater(run);
        		return;
        	}else{
        		(new nc.login.sso.ui.SSOLoader()).ssoLogin(ssokey);
        		initNodeUI();
        	}
        }
    }

    public static void initNodeUI()
    {
        NCAppletStub appletStub = NCAppletStub.getInstance();
        JApplet applet = ClientToolKit.getApplet();
        Container container = applet.getContentPane();
        container.removeAll();
        container.setLayout(new BorderLayout());
        try
        {
            java.awt.Component comp = new NodeUI();
            appletStub.setUIComponent(comp);
            container.add(comp, BorderLayout.CENTER);
        }
        catch(Throwable e)
        {
            LfwLogger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        container.validate();
    }
}