package nc.uap.lfw.ca.jdt.internal.compiler.env;

public interface IGenericType extends IDependent {

/**
 * Answer an int whose bits are set according the access constants
 * defined by the VM spec.
 * NOTE 1: We have added AccDeprecated & AccSynthetic.
 * NOTE 2: If the receiver represents a member type, the modifiers are extracted from its inner class attributes.
 */
int getModifiers();
/**
 * Answer whether the receiver contains the resolved binary form
 * or the unresolved source form of the type.
 */

boolean isBinaryType();
}
