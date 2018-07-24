package fr.agaetis.mediobot.web.controller;

import fr.agaetis.mediobot.model.mongo.Picture;
import fr.agaetis.mediobot.service.DetectionService;
import fr.agaetis.mediobot.service.FlickRService;
import fr.agaetis.mediobot.service.MongoService;
import fr.agaetis.mediobot.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/picture")
public class PictureController {
    @Autowired
    private FlickRService flickrService;

    @Autowired
    private MongoService mongoService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private DetectionService detectionService;

    @RequestMapping(value = "/media/retrieve", method = RequestMethod.POST)
    public void retrievePicturesFromMedia() {
        processFlickrMedia();
    }

    private void processFlickrMedia() {
        List<Picture> flickrPictures = flickrService.getPictures();
        flickrPictures.forEach(mongoService::add);
        flickrPictures.forEach(storageService::saveOnDisk);
    }

    @RequestMapping(value = "/detection/trigger", method = RequestMethod.POST)
    public void triggerPicturesDetection() {
        List<Picture> unprocessedPictures = mongoService.getUnprocessedPictures();
        detectionService.launchDetection(unprocessedPictures);
    }

    @RequestMapping(value = "/detection/unprocessed", method = RequestMethod.GET)
    public List<Picture> getUnprocessedPictures() {
        return mongoService.getUnprocessedPictures();
    }

    @RequestMapping(value = "/detection/processed/success", method = RequestMethod.GET)
    public List<Picture> getPicturesProcessedWithSuccess() {
        return mongoService.getPicturesProccessedWithSuccess();
    }

    @RequestMapping(value = "/detection/processed/success", method = RequestMethod.POST)
    public void pictureDetectionWasProcessed(Picture picture) {
        mongoService.proccessPitcureWithSuccess(picture);
    }

    @RequestMapping(value = "/detection/proccessed/error", method = RequestMethod.GET)
    public List<Picture> getPicturesProcessedWithError() {
        return mongoService.getPicturesProccessedWithError();
    }

    @RequestMapping(value = "/detection/proccessed/error", method = RequestMethod.POST)
    public void pictureDetectionWasProcessedWithError(Picture picture) {
        mongoService.proccessPitcureWithError(picture);
    }
}
