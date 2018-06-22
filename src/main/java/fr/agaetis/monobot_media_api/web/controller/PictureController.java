package fr.agaetis.monobot_media_api.web.controller;

import fr.agaetis.monobot_media_api.service.FlickRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PictureController {

    @Autowired
    private FlickRService flickrService;

    /**
     * Show and save newly added photo addresses
     */
    @RequestMapping("/pictures")
    public List<String> picture() {
        return this.flickrService.getGroups()
            .stream()
            .flatMap(s -> flickrService.getPhotosFromGroup(s).stream())
            .collect(Collectors.toList())
            ;
    }
}
