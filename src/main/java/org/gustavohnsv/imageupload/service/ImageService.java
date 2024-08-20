package org.gustavohnsv.imageupload.service;

import org.gustavohnsv.imageupload.model.Image;
import org.gustavohnsv.imageupload.repository.ImageRepository;
import org.gustavohnsv.imageupload.util.ImageCompressionUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ImageService {

    private static final Set<String> VALID_EXTENSIONS = new HashSet<>(Set.of(
            "jpg",
            "jpeg",
            "gif",
            "png",
            "bmp",
            "webp",
            "svg",
            "tiff",
            "tif"
    ));

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getImage(String id) {
        return imageRepository.findFirstById(id);
    }

    private boolean validateExtension(@NotNull String filename) {
        if (filename.contains("..")) {
            return false;
        }
        return VALID_EXTENSIONS.contains(filename.substring(filename.lastIndexOf(".") + 1).toLowerCase());
    }

    private boolean validateContentType(@NotNull String contentType) {
        if (!(contentType.startsWith("image/"))) {
            return false;
        }
        return VALID_EXTENSIONS.contains(contentType.substring("image/".length()));
    }

    @NotNull
    private String calculateResolutionType(int resolution) {
       return ResolutionType.getResolutionType(resolution);
    }

    @NotNull
    private String calculateSizeRemaining(double sizeRemaining, String actualCharacter) {
        if (sizeRemaining > 1024) {
            switch (actualCharacter) {
                case "B" -> actualCharacter = "KB";
                case "KB" -> actualCharacter = "MB";
                case "MB" -> actualCharacter = "GB";
            }
            sizeRemaining = sizeRemaining / 1024;
            return calculateSizeRemaining(sizeRemaining, actualCharacter);
        }
        return Math.ceil(sizeRemaining) + " " + actualCharacter;
    }

    @NotNull
    private String calculateSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        return calculateSizeRemaining(size, "B");
    }

    public void saveImage(@NotNull MultipartFile file) {
        if (validateExtension(Objects.requireNonNull(file.getOriginalFilename()))
                & validateContentType(Objects.requireNonNull(file.getContentType()))) {
            try (InputStream inputStream = file.getInputStream()) {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                byte[] compressedImageData = ImageCompressionUtil.compress(file.getBytes());
                imageRepository
                        .save(new Image(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                calculateResolutionType(bufferedImage.getHeight()),
                                new int[]{bufferedImage.getWidth(), bufferedImage.getHeight()},
                                calculateSize(compressedImageData.length),
                                compressedImageData)
                                );
            } catch (IOException e) {
                System.out.println("Error saving image: " + e.getMessage());
            }
        }
    }

    public Image retriveImage(@NotNull String id) {
        Image image = this.getImage(id);
        return new Image(image.getName(),
                image.getContentType(),
                image.getResolutionType(),
                image.getResolution(),
                image.getContentType(),
                ImageCompressionUtil.decompress(image.getData()));
    }

}
