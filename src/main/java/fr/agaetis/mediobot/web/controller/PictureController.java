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
public class PictureController {
    @Autowired
    private FlickRService flickrService;

    @Autowired
    private MongoService mongoService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private DetectionService detectionService;

    @RequestMapping(value = "/pictures/detect", method = RequestMethod.POST)
    public void triggerPicturesDetection() {
        List<Picture> unprocessedPictures = mongoService.getUnprocessedPictures();
        detectionService.launchDetection(unprocessedPictures);
    }

    @RequestMapping(value = "/pictures/media/retrieve", method = RequestMethod.POST)
    public void retrievePicturesFromMedia() {
        processFlickrMedia();
    }

    private void processFlickrMedia() {
        List<Picture> flickrPictures = flickrService.getPictures();
        flickrPictures.forEach(mongoService::saveInDatabase);
        flickrPictures.forEach(storageService::saveOnDisk);
    }
}
