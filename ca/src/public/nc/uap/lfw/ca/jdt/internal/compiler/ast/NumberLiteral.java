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

import nc.uap.lfw.ca.jdt.internal.compiler.ast.Literal;

public abstract class NumberLiteral extends Literal {

	char[] source;
	
	public NumberLiteral(char[] token, int s, int e) {
		
		this(s,e) ;
		source = token ;
	}
	
	public NumberLiteral(int s, int e) {
		super (s,e) ;
	}
	
	public boolean isValidJavaStatement(){

		return false ;
	}
	
	public char[] source(){

		return source;
	}
}
