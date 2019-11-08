package bsuir.ai.sostis.utils;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Sentence;
import bsuir.ai.sostis.model.Summary;
import bsuir.ai.sostis.model.Word;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClassicEssayUtils {

    private static final int SENTENCE_NUMBER = 10;

    // TODO: do something with docWords(we need indexes)
    public static Document indexDocument(Document document) {
        List<Word> docWords = document.getSentences().stream()
                .map(Sentence::getWords)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return document;
    }



    private static Summary buildSummary(Document document) {
        StringBuilder summary = new StringBuilder();
        List<Sentence> sentences = document.getSentences().stream()
                .sorted(Comparator.comparingInt(Sentence::getRang)
                .reversed())
                .collect(Collectors.toList())
                .subList(0, SENTENCE_NUMBER);

        sentences.forEach(sentence -> summary.append(sentence.getText()));

        return Summary.builder()
                .source(document.getTitle())
                .essay(summary.toString())
                .build();
    }
}
