package org.gustavohnsv.imageupload.controller;

import org.gustavohnsv.imageupload.model.Image;
import org.gustavohnsv.imageupload.model.Message;
import org.gustavohnsv.imageupload.service.ImageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/image/")
    public ResponseEntity<Message> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("filename") Optional<String> filename, @RequestParam("resizeFactor") double resizeFactor) {
        if (file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Message("error", "File is required"));
        }
        if (imageService.saveImage(file, filename, resizeFactor)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Message("success", "Image uploaded successfully"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Message("error", "Image upload failed"));
        }
    }

    @NotNull
    private static HttpHeaders getHttpHeadersForDownload(@NotNull Image image) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, image.getContentType());
        return headers;
    }

    @GetMapping("/image/download/")
    public ResponseEntity<Object> downloadImage(@RequestParam String id) {
        Image decompressedImage = imageService.retriveImage(id);
        if (decompressedImage == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Message("error", "Image not found"));
        } else {
            return ResponseEntity
                     .status(HttpStatus.OK)
                     .headers(getHttpHeadersForDownload(decompressedImage))
                     .body(new ByteArrayResource(decompressedImage.getData()));
        }
    }

    @DeleteMapping("/image/")
    public ResponseEntity<Message> deleteImage(@RequestParam String id) {
        Image image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Message("error", "Image not found"));
        } else {
            imageService.deleteImage(image);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Message("success", "Image deleted successfully"));
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(imageService.findAllImages());
    }

    @GetMapping("/image/")
    public ResponseEntity<Object> getImage(@RequestParam String id) {
        Image decompressedImage = imageService.retriveImage(id);
        if (decompressedImage == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Message("error", "Image not found"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", decompressedImage.getContentType())
                    .body(decompressedImage.getData());
        }
    }

    @GetMapping("/image/count/")
    public ResponseEntity<Message> getImageCount() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message("success", imageService.countImages() + " images found"));
    }

    @RequestMapping(value = "/image/", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headImage(@RequestParam String id) {
        Image image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", image.getContentType())
                    .build();
        }
    }

    @RequestMapping(value = "/image/", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsImage() {
        return ResponseEntity.ok()
                .header("Allow", "OPTIONS, GET, POST, DELETE, HEAD")
                .build();
    }

}
