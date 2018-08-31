package fr.agaetis.mediobot.web.controller;

import fr.agaetis.mediobot.model.mongo.Survey;
import fr.agaetis.mediobot.service.MongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/survey")
public class SurveyController {
    private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);

    @Autowired
    private MongoService mongoService;

    @RequestMapping(value = "/{pictureId}", method = RequestMethod.POST)
    public void pictureDetectionWasProcessed(@PathVariable String pictureId, @RequestBody Survey survey) {
        survey.setPictureId(pictureId);
        mongoService.addSurvey(survey);
    }

}
