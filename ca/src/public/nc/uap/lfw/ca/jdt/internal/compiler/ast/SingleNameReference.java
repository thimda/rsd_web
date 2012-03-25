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

import nc.uap.lfw.ca.internal.compiler.problem.ProblemSeverities;
import nc.uap.lfw.ca.jdt.core.compiler.CharOperation;
import nc.uap.lfw.ca.jdt.internal.compiler.ASTVisitor;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.ASTNode;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.Assignment;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.CompoundAssignment;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.Expression;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.NameReference;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.OperatorIds;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.SingleNameReference;
import nc.uap.lfw.ca.jdt.internal.compiler.classfmt.ClassFileConstants;
import nc.uap.lfw.ca.jdt.internal.compiler.codegen.CodeStream;
import nc.uap.lfw.ca.jdt.internal.compiler.flow.FlowContext;
import nc.uap.lfw.ca.jdt.internal.compiler.flow.FlowInfo;
import nc.uap.lfw.ca.jdt.internal.compiler.impl.CompilerOptions;
import nc.uap.lfw.ca.jdt.internal.compiler.impl.Constant;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.Binding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.BlockScope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ClassScope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.FieldBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.LocalVariableBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodScope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.MissingTypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ProblemFieldBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ProblemReasons;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ReferenceBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.Scope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.SourceTypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TagBits;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeIds;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.VariableBinding;


public class SingleNameReference extends NameReference implements OperatorIds {
    
