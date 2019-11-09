package bsuir.ai.sostis.utils;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Paragraph;
import bsuir.ai.sostis.model.Sentence;
import bsuir.ai.sostis.model.Summary;
import bsuir.ai.sostis.model.Word;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassicEssayUtils {

    private static final int SENTENCE_NUMBER = 5;

    public static Summary getClassicEssay(Document document) {
        rankSentences(document);
        return buildSummary(document);
    }

    private static void rankSentences(Document document) {
        int D = document.getText().length();
        int maxOcc = countMax(document);
        for(Paragraph paragraph : document.getParagraphs()) {
            int P = paragraph.getText().length();
            for(Sentence sentence : paragraph.getSentences()) {
                double BD = paragraph.getText().indexOf(sentence.getText());
                double BP = document.getText().indexOf(sentence.getText());
                sentence.setRank((1 - BD/D) * (1 - BP/P) *  countTFIDF(sentence, document, maxOcc));
            }
        }
    }

    private static double countTFIDF(Sentence sentence, Document document, int max) {
        double res = 1;
        for (Word word : sentence.getWords()) {
            res *= Collections.frequency(sentence.getWords(), word)
                    * (0.5 * (1 + (double) StringUtils.countOccurrencesOf(document.getText(), word.getText()) / max));
        }
        return res;
    }

    private static int countMax(Document document) {
        List<Word> words = document.getSentences()
                .stream()
                .map(Sentence::getWords)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Word::getText))
                .collect(Collectors.toList());

        int maxCount = 1;
        int currCount = 1;

        for (int i = 1; i < words.size(); i++) {
            if (words.get(i).getText().equals(words.get(i - 1).getText()))
                currCount++;
            else {
                if (currCount > maxCount)
                    maxCount = currCount;
                currCount = 1;
            }
        }

        return Math.max(currCount, maxCount);
    }

    private static Summary buildSummary(Document document) {
        StringBuilder summary = new StringBuilder();
        List<Sentence> sentences = document.getSentences().stream()
                .sorted(Comparator.comparingDouble(Sentence::getRank).reversed())
                .collect(Collectors.toList())
                .subList(0, SENTENCE_NUMBER);
        sentences.sort(Comparator.comparingInt(Sentence::getNumber));
        sentences.forEach(sentence -> summary.append(sentence.getText()));

        return createSummary(document.getTitle(), summary.toString());
    }

    private static Summary createSummary(String source, String summary) {
        return Summary.builder()
                .source(source)
                .essay(summary)
                .build();
    }
}
