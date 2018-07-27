package fr.agaetis.mediobot.model.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "picture")
@NoArgsConstructor
public class Picture {
    @Id
    private String id;
    private String url;
    private PictureOrigin origin;
    private String author;
    private String path;
    private List<PictureDetection> detections = new ArrayList<>();
    private String detectionError;
    @JsonIgnore
    private PictureDetectionStatus detectionStatus;
}