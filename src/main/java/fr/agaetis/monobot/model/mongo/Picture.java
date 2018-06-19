package fr.agaetis.monobot.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "picture")
public class Picture {

    @Id
    private String id;
    private String author;
    @Indexed(unique = true)
    private String url;
    private Number score;
    private String shapeObject;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Number getScore() {
        return this.score;
    }

    public void setScore(Number score) {
        this.score = score;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShapeObject() {
        return this.shapeObject;
    }

    public void setShapeObject(String shapeObject) {
        this.shapeObject = shapeObject;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
