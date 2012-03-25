/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package nc.uap.lfw.ca.jdt.internal.compiler.ast;

import nc.uap.lfw.ca.jdt.internal.compiler.impl.Constant;
import nc.uap.lfw.ca.jdt.internal.compiler.impl.LongConstant;

public class LongLiteralMinValue extends LongLiteral {

	final static char[] CharValue = new char[]{'-', '9','2','2','3','3','7','2','0','3','6','8','5','4','7','7','5','8','0','8','L'};
	final static Constant MIN_VALUE = LongConstant.fromValue(Long.MIN_VALUE) ; 

public LongLiteralMinValue(){
	super(CharValue,0,0);
	constant = MIN_VALUE;
}
public void computeConstant() {

	/*precomputed at creation time*/}
}
