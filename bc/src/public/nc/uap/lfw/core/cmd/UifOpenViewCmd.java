package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;

/**
 * "编辑"菜单逻辑处理,弹出指定的ViewID
 *
 */
public class UifOpenViewCmd extends UifCommand {

	private String viewId;
	//TODO,这里默认值应该是按照父窗口比例计算
	private String height = "600";
	private String width = "400";
	private String title = "TITLE";
	public UifOpenViewCmd(String viewId){
		this.viewId = viewId;
	}
	
	public UifOpenViewCmd(String viewId, String width, String height){
		this(viewId, width, height, null);
	}
	
	public UifOpenViewCmd(String viewId, String width, String height, String title) {
		this(viewId);
		this.height = height;
		this.width = width;
		this.title = title;
	}

	@Override
	public void execute() {
		getLifeCycleContext().getWindowContext().popView(viewId, width, height, title);
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
