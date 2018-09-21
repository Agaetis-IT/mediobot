package fr.agaetis.mediobot.web.controller;

import fr.agaetis.mediobot.model.mongo.Survey;
import fr.agaetis.mediobot.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/survey")
public class SurveyController {
    @Autowired
    private MongoService mongoService;

    @RequestMapping(value = "/{pictureId}", method = RequestMethod.POST)
    public void pictureDetectionWasProcessed(@PathVariable String pictureId, @RequestBody Survey survey) {
        survey.setPictureId(pictureId);
        mongoService.addSurvey(survey);
    }

}
