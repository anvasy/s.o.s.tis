package bsuir.ai.sostis.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextUtils {

    private static final Pattern SENTENCE_PATTERN = Pattern.compile("[A-ZА-Я]+[A-Za-zА-Яа-я0-9,;'\"\\s]*[.?!]\\s*");

    public static Map<Integer, String> createSentences(MultipartFile file) throws IOException {
        Map<Integer, String> sentences = new HashMap<>();
        Matcher matcher = SENTENCE_PATTERN.matcher(new String(file.getBytes()));
        int sentenceNumber = 0;
        while (matcher.find()) {
            sentences.put(sentenceNumber, matcher.group());
            sentenceNumber++;
        }

        return sentences;
    }

}
