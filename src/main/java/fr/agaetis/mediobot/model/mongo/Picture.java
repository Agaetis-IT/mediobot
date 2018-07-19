package fr.agaetis.mediobot.model.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "picture")
@NoArgsConstructor
public class Picture {

    @Id
    private String id;
    @NonNull
    @Indexed(unique = true)
    private String url;
    private String author;
    private Number score;
    private String shapeObject;
    private String path;
}