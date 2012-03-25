package nc.uap.lfw.ca.jdt.internal.compiler.env;

public interface IGenericMethod {
/**
 * Answer an int whose bits are set according the access constants
 * defined by the VM spec.
 */
// We have added AccDeprecated
int getModifiers();

boolean isConstructor();

/**
 * Answer the names of the argument
 * or null if the argument names are not available.
 */

char[][] getArgumentNames();
}
