package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;

/**
 * 设置窗口内容是否已经被编辑，可以用来阻止窗口关闭事件
 * 
 * @author licza
 * 
 */
public class UifWindowStateCmd extends UifCommand {
	private Boolean hasChanged;

	public UifWindowStateCmd(Boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	@Override
	public void execute() {
		StringBuffer buf = new StringBuffer("pageUI.setChanged(");
		buf.append(this.hasChanged);
		buf.append(");\n");
		String script = buf.toString();
		getLifeCycleContext().getApplicationContext().addExecScript(script);
	}

}
