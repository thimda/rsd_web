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

import nc.uap.lfw.ca.jdt.internal.compiler.ASTVisitor;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.CompoundAssignment;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.Expression;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.*;


public class PrefixExpression extends CompoundAssignment {

/**
 * PrefixExpression constructor comment.
 * @param lhs eclipse.jdt.internal.compiler.ast.Expression
 * @param expression eclipse.jdt.internal.compiler.ast.Expression
 * @param operator int
 */
public PrefixExpression(Expression lhs, Expression expression, int operator, int pos) {
	super(lhs, expression, operator, lhs.sourceEnd);
	this.sourceStart = pos;
	this.sourceEnd = lhs.sourceEnd;
}

public String operatorToString() {
	switch (this.operator) {
		case PLUS :
			return "++"; //$NON-NLS-1$
		case MINUS :
			return "--"; //$NON-NLS-1$
	} 
	return "unknown operator"; //$NON-NLS-1$
}

public StringBuffer printExpressionNoParenthesis(int indent, StringBuffer output) {

	output.append(operatorToString()).append(' ');
	return this.lhs.printExpression(0, output); 
} 

public boolean restrainUsageToNumericTypes() {
	return true;
}

public void traverse(ASTVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		this.lhs.traverse(visitor, scope);
	}
	visitor.endVisit(this, scope);
}
}