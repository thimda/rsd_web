package nc.uap.lfw.core.cmd;

import java.util.Map;

import nc.uap.lfw.core.cmd.base.UifCommand;

public class UifOpenWindowCmd extends UifCommand{
	private String winId;
	//TODO,这里默认值应该是按照父窗口比例计算
	private String height = "600";
	private String width = "400";
	private String title = "TITLE";
	
	public UifOpenWindowCmd(String winId){
		this.winId = winId;
	}
	
	public UifOpenWindowCmd(String winId, String width, String height){
		this(winId, width, height, null);
	}
	
	public UifOpenWindowCmd(String winId, String width, String height, String title){
		this(winId);
		this.height = height;
		this.width = width;
		this.title = title;
	}
	
	@Override
	public void execute() {
		getLifeCycleContext().getApplicationContext().navgateTo(winId, title, width, height, getWindowParam());
	}

	protected Map<String, String> getWindowParam() {
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

}
