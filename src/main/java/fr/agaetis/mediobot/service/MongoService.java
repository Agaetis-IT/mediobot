package fr.agaetis.mediobot.service;

import fr.agaetis.mediobot.model.mongo.Picture;
import fr.agaetis.mediobot.repository.mongo.PictureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoService {
    private static final Logger logger = LoggerFactory.getLogger(MongoService.class);

    @Autowired
    private PictureRepository pictureRepository;

    public List<Picture> getUnprocessedPictures() {
        return pictureRepository.findAll();
    }

    public void saveInDatabase(Picture picture) {
        logger.debug("Try to save picture {} in database", picture);

        Optional<Picture> tmp = pictureRepository.findByUrl(picture.getUrl());
        if (tmp.isPresent()) {
            logger.debug("Picture {} is already present in database", picture);
            return;
        }

        pictureRepository.save(picture);
        logger.debug("Picture saved in database : {}", picture);
    }
}
