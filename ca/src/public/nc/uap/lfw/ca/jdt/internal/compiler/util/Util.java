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
package nc.uap.lfw.ca.jdt.internal.compiler.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import nc.uap.lfw.ca.jdt.core.compiler.CharOperation;
import nc.uap.lfw.ca.jdt.internal.compiler.ast.TypeDeclaration;
import nc.uap.lfw.ca.jdt.internal.compiler.classfmt.ClassFileConstants;
import nc.uap.lfw.ca.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import nc.uap.lfw.ca.jdt.internal.compiler.util.SuffixConstants;
//import eclipse.jdt.internal.compiler.ClassFile;
//import eclipse.jdt.internal.compiler.util.Displayable;


public class Util implements SuffixConstants {

	public interface Displayable {
		String displayString(Object o);
	}

	private static final int DEFAULT_READING_SIZE = 8192;
	private static final int DEFAULT_WRITING_SIZE = 1024;
	public final static String UTF_8 = "UTF-8";	//$NON-NLS-1$
	public static final String LINE_SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$
	
	public static final String EMPTY_STRING = new String(CharOperation.NO_CHAR);
	public static final int[] EMPTY_INT_ARRAY= new int[0];
	
	/**
	 * Build all the directories and subdirectories corresponding to the packages names
	 * into the directory specified in parameters.
	 *
	 * outputPath is formed like:
	 *	   c:\temp\ the last character is a file separator
	 * relativeFileName is formed like:
	 *     java\lang\String.class *
	 *
	 * @param outputPath java.lang.String
	 * @param relativeFileName java.lang.String
	 * @return java.lang.String
	 */
	public static String buildAllDirectoriesInto(String outputPath, String relativeFileName) throws IOException {
		char fileSeparatorChar = File.separatorChar;
		String fileSeparator = File.separator;
		File f;
		outputPath = outputPath.replace('/', fileSeparatorChar);
			// these could be optimized out if we normalized paths once and for
			// all
		relativeFileName = relativeFileName.replace('/', fileSeparatorChar);
		String outputDirPath, fileName;
		int separatorIndex = relativeFileName.lastIndexOf(fileSeparatorChar);
		if (separatorIndex == -1) {
			if (outputPath.endsWith(fileSeparator)) {
				outputDirPath = outputPath.substring(0, outputPath.length() - 1);
				fileName = outputPath + relativeFileName;
			} else {
				outputDirPath = outputPath;
				fileName = outputPath + fileSeparator + relativeFileName;
			}
		} else {
			if (outputPath.endsWith(fileSeparator)) {
				outputDirPath = outputPath +
					relativeFileName.substring(0, separatorIndex);
				fileName = outputPath + relativeFileName;
			} else {
				outputDirPath = outputPath + fileSeparator +
					relativeFileName.substring(0, separatorIndex);
				fileName = outputPath + fileSeparator + relativeFileName;
			}
		}
		f = new File(outputDirPath);
		f.mkdirs();
		if (f.isDirectory()) {
			return fileName;
		} else {
			// the directory creation failed for some reason - retry using
			// a slower algorithm so as to refine the diagnostic
			if (outputPath.endsWith(fileSeparator)) {
				outputPath = outputPath.substring(0, outputPath.length() - 1);
			}
			f = new File(outputPath);
			boolean checkFileType = false;
			if (f.exists()) {
				  checkFileType = true; // pre-existed
			} else {
				// we have to create that directory
				if (!f.mkdirs()) {
					  if (f.exists()) {
							// someone else created f -- need to check its type
							checkFileType = true;
					  } else {
							// no one could create f -- complain
						throw new IOException(Messages.bind(
							Messages.output_notValidAll, f.getAbsolutePath()));
					  }
				}
			}
			if (checkFileType) {
				  if (!f.isDirectory()) {
					throw new IOException(Messages.bind(
						Messages.output_isFile, f.getAbsolutePath()));
				  }
			}
			StringBuffer outDir = new StringBuffer(outputPath);
			outDir.append(fileSeparator);
			StringTokenizer tokenizer =
				new StringTokenizer(relativeFileName, fileSeparator);
			String token = tokenizer.nextToken();
			while (tokenizer.hasMoreTokens()) {
				f = new File(outDir.append(token).append(fileSeparator).toString());
				  checkFileType = false; // reset
				if (f.exists()) {
					  checkFileType = true; // this is suboptimal, but it catches corner cases
											// in which a regular file pre-exists
				} else {
				// we have to create that directory
					if (!f.mkdir()) {
						  if (f.exists()) {
								// someone else created f -- need to check its type
								checkFileType = true;
						  } else {
								// no one could create f -- complain
							throw new IOException(Messages.bind(
								Messages.output_notValid,
									outDir.substring(outputPath.length() + 1,
										outDir.length() - 1),
									outputPath));
						  }
					}
				}
				if (checkFileType) {
					  if (!f.isDirectory()) {
						throw new IOException(Messages.bind(
							Messages.output_isFile, f.getAbsolutePath()));
					  }
				}
				token = tokenizer.nextToken();
			}
			// token contains the last one
			return outDir.append(token).toString();
		}
	}
	
