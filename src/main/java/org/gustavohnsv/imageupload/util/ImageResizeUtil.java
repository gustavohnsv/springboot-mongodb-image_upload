package org.gustavohnsv.imageupload.util;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageResizeUtil {

    @NotNull
    public static byte[] scaleAndResize(@NotNull BufferedImage bufferedImage, double resizeFactor, String extension) {
        try {
            int newW = (int) (resizeFactor * bufferedImage.getWidth());
            int newH = (int) (resizeFactor * bufferedImage.getHeight());
            java.awt.Image scaledImage = bufferedImage.getScaledInstance(newW, newH, java.awt.Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = resizedImage.createGraphics();
            graphics.drawImage(scaledImage, 0, 0, null);
            graphics.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, extension, baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            baos.close();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to scale/resize image", e);
        }
    }

}
