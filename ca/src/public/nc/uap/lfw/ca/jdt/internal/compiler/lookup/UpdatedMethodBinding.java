package nc.uap.lfw.ca.jdt.internal.compiler.lookup;

public class UpdatedMethodBinding extends MethodBinding {
	
	public TypeBinding updatedDeclaringClass;

	public UpdatedMethodBinding(TypeBinding updatedDeclaringClass, int modifiers, char[] selector, TypeBinding returnType, TypeBinding[] args, ReferenceBinding[] exceptions, ReferenceBinding declaringClass) {
		super(modifiers, selector, returnType, args, exceptions, declaringClass);
		this.updatedDeclaringClass = updatedDeclaringClass;
	}
	
	public TypeBinding constantPoolDeclaringClass() {
		return this.updatedDeclaringClass;
	}
}
