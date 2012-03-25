package nc.uap.applet;

import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JApplet;
import javax.swing.JFrame;

import nc.starter.vo.NCAppletStubImple;
import nc.uap.lfw.core.log.LfwLogger;
 
public class LfwAppletViewer extends JFrame {
	private static final long serialVersionUID = 6195829231526672595L;
	private String server = null;
	private String port = null;;
	private JApplet applet = null;
	public LfwAppletViewer(String server, String port) throws HeadlessException {
		super();
		this.server = server;
		this.port = port;
		initialize(); 
	}
	private void initialize() {
		getContentPane().setLayout(new BorderLayout());
		try {
			Class<?> objCls = Class.forName("nc.sfbase.applet.NCApplet");
			applet = (JApplet)objCls.newInstance();
			addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosing(WindowEvent e) {
					try{
						applet.destroy();
					}catch(Throwable th){}
					System.exit(0);
				}
				
			});
			AppletStub stub = getAppletStub(server, port);
			if(stub != null){
				((NCAppletStubImple)stub).setParameter("uiclsname", MockUI.class.getName());
				applet.setStub(stub);
			}
			applet.init();
			applet.start();
			getContentPane().add(applet, BorderLayout.CENTER);
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
		
	}
	private AppletStub getAppletStub(String server, String port) throws Exception{
		String urlstr = "http://"+server+":"+port+"/service/ncstarterservlet";
        URL url = new URL(urlstr);
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        PrintWriter pw = null;
        ObjectInputStream ois = null;
        AppletStub stub =null;
        try{
        	pw = new PrintWriter(con.getOutputStream());
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            pw.print("width="+(size.width * 2 / 3) + "&height=" + (size.height * 2 / 3));
            //pw.print("width=500&height=400");
            pw.close();
            pw = null;
            ois = new ObjectInputStream(con.getInputStream());
            stub = (AppletStub) ois.readObject();
         }finally{
        	if(pw != null){
        		pw.close();
        	}
        	if(ois != null){
        		ois.close();
        	}
        }
        return stub;
		
	}


}

