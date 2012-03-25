/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package nc.uap.lfw.ca.jdt.internal.compiler.lookup;

import nc.uap.lfw.ca.jdt.internal.compiler.lookup.Binding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ProblemReasons;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ReferenceBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding;

public class ProblemMethodBinding extends MethodBinding {
    
	private int problemReason;
	public MethodBinding closestMatch; // TODO (philippe) should rename into #alternateMatch
	
public ProblemMethodBinding(char[] selector, TypeBinding[] args, int problemReason) {
	this.selector = selector;
	this.parameters = (args == null || args.length == 0) ? Binding.NO_PARAMETERS : args;
	this.problemReason = problemReason;
	this.thrownExceptions = Binding.NO_EXCEPTIONS;
}
public ProblemMethodBinding(char[] selector, TypeBinding[] args, ReferenceBinding declaringClass, int problemReason) {
	this.selector = selector;
	this.parameters = (args == null || args.length == 0) ? Binding.NO_PARAMETERS : args;
	this.declaringClass = declaringClass;
	this.problemReason = problemReason;
	this.thrownExceptions = Binding.NO_EXCEPTIONS;
}
public ProblemMethodBinding(MethodBinding closestMatch, char[] selector, TypeBinding[] args, int problemReason) {
	this(selector, args, problemReason);
	this.closestMatch = closestMatch;
	if (closestMatch != null && problemReason != ProblemReasons.Ambiguous) this.declaringClass = closestMatch.declaringClass;
}
/* API
* Answer the problem id associated with the receiver.
* NoError if the receiver is a valid binding.
*/

public final int problemId() {
	return this.problemReason;
}
}
