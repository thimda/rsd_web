package nc.uap.lfw.ca.jdt.internal.compiler.env;

import nc.uap.lfw.ca.jdt.internal.compiler.impl.Constant;

public interface IBinaryField extends IGenericField {
/**
 * Answer the runtime visible and invisible annotations for this field or null if none.
 */
IBinaryAnnotation[] getAnnotations();

/**
 * 
 * @return eclipse.jdt.internal.compiler.Constant
 */
Constant getConstant();

/**
 * Answer the receiver's signature which describes the parameter &
 * return types as specified in section 4.4.4 of the Java 2 VM spec.
 */
char[] getGenericSignature();

/**
 * Answer the name of the field.
 */
char[] getName();

/**
 * Answer the tagbits set according to the bits for annotations.
 */
long getTagBits();

/**
 * Answer the resolved name of the receiver's type in the
 * class file format as specified in section 4.3.2 of the Java 2 VM spec.
 *
 * For example:
 *   - java.lang.String is Ljava/lang/String;
 *   - an int is I
 *   - a 2 dimensional array of strings is [[Ljava/lang/String;
 *   - an array of floats is [F
 */
char[] getTypeName();
}
