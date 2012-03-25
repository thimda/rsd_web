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
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.LookupEnvironment;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ParameterizedGenericMethodBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.RawTypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ReferenceBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeVariableBinding;

/**
 * Internal implementation of method bindings.
 */
class MethodBinding implements IMethodBinding {

	private static final int VALID_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
		Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.SYNCHRONIZED | Modifier.NATIVE |
		Modifier.STRICTFP;
	private static final ITypeBinding[] NO_TYPE_BINDINGS = new ITypeBinding[0];
	private nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodBinding binding;
	private BindingResolver resolver;
	private ITypeBinding[] parameterTypes;
	private ITypeBinding[] exceptionTypes;
	private String name;
	private ITypeBinding declaringClass;
	private ITypeBinding returnType;
	private String key;
	private ITypeBinding[] typeParameters;
	private ITypeBinding[] typeArguments;
	private IAnnotationBinding[] annotations;
	private IAnnotationBinding[][] parameterAnnotations;

	MethodBinding(BindingResolver resolver, nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodBinding binding) {
		this.resolver = resolver;
		this.binding = binding;
	}

	public boolean isAnnotationMember() {
		return getDeclaringClass().isAnnotation();
	}

	/**
	 * @see IMethodBinding#isConstructor()
	 */
	public boolean isConstructor() {
		return this.binding.isConstructor();
	}

	/**
	 * @see IMethodBinding#isDefaultConstructor()
	 * @since 3.0
	 */
	public boolean isDefaultConstructor() {
		final ReferenceBinding declaringClassBinding = this.binding.declaringClass;
		if (declaringClassBinding.isRawType()) {
			RawTypeBinding rawTypeBinding = (RawTypeBinding) declaringClassBinding;
			if (rawTypeBinding.genericType().isBinaryBinding()) {
				return false;
			}
			return (this.binding.modifiers & ExtraCompilerModifiers.AccIsDefaultConstructor) != 0;
		}
		if (declaringClassBinding.isBinaryBinding()) {
			return false;
		}
		return (this.binding.modifiers & ExtraCompilerModifiers.AccIsDefaultConstructor) != 0;
	}

	/**
	 * @see IBinding#getName()
	 */
	public String getName() {
		if (name == null) {
			if (this.binding.isConstructor()) {
				name = this.getDeclaringClass().getName();
			} else {
				name = new String(this.binding.selector);
			}
		}
		return name;
	}

