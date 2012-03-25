package nc.uap.lfw.core.servlet.filter.compression;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * Implementation of {@link HttpServletResponse} which will optionally compress
 * data written to the response.
 * 
 * @author Sean Owen
 */
final class CompressingHttpServletResponse extends HttpServletResponseWrapper {

	static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
	private static final String CACHE_CONTROL_HEADER = "Cache-Control";
	static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
	private static final String CONTENT_LENGTH_HEADER = "Content-Length";
	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String ETAG_HEADER = "ETag";
	static final String VARY_HEADER = "Vary";
	private static final String X_COMPRESSED_BY_HEADER = "X-Compressed-By";

	private static final String[] UNALLOWED_HEADERS = { CACHE_CONTROL_HEADER,
			CONTENT_LENGTH_HEADER, CONTENT_ENCODING_HEADER, ETAG_HEADER,
			X_COMPRESSED_BY_HEADER };

	private static final String COMPRESSED_BY_VALUE = CompressingFilter.VERSION_STRING;

	private final HttpServletResponse httpResponse;

	private final CompressingFilterContext context;

	private final String compressedContentEncoding;
	private final CompressingStreamFactory compressingStreamFactory;
	private CompressingServletOutputStream compressingSOS;

	private PrintWriter printWriter;
	private boolean isGetOutputStreamCalled;
	private boolean isGetWriterCalled;

	private boolean compressing;

	private long savedContentLength;
	private boolean savedContentLengthSet;
	private String savedContentEncoding;
	private String savedETag;
	private boolean contentTypeOK;
	private boolean noTransformSet;
	private String method;
	CompressingHttpServletResponse(HttpServletResponse httpResponse,
			CompressingStreamFactory compressingStreamFactory,
			String contentEncoding, CompressingFilterContext context, String method) {
		super(httpResponse);
		this.httpResponse = httpResponse;
		this.compressedContentEncoding = contentEncoding;
		compressing = false;
		this.compressingStreamFactory = compressingStreamFactory;
		this.context = context;
		contentTypeOK = true;
		setCommonResponseHeaders();
		this.method = method;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (isGetWriterCalled) {
			throw new IllegalStateException(
					"getWriter() has already been called");
		}
		isGetOutputStreamCalled = true;
		return getCompressingServletOutputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (isGetOutputStreamCalled) {
			throw new IllegalStateException(
					"getCompressingOutputStream() has already been called");
		}
		isGetWriterCalled = true;
		if (printWriter == null) {
			printWriter = new PrintWriter(
					new OutputStreamWriter(getCompressingServletOutputStream(),
							getCharacterEncoding()), true);
		}
		return printWriter;
	}

	@Override
	public void addHeader(String name, String value) {
		if (isAllowedHeader(name)) {
			httpResponse.addHeader(name, value);
		}
	}

	@Override
	public void addIntHeader(String name, int value) {
		if (isAllowedHeader(name)) {
			httpResponse.addIntHeader(name, value);
		}
	}

	@Override
	public void addDateHeader(String name, long value) {
		if (isAllowedHeader(name)) {
			httpResponse.addDateHeader(name, value);
		}
	}

	@Override
	public void setHeader(String name, String value) {
		if (CACHE_CONTROL_HEADER.equalsIgnoreCase(name)) {
			httpResponse.setHeader(CACHE_CONTROL_HEADER, value);
			if (value.contains("no-transform")) {
				noTransformSet = true;
				maybeAbortCompression();
			}
		} else if (CONTENT_ENCODING_HEADER.equalsIgnoreCase(name)) {
			savedContentEncoding = value;
			if (!isCompressableEncoding(value)) {
				maybeAbortCompression();
			}
		} else if (CONTENT_LENGTH_HEADER.equalsIgnoreCase(name)) {
			// Not setContentLength(); we want to potentially accommodate a long
			// value here
			doSetContentLength(Long.parseLong(value));
		} else if (CONTENT_TYPE_HEADER.equalsIgnoreCase(name)) {
			setContentType(value);
		} else if (ETAG_HEADER.equalsIgnoreCase(name)) {
			// Later, when the container perhaps sets ETag, try to set a
			// different value (just by
			// appending "-gzip" for instance) to reflect that the body is not
			// the same as the uncompressed
			// version. Otherwise caches may incorrectly return the compressed
			// version to a
			// client that doesn't want it
			savedETag = value;
			setETagHeader();
		} else if (isAllowedHeader(name)) {
			httpResponse.setHeader(name, value);
		}
	}
	
	

	@Override
	public void sendError(int sc, String msg) throws IOException {
//		this.errorCode = sc;
//		super.sendError(sc, msg);
		this.getWriter().write(method + " " + sc + " Error");
	}

