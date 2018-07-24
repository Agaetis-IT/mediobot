package fr.agaetis.mediobot.service;

import fr.agaetis.mediobot.model.mongo.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DetectionService {
    private static final Logger logger = LoggerFactory.getLogger(DetectionService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${detectobot.baseurl}")
    private String detectobotBaseUrl;

    @Value("${detectobot.version}")
    private String detectobotVersion;

    public void launchDetection(List<Picture> pictures) {
        String url = detectobotBaseUrl + "/detectobot/" + detectobotVersion + "/detection";
        logger.info("Launch detection on url {} and for pictures {}", url, pictures);
        restTemplate.postForEntity(url, pictures, Void.class);
    }
}