	public static final int READ = 0;
	public static final int WRITE = 1;
	public char[] token;
	public MethodBinding[] syntheticAccessors; // [0]=read accessor [1]=write accessor
	public TypeBinding genericCast;
	
public SingleNameReference(char[] source, long pos) {
	super();
	this.token = source;
	this.sourceStart = (int) (pos >>> 32);
	this.sourceEnd = (int) pos;
}

public FlowInfo analyseAssignment(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, Assignment assignment, boolean isCompound) {
	boolean isReachable = (flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0;
	// compound assignment extra work
	if (isCompound) { // check the variable part is initialized if blank final
		switch (this.bits & ASTNode.RestrictiveFlagMASK) {
			case Binding.FIELD : // reading a field
				FieldBinding fieldBinding;
				if ((fieldBinding = (FieldBinding) this.binding).isBlankFinal() 
						&& currentScope.needBlankFinalFieldInitializationCheck(fieldBinding)) {
					if (!flowInfo.isDefinitelyAssigned(fieldBinding)) {
						currentScope.problemReporter().uninitializedBlankFinalField(fieldBinding, this);
					}
				}
				manageSyntheticAccessIfNecessary(currentScope, flowInfo, true /*read-access*/);
				break;
			case Binding.LOCAL : // reading a local variable
				// check if assigning a final blank field
				LocalVariableBinding localBinding;
				if (!flowInfo.isDefinitelyAssigned(localBinding = (LocalVariableBinding) this.binding)) {
					currentScope.problemReporter().uninitializedLocalVariable(localBinding, this);
					// we could improve error msg here telling "cannot use compound assignment on final local variable"
				}
				if (isReachable) {
					localBinding.useFlag = LocalVariableBinding.USED;
				} else if (localBinding.useFlag == LocalVariableBinding.UNUSED) {
					localBinding.useFlag = LocalVariableBinding.FAKE_USED;
				}
		}
	}
	if (assignment.expression != null) {
		flowInfo = assignment.expression.analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
	}
	switch (this.bits & ASTNode.RestrictiveFlagMASK) {
		case Binding.FIELD : // assigning to a field
			manageSyntheticAccessIfNecessary(currentScope, flowInfo, false /*write-access*/);

			// check if assigning a final field
			FieldBinding fieldBinding = (FieldBinding) this.binding;
			if (fieldBinding.isFinal()) {
				// inside a context where allowed
				if (!isCompound && fieldBinding.isBlankFinal() && currentScope.allowBlankFinalFieldAssignment(fieldBinding)) {
					if (flowInfo.isPotentiallyAssigned(fieldBinding)) {
						currentScope.problemReporter().duplicateInitializationOfBlankFinalField(fieldBinding, this);
					} else {
						flowContext.recordSettingFinal(fieldBinding, this, flowInfo);						
					}
					flowInfo.markAsDefinitelyAssigned(fieldBinding);
				} else {
					currentScope.problemReporter().cannotAssignToFinalField(fieldBinding, this);
				}
			}
			break;
		case Binding.LOCAL : // assigning to a local variable 
			LocalVariableBinding localBinding = (LocalVariableBinding) this.binding;
			if (!flowInfo.isDefinitelyAssigned(localBinding)){// for local variable debug attributes
				this.bits |= ASTNode.FirstAssignmentToLocal;
			} else {
				this.bits &= ~ASTNode.FirstAssignmentToLocal;
			}
			if (localBinding.isFinal()) {
				if ((this.bits & ASTNode.DepthMASK) == 0) {
					// tolerate assignment to final local in unreachable code (45674)
					if ((isReachable && isCompound) || !localBinding.isBlankFinal()){
						currentScope.problemReporter().cannotAssignToFinalLocal(localBinding, this);
					} else if (flowInfo.isPotentiallyAssigned(localBinding)) {
						currentScope.problemReporter().duplicateInitializationOfFinalLocal(localBinding, this);
					} else {
						flowContext.recordSettingFinal(localBinding, this, flowInfo);								
					}
				} else {
					currentScope.problemReporter().cannotAssignToFinalOuterLocal(localBinding, this);
				}
			}
			else /* avoid double diagnostic */ if ((localBinding.tagBits & TagBits.IsArgument) != 0) {
				currentScope.problemReporter().parameterAssignment(localBinding, this);
			}
			flowInfo.markAsDefinitelyAssigned(localBinding);
	}
	manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
	return flowInfo;
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	return analyseCode(currentScope, flowContext, flowInfo, true);
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, boolean valueRequired) {
	switch (this.bits & ASTNode.RestrictiveFlagMASK) {
		case Binding.FIELD : // reading a field
			if (valueRequired || currentScope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_4) {
				manageSyntheticAccessIfNecessary(currentScope, flowInfo, true /*read-access*/);
			}
			// check if reading a final blank field
			FieldBinding fieldBinding = (FieldBinding) this.binding;
			if (fieldBinding.isBlankFinal() && currentScope.needBlankFinalFieldInitializationCheck(fieldBinding)) {
				if (!flowInfo.isDefinitelyAssigned(fieldBinding)) {
					currentScope.problemReporter().uninitializedBlankFinalField(fieldBinding, this);
				}
			}
			break;
		case Binding.LOCAL : // reading a local variable
			LocalVariableBinding localBinding;
			if (!flowInfo.isDefinitelyAssigned(localBinding = (LocalVariableBinding) this.binding)) {
				currentScope.problemReporter().uninitializedLocalVariable(localBinding, this);
			}
			if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0)	{
				localBinding.useFlag = LocalVariableBinding.USED;
			} else if (localBinding.useFlag == LocalVariableBinding.UNUSED) {
				localBinding.useFlag = LocalVariableBinding.FAKE_USED;
			}
	}
	if (valueRequired) {
		manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
	}
	return flowInfo;
}

public TypeBinding checkFieldAccess(BlockScope scope) {
	FieldBinding fieldBinding = (FieldBinding) this.binding;
	this.constant = fieldBinding.constant();
	
	this.bits &= ~ASTNode.RestrictiveFlagMASK; // clear bits
	this.bits |= Binding.FIELD;
	MethodScope methodScope = scope.methodScope();
	if (fieldBinding.isStatic()) {
		// check if accessing enum static field in initializer
		ReferenceBinding declaringClass = fieldBinding.declaringClass;
		if (declaringClass.isEnum()) {
			SourceTypeBinding sourceType = scope.enclosingSourceType();
			if (this.constant == Constant.NotAConstant
					&& !methodScope.isStatic
					&& (sourceType == declaringClass || sourceType.superclass == declaringClass) // enum constant body
					&& methodScope.isInsideInitializerOrConstructor()) {
				scope.problemReporter().enumStaticFieldUsedDuringInitialization(fieldBinding, this);
			}
		}		
	} else {
		if (scope.compilerOptions().getSeverity(CompilerOptions.UnqualifiedFieldAccess) != ProblemSeverities.Ignore) {
			scope.problemReporter().unqualifiedFieldAccess(this, fieldBinding);
		}		
		// must check for the static status....
		if (methodScope.isStatic) {
			scope.problemReporter().staticFieldAccessToNonStaticVariable(this, fieldBinding);
			return fieldBinding.type;
		}
	}

	if (isFieldUseDeprecated(fieldBinding, scope, (this.bits & ASTNode.IsStrictlyAssigned) !=0))
		scope.problemReporter().deprecatedField(fieldBinding, this);

	if ((this.bits & ASTNode.IsStrictlyAssigned) == 0
			&& methodScope.enclosingSourceType() == fieldBinding.original().declaringClass
			&& methodScope.lastVisibleFieldID >= 0
			&& fieldBinding.id >= methodScope.lastVisibleFieldID
			&& (!fieldBinding.isStatic() || methodScope.isStatic)) {
		scope.problemReporter().forwardReference(this, 0, fieldBinding);
		this.bits |= ASTNode.IgnoreNoEffectAssignCheck;
	}
	return fieldBinding.type;

}

/**
 * @see nc.uap.lfw.ca.jdt.internal.compiler.ast.Expression#computeConversion(nc.uap.lfw.ca.jdt.internal.compiler.lookup.Scope, nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding, nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding)
 */
public void computeConversion(Scope scope, TypeBinding runtimeTimeType, TypeBinding compileTimeType) {
	if (runtimeTimeType == null || compileTimeType == null)
		return;				
	if ((this.bits & Binding.FIELD) != 0 && this.binding != null && this.binding.isValidBinding()) {
		// set the generic cast after the fact, once the type expectation is fully known (no need for strict cast)
		FieldBinding field = (FieldBinding) this.binding;
		FieldBinding originalBinding = field.original();
		TypeBinding originalType = originalBinding.type;
	    // extra cast needed if field type is type variable
		if (originalType.leafComponentType().isTypeVariable()) {
	    	TypeBinding targetType = (!compileTimeType.isBaseType() && runtimeTimeType.isBaseType()) 
	    		? compileTimeType  // unboxing: checkcast before conversion
	    		: runtimeTimeType;
	        this.genericCast = originalType.genericCast(scope.boxing(targetType));
	        if (this.genericCast instanceof ReferenceBinding) {
				ReferenceBinding referenceCast = (ReferenceBinding) this.genericCast;
				if (!referenceCast.canBeSeenBy(scope)) {
		        	scope.problemReporter().invalidType(this, 
		        			new ProblemReferenceBinding(
								CharOperation.splitOn('.', referenceCast.shortReadableName()),
								referenceCast,
								ProblemReasons.NotVisible));
				}
	        }				        
		} 	
	}
	super.computeConversion(scope, runtimeTimeType, compileTimeType);
}	

public void generateAssignment(BlockScope currentScope, CodeStream codeStream, Assignment assignment, boolean valueRequired) {
//	// optimizing assignment like: i = i + 1 or i = 1 + i
//	if (assignment.expression.isCompactableOperation()) {
//		BinaryExpression operation = (BinaryExpression) assignment.expression;
//		int operator = (operation.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
//		SingleNameReference variableReference;
//		if ((operation.left instanceof SingleNameReference) && ((variableReference = (SingleNameReference) operation.left).binding == this.binding)) {
//			// i = i + value, then use the variable on the right hand side, since it has the correct implicit conversion
//			variableReference.generateCompoundAssignment(currentScope, codeStream, this.syntheticAccessors == null ? null : this.syntheticAccessors[SingleNameReference.WRITE], operation.right, operator, operation.implicitConversion, valueRequired);
//			if (valueRequired) {
//				codeStream.generateImplicitConversion(assignment.implicitConversion);
//			}				
//			return;
//		} 
//		if ((operation.right instanceof SingleNameReference)
//				&& ((operator == OperatorIds.PLUS) || (operator == OperatorIds.MULTIPLY)) // only commutative operations
//				&& ((variableReference = (SingleNameReference) operation.right).binding == this.binding)
//				&& (operation.left.constant != Constant.NotAConstant) // exclude non constant expressions, since could have side-effect
//				&& (((operation.left.implicitConversion & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4) != TypeIds.T_JavaLangString) // exclude string concatenation which would occur backwards
//				&& (((operation.right.implicitConversion & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4) != TypeIds.T_JavaLangString)) { // exclude string concatenation which would occur backwards
//			// i = value + i, then use the variable on the right hand side, since it has the correct implicit conversion
//			variableReference.generateCompoundAssignment(currentScope, codeStream, this.syntheticAccessors == null ? null : this.syntheticAccessors[SingleNameReference.WRITE], operation.left, operator, operation.implicitConversion, valueRequired);
//			if (valueRequired) {
//				codeStream.generateImplicitConversion(assignment.implicitConversion);
//			}				
//			return;
//		}
//	}
//	switch (this.bits & ASTNode.RestrictiveFlagMASK) {
//		case Binding.FIELD : // assigning to a field
//			FieldBinding fieldBinding;
//			int pc = codeStream.position;
//			if (!(fieldBinding = (FieldBinding) this.codegenBinding).isStatic()) { // need a receiver?
//				if ((this.bits & ASTNode.DepthMASK) != 0) {
//					ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((this.bits & ASTNode.DepthMASK) >> ASTNode.DepthSHIFT);
//					Object[] emulationPath = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
//					codeStream.generateOuterAccess(emulationPath, this, targetType, currentScope);
//				} else {
//					this.generateReceiver(codeStream);
//				}
//			}
//			codeStream.recordPositionsFrom(pc, this.sourceStart);
//			assignment.expression.generateCode(currentScope, codeStream, true);
//			fieldStore(codeStream, fieldBinding, this.syntheticAccessors == null ? null : this.syntheticAccessors[SingleNameReference.WRITE], valueRequired);
//			if (valueRequired) {
//				codeStream.generateImplicitConversion(assignment.implicitConversion);
//			}
//			// no need for generic cast as value got dupped
//			return;
//		case Binding.LOCAL : // assigning to a local variable
//			LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
//			if (localBinding.resolvedPosition != -1) {
//				assignment.expression.generateCode(currentScope, codeStream, true);
//			} else {
//				if (assignment.expression.constant != Constant.NotAConstant) {
//					// assigning an unused local to a constant value = no actual assignment is necessary
//					if (valueRequired) {
//						codeStream.generateConstant(assignment.expression.constant, assignment.implicitConversion);
//					}
//				} else {
//					assignment.expression.generateCode(currentScope, codeStream, true);
//					/* Even though the value may not be required, we force it to be produced, and discard it later
//					on if it was actually not necessary, so as to provide the same behavior as JDK1.2beta3.	*/
//					if (valueRequired) {
//						codeStream.generateImplicitConversion(assignment.implicitConversion); // implicit conversion
//					} else {
//						if ((localBinding.type == TypeBinding.LONG) || (localBinding.type == TypeBinding.DOUBLE)) {
//							codeStream.pop2();
//						} else {
//							codeStream.pop();
//						}
//					}
//				}
//				return;
//			}
//			// 26903, need extra cast to store null in array local var	
//			if (localBinding.type.isArrayType() 
//				&& (assignment.expression.resolvedType == TypeBinding.NULL	// arrayLoc = null
//					|| ((assignment.expression instanceof CastExpression)	// arrayLoc = (type[])null
//						&& (((CastExpression)assignment.expression).innermostCastedExpression().resolvedType == TypeBinding.NULL)))){
//				codeStream.checkcast(localBinding.type); 
//			}
//			
//			// normal local assignment (since cannot store in outer local which are final locations)
//			codeStream.store(localBinding, valueRequired);
//			if ((this.bits & ASTNode.FirstAssignmentToLocal) != 0) { // for local variable debug attributes
//				localBinding.recordInitializationStartPC(codeStream.position);
//			}
//			// implicit conversion
//			if (valueRequired) {
//				codeStream.generateImplicitConversion(assignment.implicitConversion);
//			}
//	}
}

public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
//	int pc = codeStream.position;
//	if (this.constant != Constant.NotAConstant) {
//		if (valueRequired) {
//			codeStream.generateConstant(this.constant, this.implicitConversion);
//		}
//		codeStream.recordPositionsFrom(pc, this.sourceStart);
//		return;
//	} else {
//		switch (this.bits & ASTNode.RestrictiveFlagMASK) {
//			case Binding.FIELD : // reading a field
//				FieldBinding fieldBinding = (FieldBinding) this.codegenBinding;
//				Constant fieldConstant = fieldBinding.constant();
//				if (fieldConstant != Constant.NotAConstant) {
//					// directly use inlined value for constant fields
//					if (valueRequired) {
//						codeStream.generateConstant(fieldConstant, this.implicitConversion);
//					}
//					codeStream.recordPositionsFrom(pc, this.sourceStart);
//					return;
//				}
//				if (fieldBinding.isStatic()) {
//					if (!valueRequired
//							// if no valueRequired, still need possible side-effects of <clinit> invocation, if field belongs to different class
//							&& ((FieldBinding)this.binding).original().declaringClass == this.actualReceiverType.erasure()
//							&& ((this.implicitConversion & TypeIds.UNBOXING) == 0)
//							&& this.genericCast == null) {
//						// if no valueRequired, optimize out entire gen
//						codeStream.recordPositionsFrom(pc, this.sourceStart);
//						return;
//					}
//					// managing private access							
//					if ((this.syntheticAccessors == null) || (this.syntheticAccessors[SingleNameReference.READ] == null)) {
//						codeStream.getstatic(fieldBinding);
//					} else {
//						codeStream.invokestatic(this.syntheticAccessors[SingleNameReference.READ]);
//					}
//				} else {
//					if (!valueRequired
//							&& (this.implicitConversion & TypeIds.UNBOXING) == 0 
//							&& this.genericCast == null) {
//						// if no valueRequired, optimize out entire gen
//						codeStream.recordPositionsFrom(pc, this.sourceStart);
//						return;
//					}
//					// managing enclosing instance access
//					if ((this.bits & ASTNode.DepthMASK) != 0) {
//						ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((this.bits & ASTNode.DepthMASK) >> ASTNode.DepthSHIFT);
//						Object[] emulationPath = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
//						codeStream.generateOuterAccess(emulationPath, this, targetType, currentScope);
//					} else {
//						generateReceiver(codeStream);
//					}
//					// managing private access							
//					if ((this.syntheticAccessors == null) || (this.syntheticAccessors[SingleNameReference.READ] == null)) {
//						codeStream.getfield(fieldBinding);
//					} else {
//						codeStream.invokestatic(this.syntheticAccessors[SingleNameReference.READ]);
//					}
//				}
//				break;
//			case Binding.LOCAL : // reading a local
//				LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
//				if (!valueRequired && (this.implicitConversion & TypeIds.UNBOXING) == 0) {
//					// if no valueRequired, optimize out entire gen
//					codeStream.recordPositionsFrom(pc, this.sourceStart);
//					return;
//				}
//				// outer local?
//				if ((this.bits & ASTNode.DepthMASK) != 0) {
//					// outer local can be reached either through a synthetic arg or a synthetic field
//					VariableBinding[] path = currentScope.getEmulationPath(localBinding);
//					codeStream.generateOuterAccess(path, this, localBinding, currentScope);
//				} else {
//					// regular local variable read
//					codeStream.load(localBinding);
//				}
//				break;
//			default: // type
//				codeStream.recordPositionsFrom(pc, this.sourceStart);
//				return;					
//		}
//	}
//	// required cast must occur even if no value is required
//	if (this.genericCast != null) codeStream.checkcast(this.genericCast);
//	if (valueRequired) {
//		codeStream.generateImplicitConversion(this.implicitConversion);
//	} else {
//		boolean isUnboxing = (this.implicitConversion & TypeIds.UNBOXING) != 0;
//		// conversion only generated if unboxing
//		if (isUnboxing) codeStream.generateImplicitConversion(this.implicitConversion);
//		switch (isUnboxing ? postConversionType(currentScope).id : this.resolvedType.id) {
//			case T_long :
//			case T_double :
//				codeStream.pop2();
//				break;
//			default :
//				codeStream.pop();
//		}
//	}							
//	codeStream.recordPositionsFrom(pc, this.sourceStart);
}

