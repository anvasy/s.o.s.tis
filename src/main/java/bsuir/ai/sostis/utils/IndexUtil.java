package bsuir.ai.sostis.utils;

import bsuir.ai.sostis.model.Document;
import bsuir.ai.sostis.model.Sentence;
import bsuir.ai.sostis.model.Word;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class IndexUtil {

    // TODO: do something with docWords(we need indexes)
    public static Document indexDocument(Document document) {
        List<Word> docWords = document.getSentences().stream()
                .map(Sentence::getWords)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return document;
    }
}
