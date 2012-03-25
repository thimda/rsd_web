package nc.uap.lfw.applet;

import java.awt.BorderLayout;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.swing.JPanel;

import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.desktop.ui.WorkbenchEvent;
import nc.desktop.ui.WorkbenchListener;
import nc.desktop.ui.console.NCConsoleFrame;
import nc.funcnode.bs.FuncletModel;
import nc.funcnode.bs.IOpenNodeRCService;
import nc.funcnode.ui.FuncletContext;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWidgetContainer;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.login.vo.AttachedProps;
import nc.login.vo.NCSession;
import nc.message.itf.IMessageQueryService;
import nc.message.msgcenter.event.IMessageTypeInfo;
import nc.message.msgcenter.event.IStateChangeProcessor;
import nc.message.msgtype.vo.MsgTypeCache;
import nc.message.msgtype.vo.MsgTypeVO;
import nc.message.vo.NCMessage;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.org.GroupVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.funcreg.FuncRegisterVO;
/**
 * NC节点工作区
 * @author licza
 *
 */
public class NodeWorkbench extends JPanel
    implements WorkbenchListener
{

    public NodeWorkbench(String nodeId)
    {
       	LfwLogger.debug("Nodeid:" + nodeId);
        if(nodeId.indexOf(",") > 0){
        	String[] para = nodeId.split(",");
        	this.funcCode = para[0];
        	this.param = para[1];
        }else{
        	this.funcCode = nodeId;
        }
        NCConsoleFrame.getInstance().redirectOutputStreamAsNeed();
        initUI();
        NCConsoleFrame.getInstance().setDefaultLogLevel();
    }

    protected FuncRegisterVO getFuncRegisterVO()
    {
        IFuncRegisterQueryService qryService = (IFuncRegisterQueryService)NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
        FuncRegisterVO reg = null;
        try
        {
            String funcCode = getFuncCode();
            reg = qryService.queryFunctionByCode(funcCode);
        }
        catch(BusinessException e)
        {
           LfwLogger.error(e.getMessage(), e);
        }
        return reg;
    }

    private String getFuncCode()
    {
        return funcCode;
    }

    public void enterWorkbench(WorkbenchEvent workbenchevent)
    {
    }

    public void logoutWorkbench(WorkbenchEvent workbenchevent)
    {
    }

    protected void initUI()
    {
        setLayout(new BorderLayout());
        Object aobj[];
        try
        {
        	if(funcCode != null && funcCode.length() > 1 ){
                FuncRegisterVO reg = getFuncRegisterVO();
                FuncletModel funcletModel = getFuncModel(reg);
                FuncletContext context = new FuncletContext(reg);
                if(funcletModel != null)
                    context.setFuncletModel(funcletModel);
                Class c = FuncletWidgetContainer.class;
                Constructor cons = c.getConstructor(new Class[] {FuncRegisterVO.class});
                FuncletWidgetContainer fwContainer = (FuncletWidgetContainer)cons.newInstance(new Object[] {
                    reg
                });
                Method m = c.getDeclaredMethod("initUI", new Class[] {
                     FuncletModel.class,  FuncletInitData.class
                });
                m.setAccessible(true);
                aobj = new Object[2];
                aobj[0] = funcletModel;
                m.invoke(fwContainer, aobj);
                add(fwContainer, "Center");
        	}else{
        		if(this.param != null && this.param.length() > 0){
                    try {
                    	IMessageQueryService service = (IMessageQueryService)NCLocator.getInstance().lookup(IMessageQueryService.class);
                    	LfwLogger.debug("Query Message by pk:" + param);
                    	NCMessage msg = service.queryNCMessageByPk(param);
                    	String apptype = msg.getMessage().getMsgsourcetype();
                		MsgTypeVO msgtype = MsgTypeCache.getInstance().getMsgTypeByCode(apptype);
                		if (msgtype != null && msgtype.getClickaction() != null && msgtype.getClickaction().trim().length() > 0) {
                			try {
                				LfwLogger.debug("Message Click Action :" + msgtype.getClickaction());
                				IMessageTypeInfo msgop = (IMessageTypeInfo) Class.forName(msgtype.getClickaction()).newInstance();
                				msgop.processMsgOpen(msg, this ,new IStateChangeProcessor(){
        							public void processStateChange(NCMessage msg) {
        								
        							}});
                			} catch (Exception e1) {
                				Logger.error(e1.getMessage(), e1);
                			}
                		}
        			} catch (Exception e) {
        				LfwLogger.error(e.getMessage(), e);
        			}
        		}
        	}
        }
        catch(BusinessException e)
        {
            LfwLogger.error(e.getMessage(), e);
        }
        catch(Exception e)
        {
            LfwLogger.error(e.getMessage(), e);
        }
    }

    protected FuncletModel getFuncModel(FuncRegisterVO reg)
        throws Exception
    {
        FuncletModel funcletModel = null;
        AttachedProps response = fetchOpenNodeRCResponse(reg);
        funcletModel = (FuncletModel)response.getAttachedProp(FuncletModel.class);
        return funcletModel;
    }

    private AttachedProps fetchOpenNodeRCResponse(FuncRegisterVO frVO)
        throws Exception
    {
        AttachedProps request = getOpenNodeRCRequest(frVO);
        AttachedProps response;
        IOpenNodeRCService service = (IOpenNodeRCService)NCLocator.getInstance().lookup(IOpenNodeRCService.class);
        response = service.unitedOpenNodeRC(request);
        return response;
    }

    private AttachedProps getOpenNodeRCRequest(FuncRegisterVO frVO)
    {
        WorkbenchEnvironment env = WorkbenchEnvironment.getInstance();
        String userId = env.getLoginUser().getPrimaryKey();
        NCSession session = env.getSession();
        GroupVO groupVO = env.getGroupVO();
        String dsName = "";
        BusiCenterVO bcVO = env.getLoginBusiCenter();
        if(bcVO != null)
            dsName = bcVO.getDataSourceName();
        AttachedProps request = new AttachedProps();
        request.putAttachProp(frVO);
        request.putAttachProp("dsName", dsName);
        request.putAttachProp("funcWindowType", Integer.valueOf(0));
        request.putAttachProp("userID", userId);
        request.putAttachProp("sessionID", session.getSessionID());
        request.putAttachProp("pkGroup", groupVO != null ? ((Object) (groupVO.getPk_group())) : "");
        request.putAttachProp(Integer.valueOf(env.getUserType()));
        return request;
    }

    private String funcCode = null;
    private String param = null;
    private static final long serialVersionUID = 0x76a0294b38ead48aL;
}