/*
 * Regular API for compound assignment, relies on the fact that there is only one reference to the
 * variable, which carries both synthetic read/write accessors.
 * The APIs with an extra argument is used whenever there are two references to the same variable which
 * are optimized in one access: e.g "a = a + 1" optimized into "a++".
 */
public void generateCompoundAssignment(BlockScope currentScope, CodeStream codeStream, Expression expression, int operator, int assignmentImplicitConversion, boolean valueRequired) {
	this.generateCompoundAssignment(
		currentScope, 
		codeStream, 
		this.syntheticAccessors == null ? null : this.syntheticAccessors[SingleNameReference.WRITE],
		expression,
		operator, 
		assignmentImplicitConversion, 
		valueRequired);
}

/*
 * The APIs with an extra argument is used whenever there are two references to the same variable which
 * are optimized in one access: e.g "a = a + 1" optimized into "a++".
 */
public void generateCompoundAssignment(BlockScope currentScope, CodeStream codeStream, MethodBinding writeAccessor, Expression expression, int operator, int assignmentImplicitConversion, boolean valueRequired) {
//	switch (this.bits & ASTNode.RestrictiveFlagMASK) {
//		case Binding.FIELD : // assigning to a field
//			FieldBinding fieldBinding;
//			if ((fieldBinding = (FieldBinding) this.codegenBinding).isStatic()) {
//				if ((this.syntheticAccessors == null) || (this.syntheticAccessors[SingleNameReference.READ] == null)) {
//					codeStream.getstatic(fieldBinding);
//				} else {
//					codeStream.invokestatic(this.syntheticAccessors[SingleNameReference.READ]);
//				}
//			} else {
//				if ((this.bits & ASTNode.DepthMASK) != 0) {
//					ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((this.bits & ASTNode.DepthMASK) >> ASTNode.DepthSHIFT);
//					Object[] emulationPath = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
//					codeStream.generateOuterAccess(emulationPath, this, targetType, currentScope);
//				} else {
//					codeStream.aload_0();
//				}
//				codeStream.dup();
//				if ((this.syntheticAccessors == null) || (this.syntheticAccessors[SingleNameReference.READ] == null)) {
//					codeStream.getfield(fieldBinding);
//				} else {
//					codeStream.invokestatic(this.syntheticAccessors[SingleNameReference.READ]);
//				}
//			}
//			break;
//		case Binding.LOCAL : // assigning to a local variable (cannot assign to outer local)
//			LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
//			// using incr bytecode if possible
//			switch (localBinding.type.id) {
//				case T_JavaLangString :
//					codeStream.generateStringConcatenationAppend(currentScope, this, expression);
//					if (valueRequired) {
//						codeStream.dup();
//					}
//					codeStream.store(localBinding, false);
//					return;
//				case T_int :
//					Constant assignConstant;
//					if (((assignConstant = expression.constant) != Constant.NotAConstant) 
//							&& (assignConstant.typeID() != TypeIds.T_float) // only for integral types
//							&& (assignConstant.typeID() != TypeIds.T_double)) {
//						switch (operator) {
//							case PLUS :
//								int increment  = assignConstant.intValue();
//								if (increment != (short) increment) break; // not representable as a 16-bits value
//								codeStream.iinc(localBinding.resolvedPosition, increment);
//								if (valueRequired) {
//									codeStream.load(localBinding);
//								}
//								return;
//							case MINUS :
//								increment  = -assignConstant.intValue();
//								if (increment != (short) increment) break; // not representable as a 16-bits value
//								codeStream.iinc(localBinding.resolvedPosition, increment);
//								if (valueRequired) {
//									codeStream.load(localBinding);
//								}
//								return;
//						}
//					}
//				default :
//					codeStream.load(localBinding);
//			}
//	}
//	// perform the actual compound operation
//	int operationTypeID;
//	switch(operationTypeID = (this.implicitConversion & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4) {
//		case T_JavaLangString :
//		case T_JavaLangObject :
//		case T_undefined :
//			// we enter here if the single name reference is a field of type java.lang.String or if the type of the 
//			// operation is java.lang.Object
//			// For example: o = o + ""; // where the compiled type of o is java.lang.Object.
//			codeStream.generateStringConcatenationAppend(currentScope, null, expression);
//			// no need for generic cast on previous #getfield since using Object string buffer methods.			
//			break;
//		default :
//			// promote the array reference to the suitable operation type
//			if (this.genericCast != null)
//				codeStream.checkcast(this.genericCast);
//			codeStream.generateImplicitConversion(this.implicitConversion);
//			// generate the increment value (will by itself  be promoted to the operation value)
//			if (expression == IntLiteral.One){ // prefix operation
//				codeStream.generateConstant(expression.constant, this.implicitConversion);			
//			} else {
//				expression.generateCode(currentScope, codeStream, true);
//			}		
//			// perform the operation
//			codeStream.sendOperator(operator, operationTypeID);
//			// cast the value back to the array reference type
//			codeStream.generateImplicitConversion(assignmentImplicitConversion);
//	}
//	// store the result back into the variable
//	switch (this.bits & ASTNode.RestrictiveFlagMASK) {
//		case Binding.FIELD : // assigning to a field
//			fieldStore(codeStream, (FieldBinding) this.codegenBinding, writeAccessor, valueRequired);
//			// no need for generic cast as value got dupped
//			return;
//		case Binding.LOCAL : // assigning to a local variable
//			LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
//			if (valueRequired) {
//				if ((localBinding.type == TypeBinding.LONG) || (localBinding.type == TypeBinding.DOUBLE)) {
//					codeStream.dup2();
//				} else {
//					codeStream.dup();
//				}
//			}
//			codeStream.store(localBinding, false);
//	}
}

