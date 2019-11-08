package bsuir.ai.sostis.utils;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Paragraph;
import bsuir.ai.sostis.model.Sentence;
import bsuir.ai.sostis.model.Word;
import bsuir.ai.sostis.repository.StopWordRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class TextUtils {

    private static StopWordRepository stopWordRepository;

    private static final Pattern SENTENCE_PATTERN = Pattern.compile("[A-ZА-Я]+[A-Za-zА-Яа-я0-9,;'\"\\s]*[.?!]\\s*");
    private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-zА-Яа-я]+[,;'\"\\s]\\s*");

    @SuppressWarnings("AccessStaticViaInstance")
    public TextUtils(StopWordRepository stopWordRepository) {
        this.stopWordRepository = stopWordRepository;
    }

    public static Document createDocument(MultipartFile file) throws IOException {
        String docTitle = file.getOriginalFilename();
        String docText = new String(file.getBytes());

        List<String> par = Arrays.asList(docText.split("\\n"));
        List<Paragraph> paragraphs = new ArrayList<>();
        for(int i = 0; i < par.size(); i++) {
            List<Sentence> sentences = new ArrayList<>();
            Matcher matcher = SENTENCE_PATTERN.matcher(par.get(i));
            while (matcher.find()) {
                Sentence sentence = createSentence(sentences.size(), matcher.group());
                sentences.add(sentence);
            }
            paragraphs.add(createParagraph(i, sentences, par.get(i)));
        }
        return Document.builder()
                        .text(docText)
                        .paragraphs(paragraphs)
                        .sentences(getSentences(paragraphs))
                        .title(docTitle)
                        .build();
    }

    private static List<Sentence> getSentences(List<Paragraph> paragraphs) {
        return paragraphs.stream()
                .map(Paragraph::getSentences)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
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

    private static Paragraph createParagraph(int paragraphNumber, List<Sentence> sentences, String text) {
        return Paragraph.builder()
                .number(paragraphNumber)
                .sentences(sentences)
                .text(text)
                .build();
    }
}
