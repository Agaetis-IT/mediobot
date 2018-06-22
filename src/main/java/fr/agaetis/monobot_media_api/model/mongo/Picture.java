package fr.agaetis.monobot_media_api.model.mongo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@EqualsAndHashCode
@Document(collection = "picture")
public class Picture {
    @Id
    private String url;
    private String author;
    private Number score;
    private String shapeObject;
}