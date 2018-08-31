package fr.agaetis.mediobot.service;

import fr.agaetis.mediobot.model.mongo.Picture;
import fr.agaetis.mediobot.model.mongo.PictureDetectionStatus;
import fr.agaetis.mediobot.model.mongo.Survey;
import fr.agaetis.mediobot.repository.mongo.PictureRepository;
import fr.agaetis.mediobot.repository.mongo.SurveyRepository;
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

    @Autowired
    private SurveyRepository surveyRepository;

    public void addPicture(Picture picture) {
        logger.debug("Try to add the picture {} to the database", picture);

        Optional<Picture> tmp = pictureRepository.findByUrl(picture.getUrl());
        if (tmp.isPresent()) {
            logger.debug("Picture {} is already present in database, skipped", picture);
            return;
        }

        pictureRepository.save(picture);
        logger.debug("Picture added to the database : {}", picture);
    }

    public List<Picture> getUnprocessedPictures() {
        return pictureRepository.findByDetectionStatus(PictureDetectionStatus.UNPROCESSED);
    }

    public List<Picture> getPicturesProccessedWithSuccess() {
        return pictureRepository.findByDetectionStatus(PictureDetectionStatus.SUCCESS);
    }

    public void proccessPictureWithSuccess(Picture picture) {
        logger.info("Picture with id {} was processed by Detectobot with success", picture.getId());
        picture.setDetectionStatus(PictureDetectionStatus.SUCCESS);
        pictureRepository.save(picture);
    }

    public List<Picture> getPicturesProccessedWithError() {
        return pictureRepository.findByDetectionStatus(PictureDetectionStatus.ERROR);
    }

    public void proccessPictureWithError(Picture picture) {
        logger.warn("Picture with id {} was processed by Detectobot with error", picture.getId());
        picture.setDetectionStatus(PictureDetectionStatus.ERROR);
        pictureRepository.save(picture);
    }

    public Optional<Picture> getPicture(String id) {
        logger.info("Get picture with id {} ", id);
        return Optional.ofNullable(pictureRepository.findOne(id));
    }

    public void addSurvey(Survey survey) {
        Survey savedSurvey = surveyRepository.save(survey);
        logger.info("Added a survey with id {} for picture with id {}", savedSurvey.getId(), savedSurvey.getPictureId());
    }
}
