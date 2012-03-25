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

package nc.uap.lfw.ca.jdt.core.dom;


import nc.uap.lfw.ca.internal.compiler.problem.AbortCompilation;
import nc.uap.lfw.ca.jdt.core.IJavaElement;
import nc.uap.lfw.ca.jdt.core.compiler.CharOperation;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.Expression;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.Wildcard;
import nc.uap.lfw.ca.jdt.internal.compiler.classfmt.ClassFileConstants;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ArrayBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.BaseTypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.Binding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.CaptureBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.FieldBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.LocalTypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.PackageBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.RawTypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ReferenceBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.Scope;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TagBits;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeConstants;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeVariableBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.WildcardBinding;
import nc.uap.lfw.core.log.LfwLogger;

/**
 * Internal implementation of type bindings.
 */
class TypeBinding implements ITypeBinding {
	protected static final IMethodBinding[] NO_METHOD_BINDINGS = new IMethodBinding[0];

	private static final String NO_NAME = ""; //$NON-NLS-1$
	protected static final ITypeBinding[] NO_TYPE_BINDINGS = new ITypeBinding[0];
	protected static final IVariableBinding[] NO_VARIABLE_BINDINGS = new IVariableBinding[0];

	private static final int VALID_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
		Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.STRICTFP;

	nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding binding;
	private String key;
	private BindingResolver resolver;
	private IVariableBinding[] fields;
	private IAnnotationBinding[] annotations;
	private IMethodBinding[] methods;
	private ITypeBinding[] members;
	private ITypeBinding[] interfaces;
	private ITypeBinding[] typeArguments;
	private ITypeBinding[] bounds;
	private ITypeBinding[] typeParameters;

	public TypeBinding(BindingResolver resolver, nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding binding) {
		this.binding = binding;
		this.resolver = resolver;
	}

	public ITypeBinding createArrayType(int dimension) {
		int realDimensions = dimension;
		realDimensions += this.getDimensions();
		if (realDimensions < 1 || realDimensions > 255) {
			throw new IllegalArgumentException();
		}
		return this.resolver.resolveArrayType(this, dimension);
	}

	public IAnnotationBinding[] getAnnotations() {
		if (this.annotations != null) {
			return this.annotations;
		}
		if (this.binding.isAnnotationType() || this.binding.isClass() || this.binding.isEnum() || this.binding.isInterface()) {
			nc.uap.lfw.ca.jdt.internal.compiler.lookup.ReferenceBinding refType =
				(nc.uap.lfw.ca.jdt.internal.compiler.lookup.ReferenceBinding) this.binding;
			nc.uap.lfw.ca.jdt.internal.compiler.lookup.AnnotationBinding[] internalAnnotations = refType.getAnnotations();
			int length = internalAnnotations == null ? 0 : internalAnnotations.length;
			if (length != 0) {
				IAnnotationBinding[] tempAnnotations = new IAnnotationBinding[length];
				int convertedAnnotationCount = 0;
				for (int i = 0; i < length; i++) {
					nc.uap.lfw.ca.jdt.internal.compiler.lookup.AnnotationBinding internalAnnotation = internalAnnotations[i];
					IAnnotationBinding annotationInstance = this.resolver.getAnnotationInstance(internalAnnotation);
					if (annotationInstance == null) {
						continue;
					}
					tempAnnotations[convertedAnnotationCount++] = annotationInstance;
				}
				if (convertedAnnotationCount != length) {
					if (convertedAnnotationCount == 0) {
						return this.annotations = AnnotationBinding.NoAnnotations;
					}
					System.arraycopy(tempAnnotations, 0, (tempAnnotations = new IAnnotationBinding[convertedAnnotationCount]), 0, convertedAnnotationCount);
				}
				return this.annotations = tempAnnotations;
			}
		}
		return this.annotations = AnnotationBinding.NoAnnotations;
	}

