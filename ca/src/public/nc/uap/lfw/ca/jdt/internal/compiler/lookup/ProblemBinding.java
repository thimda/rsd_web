package nc.uap.lfw.ca.jdt.internal.compiler.lookup;

import nc.uap.lfw.ca.jdt.core.compiler.CharOperation;

public class ProblemBinding extends Binding {
	public char[] name;
	public ReferenceBinding searchType;
	private int problemId;
// NOTE: must only answer the subset of the name related to the problem

public ProblemBinding(char[][] compoundName, int problemId) {
	this(CharOperation.concatWith(compoundName, '.'), problemId);
}
// NOTE: must only answer the subset of the name related to the problem

public ProblemBinding(char[][] compoundName, ReferenceBinding searchType, int problemId) {
	this(CharOperation.concatWith(compoundName, '.'), searchType, problemId);
}
ProblemBinding(char[] name, int problemId) {
	this.name = name;
	this.problemId = problemId;
}
ProblemBinding(char[] name, ReferenceBinding searchType, int problemId) {
	this(name, problemId);
	this.searchType = searchType;
}
/* API
* Answer the receiver's binding type from Binding.BindingID.
*/

public final int kind() {
	return VARIABLE | TYPE;
}
/* API
* Answer the problem id associated with the receiver.
* NoError if the receiver is a valid binding.
*/

public final int problemId() {
	return problemId;
}
public char[] readableName() {
	return name;
}
}
