package fr.agaetis.mediobot.model.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "survey")
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Survey {
    @Id
    private String id;
    private String pictureId;
    private List<String> tags;
    private List<PictureDetectionBox> boxes;
}