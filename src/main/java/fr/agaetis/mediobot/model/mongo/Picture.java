package fr.agaetis.mediobot.model.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "picture")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Picture {
    @Id
    private String id;
    private String url;
    private PictureOrigin origin;
    private String author;
    private String path;
    private List<PictureDetection> detections;
    private String detectionError;
    @JsonIgnore
    private PictureDetectionStatus detectionStatus;
}