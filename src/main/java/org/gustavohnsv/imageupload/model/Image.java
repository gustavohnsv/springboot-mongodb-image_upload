package org.gustavohnsv.imageupload.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@Data
@Document(collection = "images")
public class Image {
    @Id
    private String id;
    private final String name;
    private final String contentType;
    private final String resolutionType;
    private final int[] resolution; // { width , height }
    private final String size;
    private final byte[] data;
}
