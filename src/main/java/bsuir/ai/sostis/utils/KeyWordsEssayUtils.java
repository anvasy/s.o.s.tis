package bsuir.ai.sostis.utils;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Sentence;
import bsuir.ai.sostis.model.Word;
import bsuir.ai.sostis.service.OstisService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class KeyWordsEssayUtils {

    private static final int KEY_WORDS_NUMBER = 20;
    private static OstisService ostisService;

    public KeyWordsEssayUtils(OstisService ostisService) {
        this.ostisService = ostisService;
    }

    public static List<Word> getKeyWordsEssay(Document document) {
        Set<Word> words = document.getSentences()
                .stream()
                .map(Sentence::getWords)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Map<String, Integer> wordOccurrences = countOccurrences(document);
        int wordNumber = wordOccurrences.values().stream()
                .reduce(0, Integer::sum);
        Map<String, Double> ostisValues = ostisService.calculateRanks(document);

        for (Word word : words) {
            word.setRank(((double) wordOccurrences.get(word.getText()) / wordNumber)
                    * ostisValues.getOrDefault(word.getText(), 0.0));
        }

        List<Word> result = words.stream()
                .sorted(Comparator.comparingDouble(Word::getRank).reversed())
                .collect(Collectors.toList());
        Set<Object> seen = new HashSet<>();
        result.removeIf(r-> !seen.add(r.getText()));

        return result.size() > KEY_WORDS_NUMBER ?
                result.subList(0, KEY_WORDS_NUMBER) :
                result;
    }

    private static Map<String, Integer> countOccurrences(Document document) {
        Map<String, Integer> wordOccurrences = new HashMap<>();
        for (Sentence sentence : document.getSentences()) {
            for (Word word : sentence.getWords()) {
                wordOccurrences.computeIfPresent(word.getText(), (k, v) -> v + 1);
                wordOccurrences.putIfAbsent(word.getText(), 1);
            }
        }

        return wordOccurrences;
    }
}
