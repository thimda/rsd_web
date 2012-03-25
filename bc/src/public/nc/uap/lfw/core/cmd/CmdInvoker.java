package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.ICommand;

public final class CmdInvoker {
	public static void invoke(ICommand cmd){
		cmd.execute();
	}
	
	public static void invoke(ICommand... cmds){
		//transaction
	}
}
