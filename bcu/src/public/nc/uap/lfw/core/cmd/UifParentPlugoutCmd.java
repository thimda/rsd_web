package nc.uap.lfw.core.cmd;

import java.util.Map;

import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.WindowContext;

public class UifParentPlugoutCmd extends UifCommand {
	private String plugoutId;
	private String widgetId;
	private Map<String, Object> paramMap;
	
	/**
	 * 触发父window的plugout
	 * 
	 */
	public UifParentPlugoutCmd(String widgetId, String plugoutId){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
	}
	
	
	/**
	 * 触发父window的plugout
	 * 
	 */
	public UifParentPlugoutCmd(String widgetId, String plugoutId, Map<String, Object> paramMap){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.paramMap = paramMap;
	}
	
	@Override
	public void execute() {
		WindowContext windowctx = getLifeCycleContext().getWindowContext();
		getLifeCycleContext().getApplicationContext().addPlug(windowctx.getId() + "_" + widgetId + "_" + plugoutId, this.paramMap);
	
		StringBuffer scriptBuf = new StringBuffer();
		scriptBuf.append("parent.triggerPlugout('").append(this.widgetId).append("','").append(this.plugoutId).append("');\n");
		getLifeCycleContext().getApplicationContext().addExecScript(scriptBuf.toString());
	}
}