	@Override
	public void sendError(int sc) throws IOException {
//		this.errorCode = sc;
		this.getWriter().write(method + " " + sc + " Error");
//		super.sendError(sc);
	}

	@SuppressWarnings("restriction")
	@Override
	public void sendRedirect(String location) throws IOException {
		super.sendRedirect(location);
	}

	@SuppressWarnings("restriction")
	@Override
	public void setStatus(int sc, String sm) {
		super.setStatus(sc, sm);
	}

	@SuppressWarnings("restriction")
	@Override
	public void setStatus(int sc) {
		super.setStatus(sc);
	}

	private void maybeAbortCompression() {
		if (compressing) {
			try {
				compressingSOS.abortCompression();
			} catch (IOException ioe) {
				LfwLogger.error("Unexpected error while aborting compression",
						ioe);
			}
		}
	}

	private void setETagHeader() {
		if (savedETag != null) {
			if (compressing) {
				httpResponse.setHeader(ETAG_HEADER, savedETag + '-'
						+ compressedContentEncoding);
			} else {
				httpResponse.setHeader(ETAG_HEADER, savedETag);
			}
		}
	}

	@Override
	public void setIntHeader(String name, int value) {
		if (CONTENT_LENGTH_HEADER.equalsIgnoreCase(name)) {
			setContentLength(value);
		} else if (ETAG_HEADER.equalsIgnoreCase(name)) {
			// Later, when the container perhaps sets ETag, try to set a
			// different value (just by
			// appending "-gzip" for instance) to reflect that the body is not
			// the same as the uncompressed
			// version. Otherwise caches may incorrectly return the compressed
			// version to a
			// client that doesn't want it
			savedETag = String.valueOf(value);
			setETagHeader();
		} else if (isAllowedHeader(name)) {
			httpResponse.setIntHeader(name, value);
		}
	}

	@Override
	public void setDateHeader(String name, long value) {
		if (isAllowedHeader(name)) {
			httpResponse.setDateHeader(name, value);
		}
	}

	@Override
	public void flushBuffer() throws IOException {
		flushWriter(); // make sure nothing is buffered in the writer, if
						// applicable
		if (compressingSOS != null) {
			compressingSOS.flush();
		}
		
		httpResponse.flushBuffer();
	}

	@Override
	public void reset() {
		flushWriter(); // make sure nothing is buffered in the writer, if
						// applicable
		if (compressingSOS != null) {
			compressingSOS.reset();
		}
		httpResponse.reset();
		setCommonResponseHeaders();
		if (compressing) {
			setCompressionResponseHeaders();
		}
	}

	@Override
	public void resetBuffer() {
		flushWriter(); // make sure nothing is buffered in the writer, if
						// applicable
		if (compressingSOS != null) {
			compressingSOS.reset();
		}
		httpResponse.resetBuffer();
	}

	@Override
	public void setContentLength(int contentLength) {
		// Internally we want to be able to handle a long contentLength, but the
		// ServletResponse method
		// is declared to take an int. So we delegate to a private version that
		// handles a long, and reuse
		// that version elsewhere here.
		doSetContentLength((long) contentLength);
	}

	private void doSetContentLength(long contentLength) {
		if (compressing) {
			// do nothing -- caller-supplied content length is not meaningful
			LfwLogger
					.debug("Ignoring application-specified content length since response is compressed");
		} else {
			savedContentLength = contentLength;
			savedContentLengthSet = true;
			LfwLogger
					.debug("Saving application-specified content length for later: "
							+ contentLength);
		}
	}

	@Override
	public void setContentType(String contentType) {
		contentTypeOK = isCompressableContentType(contentType);
		if (!compressing) {
			if (!contentTypeOK && compressingSOS != null) {
				maybeAbortCompression();
			}
			httpResponse.setContentType(contentType);
		}
	}

	@Override
	public String toString() {
		return "CompressingHttpServletResponse[compressing: " + compressing
				+ ']';
	}

	boolean isCompressing() {
		return compressing;
	}

	void close() throws IOException {
		if (compressingSOS != null && !compressingSOS.isClosed()) {
			compressingSOS.close();
		}
	}

	private void setCommonResponseHeaders() {
		httpResponse.addHeader(VARY_HEADER, ACCEPT_ENCODING_HEADER);
	}

	private void setCompressionResponseHeaders() {
		String fullContentEncodingHeader = savedContentEncoding == null ? compressedContentEncoding
				: savedContentEncoding + ',' + compressedContentEncoding;
		httpResponse.setHeader(CONTENT_ENCODING_HEADER,
				fullContentEncodingHeader);
		setETagHeader();

	}

