package fr.agaetis.mediobot.repository.mongo;

import fr.agaetis.mediobot.model.mongo.Picture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PictureRepository extends MongoRepository<Picture, String> {
    Optional<Picture> findByUrl(String url);
}