	/**
	 * Returns the given bytes as a char array using a given encoding (null means platform default).
	 */
	public static char[] bytesToChar(byte[] bytes, String encoding) throws IOException {

		return getInputStreamAsCharArray(new ByteArrayInputStream(bytes), bytes.length, encoding);

	}
	
	/**
	 * Returns the outer most enclosing type's visibility for the given TypeDeclaration
	 * and visibility based on compiler options.  
	 */
	public static int computeOuterMostVisibility(TypeDeclaration typeDeclaration, int visibility) {
		while (typeDeclaration != null) {
			switch (typeDeclaration.modifiers & ExtraCompilerModifiers.AccVisibilityMASK) {
				case ClassFileConstants.AccPrivate:
					visibility = ClassFileConstants.AccPrivate;
					break;
				case ClassFileConstants.AccDefault:
					if (visibility != ClassFileConstants.AccPrivate) {
						visibility = ClassFileConstants.AccDefault;
					}
					break;
				case ClassFileConstants.AccProtected:
					if (visibility == ClassFileConstants.AccPublic) {
						visibility = ClassFileConstants.AccProtected;
					}
					break;
			}
			typeDeclaration = typeDeclaration.enclosingType;
		}	
		return visibility;
	}
	/**
	 * Returns the contents of the given file as a byte array.
	 * @throws IOException if a problem occured reading the file.
	 */
	public static byte[] getFileByteContent(File file) throws IOException {
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
			return getInputStreamAsByteArray(stream, (int) file.length());
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	/**
	 * Returns the contents of the given file as a char array.
	 * When encoding is null, then the platform default one is used
	 * @throws IOException if a problem occured reading the file.
	 */
	public static char[] getFileCharContent(File file, String encoding) throws IOException {
		InputStream stream = null;
		try {
			stream = new FileInputStream(file);
			return getInputStreamAsCharArray(stream, (int) file.length(), encoding);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	private static FileOutputStream getFileOutputStream(boolean generatePackagesStructure, String outputPath, String relativeFileName) throws IOException {
		if (generatePackagesStructure) {
			return new FileOutputStream(new File(buildAllDirectoriesInto(outputPath, relativeFileName)));
		} else {
			String fileName = null;
			char fileSeparatorChar = File.separatorChar;
			String fileSeparator = File.separator;
			// First we ensure that the outputPath exists
			outputPath = outputPath.replace('/', fileSeparatorChar);
			// To be able to pass the mkdirs() method we need to remove the extra file separator at the end of the outDir name
			int indexOfPackageSeparator = relativeFileName.lastIndexOf(fileSeparatorChar);
			if (indexOfPackageSeparator == -1) {
				if (outputPath.endsWith(fileSeparator)) {
					fileName = outputPath + relativeFileName;
				} else {
					fileName = outputPath + fileSeparator + relativeFileName;
				}
			} else {
				int length = relativeFileName.length();
				if (outputPath.endsWith(fileSeparator)) {
					fileName = outputPath + relativeFileName.substring(indexOfPackageSeparator + 1, length);
				} else {
					fileName = outputPath + fileSeparator + relativeFileName.substring(indexOfPackageSeparator + 1, length);
				}
			}
			return new FileOutputStream(new File(fileName));
		}		
	}
	
	/*
	 * NIO support to get input stream as byte array.
	 * Not used as with JDK 1.4.2 this support is slower than standard IO one...
	 * Keep it as comment for future in case of next JDK versions improve performance
	 * in this area...
	 *
	public static byte[] getInputStreamAsByteArray(FileInputStream stream, int length)
		throws IOException {

		FileChannel channel = stream.getChannel();
		int size = (int)channel.size();
		if (length >= 0 && length < size) size = length;
		byte[] contents = new byte[size];
		ByteBuffer buffer = ByteBuffer.wrap(contents);
		channel.read(buffer);
		return contents;
	}
	*/
	/**
	 * Returns the given input stream's contents as a byte array.
	 * If a length is specified (ie. if length != -1), only length bytes
	 * are returned. Otherwise all bytes in the stream are returned.
	 * Note this doesn't close the stream.
	 * @throws IOException if a problem occured reading the stream.
	 */
	public static byte[] getInputStreamAsByteArray(InputStream stream, int length)
		throws IOException {
		byte[] contents;
		if (length == -1) {
			contents = new byte[0];
			int contentsLength = 0;
			int amountRead = -1;
			do {
				int amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE);  // read at least 8K
				
				// resize contents if needed
				if (contentsLength + amountRequested > contents.length) {
					System.arraycopy(
						contents,
						0,
						contents = new byte[contentsLength + amountRequested],
						0,
						contentsLength);
				}

				// read as many bytes as possible
				amountRead = stream.read(contents, contentsLength, amountRequested);

				if (amountRead > 0) {
					// remember length of contents
					contentsLength += amountRead;
				}
			} while (amountRead != -1); 

			// resize contents if necessary
			if (contentsLength < contents.length) {
				System.arraycopy(
					contents,
					0,
					contents = new byte[contentsLength],
					0,
					contentsLength);
			}
		} else {
			contents = new byte[length];
			int len = 0;
			int readSize = 0;
			while ((readSize != -1) && (len != length)) {
				// See PR 1FMS89U
				// We record first the read size. In this case len is the actual read size.
				len += readSize;
				readSize = stream.read(contents, len, length - len);
			}
		}

		return contents;
	}

	/*
	 * NIO support to get input stream as char array.
	 * Not used as with JDK 1.4.2 this support is slower than standard IO one...
	 * Keep it as comment for future in case of next JDK versions improve performance
	 * in this area...
	public static char[] getInputStreamAsCharArray(FileInputStream stream, int length, String encoding)
		throws IOException {
	
		FileChannel channel = stream.getChannel();
		int size = (int)channel.size();
		if (length >= 0 && length < size) size = length;
		Charset charset = encoding==null?systemCharset:Charset.forName(encoding);
		if (charset != null) {
			MappedByteBuffer bbuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
		    CharsetDecoder decoder = charset.newDecoder();
		    CharBuffer buffer = decoder.decode(bbuffer);
		    char[] contents = new char[buffer.limit()];
		    buffer.get(contents);
		    return contents;
		}
		throw new UnsupportedCharsetException(SYSTEM_FILE_ENCODING);
	}
	*/
	/**
	 * Returns the given input stream's contents as a character array.
	 * If a length is specified (ie. if length != -1), this represents the number of bytes in the stream.
	 * Note this doesn't close the stream.
	 * @throws IOException if a problem occured reading the stream.
	 */
	public static char[] getInputStreamAsCharArray(InputStream stream, int length, String encoding)
		throws IOException {
		InputStreamReader reader = null;
		try {
			reader = encoding == null
						? new InputStreamReader(stream)
						: new InputStreamReader(stream, encoding);
		} catch (UnsupportedEncodingException e) {
			// encoding is not supported
			reader =  new InputStreamReader(stream);
		}
		char[] contents;
		int totalRead = 0;
		if (length == -1) {
			contents = CharOperation.NO_CHAR;
		} else {
			// length is a good guess when the encoding produces less or the same amount of characters than the file length
			contents = new char[length]; // best guess
		}

		while (true) {
			int amountRequested;
			if (totalRead < length) {
				// until known length is met, reuse same array sized eagerly
				amountRequested = length - totalRead;
			} else {
				// reading beyond known length
				int current = reader.read(); 
				if (current < 0) break;
				
				amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE);  // read at least 8K
				
				// resize contents if needed
				if (totalRead + 1 + amountRequested > contents.length)
					System.arraycopy(contents, 	0, 	contents = new char[totalRead + 1 + amountRequested], 0, totalRead);
				
				// add current character
				contents[totalRead++] = (char) current; // coming from totalRead==length
			}
			// read as many chars as possible
			int amountRead = reader.read(contents, totalRead, amountRequested);
			if (amountRead < 0) break;
			totalRead += amountRead;
		}

		// Do not keep first character for UTF-8 BOM encoding
		int start = 0;
		if (totalRead > 0 && UTF_8.equals(encoding)) {
			if (contents[0] == 0xFEFF) { // if BOM char then skip
				totalRead--;
				start = 1;
			}
		}
		
		// resize contents if necessary
		if (totalRead < contents.length)
			System.arraycopy(contents, start, contents = new char[totalRead], 	0, 	totalRead);

		return contents;
	}

	public static int getLineNumber(int position, int[] lineEnds, int g, int d) {
		if (lineEnds == null)
			return 1;
		if (d == -1)
			return 1;
		int m = g, start;
		while (g <= d) {
			m = g + (d - g) /2;
			if (position < (start = lineEnds[m])) {
				d = m-1;
			} else if (position > start) {
				g = m+1;
			} else {
				return m + 1;
			}
		}
		if (position < lineEnds[m]) {
			return m+1;
		}
		return m+2;
	}	
	/**
	 * Returns the contents of the given zip entry as a byte array.
	 * @throws IOException if a problem occured reading the zip entry.
	 */
	public static byte[] getZipEntryByteContent(ZipEntry ze, ZipFile zip)
		throws IOException {

		InputStream stream = null;
		try {
			stream = zip.getInputStream(ze);
			if (stream == null) throw new IOException("Invalid zip entry name : " + ze.getName()); //$NON-NLS-1$
			return getInputStreamAsByteArray(stream, (int) ze.getSize());
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}	

	/**
	 * Returns whether the given name is potentially a zip archive file name
	 * (it has a file extension and it is not ".java" nor ".class")
	 */
	public final static boolean isPotentialZipArchive(String name) {
		int lastDot = name.lastIndexOf('.');
		if (lastDot == -1)
			return false; // no file extension, it cannot be a zip archive name
		if (name.lastIndexOf(File.separatorChar) > lastDot)
			return false; // dot was before the last file separator, it cannot be a zip archive name
		int length = name.length();
		int extensionLength = length - lastDot - 1;
		if (extensionLength == EXTENSION_java.length()) {
			for (int i = extensionLength-1; i >=0; i--) {
				if (Character.toLowerCase(name.charAt(length - extensionLength + i)) != EXTENSION_java.charAt(i)) {
					break; // not a ".java" file, check ".class" file case below
				}
				if (i == 0) {
					return false; // it is a ".java" file, it cannot be a zip archive name
				}
			}
		}
		if (extensionLength == EXTENSION_class.length()) {
			for (int i = extensionLength-1; i >=0; i--) {
				if (Character.toLowerCase(name.charAt(length - extensionLength + i)) != EXTENSION_class.charAt(i)) {
					return true; // not a ".class" file, so this is a potential archive name
				}
			}
			return false; // it is a ".class" file, it cannot be a zip archive name
		}
		return true; // it is neither a ".java" file nor a ".class" file, so this is a potential archive name
	}

	/**
	 * Returns true iff str.toLowerCase().endsWith(".class")
	 * implementation is not creating extra strings.
	 */
	public final static boolean isClassFileName(char[] name) {
		int nameLength = name == null ? 0 : name.length;
		int suffixLength = SUFFIX_CLASS.length;
		if (nameLength < suffixLength) return false;

		for (int i = 0, offset = nameLength - suffixLength; i < suffixLength; i++) {
			char c = name[offset + i];
			if (c != SUFFIX_class[i] && c != SUFFIX_CLASS[i]) return false;
		}
		return true;		
	}			
	/**
	 * Returns true iff str.toLowerCase().endsWith(".class")
	 * implementation is not creating extra strings.
	 */
	public final static boolean isClassFileName(String name) {
		int nameLength = name == null ? 0 : name.length();
		int suffixLength = SUFFIX_CLASS.length;
		if (nameLength < suffixLength) return false;

		for (int i = 0; i < suffixLength; i++) {
			char c = name.charAt(nameLength - i - 1);
			int suffixIndex = suffixLength - i - 1;
			if (c != SUFFIX_class[suffixIndex] && c != SUFFIX_CLASS[suffixIndex]) return false;
		}
		return true;		
	}
	/* TODO (philippe) should consider promoting it to CharOperation
	 * Returns whether the given resource path matches one of the inclusion/exclusion
	 * patterns.
	 * NOTE: should not be asked directly using pkg root pathes
	 * @see IClasspathEntry#getInclusionPatterns
	 * @see IClasspathEntry#getExclusionPatterns
	 */
	public final static boolean isExcluded(char[] path, char[][] inclusionPatterns, char[][] exclusionPatterns, boolean isFolderPath) {
		if (inclusionPatterns == null && exclusionPatterns == null) return false;

		inclusionCheck: if (inclusionPatterns != null) {
			for (int i = 0, length = inclusionPatterns.length; i < length; i++) {
				char[] pattern = inclusionPatterns[i];
				char[] folderPattern = pattern;
				if (isFolderPath) {
					int lastSlash = CharOperation.lastIndexOf('/', pattern);
					if (lastSlash != -1 && lastSlash != pattern.length-1){ // trailing slash -> adds '**' for free (see http://ant.apache.org/manual/dirtasks.html)
						int star = CharOperation.indexOf('*', pattern, lastSlash);
						if ((star == -1
								|| star >= pattern.length-1 
								|| pattern[star+1] != '*')) {
							folderPattern = CharOperation.subarray(pattern, 0, lastSlash);
						}
					}
				}
				if (CharOperation.pathMatch(folderPattern, path, true, '/')) {
					break inclusionCheck;
				}
			}
			return true; // never included
		}
		if (isFolderPath) {
			path = CharOperation.concat(path, new char[] {'*'}, '/');
		}
		if (exclusionPatterns != null) {
			for (int i = 0, length = exclusionPatterns.length; i < length; i++) {
				if (CharOperation.pathMatch(exclusionPatterns[i], path, true, '/')) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true iff str.toLowerCase().endsWith(".java")
	 * implementation is not creating extra strings.
	 */
	public final static boolean isJavaFileName(char[] name) {
		int nameLength = name == null ? 0 : name.length;
		int suffixLength = SUFFIX_JAVA.length;
		if (nameLength < suffixLength) return false;

		for (int i = 0, offset = nameLength - suffixLength; i < suffixLength; i++) {
			char c = name[offset + i];
			if (c != SUFFIX_java[i] && c != SUFFIX_JAVA[i]) return false;
		}
		return true;		
	}

	/**
	 * Returns true iff str.toLowerCase().endsWith(".java")
	 * implementation is not creating extra strings.
	 */
	public final static boolean isJavaFileName(String name) {
		int nameLength = name == null ? 0 : name.length();
		int suffixLength = SUFFIX_JAVA.length;
		if (nameLength < suffixLength) return false;

		for (int i = 0; i < suffixLength; i++) {
			char c = name.charAt(nameLength - i - 1);
			int suffixIndex = suffixLength - i - 1;
			if (c != SUFFIX_java[suffixIndex] && c != SUFFIX_JAVA[suffixIndex]) return false;
		}
		return true;
	}

	public static void reverseQuickSort(char[][] list, int left, int right) {
		int original_left= left;
		int original_right= right;
		char[] mid= list[(right + left) / 2];
		do {
			while (CharOperation.compareTo(list[left], mid) > 0) {
				left++;
			}
			while (CharOperation.compareTo(mid, list[right]) > 0) {
				right--;
			}
			if (left <= right) {
				char[] tmp= list[left];
				list[left]= list[right];
				list[right]= tmp;
				left++;
				right--;
			}
		} while (left <= right);
		if (original_left < right) {
			reverseQuickSort(list, original_left, right);
		}
		if (left < original_right) {
			reverseQuickSort(list, left, original_right);
		}
	}
	public static void reverseQuickSort(char[][] list, int left, int right, int[] result) {
		int original_left= left;
		int original_right= right;
		char[] mid= list[(right + left) / 2];
		do {
			while (CharOperation.compareTo(list[left], mid) > 0) {
				left++;
			}
			while (CharOperation.compareTo(mid, list[right]) > 0) {
				right--;
			}
			if (left <= right) {
				char[] tmp= list[left];
				list[left]= list[right];
				list[right]= tmp;
				int temp = result[left];
				result[left] = result[right];
				result[right] = temp;
				left++;
				right--;
			}
		} while (left <= right);
		if (original_left < right) {
			reverseQuickSort(list, original_left, right, result);
		}
		if (left < original_right) {
			reverseQuickSort(list, left, original_right, result);
		}
	}
	/**
	 * INTERNAL USE-ONLY
	 * Search the column number corresponding to a specific position
	 */
	public static final int searchColumnNumber(int[] startLineIndexes, int lineNumber, int position) {
		switch(lineNumber) {
			case 1 :
				return position + 1;
			case 2:
				return position - startLineIndexes[0];
			default:
				int line = lineNumber - 2;
	    		int length = startLineIndexes.length;
	    		if (line >= length) {
	    			return position - startLineIndexes[length - 1];
	    		}
	    		return position - startLineIndexes[line];
		}
	}

	/**
	 * Converts a boolean value into Boolean.
	 * @param bool The boolean to convert
	 * @return The corresponding Boolean object (TRUE or FALSE).
	 */
	public static Boolean toBoolean(boolean bool) {
		if (bool) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	/**
	 * Converts an array of Objects into String.
	 */
	public static String toString(Object[] objects) {
		return toString(objects, 
			new Displayable(){ 
				public String displayString(Object o) { 
					if (o == null) return "null"; //$NON-NLS-1$
					return o.toString(); 
				}
			});
	}

	/**
	 * Converts an array of Objects into String.
	 */
	public static String toString(Object[] objects, Displayable renderer) {
		if (objects == null) return ""; //$NON-NLS-1$
		StringBuffer buffer = new StringBuffer(10);
		for (int i = 0; i < objects.length; i++){
			if (i > 0) buffer.append(", "); //$NON-NLS-1$
			buffer.append(renderer.displayString(objects[i]));
		}
		return buffer.toString();
	}

	/**
	 * outputPath is formed like:
	 *	   c:\temp\ the last character is a file separator
	 * relativeFileName is formed like:
	 *     java\lang\String.class
	 * @param generatePackagesStructure a flag to know if the packages structure has to be generated.
	 * @param outputPath the given output directory
	 * @param relativeFileName the given relative file name
	 * @param classFile the given classFile to write
	 *
	 */
//	public static void writeToDisk(boolean generatePackagesStructure, String outputPath, String relativeFileName, ClassFile classFile) throws IOException {
//		FileOutputStream file = getFileOutputStream(generatePackagesStructure, outputPath, relativeFileName);
//		/* use java.nio to write
//		if (true) {
//			FileChannel ch = file.getChannel();
//			try {
//				ByteBuffer buffer = ByteBuffer.allocate(classFile.headerOffset + classFile.contentsOffset);
//				buffer.put(classFile.header, 0, classFile.headerOffset);
//				buffer.put(classFile.contents, 0, classFile.contentsOffset);
//				buffer.flip();
//				while (true) {
//					if (ch.write(buffer) == 0) break;
//				}
//			} finally {
//				ch.close();
//			}
//			return;
//		}
//		*/
//		BufferedOutputStream output = new BufferedOutputStream(file, DEFAULT_WRITING_SIZE);
////		BufferedOutputStream output = new BufferedOutputStream(file);
//		try {
//			// if no IOException occured, output cannot be null
//			output.write(classFile.header, 0, classFile.headerOffset);
//			output.write(classFile.contents, 0, classFile.contentsOffset);
//			output.flush();
//		} catch(IOException e) {
//			throw e;
//		} finally {
//			output.close();
//		}
//	}
}