public void generatePostIncrement(BlockScope currentScope, CodeStream codeStream, CompoundAssignment postIncrement, boolean valueRequired) {
//	switch (this.bits & ASTNode.RestrictiveFlagMASK) {
//		case Binding.FIELD : // assigning to a field
//			FieldBinding fieldBinding;
//			if ((fieldBinding = (FieldBinding) this.codegenBinding).isStatic()) {
//				if ((this.syntheticAccessors == null) || (this.syntheticAccessors[SingleNameReference.READ] == null)) {
//					codeStream.getstatic(fieldBinding);
//				} else {
//					codeStream.invokestatic(this.syntheticAccessors[SingleNameReference.READ]);
//				}
//			} else {
//				if ((this.bits & ASTNode.DepthMASK) != 0) {
//					ReferenceBinding targetType = currentScope.enclosingSourceType().enclosingTypeAt((this.bits & ASTNode.DepthMASK) >> ASTNode.DepthSHIFT);
//					Object[] emulationPath = currentScope.getEmulationPath(targetType, true /*only exact match*/, false/*consider enclosing arg*/);
//					codeStream.generateOuterAccess(emulationPath, this, targetType, currentScope);
//				} else {
//					codeStream.aload_0();
//				}
//				codeStream.dup();
//				if ((this.syntheticAccessors == null) || (this.syntheticAccessors[SingleNameReference.READ] == null)) {
//					codeStream.getfield(fieldBinding);
//				} else {
//					codeStream.invokestatic(this.syntheticAccessors[SingleNameReference.READ]);
//				}
//			}
//			TypeBinding operandType;
//			if (this.genericCast != null) {
//				codeStream.checkcast(this.genericCast);
//				operandType = this.genericCast;
//			} else {
//				operandType = fieldBinding.type;
//			}
//			if (valueRequired) {
//				if (fieldBinding.isStatic()) {
//					if ((operandType == TypeBinding.LONG) || (operandType == TypeBinding.DOUBLE)) {
//						codeStream.dup2();
//					} else {
//						codeStream.dup();
//					}
//				} else { // Stack:  [owner][old field value]  ---> [old field value][owner][old field value]
//					if ((operandType == TypeBinding.LONG) || (operandType == TypeBinding.DOUBLE)) {
//						codeStream.dup2_x1();
//					} else {
//						codeStream.dup_x1();
//					}
//				}
//			}
//			codeStream.generateImplicitConversion(this.implicitConversion);		
//			codeStream.generateConstant(postIncrement.expression.constant, this.implicitConversion);
//			codeStream.sendOperator(postIncrement.operator, this.implicitConversion & TypeIds.COMPILE_TYPE_MASK);
//			codeStream.generateImplicitConversion(postIncrement.preAssignImplicitConversion);
//			fieldStore(codeStream, fieldBinding, this.syntheticAccessors == null ? null : this.syntheticAccessors[SingleNameReference.WRITE], false);
//			// no need for generic cast 
//			return;
//		case Binding.LOCAL : // assigning to a local variable
//			LocalVariableBinding localBinding = (LocalVariableBinding) this.codegenBinding;
//			// using incr bytecode if possible
//			if (localBinding.type == TypeBinding.INT) {
//				if (valueRequired) {
//					codeStream.load(localBinding);
//				}
//				if (postIncrement.operator == OperatorIds.PLUS) {
//					codeStream.iinc(localBinding.resolvedPosition, 1);
//				} else {
//					codeStream.iinc(localBinding.resolvedPosition, -1);
//				}
//			} else {
//				codeStream.load(localBinding);
//				if (valueRequired){
//					if ((localBinding.type == TypeBinding.LONG) || (localBinding.type == TypeBinding.DOUBLE)) {
//						codeStream.dup2();
//					} else {
//						codeStream.dup();
//					}
//				}
//				codeStream.generateImplicitConversion(this.implicitConversion);
//				codeStream.generateConstant(postIncrement.expression.constant, this.implicitConversion);
//				codeStream.sendOperator(postIncrement.operator, this.implicitConversion & TypeIds.COMPILE_TYPE_MASK);
//				codeStream.generateImplicitConversion(postIncrement.preAssignImplicitConversion);
//				codeStream.store(localBinding, false);
//			}
//	}
}	

