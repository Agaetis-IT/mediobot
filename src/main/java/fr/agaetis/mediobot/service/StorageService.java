package fr.agaetis.mediobot.service;

import fr.agaetis.mediobot.model.mongo.Picture;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    @Value("${app.pictures.storage.basepath}")
    private String basePath;

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    public void saveOnDisk(Picture picture) {
        logger.debug("Try to store picture on disk : {}", picture);

        Path finalPath = Paths.get(basePath + picture.getPath());
        if (Files.exists(finalPath)) {
            logger.debug("Picture already present on disk : {}", picture);
            return;
        }

        try {
            FileUtils.copyURLToFile(new URL(picture.getUrl()), finalPath.toFile());
            logger.debug("Picture stored on disk : {}", picture);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    public byte[] getPictureImage(String path) throws IOException {
        Path finalPath = Paths.get(basePath + path);
        return Files.readAllBytes(finalPath);
    }
}
