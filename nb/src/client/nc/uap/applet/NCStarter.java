package nc.uap.applet;

import nc.uap.lfw.core.log.LfwLogger;

public class NCStarter {

    public static void main(java.lang.String[] args) {
        try {
    		System.setProperty("sun.swing.enableImprovedDragGesture", "");
    		NCLauncher.main(args);
        } catch (Throwable exception) {
            //System.err.println("javax.swing.JPanel 的 main() 中发生异常");
            //exception.printStackTrace(System.out);
        	LfwLogger.error("javax.swing.JPanel 的 main() 中发生异常");
        }
    }
}