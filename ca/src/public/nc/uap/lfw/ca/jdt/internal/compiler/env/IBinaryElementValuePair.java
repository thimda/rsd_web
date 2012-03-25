package nc.uap.lfw.ca.jdt.internal.compiler.env;

/**
 * This represents the class file information about a member value pair of an annotation.
 */
public interface IBinaryElementValuePair {

/** @return the name of the member */
char[] getName();

/**
 * Return {@link ClassSignature} for a Class {@link java.lang.Class}.
 * Return {@link org.eclipse.jdt.internal.compiler.impl.Constant} for compile-time constant of primitive type, as well as String literals.
 * Return {@link EnumConstantSignature} if value is an enum constant.
 * Return {@link IBinaryAnnotation} for annotation type.
 * Return {@link Object}[] for array type.
 * 
 * @return the value of this member value pair
 */
Object getValue();
}
