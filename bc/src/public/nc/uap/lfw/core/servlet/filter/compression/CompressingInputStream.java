package nc.uap.lfw.core.servlet.filter.compression;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementations of this interface encapsulate an {@link InputStream} that decompresses data read from it.
 * 
 * @author Sean Owen
 * @since 1.6
 */
interface CompressingInputStream {

	InputStream getCompressingInputStream() throws IOException;

}
