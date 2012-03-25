/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import nc.uap.lfw.ca.jdt.internal.compiler.ast.ASTNode;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.QualifiedTypeReference;
import nc.uap.lfw.ca.jdt.internal.compiler.impl.Constant;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.Binding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.BlockScope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ClassScope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.PackageBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.Scope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding;



public class JavadocQualifiedTypeReference extends QualifiedTypeReference {

	public int tagSourceStart, tagSourceEnd;
	public PackageBinding packageBinding;

	public JavadocQualifiedTypeReference(char[][] sources, long[] pos, int tagStart, int tagEnd) {
		super(sources, pos);
		this.tagSourceStart = tagStart;
		this.tagSourceEnd = tagEnd;
		this.bits |= ASTNode.InsideJavadoc;
	}

	/*
	 * We need to modify resolving behavior to handle package references
	 */
	private TypeBinding internalResolveType(Scope scope, boolean checkBounds) {
		// handle the error here
		this.constant = Constant.NotAConstant;
		if (this.resolvedType != null) // is a shared type reference which was already resolved
			return this.resolvedType.isValidBinding() ? this.resolvedType : this.resolvedType.closestMatch(); // already reported error

		TypeBinding type = this.resolvedType = getTypeBinding(scope);
		// End resolution when getTypeBinding(scope) returns null. This may happen in
		// certain circumstances, typically when an illegal access is done on a type 
		// variable (see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=204749)
		if (type == null) return null;		
		if (!type.isValidBinding()) {
			Binding binding = scope.getTypeOrPackage(this.tokens);
			if (binding instanceof PackageBinding) {
				this.packageBinding = (PackageBinding) binding;
			} else {
				reportInvalidType(scope);
			}
			return null;
		}
		if (isTypeUseDeprecated(type, scope))
			reportDeprecatedType(type, scope);
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=209936
		// raw convert all enclosing types when dealing with Javadoc references
		if (type.isGenericType() || type.isParameterizedType()) {
			this.resolvedType = scope.environment().convertToRawType(type, true /*force the conversion of enclosing types*/);
		}		
		return this.resolvedType;
	}
	protected void reportDeprecatedType(TypeBinding type, Scope scope) {
		scope.problemReporter().javadocDeprecatedType(type, this, scope.getDeclarationModifiers());
	}

	protected void reportInvalidType(Scope scope) {
		scope.problemReporter().javadocInvalidType(this, this.resolvedType, scope.getDeclarationModifiers());
	}
	public TypeBinding resolveType(BlockScope blockScope, boolean checkBounds) {
		return internalResolveType(blockScope, checkBounds);
	}

	public TypeBinding resolveType(ClassScope classScope) {
		return internalResolveType(classScope, false);
	}

	/* (non-Javadoc)
	 * Redefine to capture javadoc specific signatures
	 * @see eclipse.jdt.internal.compiler.ast.ASTNode#traverse(eclipse.jdt.internal.compiler.ASTVisitor, eclipse.jdt.internal.compiler.lookup.BlockScope)
	 */
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}

	public void traverse(ASTVisitor visitor, ClassScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
}
