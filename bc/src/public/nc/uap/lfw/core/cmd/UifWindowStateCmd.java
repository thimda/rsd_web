package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;

/**
 * ���ô��������Ƿ��Ѿ����༭������������ֹ���ڹر��¼�
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
