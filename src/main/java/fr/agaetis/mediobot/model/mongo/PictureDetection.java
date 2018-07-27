package fr.agaetis.mediobot.model.mongo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PictureDetection {
    private PictureDetectionBox box;
    private String label;
    private double score;
}