	/*
	 * @see ITypeBinding#getBinaryName()
	 * @since 3.0
	 */
	public String getBinaryName() {
		if (this.binding.isCapture()) {
			return null; // no binary name for capture binding
		} else if (this.binding.isTypeVariable()) {
			TypeVariableBinding typeVariableBinding = (TypeVariableBinding) this.binding;
			nc.uap.lfw.ca.jdt.internal.compiler.lookup.Binding declaring = typeVariableBinding.declaringElement;
			StringBuffer binaryName = new StringBuffer();
			switch(declaring.kind()) {
				case nc.uap.lfw.ca.jdt.internal.compiler.lookup.Binding.METHOD :
					MethodBinding methodBinding = (MethodBinding) declaring;
					char[] constantPoolName = methodBinding.declaringClass.constantPoolName();
					if (constantPoolName == null) return null;
					binaryName
						.append(CharOperation.replaceOnCopy(constantPoolName, '/', '.'))
						.append('$')
						.append(methodBinding.signature())
						.append('$')
						.append(typeVariableBinding.sourceName);
					break;
				default :
					nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding typeBinding = (nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding) declaring;
					constantPoolName = typeBinding.constantPoolName();
					if (constantPoolName == null) return null;
					binaryName
						.append(CharOperation.replaceOnCopy(constantPoolName, '/', '.'))
						.append('$')
						.append(typeVariableBinding.sourceName);
			}
			return String.valueOf(binaryName);
		}
 		char[] constantPoolName = this.binding.constantPoolName();
		if (constantPoolName == null) return null;
		char[] dotSeparated = CharOperation.replaceOnCopy(constantPoolName, '/', '.');
		return new String(dotSeparated);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getBound()
	 */
	public ITypeBinding getBound() {
		switch (this.binding.kind()) {
			case Binding.WILDCARD_TYPE :
			case Binding.INTERSECTION_TYPE :
				WildcardBinding wildcardBinding = (WildcardBinding) this.binding;
				if (wildcardBinding.bound != null) {
					return this.resolver.getTypeBinding(wildcardBinding.bound);
				}
				break;
		}
		return null;
	}

	/*
	 * @see ITypeBinding#getComponentType()
	 */
	public ITypeBinding getComponentType() {
		if (!this.isArray()) {
			return null;
		}
		ArrayBinding arrayBinding = (ArrayBinding) binding;
		return resolver.getTypeBinding(arrayBinding.elementsType());
	}

	/*
	 * @see ITypeBinding#getDeclaredFields()
	 */
	public synchronized IVariableBinding[] getDeclaredFields() {
		if (this.fields != null) {
			return this.fields;
		}
		try {
			if (isClass() || isInterface() || isEnum()) {
				ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
				FieldBinding[] fieldBindings = referenceBinding.availableFields(); // resilience
				int length = fieldBindings.length;
				if (length != 0) {
					int convertedFieldCount = 0;
					IVariableBinding[] newFields = new IVariableBinding[length];
					for (int i = 0; i < length; i++) {
						FieldBinding fieldBinding = fieldBindings[i];
						IVariableBinding variableBinding = this.resolver.getVariableBinding(fieldBinding);
						if (variableBinding == null) {
							return this.fields = NO_VARIABLE_BINDINGS;
						}
						newFields[convertedFieldCount++] = variableBinding;
					}

					if (convertedFieldCount != length) {
						if (convertedFieldCount == 0) {
							return this.fields = NO_VARIABLE_BINDINGS;
						}						
						System.arraycopy(newFields, 0, (newFields = new IVariableBinding[convertedFieldCount]), 0, convertedFieldCount);
					}					
					return this.fields = newFields;
				}
			}
		} catch (RuntimeException e) {
			
			LfwLogger.error(e.getMessage(), e);
		}
		return this.fields = NO_VARIABLE_BINDINGS;
	}

	/*
	 * @see ITypeBinding#getDeclaredMethods()
	 */
	public synchronized IMethodBinding[] getDeclaredMethods() {
		if (this.methods != null) {
			return this.methods;
		}
		try {
			if (isClass() || isInterface() || isEnum()) {
				ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
				nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodBinding[] internalMethods = referenceBinding.availableMethods(); // be resilient
				int length = internalMethods.length;
				if (length != 0) {
					int convertedMethodCount = 0;
					IMethodBinding[] newMethods = new IMethodBinding[length];
					for (int i = 0; i < length; i++) {
						nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodBinding methodBinding = internalMethods[i];
						if (methodBinding.isDefaultAbstract() || methodBinding.isSynthetic() || (methodBinding.isConstructor() && isInterface())) {
							continue;
						}
						IMethodBinding methodBinding2 = this.resolver.getMethodBinding(methodBinding);
						if (methodBinding2 != null) {
							newMethods[convertedMethodCount++] = methodBinding2;
						}
					}
					if (convertedMethodCount != length) {
						if (convertedMethodCount == 0) {
							return this.methods = NO_METHOD_BINDINGS;
						}
						System.arraycopy(newMethods, 0, (newMethods = new IMethodBinding[convertedMethodCount]), 0, convertedMethodCount);
					}
					return this.methods = newMethods;
				}
			}
		} catch (RuntimeException e) {
				LfwLogger.error(e.getMessage(), e);
		}
		return this.methods = NO_METHOD_BINDINGS;
	}

	/*
	 * @see ITypeBinding#getDeclaredModifiers()
	 */
	public int getDeclaredModifiers() {
		return getModifiers();
	}

	/*
	 * @see ITypeBinding#getDeclaredTypes()
	 */
	public synchronized ITypeBinding[] getDeclaredTypes() {
		if (this.members != null) {
			return this.members;
		}
		try {
			if (isClass() || isInterface() || isEnum()) {
				ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
				ReferenceBinding[] internalMembers = referenceBinding.memberTypes();
				int length = internalMembers.length;
				if (length != 0) {
					ITypeBinding[] newMembers = new ITypeBinding[length];
					for (int i = 0; i < length; i++) {
						ITypeBinding typeBinding = this.resolver.getTypeBinding(internalMembers[i]);
						if (typeBinding == null) {
							return this.members = NO_TYPE_BINDINGS;
						}
						newMembers[i] = typeBinding;
					}
					return this.members = newMembers;
				}
			}
		} catch (RuntimeException e) {
				LfwLogger.error(e.getMessage(), e);
		}
		return this.members = NO_TYPE_BINDINGS;
	}

	/*
	 * @see ITypeBinding#getDeclaringMethod()
	 */
	public synchronized IMethodBinding getDeclaringMethod() {
		if (this.binding instanceof LocalTypeBinding) {
			LocalTypeBinding localTypeBinding = (LocalTypeBinding) this.binding;
			MethodBinding methodBinding = localTypeBinding.enclosingMethod;
			if (methodBinding != null) {
				try {
					return this.resolver.getMethodBinding(localTypeBinding.enclosingMethod);
				} catch (RuntimeException e) {
						LfwLogger.error(e.getMessage(), e);
				}
			}
		} else if (this.binding.isTypeVariable()) {
			TypeVariableBinding typeVariableBinding = (TypeVariableBinding) this.binding;
			Binding declaringElement = typeVariableBinding.declaringElement;
			if (declaringElement instanceof MethodBinding) {
				try {
					return this.resolver.getMethodBinding((MethodBinding)declaringElement);
				} catch (RuntimeException e) {
						LfwLogger.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	/*
	 * @see ITypeBinding#getDeclaringClass()
	 */
	public synchronized ITypeBinding getDeclaringClass() {
		if (isClass() || isInterface() || isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			if (referenceBinding.isNestedType()) {
				try {
					return this.resolver.getTypeBinding(referenceBinding.enclosingType());
				} catch (RuntimeException e) {
						LfwLogger.error(e.getMessage(), e);
				}
			}
		} else if (this.binding.isTypeVariable()) {
			TypeVariableBinding typeVariableBinding = (TypeVariableBinding) this.binding;
			Binding declaringElement = typeVariableBinding.isCapture() ? ((CaptureBinding) typeVariableBinding).sourceType : typeVariableBinding.declaringElement;
			if (declaringElement instanceof ReferenceBinding) {
				try {
					return this.resolver.getTypeBinding((ReferenceBinding)declaringElement);
				} catch (RuntimeException e) {
						LfwLogger.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	/*
	 * @see ITypeBinding#getDimensions()
	 */
	public int getDimensions() {
		if (!this.isArray()) {
			return 0;
		}
		ArrayBinding arrayBinding = (ArrayBinding) binding;
		return arrayBinding.dimensions;
	}

	/*
	 * @see ITypeBinding#getElementType()
	 */
	public ITypeBinding getElementType() {
		if (!this.isArray()) {
			return null;
		}
		ArrayBinding arrayBinding = (ArrayBinding) binding;
		return resolver.getTypeBinding(arrayBinding.leafComponentType);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getTypeDeclaration()
	 */
	public ITypeBinding getTypeDeclaration() {
		if (this.binding instanceof ParameterizedTypeBinding)
			return this.resolver.getTypeBinding(((ParameterizedTypeBinding)this.binding).genericType());
		return this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getErasure()
	 */
	public ITypeBinding getErasure() {
		return this.resolver.getTypeBinding(this.binding.erasure());
	}

	public synchronized ITypeBinding[] getInterfaces() {
		if (this.interfaces != null) {
			return this.interfaces;
		}
		if (this.binding == null)
			return this.interfaces = NO_TYPE_BINDINGS;
		switch (this.binding.kind()) {
			case Binding.ARRAY_TYPE :
			case Binding.BASE_TYPE :
				return this.interfaces = NO_TYPE_BINDINGS;
		}
		ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
		ReferenceBinding[] internalInterfaces = null;
		try {
			internalInterfaces = referenceBinding.superInterfaces();
		} catch (RuntimeException e) {
				LfwLogger.error(e.getMessage(), e);
		}
		int length = internalInterfaces == null ? 0 : internalInterfaces.length;
		if (length != 0) {
			ITypeBinding[] newInterfaces = new ITypeBinding[length];
			int interfacesCounter = 0;
			for (int i = 0; i < length; i++) {
				ITypeBinding typeBinding = this.resolver.getTypeBinding(internalInterfaces[i]);
				if (typeBinding == null) {
					continue;
				}
				newInterfaces[interfacesCounter++] = typeBinding;
			}
			if (length != interfacesCounter) {
				System.arraycopy(newInterfaces, 0, (newInterfaces = new ITypeBinding[interfacesCounter]), 0, interfacesCounter);
			}
			return this.interfaces = newInterfaces;
		}
		return this.interfaces = NO_TYPE_BINDINGS;
	}

	public IJavaElement getJavaElement() {
//		JavaElement element = getUnresolvedJavaElement();
//		if (element != null)
//			return element.resolved(this.binding);
//		if (isRecovered()) {
//			IPackageBinding packageBinding = getPackage();
//			if (packageBinding != null) {
//				final IJavaElement javaElement = packageBinding.getJavaElement();
//				if (javaElement != null && javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
//					// best effort: we don't know if the recovered binding is a binary or source binding, so go with a compilation unit
//					return ((PackageFragment) javaElement).getCompilationUnit(new String(this.binding.sourceName()) + SuffixConstants.SUFFIX_STRING_java);
//				}
//			}
//			return null;
//		}
		return null;
	}

//	private JavaElement getUnresolvedJavaElement() {
//		return getUnresolvedJavaElement(this.binding);
//	}
//	private JavaElement getUnresolvedJavaElement(org.eclipse.jdt.internal.compiler.lookup.TypeBinding typeBinding ) {
//		if (this.resolver instanceof DefaultBindingResolver) {
//			DefaultBindingResolver defaultBindingResolver = (DefaultBindingResolver) this.resolver;
//			return org.eclipse.jdt.internal.core.util.Util.getUnresolvedJavaElement(
//					typeBinding,
//					defaultBindingResolver.workingCopyOwner,
//					defaultBindingResolver.getBindingsToNodesMap());
//		} else {
//			return org.eclipse.jdt.internal.core.util.Util.getUnresolvedJavaElement(typeBinding, null, null);
//		}
//	}

	/*
	 * @see IBinding#getKey()
	 */
	public String getKey() {
		if (this.key == null) {
			this.key = new String(this.binding.computeUniqueKey());
		}
		return this.key;
	}

	/*
	 * @see IBinding#getKind()
	 */
	public int getKind() {
		return IBinding.TYPE;
	}

	/*
	 * @see IBinding#getModifiers()
	 */
	public int getModifiers() {
		if (isClass()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			final int accessFlags = referenceBinding.getAccessFlags() & VALID_MODIFIERS;
			if (referenceBinding.isAnonymousType()) {
				return accessFlags & ~Modifier.FINAL;
			}
			return accessFlags;
		} else if (isAnnotation()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			final int accessFlags = referenceBinding.getAccessFlags() & VALID_MODIFIERS;
			// clear the AccAbstract, AccAnnotation and the AccInterface bits
			return accessFlags & ~(ClassFileConstants.AccAbstract | ClassFileConstants.AccInterface | ClassFileConstants.AccAnnotation);
		} else if (isInterface()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			final int accessFlags = referenceBinding.getAccessFlags() & VALID_MODIFIERS;
			// clear the AccAbstract and the AccInterface bits
			return accessFlags & ~(ClassFileConstants.AccAbstract | ClassFileConstants.AccInterface);
		} else if (isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			final int accessFlags = referenceBinding.getAccessFlags() & VALID_MODIFIERS;
			// clear the AccEnum bits
			return accessFlags & ~ClassFileConstants.AccEnum;
		} else {
			return Modifier.NONE;
		}
	}

	public String getName() {
		StringBuffer buffer;
		switch (this.binding.kind()) {

			case Binding.WILDCARD_TYPE :
			case Binding.INTERSECTION_TYPE:
				WildcardBinding wildcardBinding = (WildcardBinding) this.binding;
				buffer = new StringBuffer();
				buffer.append(TypeConstants.WILDCARD_NAME);
				if (wildcardBinding.bound != null) {
					switch(wildcardBinding.boundKind) {
				        case Wildcard.SUPER :
				        	buffer.append(TypeConstants.WILDCARD_SUPER);
				            break;
				        case Wildcard.EXTENDS :
				        	buffer.append(TypeConstants.WILDCARD_EXTENDS);
					}
					buffer.append(getBound().getName());
				}
				return String.valueOf(buffer);

			case Binding.TYPE_PARAMETER :
				if (isCapture()) {
					return NO_NAME;
				}
				TypeVariableBinding typeVariableBinding = (TypeVariableBinding) this.binding;
				return new String(typeVariableBinding.sourceName);

			case Binding.PARAMETERIZED_TYPE :
				ParameterizedTypeBinding parameterizedTypeBinding = (ParameterizedTypeBinding) this.binding;
				buffer = new StringBuffer();
				buffer.append(parameterizedTypeBinding.sourceName());
				ITypeBinding[] tArguments = getTypeArguments();
				final int typeArgumentsLength = tArguments.length;
				if (typeArgumentsLength != 0) {
					buffer.append('<');
					for (int i = 0; i < typeArgumentsLength; i++) {
						if (i > 0) {
							buffer.append(',');
						}
						buffer.append(tArguments[i].getName());
					}
					buffer.append('>');
				}
				return String.valueOf(buffer);

			case Binding.RAW_TYPE :
				return getTypeDeclaration().getName();

			case Binding.ARRAY_TYPE :
				ITypeBinding elementType = getElementType();
				if (elementType.isLocal() || elementType.isAnonymous() || elementType.isCapture()) {
					return NO_NAME;
				}
				int dimensions = getDimensions();
				char[] brackets = new char[dimensions * 2];
				for (int i = dimensions * 2 - 1; i >= 0; i -= 2) {
					brackets[i] = ']';
					brackets[i - 1] = '[';
				}
				buffer = new StringBuffer(elementType.getName());
				buffer.append(brackets);
				return String.valueOf(buffer);

			default :
				if (isPrimitive() || isNullType()) {
					BaseTypeBinding baseTypeBinding = (BaseTypeBinding) this.binding;
					return new String(baseTypeBinding.simpleName);
				}
				if (isAnonymous()) {
					return NO_NAME;
				}
				return new String(this.binding.sourceName());
		}
	}

	/*
	 * @see ITypeBinding#getPackage()
	 */
	public IPackageBinding getPackage() {
		switch (this.binding.kind()) {
			case Binding.BASE_TYPE :
			case Binding.ARRAY_TYPE :
			case Binding.TYPE_PARAMETER : // includes capture scenario
			case Binding.WILDCARD_TYPE :
			case Binding.INTERSECTION_TYPE:
				return null;
		}
		ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
		return this.resolver.getPackageBinding(referenceBinding.getPackage());
	}

	/**
	 * @see nc.uap.lfw.ca.jdt.core.dom.ITypeBinding#getQualifiedName()
	 */
	public String getQualifiedName() {
		StringBuffer buffer;
		switch (this.binding.kind()) {

			case Binding.WILDCARD_TYPE :
			case Binding.INTERSECTION_TYPE:
				WildcardBinding wildcardBinding = (WildcardBinding) this.binding;
				buffer = new StringBuffer();
				buffer.append(TypeConstants.WILDCARD_NAME);
				final ITypeBinding bound = getBound();
				if (bound != null) {
					switch(wildcardBinding.boundKind) {
						case Wildcard.SUPER :
							buffer.append(TypeConstants.WILDCARD_SUPER);
							break;
						case Wildcard.EXTENDS :
							buffer.append(TypeConstants.WILDCARD_EXTENDS);
					}
					buffer.append(bound.getQualifiedName());
				}
				return String.valueOf(buffer);

			case Binding.RAW_TYPE :
				return getTypeDeclaration().getQualifiedName();

			case Binding.ARRAY_TYPE :
				ITypeBinding elementType = getElementType();
				if (elementType.isLocal() || elementType.isAnonymous() || elementType.isCapture()) {
					return elementType.getQualifiedName();
				}
				final int dimensions = getDimensions();
				char[] brackets = new char[dimensions * 2];
				for (int i = dimensions * 2 - 1; i >= 0; i -= 2) {
					brackets[i] = ']';
					brackets[i - 1] = '[';
				}
				buffer = new StringBuffer(elementType.getQualifiedName());
				buffer.append(brackets);
				return String.valueOf(buffer);

			case Binding.TYPE_PARAMETER :
				if (isCapture()) {
					return NO_NAME;
				}
				TypeVariableBinding typeVariableBinding = (TypeVariableBinding) this.binding;
				return new String(typeVariableBinding.sourceName);

			case Binding.PARAMETERIZED_TYPE :
				buffer = new StringBuffer();
				if (isMember()) {
					buffer
						.append(getDeclaringClass().getQualifiedName())
						.append('.');
					ParameterizedTypeBinding parameterizedTypeBinding = (ParameterizedTypeBinding) this.binding;
					buffer.append(parameterizedTypeBinding.sourceName());
					ITypeBinding[] tArguments = getTypeArguments();
					final int typeArgumentsLength = tArguments.length;
					if (typeArgumentsLength != 0) {
						buffer.append('<');
						for (int i = 0; i < typeArgumentsLength; i++) {
							if (i > 0) {
								buffer.append(',');
							}
							buffer.append(tArguments[i].getQualifiedName());
						}
						buffer.append('>');
					}
					return String.valueOf(buffer);
				}
				buffer.append(getTypeDeclaration().getQualifiedName());
				ITypeBinding[] tArguments = getTypeArguments();
				final int typeArgumentsLength = tArguments.length;
				if (typeArgumentsLength != 0) {
					buffer.append('<');
					for (int i = 0; i < typeArgumentsLength; i++) {
						if (i > 0) {
							buffer.append(',');
						}
						buffer.append(tArguments[i].getQualifiedName());
					}
					buffer.append('>');
				}
				return String.valueOf(buffer);

			default :
				if (isAnonymous() || this.binding.isLocalType()) {
					return NO_NAME;
				}
				if (isPrimitive() || isNullType()) {
					BaseTypeBinding baseTypeBinding = (BaseTypeBinding) this.binding;
					return new String(baseTypeBinding.simpleName);
				}
				if (isMember()) {
					buffer = new StringBuffer();
					buffer
						.append(getDeclaringClass().getQualifiedName())
						.append('.');
					buffer.append(getName());
					return String.valueOf(buffer);
				}
				PackageBinding packageBinding = this.binding.getPackage();
				buffer = new StringBuffer();
				if (packageBinding != null && packageBinding.compoundName != CharOperation.NO_CHAR_CHAR) {
					buffer.append(CharOperation.concatWith(packageBinding.compoundName, '.')).append('.');
				}
				buffer.append(getName());
				return String.valueOf(buffer);
		}
	}

	/*
	 * @see ITypeBinding#getSuperclass()
	 */
	public synchronized ITypeBinding getSuperclass() {
		if (this.binding == null)
			return null;
		switch (this.binding.kind()) {
			case Binding.ARRAY_TYPE :
			case Binding.BASE_TYPE :
				return null;
			default:
				// no superclass for interface types (interface | annotation type)
				if (this.binding.isInterface())
					return null;
		}
		ReferenceBinding superclass = null;
		try {
			superclass = ((ReferenceBinding)this.binding).superclass();
		} catch (RuntimeException e) {
				LfwLogger.error(e.getMessage(), e);
			return this.resolver.resolveWellKnownType("java.lang.Object"); //$NON-NLS-1$
		}
		if (superclass == null) {
			return null;
		}
		return this.resolver.getTypeBinding(superclass);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getTypeArguments()
	 */
	public ITypeBinding[] getTypeArguments() {
		if (this.typeArguments != null) {
			return this.typeArguments;
		}
		if (this.binding.isParameterizedType()) {
			ParameterizedTypeBinding parameterizedTypeBinding = (ParameterizedTypeBinding) this.binding;
			final nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding[] arguments = parameterizedTypeBinding.arguments;
			if (arguments != null) {
				int argumentsLength = arguments.length;
				ITypeBinding[] newTypeArguments = new ITypeBinding[argumentsLength];
				for (int i = 0; i < argumentsLength; i++) {
					ITypeBinding typeBinding = this.resolver.getTypeBinding(arguments[i]);
					if (typeBinding == null) {
						return this.typeArguments = NO_TYPE_BINDINGS;
					}
					newTypeArguments[i] = typeBinding;
				}
				return this.typeArguments = newTypeArguments;
			}
		}
		return this.typeArguments = NO_TYPE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getTypeBounds()
	 */
	public ITypeBinding[] getTypeBounds() {
		if (this.bounds != null) {
			return this.bounds;
		}
		if (this.binding instanceof TypeVariableBinding) {
			TypeVariableBinding typeVariableBinding = (TypeVariableBinding) this.binding;
			ReferenceBinding varSuperclass = typeVariableBinding.superclass();
			nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding firstClassOrArrayBound = typeVariableBinding.firstBound;
			int boundsLength = 0;
			if (firstClassOrArrayBound != null) {
				if (firstClassOrArrayBound == varSuperclass) {
					boundsLength++;
				} else if (firstClassOrArrayBound.isArrayType()) { // capture of ? extends/super arrayType
					boundsLength++;
				} else {
					firstClassOrArrayBound = null;
				}
			}
			ReferenceBinding[] superinterfaces = typeVariableBinding.superInterfaces();
			int superinterfacesLength = 0;
			if (superinterfaces != null) {
				superinterfacesLength = superinterfaces.length;
				boundsLength += superinterfacesLength;
			}
			if (boundsLength != 0) {
				ITypeBinding[] typeBounds = new ITypeBinding[boundsLength];
				int boundsIndex = 0;
				if (firstClassOrArrayBound != null) {
					ITypeBinding typeBinding = this.resolver.getTypeBinding(firstClassOrArrayBound);
					if (typeBinding == null) {
						return this.bounds = NO_TYPE_BINDINGS;
					}
					typeBounds[boundsIndex++] = typeBinding;
				}
				if (superinterfaces != null) {
					for (int i = 0; i < superinterfacesLength; i++, boundsIndex++) {
						ITypeBinding typeBinding = this.resolver.getTypeBinding(superinterfaces[i]);
						if (typeBinding == null) {
							return this.bounds = NO_TYPE_BINDINGS;
						}
						typeBounds[boundsIndex] = typeBinding;
					}
				}
				return this.bounds = typeBounds;
			}
		}
		return this.bounds = NO_TYPE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getTypeParameters()
	 */
	public ITypeBinding[] getTypeParameters() {
		if (this.typeParameters != null) {
			return this.typeParameters;
		}
		switch(this.binding.kind()) {
			case Binding.RAW_TYPE :
			case Binding.PARAMETERIZED_TYPE :
				return this.typeParameters = NO_TYPE_BINDINGS;
		}
		TypeVariableBinding[] typeVariableBindings = this.binding.typeVariables();
		int typeVariableBindingsLength = typeVariableBindings == null ? 0 : typeVariableBindings.length;
		if (typeVariableBindingsLength != 0) {
			ITypeBinding[] newTypeParameters = new ITypeBinding[typeVariableBindingsLength];
			for (int i = 0; i < typeVariableBindingsLength; i++) {
				ITypeBinding typeBinding = this.resolver.getTypeBinding(typeVariableBindings[i]);
				if (typeBinding == null) {
					return this.typeParameters = NO_TYPE_BINDINGS;
				}
				newTypeParameters[i] = typeBinding;
			}
			return this.typeParameters = newTypeParameters;
		}
		return this.typeParameters = NO_TYPE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getWildcard()
	 * @since 3.1
	 */
	public ITypeBinding getWildcard() {
		if (this.binding instanceof CaptureBinding) {
			CaptureBinding captureBinding = (CaptureBinding) this.binding;
			return this.resolver.getTypeBinding(captureBinding.wildcard);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isGenericType()
	 * @since 3.1
	 */
	public boolean isGenericType() {
		// equivalent to return getTypeParameters().length > 0;
		if (isRawType()) {
			return false;
		}
		TypeVariableBinding[] typeVariableBindings = this.binding.typeVariables();
		return (typeVariableBindings != null && typeVariableBindings.length > 0);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isAnnotation()
	 */
	public boolean isAnnotation() {
		return this.binding.isAnnotationType();
	}

	/*
	 * @see ITypeBinding#isAnonymous()
	 */
	public boolean isAnonymous() {
		if (isClass() || isInterface() || isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			return referenceBinding.isAnonymousType();
		}
		return false;
	}

	/*
	 * @see ITypeBinding#isArray()
	 */
	public boolean isArray() {
		return binding.isArrayType();
	}

	/* (non-Javadoc)
	 * @see ITypeBinding#isAssignmentCompatible(ITypeBinding)
	 */
	public boolean isAssignmentCompatible(ITypeBinding type) {
		try {
			if (this == type) return true;
			if (!(type instanceof TypeBinding)) return false;
			TypeBinding other = (TypeBinding) type;
			Scope scope = this.resolver.scope();
			if (scope == null) return false;
			return this.binding.isCompatibleWith(other.binding) || scope.isBoxingCompatibleWith(this.binding, other.binding);
		} catch (AbortCompilation e) {
			// don't surface internal exception to clients
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=143013
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ITypeBinding#isCapture()
	 */
	public boolean isCapture() {
		return this.binding.isCapture();
	}

	/* (non-Javadoc)
	 * @see ITypeBinding#isCastCompatible(ITypeBinding)
	 */
	public boolean isCastCompatible(ITypeBinding type) {
		try {
			Expression expression = new Expression() {
				public StringBuffer printExpression(int indent,StringBuffer output) {
					return null;
				}
			};
			Scope scope = this.resolver.scope();
			if (scope == null) return false;
			if (!(type instanceof TypeBinding)) return false;
			nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding expressionType = ((TypeBinding) type).binding;
			// simulate capture in case checked binding did not properly get extracted from a reference
			expressionType = expressionType.capture(scope, 0);
			return expression.checkCastTypesCompatibility(scope, this.binding, expressionType, null);
		} catch (AbortCompilation e) {
			// don't surface internal exception to clients
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=143013
			return false;
		}
	}

	/*
	 * @see ITypeBinding#isClass()
	 */
	public boolean isClass() {
		switch (this.binding.kind()) {
			case Binding.TYPE_PARAMETER :
			case Binding.WILDCARD_TYPE :
			case Binding.INTERSECTION_TYPE :
				return false;
		}
		return this.binding.isClass();
	}

	/*
	 * @see IBinding#isDeprecated()
	 */
	public boolean isDeprecated() {
		if (isClass() || isInterface() || isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			return referenceBinding.isDeprecated();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see ITypeBinding#isEnum()
	 */
	public boolean isEnum() {
		return this.binding.isEnum();
	}

	/*
	 * @see IBinding#isEqualTo(Binding)
	 * @since 3.1
	 */
	public boolean isEqualTo(IBinding other) {
		if (other == this) {
			// identical binding - equal (key or no key)
			return true;
		}
		if (other == null) {
			// other binding missing
			return false;
		}
		if (!(other instanceof TypeBinding)) {
			return false;
		}
		nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding otherBinding = ((TypeBinding) other).binding;
		// check return type
		return BindingComparator.isEqual(this.binding, otherBinding);
	}

	/*
	 * @see ITypeBinding#isFromSource()
	 */
	public boolean isFromSource() {
		if (isClass() || isInterface() || isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			if (referenceBinding.isRawType()) {
				return !((RawTypeBinding) referenceBinding).genericType().isBinaryBinding();
			} else if (referenceBinding.isParameterizedType()) {
				ParameterizedTypeBinding parameterizedTypeBinding = (ParameterizedTypeBinding) referenceBinding;
				nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding erasure = parameterizedTypeBinding.erasure();
				if (erasure instanceof ReferenceBinding) {
					return !((ReferenceBinding) erasure).isBinaryBinding();
				}
				return false;
			} else {
				return !referenceBinding.isBinaryBinding();
			}
		} else if (isTypeVariable()) {
			final TypeVariableBinding typeVariableBinding = (TypeVariableBinding) this.binding;
			final Binding declaringElement = typeVariableBinding.declaringElement;
			if (declaringElement instanceof MethodBinding) {
				MethodBinding methodBinding = (MethodBinding) declaringElement;
				return !methodBinding.declaringClass.isBinaryBinding();
			} else {
				final nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding typeBinding = (nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding) declaringElement;
				if (typeBinding instanceof ReferenceBinding) {
					return !((ReferenceBinding) typeBinding).isBinaryBinding();
				} else if (typeBinding instanceof ArrayBinding) {
					final ArrayBinding arrayBinding = (ArrayBinding) typeBinding;
					final nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding leafComponentType = arrayBinding.leafComponentType;
					if (leafComponentType instanceof ReferenceBinding) {
						return !((ReferenceBinding) leafComponentType).isBinaryBinding();
					}
				}
			}

		} else if (isCapture()) {
			CaptureBinding captureBinding = (CaptureBinding) this.binding;
			return !captureBinding.sourceType.isBinaryBinding();
		}
		return false;
	}

	/*
	 * @see ITypeBinding#isInterface()
	 */
	public boolean isInterface() {
		switch (this.binding.kind()) {
			case Binding.TYPE_PARAMETER :
			case Binding.WILDCARD_TYPE :
			case Binding.INTERSECTION_TYPE :
				return false;
		}
		return this.binding.isInterface();
	}

	/*
	 * @see ITypeBinding#isLocal()
	 */
	public boolean isLocal() {
		if (isClass() || isInterface() || isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			return referenceBinding.isLocalType() && !referenceBinding.isMemberType();
		}
		return false;
	}

	/*
	 * @see ITypeBinding#isMember()
	 */
	public boolean isMember() {
		if (isClass() || isInterface() || isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			return referenceBinding.isMemberType();
		}
		return false;
	}

	/*
	 * @see ITypeBinding#isNested()
	 */
	public boolean isNested() {
		if (isClass() || isInterface() || isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			return referenceBinding.isNestedType();
		}
		return false;
	}

	/**
	 * @see ITypeBinding#isNullType()
	 */
	public boolean isNullType() {
		return this.binding == nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding.NULL;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isParameterizedType()
	 */
	public boolean isParameterizedType() {
		return this.binding.isParameterizedType() && ((ParameterizedTypeBinding) this.binding).arguments != null;
	}

	/*
	 * @see ITypeBinding#isPrimitive()
	 */
	public boolean isPrimitive() {
		return !isNullType() && binding.isBaseType();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isRawType()
	 */
	public boolean isRawType() {
		return this.binding.isRawType();
	}

	/* (non-Javadoc)
	 * @see IBinding#isRecovered()
	 */
	public boolean isRecovered() {
		return (this.binding.tagBits & TagBits.HasMissingType) != 0;
	}

	/* (non-Javadoc)
	 * @see ITypeBinding#isSubTypeCompatible(ITypeBinding)
	 */
	public boolean isSubTypeCompatible(ITypeBinding type) {
		try {
			if (this == type) return true;
			if (this.binding.isBaseType()) return false;
			if (!(type instanceof TypeBinding)) return false;
			TypeBinding other = (TypeBinding) type;
			if (other.binding.isBaseType()) return false;
			return this.binding.isCompatibleWith(other.binding);
		} catch (AbortCompilation e) {
			// don't surface internal exception to clients
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=143013
			return false;
		}
	}

	/**
	 * @see IBinding#isSynthetic()
	 */
	public boolean isSynthetic() {
		return false;
	}

	/*
	 * @see ITypeBinding#isTopLevel()
	 */
	public boolean isTopLevel() {
		if (isClass() || isInterface() || isEnum()) {
			ReferenceBinding referenceBinding = (ReferenceBinding) this.binding;
			return !referenceBinding.isNestedType();
		}
		return false;
	}

	/*
	 * @see ITypeBinding#isTypeVariable()
	 */
	public boolean isTypeVariable() {
		return this.binding.isTypeVariable() && !this.binding.isCapture();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isUpperbound()
	 */
	public boolean isUpperbound() {
		switch (this.binding.kind()) {
			case Binding.WILDCARD_TYPE :
				return ((WildcardBinding) this.binding).boundKind == Wildcard.EXTENDS;
			case Binding.INTERSECTION_TYPE :
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isWildcardType()
	 */
	public boolean isWildcardType() {
		return this.binding.isWildcard();
	}

	/*
	 * For debugging purpose only.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.binding.toString();
	}
}