	public IAnnotationBinding[] getAnnotations() {
		if (this.annotations != null) {
			return this.annotations;
		}
		nc.uap.lfw.ca.jdt.internal.compiler.lookup.AnnotationBinding[] internalAnnotations = this.binding.getAnnotations();
		int length = internalAnnotations == null ? 0 : internalAnnotations.length;
		if (length != 0) {
			IAnnotationBinding[] tempAnnotations = new IAnnotationBinding[length];
			int convertedAnnotationCount = 0;
			for (int i = 0; i < length; i++) {
				nc.uap.lfw.ca.jdt.internal.compiler.lookup.AnnotationBinding internalAnnotation = internalAnnotations[i];
				final IAnnotationBinding annotationInstance = this.resolver.getAnnotationInstance(internalAnnotation);
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
		return this.annotations = AnnotationBinding.NoAnnotations;
	}

	/**
	 * @see IMethodBinding#getDeclaringClass()
	 */
	public ITypeBinding getDeclaringClass() {
		if (this.declaringClass == null) {
			this.declaringClass = this.resolver.getTypeBinding(this.binding.declaringClass);
		}
		return declaringClass;
	}

	public IAnnotationBinding[] getParameterAnnotations(int index) {
		if (getParameterTypes() == NO_TYPE_BINDINGS) {
			return AnnotationBinding.NoAnnotations;
		}
		if (this.parameterAnnotations != null) {
			return this.parameterAnnotations[index];
		}
		nc.uap.lfw.ca.jdt.internal.compiler.lookup.AnnotationBinding[][] bindingAnnotations = this.binding.getParameterAnnotations();
		if (bindingAnnotations == null) return AnnotationBinding.NoAnnotations;

		int length = bindingAnnotations.length;
		IAnnotationBinding[][] domAnnotations = new IAnnotationBinding[length][];
		for (int i = 0; i < length; i++) {
			nc.uap.lfw.ca.jdt.internal.compiler.lookup.AnnotationBinding[] paramBindingAnnotations = bindingAnnotations[i];
			int pLength = paramBindingAnnotations.length;
			domAnnotations[i] = new AnnotationBinding[pLength];
			for (int j=0; j<pLength; j++) {
				IAnnotationBinding domAnnotation = this.resolver.getAnnotationInstance(paramBindingAnnotations[j]);
				if (domAnnotation == null) {
					domAnnotations[i] = AnnotationBinding.NoAnnotations;
					break;
				}
				domAnnotations[i][j] = domAnnotation;
			}
		}
		this.parameterAnnotations = domAnnotations;
		
		return this.parameterAnnotations[index];
	}

	/**
	 * @see IMethodBinding#getParameterTypes()
	 */
	public ITypeBinding[] getParameterTypes() {
		if (this.parameterTypes != null) {
			return parameterTypes;
		}
		nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding[] parameters = this.binding.parameters;
		int length = parameters == null ? 0 : parameters.length;
		if (length == 0) {
			return this.parameterTypes = NO_TYPE_BINDINGS;
		} else {
			ITypeBinding[] paramTypes = new ITypeBinding[length];
			for (int i = 0; i < length; i++) {
				final TypeBinding parameterBinding = parameters[i];
				if (parameterBinding != null) {
					ITypeBinding typeBinding = this.resolver.getTypeBinding(parameterBinding);
					if (typeBinding == null) {
						return this.parameterTypes = NO_TYPE_BINDINGS;
					}
					paramTypes[i] = typeBinding;
				} else {
					// log error
					StringBuffer message = new StringBuffer("Report method binding where a parameter is null:\n");  //$NON-NLS-1$
					message.append(this.toString());
//					Util.log(new IllegalArgumentException(), message.toString());
					// report no binding since one or more parameter has no binding
					return this.parameterTypes = NO_TYPE_BINDINGS;
				}
			}
			return this.parameterTypes = paramTypes;
		}
	}

	/**
	 * @see IMethodBinding#getReturnType()
	 */
	public ITypeBinding getReturnType() {
		if (this.returnType == null) {
			this.returnType = this.resolver.getTypeBinding(this.binding.returnType);
		}
		return this.returnType;
	}

	public Object getDefaultValue() {
		if (isAnnotationMember())
			return MemberValuePairBinding.buildDOMValue(this.binding.getDefaultValue(), this.resolver);
		return null;
	}

	/**
	 * @see IMethodBinding#getExceptionTypes()
	 */
	public ITypeBinding[] getExceptionTypes() {
		if (this.exceptionTypes != null) {
			return exceptionTypes;
		}
		nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding[] exceptions = this.binding.thrownExceptions;
		int length = exceptions == null ? 0 : exceptions.length;
		if (length == 0) {
			return this.exceptionTypes = NO_TYPE_BINDINGS;
		}
		ITypeBinding[] exTypes = new ITypeBinding[length];
		for (int i = 0; i < length; i++) {
			ITypeBinding typeBinding = this.resolver.getTypeBinding(exceptions[i]);
			if (typeBinding == null) {
				return this.exceptionTypes = NO_TYPE_BINDINGS;
			}
			exTypes[i] = typeBinding;
		}
		return this.exceptionTypes = exTypes;
	}

	public IJavaElement getJavaElement() {
		return null;
//		JavaElement element = getUnresolvedJavaElement();
//		if (element == null)
//			return null;
//		return element.resolved(this.binding);
	}

	private IJavaElement getUnresolvedJavaElement() {
		return null;
//		if (!(this.resolver instanceof DefaultBindingResolver)) return null;
//		
//		DefaultBindingResolver defaultBindingResolver = (DefaultBindingResolver) this.resolver;
//		return Util.getUnresolvedJavaElement(
//				this.binding,
//				defaultBindingResolver.workingCopyOwner,
//				defaultBindingResolver.getBindingsToNodesMap());
	}

	/**
	 * @see IBinding#getKind()
	 */
	public int getKind() {
		return IBinding.METHOD;
	}

	/**
	 * @see IBinding#getModifiers()
	 */
	public int getModifiers() {
		return this.binding.getAccessFlags() & VALID_MODIFIERS;
	}

	/**
	 * @see IBinding#isDeprecated()
	 */
	public boolean isDeprecated() {
		return this.binding.isDeprecated();
	}

	/**
	 * @see IBinding#isRecovered()
	 */
	public boolean isRecovered() {
		return false;
	}

	/**
	 * @see IBinding#isSynthetic()
	 */
	public boolean isSynthetic() {
		return this.binding.isSynthetic();
	}

	/**
	 * @see nc.uap.lfw.ca.jdt.core.dom.IMethodBinding#isVarargs()
	 * @since 3.1
	 */
	public boolean isVarargs() {
		return this.binding.isVarargs();
	}

	/**
	 * @see IBinding#getKey()
	 */
	public String getKey() {
		if (this.key == null) {
			this.key = new String(this.binding.computeUniqueKey());
		}
		return this.key;
	}

	/**
	 * @see IBinding#isEqualTo(IBinding)
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
		if (!(other instanceof MethodBinding)) {
			return false;
		}
		nc.uap.lfw.ca.jdt.internal.compiler.lookup.MethodBinding otherBinding = ((MethodBinding) other).binding;
		return BindingComparator.isEqual(this.binding, otherBinding);
	}

	/**
	 * @see nc.uap.lfw.ca.jdt.core.dom.IMethodBinding#getTypeParameters()
	 */
	public ITypeBinding[] getTypeParameters() {
		if (this.typeParameters != null) {
			return this.typeParameters;
		}
		TypeVariableBinding[] typeVariableBindings = this.binding.typeVariables();
		int typeVariableBindingsLength = typeVariableBindings == null ? 0 : typeVariableBindings.length;
		if (typeVariableBindingsLength == 0) {
			return this.typeParameters = NO_TYPE_BINDINGS;
		}
		ITypeBinding[] tParameters = new ITypeBinding[typeVariableBindingsLength];
		for (int i = 0; i < typeVariableBindingsLength; i++) {
			ITypeBinding typeBinding = this.resolver.getTypeBinding(typeVariableBindings[i]);
			if (typeBinding == null) {
				return this.typeParameters = NO_TYPE_BINDINGS;
			}
			tParameters[i] = typeBinding;
		}
		return this.typeParameters = tParameters;
	}

	/**
	 * @see nc.uap.lfw.ca.jdt.core.dom.IMethodBinding#isGenericMethod()
	 * @since 3.1
	 */
	public boolean isGenericMethod() {
		// equivalent to return getTypeParameters().length > 0;
		if (this.typeParameters != null) {
			return this.typeParameters.length > 0;
		}
		TypeVariableBinding[] typeVariableBindings = this.binding.typeVariables();
		return (typeVariableBindings != null && typeVariableBindings.length > 0);
	}

	/**
	 * @see nc.uap.lfw.ca.jdt.core.dom.IMethodBinding#getTypeArguments()
	 */
	public ITypeBinding[] getTypeArguments() {
		if (this.typeArguments != null) {
			return this.typeArguments;
		}

		if (this.binding instanceof ParameterizedGenericMethodBinding) {
			ParameterizedGenericMethodBinding genericMethodBinding = (ParameterizedGenericMethodBinding) this.binding;
			nc.uap.lfw.ca.jdt.internal.compiler.lookup.TypeBinding[] typeArgumentsBindings = genericMethodBinding.typeArguments;
			int typeArgumentsLength = typeArgumentsBindings == null ? 0 : typeArgumentsBindings.length;
			if (typeArgumentsLength != 0) {
				ITypeBinding[] tArguments = new ITypeBinding[typeArgumentsLength];
				for (int i = 0; i < typeArgumentsLength; i++) {
					ITypeBinding typeBinding = this.resolver.getTypeBinding(typeArgumentsBindings[i]);
					if (typeBinding == null) {
						return this.typeArguments = NO_TYPE_BINDINGS;
					}
					tArguments[i] = typeBinding;
				}
				return this.typeArguments = tArguments;
			}
		}
		return this.typeArguments = NO_TYPE_BINDINGS;
	}

	/**
	 * @see nc.uap.lfw.ca.jdt.core.dom.IMethodBinding#isParameterizedMethod()
	 */
	public boolean isParameterizedMethod() {
		return (this.binding instanceof ParameterizedGenericMethodBinding)
			&& !((ParameterizedGenericMethodBinding) this.binding).isRaw;
	}

	/**
	 * @see nc.uap.lfw.ca.jdt.core.dom.IMethodBinding#isRawMethod()
	 */
	public boolean isRawMethod() {
		return (this.binding instanceof ParameterizedGenericMethodBinding)
			&& ((ParameterizedGenericMethodBinding) this.binding).isRaw;
	}

	public boolean isSubsignature(IMethodBinding otherMethod) {
		try {
			LookupEnvironment lookupEnvironment = this.resolver.lookupEnvironment();
			return lookupEnvironment != null
				&& lookupEnvironment.methodVerifier().isMethodSubsignature(this.binding, ((MethodBinding) otherMethod).binding);
		} catch (AbortCompilation e) {
			// don't surface internal exception to clients
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=143013
			return false;
		}
	}

	/**
	 * @see nc.uap.lfw.ca.jdt.core.dom.IMethodBinding#getMethodDeclaration()
	 */
	public IMethodBinding getMethodDeclaration() {
		return this.resolver.getMethodBinding(this.binding.original());
	}

	/**
	 * @see IMethodBinding#overrides(IMethodBinding)
	 */
	public boolean overrides(IMethodBinding otherMethod) {
			LookupEnvironment lookupEnvironment = this.resolver.lookupEnvironment();
			return lookupEnvironment != null
				&& lookupEnvironment.methodVerifier().doesMethodOverride(this.binding, ((MethodBinding) otherMethod).binding);
	}

	/**
	 * For debugging purpose only.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.binding.toString();
	}
}