public void generateReceiver(CodeStream codeStream) {
//	codeStream.aload_0();
}

/**
 * @see nc.uap.lfw.ca.jdt.internal.compiler.lookup.InvocationSite#genericTypeArguments()
 */
public TypeBinding[] genericTypeArguments() {
	return null;
}

/**
 * Returns the local variable referenced by this node. Can be a direct reference (SingleNameReference)
 * or thru a cast expression etc...
 */
public LocalVariableBinding localVariableBinding() {
	switch (this.bits & ASTNode.RestrictiveFlagMASK) {
		case Binding.FIELD : // reading a field
			break;
		case Binding.LOCAL : // reading a local variable
			return (LocalVariableBinding) this.binding;
	}
	return null;
}

public void manageEnclosingInstanceAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {
	if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0)	{
		//If inlinable field, forget the access emulation, the code gen will directly target it
		if (((this.bits & ASTNode.DepthMASK) == 0) || (this.constant != Constant.NotAConstant)) return;
	
		if ((this.bits & ASTNode.RestrictiveFlagMASK) == Binding.LOCAL) {
			currentScope.emulateOuterAccess((LocalVariableBinding) this.binding);
		}
	}
}

public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo, boolean isReadAccess) {
	if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) != 0)	return;

	//If inlinable field, forget the access emulation, the code gen will directly target it
	if (this.constant != Constant.NotAConstant)
		return;

	if ((this.bits & Binding.FIELD) != 0) {
		FieldBinding fieldBinding = (FieldBinding) this.binding;
		FieldBinding codegenField = fieldBinding.original();
		this.codegenBinding = codegenField;
		if (((this.bits & ASTNode.DepthMASK) != 0)
			&& (codegenField.isPrivate() // private access
				|| (codegenField.isProtected() // implicit protected access
						&& codegenField.declaringClass.getPackage() != currentScope.enclosingSourceType().getPackage()))) {
			if (this.syntheticAccessors == null)
				this.syntheticAccessors = new MethodBinding[2];
			this.syntheticAccessors[isReadAccess ? SingleNameReference.READ : SingleNameReference.WRITE] = 
			    ((SourceTypeBinding)currentScope.enclosingSourceType().
					enclosingTypeAt((this.bits & ASTNode.DepthMASK) >> ASTNode.DepthSHIFT)).addSyntheticMethod(codegenField, isReadAccess);
			currentScope.problemReporter().needToEmulateFieldAccess(codegenField, this, isReadAccess);
			return;
		}
		// if the binding declaring class is not visible, need special action
		// for runtime compatibility on 1.2 VMs : change the declaring class of the binding
		// NOTE: from target 1.2 on, field's declaring class is touched if any different from receiver type
		// and not from Object or implicit static field access.	
		if (fieldBinding.declaringClass != this.actualReceiverType
				&& !this.actualReceiverType.isArrayType()
				&& fieldBinding.declaringClass != null // array.length
				&& fieldBinding.constant() == Constant.NotAConstant) {
			CompilerOptions options = currentScope.compilerOptions();
			if ((options.targetJDK >= ClassFileConstants.JDK1_2
					&& (options.complianceLevel >= ClassFileConstants.JDK1_4 || !fieldBinding.isStatic())
					&& fieldBinding.declaringClass.id != TypeIds.T_JavaLangObject) // no change for Object fields
					|| !fieldBinding.declaringClass.canBeSeenBy(currentScope)) {
		
					this.codegenBinding = 
					    currentScope.enclosingSourceType().getUpdatedFieldBinding(
						       codegenField, 
						        (ReferenceBinding)this.actualReceiverType.erasure());
			}
		}					
	}
}

