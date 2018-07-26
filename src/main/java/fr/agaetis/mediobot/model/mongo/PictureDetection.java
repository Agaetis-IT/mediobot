package fr.agaetis.mediobot.model.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PictureDetection {
    private PictureDetectionBox box;
    private String label;
    private double score;
}