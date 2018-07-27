package fr.agaetis.mediobot.web.controller;

import fr.agaetis.mediobot.model.mongo.Picture;
import fr.agaetis.mediobot.service.DetectionService;
import fr.agaetis.mediobot.service.FlickRService;
import fr.agaetis.mediobot.service.MongoService;
import fr.agaetis.mediobot.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/picture")
public class PictureController {
    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

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
        List<PictureDetectobotInputView> unprocessedPictures = mongoService.getUnprocessedPictures()
            .stream()
            .map(p -> new PictureDetectobotInputView(p.getId(), p.getUrl(), p.getOrigin(), p.getAuthor(), p.getPath()))
            .collect(Collectors.toList());
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
    public void pictureDetectionWasProcessed(@RequestBody Picture picture) {
        mongoService.proccessPitcureWithSuccess(picture);
    }

    @RequestMapping(value = "/detection/processed/error", method = RequestMethod.GET)
    public List<Picture> getPicturesProcessedWithError() {
        return mongoService.getPicturesProccessedWithError();
    }

    @RequestMapping(value = "/detection/processed/error", method = RequestMethod.POST)
    public void pictureDetectionWasProcessedWithError(@RequestBody Picture picture) {
        mongoService.proccessPitcureWithError(picture);
    }

    @RequestMapping(
        value = "/image/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getPictureImage(@PathVariable String id) {

        Optional<Picture> picture = mongoService.getPicture(id);
        if (!picture.isPresent()) {
            logger.error("Picture with id {} not found in database", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String picturePath = picture.get().getPath();
        try {
            byte[] image = storageService.getPictureImage(picturePath);
            return new ResponseEntity<>(image, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Image with path {} not found on disk", picturePath);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
