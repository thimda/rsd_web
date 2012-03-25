package nc.uap.lfw.core.servlet.filter.compression;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Implementations of this interface encapsulate an {@link OutputStream} that compresses data written to it. This
 * includes the compressing {@link OutputStream} itself (see {@link #getCompressingOutputStream()}), and the ability to
 * tell the stream that no more data will be written, so that the stream may write any trailing data needed by the
 * compression algorithm (see {@link #finish()}).
 *
 * @author Sean Owen
 */
interface CompressingOutputStream {

	OutputStream getCompressingOutputStream();

	void finish() throws IOException;

}
