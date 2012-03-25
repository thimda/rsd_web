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
package nc.uap.lfw.ca.jdt.internal.compiler.ast;

import nc.uap.lfw.ca.jdt.internal.compiler.flow.FlowContext;
import nc.uap.lfw.ca.jdt.internal.compiler.flow.FlowInfo;
import nc.uap.lfw.ca.jdt.internal.compiler.impl.Constant;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.BlockScope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding;

public abstract class Literal extends Expression {

	public Literal(int s, int e) {

		sourceStart = s;
		sourceEnd = e;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {
			
		return flowInfo;
	}

	public abstract void computeConstant();

	public abstract TypeBinding literalType(BlockScope scope);

	public StringBuffer printExpression(int indent, StringBuffer output){
	
		return output.append(source());
	 }
	 
	public TypeBinding resolveType(BlockScope scope) {
		// compute the real value, which must range its type's range
		this.resolvedType = literalType(scope);

		// in case of error, constant did remain null
		computeConstant();
		if (constant == null) {
			scope.problemReporter().constantOutOfRange(this, this.resolvedType);
			constant = Constant.NotAConstant;
		}
		return this.resolvedType;
	}

	public abstract char[] source();
}
