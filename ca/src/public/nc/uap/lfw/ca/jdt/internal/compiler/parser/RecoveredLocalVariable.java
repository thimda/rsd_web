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
package nc.uap.lfw.ca.jdt.internal.compiler.parser;

/**
 * Internal local variable structure for parsing recovery 
 */
import nc.uap.lfw.ca.jdt.internal.compiler.ast.ASTNode;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.Annotation;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.ArrayTypeReference;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.Expression;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.LocalDeclaration;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.Statement;
import nc.uap.lfw.ca.jdt.internal.compiler.parser.RecoveredElement;
import nc.uap.lfw.ca.jdt.internal.compiler.parser.RecoveredStatement;


public class RecoveredLocalVariable extends RecoveredStatement {

	public RecoveredAnnotation[] annotations;
	public int annotationCount;
	
	public int modifiers;
	public int modifiersStart;
	
	public LocalDeclaration localDeclaration;
	boolean alreadyCompletedLocalInitialization;
public RecoveredLocalVariable(LocalDeclaration localDeclaration, RecoveredElement parent, int bracketBalance){
	super(localDeclaration, parent, bracketBalance);
	this.localDeclaration = localDeclaration;
	this.alreadyCompletedLocalInitialization = localDeclaration.initialization != null;
}
/*
 * Record an expression statement if local variable is expecting an initialization expression. 
 */
public RecoveredElement add(Statement stmt, int bracketBalanceValue) {

	if (this.alreadyCompletedLocalInitialization || !(stmt instanceof Expression)) {
		return super.add(stmt, bracketBalanceValue);
	} else {
		this.alreadyCompletedLocalInitialization = true;
		this.localDeclaration.initialization = (Expression)stmt;
		this.localDeclaration.declarationSourceEnd = stmt.sourceEnd;
		this.localDeclaration.declarationEnd = stmt.sourceEnd;
		return this;
	}
}
public void attach(RecoveredAnnotation[] annots, int annotCount, int mods, int modsSourceStart) {
	if (annotCount > 0) {
		Annotation[] existingAnnotations = this.localDeclaration.annotations;
		if (existingAnnotations != null) {
			this.annotations = new RecoveredAnnotation[annotCount];
			this.annotationCount = 0;
			next : for (int i = 0; i < annotCount; i++) {
				for (int j = 0; j < existingAnnotations.length; j++) {
					if (annots[i].annotation == existingAnnotations[j]) continue next;
				}
				this.annotations[this.annotationCount++] = annots[i];
			}
		} else {
			this.annotations = annots;
			this.annotationCount = annotCount;
		}
	}
	
	if (mods != 0) {
		this.modifiers = mods;
		this.modifiersStart = modsSourceStart;
	}
}
/* 
 * Answer the associated parsed structure
 */
public ASTNode parseTree(){
	return localDeclaration;
}
/*
 * Answer the very source end of the corresponding parse node
 */
public int sourceEnd(){
	return this.localDeclaration.declarationSourceEnd;
}
public String toString(int tab) {	
	return tabString(tab) + "Recovered local variable:\n" + localDeclaration.print(tab + 1, new StringBuffer(10)); //$NON-NLS-1$
}
public Statement updatedStatement(){
	/* update annotations */
	if (modifiers != 0) {
		this.localDeclaration.modifiers |= modifiers;
		if (this.modifiersStart < this.localDeclaration.declarationSourceStart) {
			this.localDeclaration.declarationSourceStart = modifiersStart;
		}
	}
	/* update annotations */
	if (annotationCount > 0){
		int existingCount = localDeclaration.annotations == null ? 0 : localDeclaration.annotations.length;
		Annotation[] annotationReferences = new Annotation[existingCount + annotationCount];
		if (existingCount > 0){
			System.arraycopy(localDeclaration.annotations, 0, annotationReferences, annotationCount, existingCount);
		}
		for (int i = 0; i < annotationCount; i++){
			annotationReferences[i] = annotations[i].updatedAnnotationReference();
		}
		localDeclaration.annotations = annotationReferences;
		
		int start = this.annotations[0].annotation.sourceStart;
		if (start < this.localDeclaration.declarationSourceStart) {
			this.localDeclaration.declarationSourceStart = start;
		}
	}
	return localDeclaration;
}
/*
 * A closing brace got consumed, might have closed the current element,
 * in which case both the currentElement is exited.
 *
 * Fields have no associated braces, thus if matches, then update parent.
 */
public RecoveredElement updateOnClosingBrace(int braceStart, int braceEnd){
	if (bracketBalance > 0){ // was an array initializer
		bracketBalance--;
		if (bracketBalance == 0) alreadyCompletedLocalInitialization = true;
		return this;
	}
	if (parent != null){
		return parent.updateOnClosingBrace(braceStart, braceEnd);
	}
	return this;
}
/*
 * An opening brace got consumed, might be the expected opening one of the current element,
 * in which case the bodyStart is updated.
 */
public RecoveredElement updateOnOpeningBrace(int braceStart, int braceEnd){
	if (localDeclaration.declarationSourceEnd == 0 
		&& (localDeclaration.type instanceof ArrayTypeReference || localDeclaration.type instanceof ArrayQualifiedTypeReference)
		&& !alreadyCompletedLocalInitialization){
		bracketBalance++;
		return null; // no update is necessary	(array initializer)
	}
	// might be an array initializer
	this.updateSourceEndIfNecessary(braceStart - 1, braceEnd - 1);	
	return this.parent.updateOnOpeningBrace(braceStart, braceEnd);	
}
public void updateParseTree(){
	this.updatedStatement();
}
/*
 * Update the declarationSourceEnd of the corresponding parse node
 */
public void updateSourceEndIfNecessary(int bodyStart, int bodyEnd){
	if (this.localDeclaration.declarationSourceEnd == 0) {
		this.localDeclaration.declarationSourceEnd = bodyEnd;
		this.localDeclaration.declarationEnd = bodyEnd;	
	}
}
}
