package org.gustavohnsv.imageupload.repository;

import org.gustavohnsv.imageupload.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {

    Image findFirstById(String id);

}
