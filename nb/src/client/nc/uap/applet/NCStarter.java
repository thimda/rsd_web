package nc.uap.applet;

import nc.uap.lfw.core.log.LfwLogger;

public class NCStarter {

    public static void main(java.lang.String[] args) {
        try {
    		System.setProperty("sun.swing.enableImprovedDragGesture", "");
    		NCLauncher.main(args);
        } catch (Throwable exception) {
            //System.err.println("javax.swing.JPanel �� main() �з����쳣");
            //exception.printStackTrace(System.out);
        	LfwLogger.error("javax.swing.JPanel �� main() �з����쳣");
        }
    }
}