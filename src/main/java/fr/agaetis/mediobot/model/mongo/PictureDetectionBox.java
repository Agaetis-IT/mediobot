package fr.agaetis.mediobot.model.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PictureDetectionBox {
    private double topLeftX;
    private double topLeftY;
    private double bottomRightX;
    private double bottomRightY;
}