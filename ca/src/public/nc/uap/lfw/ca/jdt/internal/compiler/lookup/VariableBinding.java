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
package nc.uap.lfw.ca.jdt.internal.compiler.lookup;


import nc.uap.lfw.ca.jdt.internal.compiler.ast.ASTNode;
import nc.uap.lfw.ca.jdt.internal.compiler.classfmt.ClassFileConstants;
import nc.uap.lfw.ca.jdt.internal.compiler.impl.Constant;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.Binding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TagBits;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding;


public abstract class VariableBinding extends Binding {
    
	public int modifiers;
	public TypeBinding type;
	public char[] name;
	protected Constant constant;
	public int id; // for flow-analysis (position in flowInfo bit vector)
	public long tagBits;

	public VariableBinding(char[] name, TypeBinding type, int modifiers, Constant constant) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
		this.constant = constant;
		if (type != null) {
			this.tagBits |= (type.tagBits & TagBits.HasMissingType);
		}
	}
	
	public Constant constant() {
		return this.constant;
	}

	public abstract AnnotationBinding[] getAnnotations();

	public final boolean isBlankFinal(){
		return (modifiers & ExtraCompilerModifiers.AccBlankFinal) != 0;
	}
	/* Answer true if the receiver is final and cannot be changed
	*/
	
	public final boolean isFinal() {
		return (modifiers & ClassFileConstants.AccFinal) != 0;
	}
	public char[] readableName() {
		return name;
	}
	public void setConstant(Constant constant) {
		this.constant = constant;
	}
	public String toString() {
		StringBuffer output = new StringBuffer(10);
		ASTNode.printModifiers(this.modifiers, output);
		if ((this.modifiers & ExtraCompilerModifiers.AccUnresolved) != 0) {
			output.append("[unresolved] "); //$NON-NLS-1$
		}
		output.append(type != null ? type.debugName() : "<no type>"); //$NON-NLS-1$
		output.append(" "); //$NON-NLS-1$
		output.append((name != null) ? new String(name) : "<no name>"); //$NON-NLS-1$
		return output.toString();
	}
}
