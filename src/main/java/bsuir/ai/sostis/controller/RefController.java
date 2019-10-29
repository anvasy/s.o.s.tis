package bsuir.ai.sostis.controller;

import bsuir.ai.sostis.utils.TextUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class RefController {

    @GetMapping(value = {"/", "/value"})
    public ModelAndView getHomePage() {
        return new ModelAndView("home");
    }

    @GetMapping(value = "/ref", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadDocument(@RequestParam("document") MultipartFile file) {
        try {
            TextUtils.createSentences(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
