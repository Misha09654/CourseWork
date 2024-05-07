import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class StegoMessenger {
    private static final String END_OF_MESSAGE = "EOF";

    public static void writeMessage(File bmpFile, String message, String key) {
        try {
            BufferedImage image = ImageIO.read(bmpFile);
            message += END_OF_MESSAGE;
            byte[] messageBytes = message.getBytes();
            byte[] keyBytes = key.getBytes();
            byte[] encryptedMessage = new byte[messageBytes.length];

            for (int i = 0; i < messageBytes.length; i++) {
                encryptedMessage[i] = (byte) (messageBytes[i] ^ keyBytes[i % keyBytes.length]);
            }

            int bitIndex = 0;
            boolean finished = false;

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (bitIndex >= encryptedMessage.length * 8) {
                        finished = true;
                        break;
                    }

                    int color = image.getRGB(x, y);
                    int alpha = (color >> 24) & 0xff;
                    int red = (color >> 16) & 0xfe;
                    int green = (color >> 8) & 0xfe;
                    int blue = color & 0xfe;

                    if (bitIndex < encryptedMessage.length * 8) {
                        red |= (encryptedMessage[bitIndex / 8] >> (7 - bitIndex % 8)) & 1;
                        bitIndex++;
                    }
                    if (bitIndex < encryptedMessage.length * 8) {
                        green |= (encryptedMessage[bitIndex / 8] >> (7 - bitIndex % 8)) & 1;
                        bitIndex++;
                    }
                    if (bitIndex < encryptedMessage.length * 8) {
                        blue |= (encryptedMessage[bitIndex / 8] >> (7 - bitIndex % 8)) & 1;
                        bitIndex++;
                    }

                    color = (alpha << 24) | (red << 16) | (green << 8) | blue;
                    image.setRGB(x, y, color);
                }
                if (finished) break;
            }

            ImageIO.write(image, "bmp", bmpFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String readMessage(File bmpFile, String key) {
        try {
            BufferedImage image = ImageIO.read(bmpFile);
            byte[] keyBytes = key.getBytes();
            byte[] decryptedMessage = new byte[image.getWidth() * image.getHeight() / 8];
            int bitIndex = 0;

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (bitIndex / 8 < decryptedMessage.length) {
                        int color = image.getRGB(x, y);
                        decryptedMessage[bitIndex / 8] |= ((color >> 16) & 1) << (7 - bitIndex % 8);
                        bitIndex++;
                        decryptedMessage[bitIndex / 8] |= ((color >> 8) & 1) << (7 - bitIndex % 8);
                        bitIndex++;
                        decryptedMessage[bitIndex / 8] |= (color & 1) << (7 - bitIndex % 8);
                        bitIndex++;
                    }
                }
            }

            for (int i = 0; i < decryptedMessage.length; i++) {
                decryptedMessage[i] ^= keyBytes[i % keyBytes.length];
            }

            String result = new String(decryptedMessage);
            return result.substring(0, result.indexOf(END_OF_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}