package fr.agaetis.mediobot.repository.mongo;

import fr.agaetis.mediobot.model.mongo.Picture;
import fr.agaetis.mediobot.model.mongo.PictureDetectionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends MongoRepository<Picture, String> {
    Optional<Picture> findByUrl(String url);

    List<Picture> findByDetectionStatus(PictureDetectionStatus unprocessed);
}
