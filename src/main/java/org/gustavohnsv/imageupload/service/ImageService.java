package org.gustavohnsv.imageupload.service;

import org.apache.commons.lang3.StringUtils;
import org.gustavohnsv.imageupload.model.Image;
import org.gustavohnsv.imageupload.repository.ImageRepository;
import org.gustavohnsv.imageupload.util.ImageCompressionUtil;
import org.gustavohnsv.imageupload.util.ImageResizeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        if (StringUtils.countMatches(filename, ".") > 1) {
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

    public boolean saveImage(@NotNull MultipartFile file, Optional<String> filename, double resizeFactor) {
        String fileName = filename.orElseGet(file::getOriginalFilename);
        if (validateExtension(Objects.requireNonNull(fileName))
                & validateContentType(Objects.requireNonNull(file.getContentType()))) {
            try (InputStream inputStream = file.getInputStream()) {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                byte[] compressedImageData = ImageCompressionUtil.compress(
                        ImageResizeUtil.scaleAndResize(
                                bufferedImage,
                                resizeFactor,
                                getExtension(file.getOriginalFilename())
                        )
                );
                imageRepository
                        .save(new Image(
                                fileName,
                                file.getContentType(),
                                calculateResolutionType((int) resizeFactor * bufferedImage.getWidth()),
                                new int[]{
                                        (int) (resizeFactor * bufferedImage.getWidth()),
                                        (int) (resizeFactor * bufferedImage.getHeight())
                                },
                                calculateSize(compressedImageData.length),
                                compressedImageData)
                                );
                return true;
            } catch (IOException e) {
                System.out.println("Error saving image: " + e.getMessage());
            }
        }
        return false;
    }

    private String getExtension(String filename) {
        return StringUtils.substringAfterLast(filename, ".");
    }

    @Nullable
    private static String setFilename(@NotNull MultipartFile file, @NotNull Optional<String> filename) {
        String fileName;
        if (filename.isPresent() && !filename.get().isEmpty()) {
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName = filename.get() + extension;
        } else {
            fileName = file.getOriginalFilename();
        }
        return fileName;
    }

    public Image retriveImage(@NotNull String id) {
        Image image = this.getImage(id);
        if (image == null) {
            return null;
        } else {
            return new Image(image.getName(),
                    image.getContentType(),
                    image.getResolutionType(),
                    image.getResolution(),
                    image.getContentType(),
                    ImageCompressionUtil.decompress(image.getData()));
        }
    }

    public void deleteImage(Image image) {
        imageRepository.delete(image);
    }

    public long countImages() {
        return imageRepository.count();
    }

    public List<Image> findAllImages() {
        return imageRepository.findAll();
    }

}
