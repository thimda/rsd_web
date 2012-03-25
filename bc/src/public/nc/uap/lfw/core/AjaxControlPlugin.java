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
 * ����Ajax����ĺ��Ĵ����߼�
 * @author dengjt
 *
 */
public class AjaxControlPlugin implements ControlPlugin {
	private static final String ENCODING = "UTF-8";
	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res,
			String path) throws Exception {
		String[] results = null;
		//���ؿͻ��˵��Ѻ���Ϣ��
		String expText = "";
		//���ؿͻ��˵����Ǵ�����Ϣ
		String expMsg = "";
		//���ؿͻ��˵��쳣ջ
		String expStack = "";
		//�����Ƿ�ɹ��ı�ʾ�����ڿͻ���ͳһ�쳣����
		String success = "true";
		try {
			req.setCharacterEncoding(ENCODING);
			res.setCharacterEncoding(ENCODING);
			//����Ajax����ͳһ��װ��xml����
			res.setContentType("text/xml");
			RequestLifeCycleContext appCtx = new RequestLifeCycleContext();
			RequestLifeCycleContext.set(appCtx);
			appCtx.setPhase(LifeCyclePhase.ajax);
			
			PageMeta pageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
			if(pageMeta == null){
				LfwLogger.error("û�л��PageMeta");
				return;
			}
			String processClazz = pageMeta.getProcessorClazz();
			if(processClazz == null || processClazz.trim().equals("")){
				if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE))
					processClazz = "nc.uap.lfw.core.event.AppRequestProcessor";
				else
					processClazz = EventRequestProcessor.class.getName();
				//throw new LfwRuntimeException("û��������ȷ��ҵ������");
			}
			
			Class<?> c = Class.forName(processClazz);
			IRequestProcessor processor = (IRequestProcessor) c.newInstance();
			//����ҵ���߼��������
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
		//��׽�����쳣����װ�ɹ̶�xml�����ڿͻ���ͳһ�쳣����
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
		
		//���췵�ش�
		Document doc = XmlUtilPatch.getNewDocument();
		Element rootNode = doc.createElement("xml");
		doc.appendChild(rootNode);
		
		Element expTextNode = doc.createElement("exp-text");
		expTextNode.appendChild(doc.createTextNode(expText));
		rootNode.appendChild(expTextNode);
		
		Element msgNode = doc.createElement("show-message");
		msgNode.appendChild(doc.createTextNode(expMsg == null?"�������̳����쳣":expMsg));
		rootNode.appendChild(msgNode);
		
		Element stackNode = doc.createElement("exp-stack");
		//content�������CDATA���ͽڵ��У�����firefox�����4096���ȵ����ƣ��Ӷ��ص�Ӧ����ʾ�Ĳ��֡�
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
		
		//ֱ����ָ������д���������
		XMLUtil.printDOMTree(res.getWriter(), doc, 0, ENCODING);
	}

}
