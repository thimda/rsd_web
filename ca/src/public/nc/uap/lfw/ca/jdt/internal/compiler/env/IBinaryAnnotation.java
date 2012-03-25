package nc.uap.lfw.ca.jdt.internal.compiler.env;

/**
 * This represents class file information about an annotation instance.
 */
public interface IBinaryAnnotation {

/**
 * @return the signature of the annotation type.
 */
char[] getTypeName();

/**
 * @return the list of element value pairs of the annotation
 */
IBinaryElementValuePair[] getElementValuePairs();
}
