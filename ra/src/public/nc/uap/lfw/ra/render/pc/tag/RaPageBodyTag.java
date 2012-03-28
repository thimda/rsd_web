package nc.uap.lfw.ra.render.pc.tag;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.ClientSession;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.BasePageModel;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.model.util.LfwMaskerUtil;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.core.tags.SimpleTagWithAttribute;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;

import org.json.MarshallException;

public class RaPageBodyTag extends SimpleTagWithAttribute {
	protected static final String DS_SCRIPT = "dsScript";
	protected static final String ALL_SCRIPT = "allScript";
	public static final String CS_PRE = "$cs_";
	
	/**
	 * Tag类调用入口
	 */
	public void doTag() throws JspException, IOException {
		doRender();
	}
	
	protected void doRender() throws JspException, IOException {
		BasePageModel model = (BasePageModel) getJspContext().getAttribute("pageModel");
		try {
			long startTime = 0;
			if(Logger.isInfoEnabled())
				startTime = System.currentTimeMillis();
			
//			if(showLoadingPage){
//				getJspContext().getOut().write(addLoadingPage());
//				//getJspContext().getOut().flush();
//			}
			
			
			//获得此页面的PageMeta
			PageMeta pageMeta = model.getPageMeta();
			
			//此Buf中的内容，将能够独立形成脚本文件
			StringBuffer scriptBuf = new StringBuffer(4096);
			getPageContext().setAttribute(ALL_SCRIPT, scriptBuf);
			
			//控件绑定ds的逻辑统一放在此处。最终合并到scriptBuf中，之所以这么做，是因为tree等画节点的时候需要完整的width信息。而只有
			//所有控件都渲染完成，这种信息才能由浏览器补充完整。所以将设置数据放在最后进行。注意：所有缓画的控件，例如TabItem和CardLayout等
			//都可能需要自己处理这种缓画策略
			StringBuffer dsScriptBuf = new StringBuffer();
//			getJspContext().setAttribute(DS_SCRIPT, dsScriptBuf);
			
			
			//页面初始化函数,所有js逻辑包含其中
			scriptBuf.append("function pageBodyScript(){\n");
			
			//加入masker信息
			LfwMaskerUtil.makeMaskerScript(scriptBuf);
			
			PageContext pageContext = (PageContext) getJspContext();
			Iterator it = pageContext.getRequest().getParameterMap().entrySet().iterator();
			
			scriptBuf.append(createPageUI(pageMeta));
			//将所有请求参数植入全局请求参数Map
			scriptBuf.append("window.$paramsMap = new HashMap();\n");
			
			while(it.hasNext())
			{
				Entry entry = (Entry) it.next();
				scriptBuf.append("setParameter(\"")
				         .append(entry.getKey())
				         .append("\",\"");
				         String str = StringUtil.convertToCorrectEncoding(((String[])entry.getValue())[0]); 
		         scriptBuf.append(JsURLEncoder.encode(str, "UTF-8")) 
				         .append("\");\n");
			}
			
			//render client attribute
			renderClientSessionAttribute(model, scriptBuf);
			
			if(pageMeta.getCaption() != null)
				scriptBuf.append("document.title = \"")
						 .append(pageMeta.getCaption())
						 .append("\";\n");
			
			Map<String, ExtAttribute> attrMap = pageMeta.getExtendMap();
			if(attrMap != null && !attrMap.isEmpty())
			{
				Iterator<String> attrIt = attrMap.keySet().iterator();
				while(attrIt.hasNext())
				{
					String attrName = attrIt.next();
					scriptBuf.append("pageUI.addAttribute('" + attrName + "', '" + attrMap.get(attrName) + "');\n");
				}
			}
		
			
			if(getJspBody() != null)
				getJspBody().invoke(getJspContext().getOut());

			//将dsScript的设置脚本加入页面脚本中
			scriptBuf.append(dsScriptBuf.toString());
			
			scriptBuf.append("if(typeof(externalInit) != 'undefined'){\n externalInit();\n}\n");
			scriptBuf.append("pageUI.$afterPageInit();\n");
			
			// 添加ClientEnvironment
			
			scriptBuf.append("pageUI.$beforeInitData();\n")
					 .append("pageUI.$beforeActive();\n");
			
			
			scriptBuf.append("layoutInitFunc();\n");
			
			scriptBuf.append("window.renderDone = true;\n");
			scriptBuf.append("if(window.editMode){document.body.className='unselectable'}\n");
			scriptBuf.append("\n");
			scriptBuf.append("}\n");
			
			addDynamicScript(scriptBuf);
			
			
			if(Logger.isInfoEnabled()){
				long endTime = System.currentTimeMillis();
				Logger.info("初始化PageMode所用时间:" + (endTime - startTime) + "ms");
			}
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			if(model != null)
				model.destroy();
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			throw new LfwRuntimeException(e);
		}
	}
	
	private void addDynamicScript(StringBuffer scriptBuf) throws IOException {
		if (!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION))
			System.out.println(scriptBuf.toString());
		String script = "<script>\n" + scriptBuf.toString() + "</script>";
		getJspContext().getOut().write(script);
	}
	
	
	private void renderClientSessionAttribute(BasePageModel model, StringBuffer scriptBuf) {
		try {
			ClientSession cs = model.getClientSession();
			if(cs != null) {
				Map<String, Serializable> map = cs.getAttributeMap();
				if(map != null && map.size() > 0) {
					LfwJsonSerializer jsonSerialzer = LfwJsonSerializer.getInstance();
					String jsonStr = jsonSerialzer.toJSON(map);
					scriptBuf.append(CS_PRE)
					         .append("clientSession = ")
					         .append(jsonStr)
					         .append(";\n");
				}
				Map<String, Serializable> stickMap = cs.getStickAttributeMap();
				if(stickMap != null && stickMap.size() > 0){
					Iterator<Entry<String, Serializable>> it = stickMap.entrySet().iterator();
					scriptBuf.append(CS_PRE)
							 .append("clientStickKeys = '");
					while(it.hasNext()){
						Entry<String, Serializable> entry = it.next();
						if(entry.getValue() == null){
							Logger.warn("client session value is null, key is:" + entry.getKey());
							continue;
						}
						scriptBuf.append(entry.getKey())
								 .append("=")
								 .append(JsURLEncoder.encode(entry.getValue().toString(), "UTF-8"));
						if(it.hasNext())
							scriptBuf.append("&");
					}
					scriptBuf.append("';\n");
				}
			}
		} 
		catch (MarshallException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "PageModelTag-000000")/*初始化客户端记录对象出现问题:*/ + e.getMessage());
		}
	}

	private String createPageUI(PageMeta pm) {
		StringBuffer buf = new StringBuffer();
		String caption = pm.getCaption();
		if(caption == null)
			caption = "";
		buf.append("window.pageUI = new PageUI('")
		   .append(pm.getId())
		   .append("','")
		   .append(pm.getCaption())
		   .append("');\n");

		buf.append("getApplication().addPageUI('")
		   .append(pm.getId())
		   .append("', window.pageUI);\n");
		
//		String eventStr = addEventSupport(pm, null, "pageUI", null);
//		buf.append(eventStr);
		return buf.toString();
	}
}
