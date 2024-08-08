package org.gustavohnsv.imageupload.controller;

import org.gustavohnsv.imageupload.model.Image;
import org.gustavohnsv.imageupload.model.Message;
import org.gustavohnsv.imageupload.repository.ImageRepository;
import org.gustavohnsv.imageupload.service.ImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;
    private final ImageRepository imageRepository;

    public ImageController(ImageService imageService, ImageRepository imageRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    @PostMapping("/image/")
    public ResponseEntity<Message> uploadImage(@RequestParam("file") MultipartFile file) {
        imageService.saveImage(file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message("success", "Image uploaded successfully"));
    }

    @GetMapping("/image/download/")
    public ResponseEntity<Object> downloadImage(@RequestParam String id) {
        Image image = imageService.getImage(id);
        byte[] imageData = image.getData();
        String imageContentType = image.getContentType();
        if (imageData == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Message("error", "Image not found"));
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, imageContentType);
            return ResponseEntity
                     .status(HttpStatus.OK)
                     .headers(headers)
                     .body(new ByteArrayResource(imageData));
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
            imageRepository.delete(image);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Message("success", "Image deleted successfully"));
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(imageRepository.findAll());
    }

    @GetMapping("/image/")
    public ResponseEntity<byte[]> getImage(@RequestParam String id) {
        Image image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", image.getContentType())
                    .body(image.getData());
        }
    }

    @GetMapping("/image/count/")
    public ResponseEntity<Message> getImageCount() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message("success", imageRepository.count() + " images found"));
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
