package fr.agaetis.mediobot.service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import fr.agaetis.mediobot.model.mongo.Picture;
import fr.agaetis.mediobot.model.mongo.PictureDetectionStatus;
import fr.agaetis.mediobot.model.mongo.PictureOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlickRService {
    private static final Logger logger = LoggerFactory.getLogger(FlickRService.class);

    private Flickr flickr;
    @Value("#{'${flickr.groups}'.split(',')}")
    private List<String> groups;
    @Value("${FLICKR_API_KEY}")
    private String API_KEY;
    @Value("${FLICKR_API_SECRET}")
    private String API_SECRET;
    @Value("${flickr.maxPicturesPerGroup}")
    private int maxPicturesPerGroup;
    private static final int MAX_PER_PAGE = 100;

    @PostConstruct
    public void init() {
        flickr = new Flickr(API_KEY, API_SECRET, new REST());
    }

    public List<Picture> getPictures() {
        return groups
            .stream()
            .flatMap(s -> getPhotosFromGroup(s).stream())
            .collect(Collectors.toList());
    }

    private List<Picture> getPhotosFromGroup(String groupId) {
        Integer page = 0;
        int maxPerPage = (maxPicturesPerGroup == -1 || maxPicturesPerGroup > MAX_PER_PAGE) ? MAX_PER_PAGE : maxPicturesPerGroup;
        boolean atEnd = false;

        List<Photo> photos = new ArrayList<>();

        logger.info("Retrieve pictures from group: {} with max {} ", groupId, maxPicturesPerGroup);
        while (!atEnd) {
            try {

                int nbPerPage = maxPerPage;
                if ( (maxPicturesPerGroup - photos.size()) <= maxPerPage ) {
                    nbPerPage = maxPicturesPerGroup - photos.size();
                    atEnd = true;
                    logger.debug("at end because max photos in group reached this iteration : {} ", nbPerPage);
                }

                logger.debug("request {} photos with page {}", nbPerPage, page);
                List<Photo> currentPhotos = flickr.getPoolsInterface().getPhotos(groupId, null, nbPerPage, page);
                page ++;

                if (currentPhotos.isEmpty()) {
                    atEnd = true;
                    logger.debug("at end because no more photos in group");
                } else {
                    logger.debug("add {} photos", currentPhotos.size());
                    photos.addAll(currentPhotos);
                }

            } catch (FlickrException e) {
                logger.error("Error: {}", e.toString());
                return new ArrayList<>();
            }
        }

        logger.info("Total pictures retrieved : {}", photos.size());
        return photos.stream()
            .map(this::convertPhotoToPicture)
            .collect(Collectors.toList());
    }

    private Picture convertPhotoToPicture(Photo photo) {
        Picture picture = new Picture();
        picture.setOrigin(PictureOrigin.FLICKR);
        picture.setDetectionStatus(PictureDetectionStatus.UNPROCESSED);
        picture.setUrl(getUrlForPhoto(photo));
        picture.setAuthor(photo.getOwner().getUsername());
        picture.setPath("/flickr/" + getPictureNameFromPhoto(photo));
        return picture;
    }

    private String getUrlForPhoto(Photo photo) {
        return String.format(
            "https://farm%s.staticflickr.com/%s/%s",
            photo.getFarm(),
            photo.getServer(),
            getPictureNameFromPhoto(photo)
        );
    }

    private String getPictureNameFromPhoto(Photo photo) {
        return String.format("%s_%s.jpg", photo.getId(), photo.getSecret());
    }
}
