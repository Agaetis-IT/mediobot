package fr.agaetis.mediobot.service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import fr.agaetis.mediobot.model.mongo.Picture;
import fr.agaetis.mediobot.repository.mongo.PictureRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlickRService {

    @Autowired
    private PictureRepository pictureRepository;
    private Flickr flickr;
    @Value("#{'${flickr.groups}'.split(',')}")
    private List<String> groups;
    @Value("${FLICKR_API_KEY}")
    private String API_KEY;
    @Value("${FLICKR_API_SECRET}")
    private String API_SECRET;
    @Value("${app.basepath.picture}")
    private String basePath;

    private static final Logger logger = LoggerFactory.getLogger(FlickRService.class);

    private Picture saveInDatabase(Photo photo) {
        String url = getUrlForPhoto(photo);
        Picture picture = new Picture();
        picture.setUrl(url);
        picture.setAuthor(photo.getOwner().getUsername());
        Optional<Picture> tmp = pictureRepository.findByUrl(getUrlForPhoto(photo));

        if (!tmp.isPresent()) {
            String finalPath = basePath + photo.getId() + photo.getSecret() + ".jpg";
            picture.setPath(finalPath);

            try {
                FileUtils.copyURLToFile(new URL(picture.getUrl()), new File(finalPath), 10000, 10000);
                picture = pictureRepository.save(picture);
                tmp = Optional.of(picture);
            } catch (IOException e) {
                logger.error(e.toString());
                return null;
            }
        }

        return tmp.get();
    }

    private String getUrlForPhoto(Photo photo) {
        return String.format(
            "https://farm%s.staticflickr.com/%s/%s_%s.jpg",
            photo.getFarm(),
            photo.getServer(),
            photo.getId(),
            photo.getSecret()
        );
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
            .map(this::saveInDatabase)
            .collect(Collectors.toList());
    }

    @PostConstruct
    public void init() {
        flickr = new Flickr(API_KEY, API_SECRET, new REST());
    }

    public List<Picture> getPictures() {
        return groups
            .stream()
            .flatMap(s -> getPhotosFromGroup(s).stream())
            .collect(Collectors.toList())
            ;
    }
}
