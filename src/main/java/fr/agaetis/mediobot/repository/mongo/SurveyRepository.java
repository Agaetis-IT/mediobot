package fr.agaetis.mediobot.repository.mongo;

import fr.agaetis.mediobot.model.mongo.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyRepository extends MongoRepository<Survey, String> {
}
