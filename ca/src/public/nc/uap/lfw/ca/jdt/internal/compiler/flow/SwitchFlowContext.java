package nc.uap.lfw.ca.jdt.internal.compiler.flow;

import nc.uap.lfw.ca.jdt.internal.compiler.ast.ASTNode;
import nc.uap.lfw.ca.jdt.internal.compiler.codegen.BranchLabel;

/**
 * Reflects the context of code analysis, keeping track of enclosing
 *	try statements, exception handlers, etc...
 */
public class SwitchFlowContext extends FlowContext {
	
	public BranchLabel breakLabel;
	public UnconditionalFlowInfo initsOnBreak = FlowInfo.DEAD_END;
	
public SwitchFlowContext(FlowContext parent, ASTNode associatedNode, BranchLabel breakLabel) {
	super(parent, associatedNode);
	this.breakLabel = breakLabel;
}

public BranchLabel breakLabel() {
	return breakLabel;
}

public String individualToString() {
	StringBuffer buffer = new StringBuffer("Switch flow context"); //$NON-NLS-1$
	buffer.append("[initsOnBreak -").append(initsOnBreak.toString()).append(']'); //$NON-NLS-1$
	return buffer.toString();
}

public boolean isBreakable() {
	return true;
}

public void recordBreakFrom(FlowInfo flowInfo) {
	if ((initsOnBreak.tagBits & FlowInfo.UNREACHABLE) == 0) {
		initsOnBreak = initsOnBreak.mergedWith(flowInfo.unconditionalInits());
	} 
	else {
		initsOnBreak = flowInfo.unconditionalCopy();
	}
}
}
