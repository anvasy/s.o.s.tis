package bsuir.ai.sostis.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    Summary classicEssay;
    List<Word> keyWordsEssay;
    Double responseTime;
}
