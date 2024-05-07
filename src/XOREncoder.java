public class XOREncoder {
    public static byte[] encode(byte[] data, String key) {
        byte[] encoded = new byte[data.length];
        byte[] keyBytes = key.getBytes();
        for (int i = 0; i < data.length; i++) {
            encoded[i] = (byte) (data[i] ^ keyBytes[i % keyBytes.length]);
        }
        return encoded;
    }

    public static byte[] decode(byte[] data, String key) {
        return encode(data, key);
    }
}
