package nc.uap.lfw.applet;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import nc.sfbase.client.ClientToolKit;

/**
 * NC节点界面
 * @author licza
 *
 */
public class NodeUI extends JPanel
{

    public NodeUI()
    {
        initialize();
    }

    private void initialize()
    {
        setLayout(new BorderLayout());
        initUI();
    }

    public void initUI()
    {
        removeAll();
        JPanel panel = getNodePanel();
        add(panel, BorderLayout.CENTER);
    }

    private JPanel getNodePanel()
    {
        String nodeId = ClientToolKit.getAppletParam("nodeId");
        return new NodeWorkbench(nodeId);
    }

    private static final long serialVersionUID = 0xf4f70cb994a11103L;
}