package fr.agaetis.monobot.web.controller;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import fr.agaetis.monobot.model.mongo.Picture;
import fr.agaetis.monobot.repository.mongo.PictureRepository;
import fr.agaetis.monobot.service.FlickRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@RestController
public class PictureController {

    private final FlickRService flickrService;
    private final PictureRepository pictureRepository;
    private Flickr flickr;
    private ArrayList<String> urls = new ArrayList<>();


    @Autowired
    public PictureController(FlickRService flickrService, PictureRepository pictureRepository) {
        this.flickrService = flickrService;
        this.pictureRepository = pictureRepository;
    }

    /**
     * Show and save newly added photo addresses
     */
    @RequestMapping("/pictures")
    public ArrayList<String> picture() throws FlickrException {
        this.flickr = this.flickrService.getInstance();
        String[] groups = this.flickrService.getGroups();

        for (int i = 0; i < groups.length; i++) {
            this.retrievePhotoFromGroup(groups[i]);
        }

        return this.urls;
    }

    /**
     * Retrieve five photos from groups
     *
     * @param groupId Flickr GroupId
     * @throws FlickrException Exception
     */
    private void retrievePhotoFromGroup(String groupId) throws FlickrException {
        Integer page = 0;
        PhotoList photos = this.flickr.getPoolsInterface().getPhotos(groupId, null, 5, page);

        for (Iterator<Photo> j = photos.iterator(); j.hasNext(); ) {
            Photo item = j.next();

            String url = String.format(
                "https://farm%s.staticflickr.com/%s/%s_%s.jpg",
                item.getFarm(),
                item.getServer(),
                item.getId(),
                item.getSecret()
            );

            Optional<Picture> exist = this.pictureRepository.findByUrl(url);

            if (exist.isPresent()) {
                continue;
            }

            Picture picture = new Picture();
            picture.setUrl(url);
            picture.setAuthor(String.format(
                "%s (%s)",
                item.getOwner().getRealName(),
                item.getOwner().getUsername()
            ));

            this.pictureRepository.save(picture);

            this.urls.add(url);
        }
    }
}
