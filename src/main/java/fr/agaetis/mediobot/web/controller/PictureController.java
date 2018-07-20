package fr.agaetis.mediobot.web.controller;

import fr.agaetis.mediobot.model.mongo.Picture;
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

    @RequestMapping(value = "/pictures/unprocessed", method = RequestMethod.GET)
    public List<Picture> getUnprocessedPictures() {
        return mongoService.getUnprocessedPictures();
    }

    @RequestMapping(value = "/pictures/media", method = RequestMethod.POST)
    public void retrievePicturesFromMedia() {
        processFlickrMedia();
    }

    private void processFlickrMedia() {
        List<Picture> flickrPictures = flickrService.getPictures();
        flickrPictures.forEach(mongoService::saveInDatabase);
        flickrPictures.forEach(storageService::saveOnDisk);
    }
}