public int nullStatus(FlowInfo flowInfo) {
	if (this.constant != null && this.constant != Constant.NotAConstant) {
		return FlowInfo.NON_NULL; // constant expression cannot be null
	}
	switch (this.bits & ASTNode.RestrictiveFlagMASK) {
		case Binding.FIELD : // reading a field
			return FlowInfo.UNKNOWN;
		case Binding.LOCAL : // reading a local variable
			LocalVariableBinding local = (LocalVariableBinding) this.binding;
			if (local != null) {
				if (flowInfo.isDefinitelyNull(local))
					return FlowInfo.NULL;
				if (flowInfo.isDefinitelyNonNull(local))
					return FlowInfo.NON_NULL;
				return FlowInfo.UNKNOWN;
			}
	}
	return FlowInfo.NON_NULL; // never get there 
}

	/**
 * @see nc.uap.lfw.ca.jdt.internal.compiler.ast.Expression#postConversionType(Scope)
 */
public TypeBinding postConversionType(Scope scope) {
	TypeBinding convertedType = this.resolvedType;
	if (this.genericCast != null) 
		convertedType = this.genericCast;
	int runtimeType = (this.implicitConversion & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4;
	switch (runtimeType) {
		case T_boolean :
			convertedType = TypeBinding.BOOLEAN;
			break;
		case T_byte :
			convertedType = TypeBinding.BYTE;
			break;
		case T_short :
			convertedType = TypeBinding.SHORT;
			break;
		case T_char :
			convertedType = TypeBinding.CHAR;
			break;
		case T_int :
			convertedType = TypeBinding.INT;
			break;
		case T_float :
			convertedType = TypeBinding.FLOAT;
			break;
		case T_long :
			convertedType = TypeBinding.LONG;
			break;
		case T_double :
			convertedType = TypeBinding.DOUBLE;
			break;
		default :
	}		
	if ((this.implicitConversion & TypeIds.BOXING) != 0) {
		convertedType = scope.environment().computeBoxingType(convertedType);
	}
	return convertedType;
}

public StringBuffer printExpression(int indent, StringBuffer output){
	return output.append(this.token);
}
public TypeBinding reportError(BlockScope scope) {
	//=====error cases=======
	this.constant = Constant.NotAConstant;
	if (this.binding instanceof ProblemFieldBinding) {
		scope.problemReporter().invalidField(this, (FieldBinding) this.binding);
	} else if (this.binding instanceof ProblemReferenceBinding || this.binding instanceof MissingTypeBinding) {
		scope.problemReporter().invalidType(this, (TypeBinding) this.binding);
	} else {
		scope.problemReporter().unresolvableReference(this, this.binding);
	}
	return null;
}
	
public TypeBinding resolveType(BlockScope scope) {
	// for code gen, harm the restrictiveFlag 	

	if (this.actualReceiverType != null) {
		this.binding = scope.getField(this.actualReceiverType, this.token, this);
	} else {
		this.actualReceiverType = scope.enclosingSourceType();
		this.binding = scope.getBinding(this.token, this.bits & ASTNode.RestrictiveFlagMASK, this, true /*resolve*/);
	}
	this.codegenBinding = this.binding;
	if (this.binding.isValidBinding()) {
		switch (this.bits & ASTNode.RestrictiveFlagMASK) {
			case Binding.VARIABLE : // =========only variable============
			case Binding.VARIABLE | Binding.TYPE : //====both variable and type============
				if (this.binding instanceof VariableBinding) {
					VariableBinding variable = (VariableBinding) this.binding;
					TypeBinding variableType;
					if (this.binding instanceof LocalVariableBinding) {
						this.bits &= ~ASTNode.RestrictiveFlagMASK;  // clear bits
						this.bits |= Binding.LOCAL;
						if (!variable.isFinal() && (this.bits & ASTNode.DepthMASK) != 0) {
							scope.problemReporter().cannotReferToNonFinalOuterLocal((LocalVariableBinding)variable, this);
						}
						variableType = variable.type;
						this.constant = (this.bits & ASTNode.IsStrictlyAssigned) == 0 ? variable.constant() : Constant.NotAConstant;
					} else {
						// a field
						variableType = checkFieldAccess(scope);
					}
					// perform capture conversion if read access
					if (variableType != null) {
						this.resolvedType = variableType = (((this.bits & ASTNode.IsStrictlyAssigned) == 0) 
								? variableType.capture(scope, this.sourceEnd)
								: variableType);
						if ((variableType.tagBits & TagBits.HasMissingType) != 0) {
							if ((this.bits & Binding.LOCAL) == 0) {
								// only complain if field reference (for local, its type got flagged already)
								scope.problemReporter().invalidType(this, variableType);
							}
							return null;
						}
					}
					return variableType;
				}

				// thus it was a type
				this.bits &= ~ASTNode.RestrictiveFlagMASK;  // clear bits
				this.bits |= Binding.TYPE;
			case Binding.TYPE : //========only type==============
				this.constant = Constant.NotAConstant;
				//deprecated test
				TypeBinding type = (TypeBinding)this.binding;
				if (isTypeUseDeprecated(type, scope))
					scope.problemReporter().deprecatedType(type, this);
				type = scope.environment().convertToRawType(type, false /*do not force conversion of enclosing types*/);
				return this.resolvedType = type;
		}
	}
	// error scenarii
	return this.resolvedType = this.reportError(scope);
}

public void traverse(ASTVisitor visitor, BlockScope scope) {
	visitor.visit(this, scope);
	visitor.endVisit(this, scope);
}

public void traverse(ASTVisitor visitor, ClassScope scope) {
	visitor.visit(this, scope);
	visitor.endVisit(this, scope);
}

public String unboundReferenceErrorName(){
	return new String(this.token);
}	
}
