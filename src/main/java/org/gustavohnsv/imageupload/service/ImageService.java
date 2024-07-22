package org.gustavohnsv.imageupload.service;

import org.gustavohnsv.imageupload.model.Image;
import org.gustavohnsv.imageupload.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
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

    private boolean validateExtension(String filename) {
        if (filename.contains("..")) {
            return false;
        }
        return VALID_EXTENSIONS.contains(filename.substring(filename.lastIndexOf(".") + 1).toLowerCase());
    }

    private boolean validateContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        if (!(contentType.startsWith("image/"))) {
            return false;
        }
        return VALID_EXTENSIONS.contains(contentType.substring("image/".length()));
    }

    public void saveImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        if (!(validateExtension(image.getName()))) {
            throw new IllegalArgumentException("Invalid image extension");
        }
        image.setContentType(file.getContentType());
        if (!validateContentType(image.getContentType())) {
            throw new IllegalArgumentException("Invalid image content type");
        }
        image.setData(file.getBytes());
        imageRepository.save(image);
    }

    public Image getImage(String id) {
        return imageRepository.findById(id).orElse(null);
    }

}
