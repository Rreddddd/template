package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public abstract class CaptchaUtil {

    private final static Random random = new Random();
    private final static Color[] colors = {Color.BLACK, Color.RED, Color.BLUE};
    private final static String characters = "abcdefghijklmnopqrstuvwsyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String random(int width, int height, OutputStream outputStream) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLUE);
        graphics.drawRect(0, 0, width - 1, height - 1);
        for (int i = 0; i < 10; i++) {
            graphics.setColor(new Color(randNum(0, 255), randNum(0, 255), randNum(0, 255)));
            graphics.drawLine(randNum(0, width), randNum(0, height), randNum(0, width), randNum(0, height));
        }
        graphics.setFont(new Font("黑体", Font.BOLD, 20));
        StringBuilder codes = new StringBuilder();
        int x_offset = 5, y_offset = height / 2 + 5;
        int x_separation = width / 4;
        for (int i = 0; i < 4; i++) {
            int r = randNum(-180, 180);
            String code = characters.charAt(randNum(0, characters.length() - 1)) + "";
            graphics.setColor(colors[randNum(0, colors.length)]);
            graphics.drawString(code, x_offset + i * x_separation, y_offset);
            codes.append(code);
        }
        ImageIO.write(bufferedImage, "jpg", outputStream);
        return codes.toString();
    }

    private static int randNum(int min, int max) {
        if (max < min) {
            max = min;
        }
        return random.nextInt(max - min) + min;
    }
}
