package bsuir.ai.sostis.utils;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Sentence;
import bsuir.ai.sostis.model.Word;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final Pattern SENTENCE_PATTERN = Pattern.compile("[A-ZА-Я]+[A-Za-zА-Яа-я0-9,;'\"\\s]*[.?!]\\s*");
    private static final Pattern WORD_PATTERN = Pattern.compile("[A-ZА-Я]+[A-Za-zА-Яа-я]*[,;'\"\\s]\\s*");

    public static Document createDocument(MultipartFile file) throws IOException {
        String docTitle = file.getOriginalFilename();
        String docText = new String(file.getBytes());

        List<Sentence> sentences = new ArrayList<>();
        Matcher matcher = SENTENCE_PATTERN.matcher(docText);
        while (matcher.find()) {
            Sentence sentence = createSentence(sentences.size(), matcher.group());
            sentences.add(sentence);
        }

        return IndexUtil.indexDocument(
                Document.builder()
                        .text(docText)
                        .sentences(sentences)
                        .title(docTitle)
                        .build()
        );
    }

    private static Sentence createSentence(int sentenceNumber, String sentenceText) {
        List<Word> words = new ArrayList<>();
        Matcher matcher = WORD_PATTERN.matcher(sentenceText);
        while (matcher.find()) {
            Word word = createWord(words.size(), matcher.group());
            words.add(word);
        }

        return Sentence.builder()
                .number(sentenceNumber)
                .text(sentenceText)
                .words(words)
                .build();
    }

    private static Word createWord(int wordNumber, String wordText) {
        return Word.builder()
                .number(wordNumber)
                .text(wordText)
                .build();
    }
}
