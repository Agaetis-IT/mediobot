package fr.agaetis.mediobot.service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
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
        PhotoList<Photo> photos;
        try {
            logger.debug("trying group: {} ", groupId);
            photos = flickr.getPoolsInterface().getPhotos(groupId, null, 33, page);
            logger.debug("total photos: {}", photos.getTotal());
            logger.debug("total pages: {}", photos.getPages());
        } catch (FlickrException e) {
            logger.error("Error: {}", e.toString());
            return new ArrayList<>();
        }

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
