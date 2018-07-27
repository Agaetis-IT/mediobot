package fr.agaetis.mediobot.web.controller;

import fr.agaetis.mediobot.model.mongo.PictureOrigin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureDetectobotInputView {
    private String id;
    private String url;
    private PictureOrigin origin;
    private String author;
    private String path;
}
