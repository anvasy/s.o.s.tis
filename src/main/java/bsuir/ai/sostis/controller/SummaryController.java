package bsuir.ai.sostis.controller;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.utils.ClassicEssayUtils;
import bsuir.ai.sostis.utils.KeyWordsEssayUtils;
import bsuir.ai.sostis.utils.TextUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
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
            ClassicEssayUtils.getClassicEssay(document);
            KeyWordsEssayUtils.getKeyWordsEssay(document);
            double responseTime = ((double)System.currentTimeMillis() - startTime)/1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
