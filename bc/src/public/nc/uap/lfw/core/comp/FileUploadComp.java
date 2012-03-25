package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.FileUploadContext;
import nc.uap.lfw.core.event.conf.FileListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;

/**
 * 文件上传组件
 * 
 * @author guoweic
 *
 */
public class FileUploadComp extends WebComponent {

	private static final long serialVersionUID = 7174343297374477986L;
	
	// 是否有Form
	private boolean withForm = true;
	// 文件保存处理类
	private String handler = null;
	// 默认数量
	private int defaultSize = 1;
	// 最大数量
	private int maxSize = 1;
	// 是否允许增加
	private boolean allowAdd = true;
	
	public boolean isWithForm() {
		return withForm;
	}
	public void setWithForm(boolean withForm) {
		this.withForm = withForm;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public int getDefaultSize() {
		return defaultSize;
	}
	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public boolean isAllowAdd() {
		return allowAdd;
	}
	public void setAllowAdd(boolean allowAdd) {
		this.allowAdd = allowAdd;
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(FileListener.class);
		return list;
	}
	
	@Override
	public BaseContext getContext() {
		FileUploadContext ctx = new FileUploadContext();
		ctx.setId(this.getId());
		ctx.setEnabled(this.enabled);
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		FileUploadContext fuCtx = (FileUploadContext) ctx;
		this.setEnabled(fuCtx.isEnabled());
		this.setCtxChanged(false);
	}
	
	
	
}