	void rawStreamCommitted() {
		assert !compressing;
		if (savedContentLengthSet) {
			// setContentLength() takes an int; we're trying to accommodate the
			// case where the
			// content length exceeded Integer.MAX_VALUE. We saved it as a long,
			// but can't send
			// that to the underlying response's setContentLength() method
			// unless it's not more
			// than Integer.MAX_VALUE. If it is, we try to write it directly as
			// a String to
			// the header
			if (savedContentLength <= (long) Integer.MAX_VALUE) {
				httpResponse.setContentLength((int) savedContentLength);
			} else {
				httpResponse.setHeader(CONTENT_LENGTH_HEADER, String
						.valueOf(savedContentLength));
			}
		}
		if (savedContentEncoding != null) {
			httpResponse.setHeader(CONTENT_ENCODING_HEADER,
					savedContentEncoding);
		}
	}

	void switchToCompression() {
		assert !compressing;
		compressing = true;
		setCompressionResponseHeaders();
	}

	/**
	 * <p>
	 * Returns true if and only if the named HTTP header may be set directly by
	 * the application, as some headers must be handled specially. null is
	 * allowed, though it setting a header named null will probably generate an
	 * exception from the underlying {@link HttpServletResponse}.
	 * {@link #CONTENT_LENGTH_HEADER}, {@link #CONTENT_ENCODING_HEADER} and
	 * {@link #X_COMPRESSED_BY_HEADER} are not allowed.
	 * </p>
	 * 
	 * @param header
	 *            name of HTTP header
	 * @return true if and only if header can be set directly by application
	 */
	private boolean isAllowedHeader(String header) {
		boolean unallowed = header != null
				&& equalsIgnoreCaseAny(header, UNALLOWED_HEADERS);
		return !unallowed;
	}

	private static boolean equalsIgnoreCaseAny(String a, String... others) {
		for (String other : others) {
			if (a.equalsIgnoreCase(other)) {
				return true;
			}
		}
		return false;
	}

	private void flushWriter() {
		if (printWriter != null) {
			printWriter.flush();
		}
	}

	/**
	 * <p>
	 * Checks to see if the given content type should be compressed. If the
	 * content type indicates it is already a compressed format (e.g. contains
	 * "gzip") then this wil return <code>false</code>.
	 * </p>
	 * 
	 * <p>
	 * Otherwise this checks against the <code>includeContentTypes</code> and
	 * <code>excludeContentTypes</code> filter init parameters; if the former is
	 * set and the given content type is in that parameter's list, or if the
	 * latter is set and the content type is not in that list, then this method
	 * returns <code>true</code>.
	 * </p>
	 * 
	 * @param contentType
	 *            content type of response
	 * @return true if and only if the given content type should be compressed
	 */
	private boolean isCompressableContentType(String contentType) {
		String contentTypeOnly = contentType;
		if (contentType != null) {
			int semicolonIndex = contentType.indexOf((int) ';');
			if (semicolonIndex >= 0) {
				contentTypeOnly = contentType.substring(0, semicolonIndex);
			}
		} else {
			return true;
		}

		for (String compressionEncoding : CompressingStreamFactory.ALL_COMPRESSION_ENCODINGS) {
			if (contentTypeOnly.contains(compressionEncoding)) {
				return false;
			}
		}
		boolean isContained = context.getContentTypes().contains(
				contentTypeOnly);
		return context.isIncludeContentTypes() ? isContained : !isContained;
	}

	private boolean isCompressableEncoding(String encoding) {
		if (encoding == null) {
			return true;
		}
		for (String compressionEncoding : CompressingStreamFactory.ALL_COMPRESSION_ENCODINGS) {
			if (encoding.equals(compressionEncoding)) {
				return false;
			}
		}
		return true;
	}

	private CompressingServletOutputStream getCompressingServletOutputStream()
			throws IOException {
		if (compressingSOS == null) {
			compressingSOS = new CompressingServletOutputStream(httpResponse
					.getOutputStream(), compressingStreamFactory, this,
					context);
		}

		if (!compressingSOS.isClosed()) {
			// Do we already know we don't want to compress?
			// Is there a reason we know compression will be used, already?
			if (mustNotCompress()) {
				compressingSOS.abortCompression();
			} else if (mustCompress()) {
				compressingSOS.engageCompression();
			}
		}

		return compressingSOS;
	}

	private boolean mustNotCompress() {
		if (!contentTypeOK) {
			return true;
		}
		if (savedContentLengthSet
				&& savedContentLength < (long) context
						.getCompressionThreshold()) {
			return true;
		}
		if (noTransformSet) {
			return true;
		}
		if (!isCompressableEncoding(savedContentEncoding)) {
			return true;
		}
		return false;
	}

	private boolean mustCompress() {
		int threshold = context.getCompressionThreshold();
		if (threshold <= 0
				|| (savedContentLengthSet && savedContentLength >= threshold)) {
			return true;
		}
		return false;
	}
}
