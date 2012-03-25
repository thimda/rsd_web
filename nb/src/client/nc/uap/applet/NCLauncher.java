package nc.uap.applet;

import java.awt.Toolkit;

import javax.swing.JFrame;

 
public class NCLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        if (args != null && args.length == 2) {
            System.setProperty("nc.jstart.server", args[0]);
            System.setProperty("nc.jstart.port", args[1]); 
        }
        String server = System.getProperty("nc.jstart.server");
        String port = System.getProperty("nc.jstart.port");
        if(isNullString(server) ){
        	server= "localhost";
        }
        if( isNullString(port)){
        	port = "80";
        } 
       	startNC(server, port);
	}
	private static boolean isNullString(String str){
		return str == null || str.trim().length() == 0;
	}
	private static void startNC(String server, String port) {
		LfwAppletViewer viewer = new LfwAppletViewer(server,  port);
//		viewer.setSize(((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 2 / 3),
//				((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 2 / 3));
		
		int width =(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 2 / 3;
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 2 / 3;
		
		viewer.setBounds((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - width) / 2, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - height) / 2, width, height);
		viewer.setAlwaysOnTop(true);
		viewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		viewer.setState(JFrame.NORMAL);
//        if (Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH)){
//            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        }
		viewer.setVisible(true);
		
	}

}

