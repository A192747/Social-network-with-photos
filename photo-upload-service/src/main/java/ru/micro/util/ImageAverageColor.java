package ru.micro.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageAverageColor {
    public static String getAverageColorHex(InputStream inputStream) throws IOException {
        // Преобразование InputStream в BufferedImage
        BufferedImage image = ImageIO.read(inputStream);

        long red = 0, green = 0, blue = 0;
        int width = image.getWidth();
        int height = image.getHeight();

        // Итерация по всем пикселям изображения
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                red += (rgb >> 16) & 0xFF;
                green += (rgb >> 8) & 0xFF;
                blue += rgb & 0xFF;
            }
        }

        // Вычисление среднего значения для каждого цвета
        red /= (width * height);
        green /= (width * height);
        blue /= (width * height);

        // Преобразование среднего цвета в формат HEX
        String hexColor = String.format("#%02x%02x%02x", red, green, blue);

        return hexColor;
    }
}
