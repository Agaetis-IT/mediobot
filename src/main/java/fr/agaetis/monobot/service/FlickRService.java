package fr.agaetis.monobot.service;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlickRService {

    private static Flickr flickr = null;
    @Value("${app.flickr.groups}")
    private String[] groups;
    @Value("${app.api.key}")
    private String API_KEY;
    @Value("${app.api.secret}")
    private String API_SECRET;

    public Flickr getInstance() {
        if (null == flickr) {
            flickr = new Flickr(this.API_KEY, this.API_SECRET, new REST());
        }

        return flickr;
    }

    public String[] getGroups() {
        return this.groups;
    }
}
