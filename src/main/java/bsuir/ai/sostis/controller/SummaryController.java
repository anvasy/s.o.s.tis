package bsuir.ai.sostis.controller;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Result;
import bsuir.ai.sostis.model.Summary;
import bsuir.ai.sostis.model.Word;
import bsuir.ai.sostis.utils.ClassicEssayUtils;
import bsuir.ai.sostis.utils.KeyWordsEssayUtils;
import bsuir.ai.sostis.utils.TextUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
public class SummaryController {

    @GetMapping(value = {"/", "/value"})
    public ModelAndView getHomePage() {
        return new ModelAndView("home");
    }

    @PostMapping(value = "/summary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity summarize(@RequestParam("document") MultipartFile file) {
        try {
            long startTime = System.currentTimeMillis();
            Document document = TextUtils.createDocument(file);
            Summary classicEssay = ClassicEssayUtils.getClassicEssay(document);
            List<Word> keyWordsEssay = KeyWordsEssayUtils.getKeyWordsEssay(document);
            double responseTime = ((double) System.currentTimeMillis() - startTime) / 1000;
            return new ResponseEntity<>(
                    new Result(classicEssay, keyWordsEssay, responseTime),
                    HttpStatus.OK
            );
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }
}
