package bsuir.ai.sostis.service;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Paragraph;
import bsuir.ai.sostis.model.Sentence;
import bsuir.ai.sostis.model.Word;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OstisService {
    private static final String OSTIS_API_PATH = "/api/idtf/find/?substr={token}";

    private final List<String> ostisSystems;
    private final RestTemplate restTemplate;

    public OstisService(@Value("http://ims.ostis.net") List<String> ostisSystems) {
        this.ostisSystems = ostisSystems;
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Double> calculateRanks(Document document) {
        Map<String, Integer> wordUsage = new HashMap<>();
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                for (Word word : sentence.getWords()) {
                    for (String ostis : ostisSystems) {
                        wordUsage.computeIfAbsent(word.getText(), key -> requestInfoFromOstisService(ostis, key) + 1);
                    }
                }
            }
        }
        double count = wordUsage.values().stream().mapToInt(k -> k).count();
        Map<String, Double> result = new HashMap<>();

        for (Map.Entry<String, Integer> entry : wordUsage.entrySet()) {
            result.put(entry.getKey(), entry.getValue() / count);
        }

        return result;
    }

    private int requestInfoFromOstisService(String ostis, String text) {
        return new JSONObject(Objects.requireNonNull(restTemplate.getForObject(ostis + OSTIS_API_PATH, String.class, text))).getJSONArray("main").length();
    }
}
