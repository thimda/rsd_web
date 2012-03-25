package nc.uap.lfw.ca.jdt.internal.compiler.flow;

import nc.uap.lfw.ca.jdt.core.compiler.CharOperation;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.ASTNode;
import nc.uap.lfw.ca.jdt.internal.compiler.codegen.BranchLabel;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.BlockScope;

/**
 * Reflects the context of code analysis, keeping track of enclosing
 *	try statements, exception handlers, etc...
 */
public class LabelFlowContext extends SwitchFlowContext {
	
	public char[] labelName;
	
public LabelFlowContext(FlowContext parent, ASTNode associatedNode, char[] labelName, BranchLabel breakLabel, BlockScope scope) {
	super(parent, associatedNode, breakLabel);
	this.labelName = labelName;
	checkLabelValidity(scope);
}

void checkLabelValidity(BlockScope scope) {
	// check if label was already defined above
	FlowContext current = parent;
	while (current != null) {
		char[] currentLabelName;
		if (((currentLabelName = current.labelName()) != null)
			&& CharOperation.equals(currentLabelName, labelName)) {
			scope.problemReporter().alreadyDefinedLabel(labelName, associatedNode);
		}
		current = current.parent;
	}
}

public String individualToString() {
	return "Label flow context [label:" + String.valueOf(labelName) + "]"; //$NON-NLS-2$ //$NON-NLS-1$
}

public char[] labelName() {
	return labelName;
}
}
