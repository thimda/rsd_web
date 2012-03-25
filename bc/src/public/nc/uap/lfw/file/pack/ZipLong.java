package nc.uap.lfw.file.pack;

/**
 * Utility class that represents a four byte integer with conversion
 * rules for the big endian byte order of ZIP files.
 *
 */
public final class ZipLong implements Cloneable {

    private long value;

    /**
     * Create instance from a number.
     * @param value the long to store as a ZipLong
     * @since 1.1
     */
    public ZipLong(long value) {
        this.value = value;
    }

    /**
     * Create instance from bytes.
     * @param bytes the bytes to store as a ZipLong
     * @since 1.1
     */
    public ZipLong (byte[] bytes) {
        this(bytes, 0);
    }

    /**
     * Create instance from the four bytes starting at offset.
     * @param bytes the bytes to store as a ZipLong
     * @param offset the offset to start
     * @since 1.1
     */
    public ZipLong (byte[] bytes, int offset) {
        value = ZipLong.getValue(bytes, offset);
    }

    /**
     * Get value as four bytes in big endian byte order.
     * @since 1.1
     * @return value as four bytes in big endian order
     */
    public byte[] getBytes() {
        return ZipLong.getBytes(value);
    }

    /**
     * Get value as Java long.
     * @since 1.1
     * @return value as a long
     */
    public long getValue() {
        return value;
    }

    /**
     * Get value as four bytes in big endian byte order.
     * @param value the value to convert
     * @return value as four bytes in big endian byte order
     */
    public static byte[] getBytes(long value) {
        byte[] result = new byte[4];
        result[0] = (byte) ((value & 0xFF));
        result[1] = (byte) ((value & 0xFF00) >> 8);
        result[2] = (byte) ((value & 0xFF0000) >> 16);
        result[3] = (byte) ((value & 0xFF000000L) >> 24);
        return result;
    }

    /**
     * Helper method to get the value as a Java long from four bytes starting at given array offset
     * @param bytes the array of bytes
     * @param offset the offset to start
     * @return the correspondanding Java long value
     */
    public static long getValue(byte[] bytes, int offset) {
        long value = (bytes[offset + 3] << 24) & 0xFF000000L;
        value += (bytes[offset + 2] << 16) & 0xFF0000;
        value += (bytes[offset + 1] << 8) & 0xFF00;
        value += (bytes[offset] & 0xFF);
        return value;
    }

    /**
     * Helper method to get the value as a Java long from a four-byte array
     * @param bytes the array of bytes
     * @return the correspondanding Java long value
     */
    public static long getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    /**
     * Override to make two instances with same value equal.
     * @param o an object to compare
     * @return true if the objects are equal
     * @since 1.1
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ZipLong)) {
            return false;
        }
        return value == ((ZipLong) o).getValue();
    }

    /**
     * Override to make two instances with same value equal.
     * @return the value stored in the ZipLong
     * @since 1.1
     */
    public int hashCode() {
        return (int) value;
    }
}
