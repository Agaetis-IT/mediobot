package fr.agaetis.mediobot.model.mongo;

import lombok.*;

@NoArgsConstructor // constructor needed for reconstruction
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PictureDetectionBox {
    private double topLeftX;
    private double topLeftY;
    private double bottomRightX;
    private double bottomRightY;
}