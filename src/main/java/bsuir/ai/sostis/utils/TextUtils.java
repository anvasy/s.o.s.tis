package bsuir.ai.sostis.utils;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Sentence;
import bsuir.ai.sostis.model.Word;
import bsuir.ai.sostis.repository.StopWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextUtils {

    private static StopWordRepository stopWordRepository;

    private static final Pattern SENTENCE_PATTERN = Pattern.compile("[A-ZА-Я]+[A-Za-zА-Яа-я0-9,;'\"\\s]*[.?!]\\s*");
    private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-zА-Яа-я]+[,;'\"\\s]\\s*");

    public TextUtils(StopWordRepository stopWordRepository) {
        this.stopWordRepository = stopWordRepository;
    }

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
        removeExtraWords(words);
        return Sentence.builder()
                .number(sentenceNumber)
                .text(sentenceText)
                .words(words)
                .build();
    }

    private static void removeExtraWords(List<Word> words) {
        words.removeIf(word -> word.getText().matches("[^А-Яа-я]+"));
        words.forEach(word -> word.setText(word.getText().replaceAll("[\\s,.():!?]", "")));
        words.removeIf(word -> Objects.nonNull(stopWordRepository.findByText(word.getText())));
    }

    private static Word createWord(int wordNumber, String wordText) {
        return Word.builder()
                .number(wordNumber)
                .text(wordText)
                .build();
    }
}
