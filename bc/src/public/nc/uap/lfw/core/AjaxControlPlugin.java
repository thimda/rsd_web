package nc.uap.lfw.core;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.exception.InteractionInfo;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.AppTypeUtil;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.core.processor.EventRequestProcessor;
import nc.uap.lfw.core.processor.IRequestProcessor;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * 处理Ajax请求的核心处理逻辑
 * @author dengjt
 *
 */
public class AjaxControlPlugin implements ControlPlugin {
	private static final String ENCODING = "UTF-8";
	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res,
			String path) throws Exception {
		String[] results = null;
		//传回客户端的友好信息。
		String expText = "";
		//传回客户端的真是错误信息
		String expMsg = "";
		//传回客户端的异常栈
		String expStack = "";
		//操作是否成功的表示。便于客户端统一异常处理
		String success = "true";
		try {
			req.setCharacterEncoding(ENCODING);
			res.setCharacterEncoding(ENCODING);
			//所有Ajax请求统一封装成xml传回
			res.setContentType("text/xml");
			RequestLifeCycleContext appCtx = new RequestLifeCycleContext();
			RequestLifeCycleContext.set(appCtx);
			appCtx.setPhase(LifeCyclePhase.ajax);
			
			PageMeta pageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
			if(pageMeta == null){
				LfwLogger.error("没有获得PageMeta");
				return;
			}
			String processClazz = pageMeta.getProcessorClazz();
			if(processClazz == null || processClazz.trim().equals("")){
				if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE))
					processClazz = "nc.uap.lfw.core.event.AppRequestProcessor";
				else
					processClazz = EventRequestProcessor.class.getName();
				//throw new LfwRuntimeException("没有设置正确的业务处理类");
			}
			
			Class<?> c = Class.forName(processClazz);
			IRequestProcessor processor = (IRequestProcessor) c.newInstance();
			//进行业务逻辑代码调用
			results = processor.doProcess();
		}
		catch(LfwInteractionException e){
			InteractionInfo info = e.getInfo();
			String infoJson = LfwJsonSerializer.getInstance().toJsObject(info);
			results = new String[]{infoJson};
			success = "interaction";
		}
		catch(LfwRuntimeException e)
		{
			expText = e.getMessage();
			expMsg = e.getHint();
			StringWriter sw = new StringWriter();
			expStack = sw.toString();
			success = "false";
		}
		//捕捉所有异常。封装成固定xml，便于客户端统一异常处理
		catch(Throwable e)
		{
			LfwLogger.error(e.getMessage(), e);
			expText = e.getMessage();
			expMsg = expText;
			StringWriter sw = new StringWriter();
			expStack = sw.toString();
			success = "false";
		}finally{
			RequestLifeCycleContext.set(null);
			//ThreadTracer.getInstance().endThreadMonitor();
		}
		
		//构造返回串
		Document doc = XmlUtilPatch.getNewDocument();
		Element rootNode = doc.createElement("xml");
		doc.appendChild(rootNode);
		
		Element expTextNode = doc.createElement("exp-text");
		expTextNode.appendChild(doc.createTextNode(expText));
		rootNode.appendChild(expTextNode);
		
		Element msgNode = doc.createElement("show-message");
		msgNode.appendChild(doc.createTextNode(expMsg == null?"操作过程出现异常":expMsg));
		rootNode.appendChild(msgNode);
		
		Element stackNode = doc.createElement("exp-stack");
		//content必须放在CDATA类型节点中，否则firefox会出现4096长度的限制，从而截掉应该显示的部分。
		stackNode.appendChild(doc.createCDATASection(expStack));
		rootNode.appendChild(stackNode);
		
		
		Element successNode = doc.createElement("success");
		successNode.appendChild(doc.createTextNode(success));
		rootNode.appendChild(successNode);
		
		Element contentsNode = doc.createElement("contents");
		rootNode.appendChild(contentsNode);
		
		if(results != null)
		{
			for (int i = 0; i < results.length; i++) {
				Element contentNode = doc.createElement("content");
				contentNode.appendChild(doc.createCDATASection(results[i]));
				contentsNode.appendChild(contentNode);
			}
		}
		
		//直接以指定编码写出到输出流
		XMLUtil.printDOMTree(res.getWriter(), doc, 0, ENCODING);
	}

}
