package fr.agaetis.mediobot.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "picture")
public class Picture {
    @Id
    private String url;
    private String author;
    private Number score;
    private String shapeObject;
}