package org.gustavohnsv.imageupload.util;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageCompressionUtil {

    @NotNull
    public static byte[] compress(byte[] data) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
            Deflater deflater = new Deflater();
            deflater.setInput(data);
            deflater.finish();
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                bos.write(buffer, 0, count);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to compress image", e);
        }
    }

    @NotNull
    public static byte[] decompress(byte[] data) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                bos.write(buffer, 0, count);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress image", e);
        } catch (DataFormatException e) {
            throw new RuntimeException("Incorrect data format", e);
        }
    }

}
