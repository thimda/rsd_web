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

import nc.uap.lfw.ca.jdt.internal.compiler.impl.Constant;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.FieldBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ReferenceBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TagBits;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding;


public class SyntheticFieldBinding extends FieldBinding {
	
	public int index;
	
	public SyntheticFieldBinding(char[] name, TypeBinding type, int modifiers, ReferenceBinding declaringClass, Constant constant, int index) {
		super(name, type, modifiers, declaringClass, constant);
		this.index = index;
		this.tagBits |= (TagBits.AnnotationResolved | TagBits.DeprecatedAnnotationResolved);
	}
}
