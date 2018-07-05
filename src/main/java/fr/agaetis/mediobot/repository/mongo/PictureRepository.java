package fr.agaetis.mediobot.repository.mongo;

import fr.agaetis.mediobot.model.mongo.Picture;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PictureRepository extends CrudRepository<Picture, String> {

    @Query(value = "{'url': ?0}")
    Optional<Picture> findByUrl(String url);
}
