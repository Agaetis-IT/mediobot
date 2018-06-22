package fr.agaetis.monobot_media_api.service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import fr.agaetis.monobot_media_api.model.mongo.Picture;
import fr.agaetis.monobot_media_api.repository.mongo.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlickRService {

    @Autowired
    private PictureRepository pictureRepository;
    private Flickr flickr;
    @Value("${flickr.groups}")
    private List<String> groups;
    @Value("${app.api.key}")
    private String API_KEY;
    @Value("${app.api.secret}")
    private String API_SECRET;

    private String saveInDatabase(Photo photo) {
        String url = getUrlForPhoto(photo);
        Picture picture = new Picture();
        picture.setUrl(url);
        picture.setAuthor(photo.getOwner().getUsername());

        pictureRepository.save(picture);

        return url;
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

    private boolean isPhotoCanSaved(Photo photo) {
        return !pictureRepository.findByUrl(getUrlForPhoto(photo)).isPresent();
    }

    private List<String> getPhotosFromGroup(String groupId) {
        Integer page = 0;
        PhotoList<Photo> photos;

        try {
            photos = flickr.getPoolsInterface().getPhotos(groupId, null, 15, page);
        } catch (FlickrException e) {
            return new ArrayList<>();
        }

        return photos.stream()
            .filter(this::isPhotoCanSaved)
            .map(this::saveInDatabase)
            .collect(Collectors.toList());
    }

    @PostConstruct
    public void init() {
        flickr = new Flickr(this.API_KEY, this.API_SECRET, new REST());
    }

    public List<String> getPictures() {
        return groups
            .stream()
            .flatMap(s -> getPhotosFromGroup(s).stream())
            .collect(Collectors.toList())
            ;
    }
}
