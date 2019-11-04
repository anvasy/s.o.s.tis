package bsuir.ai.sostis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stop_word")
public class StopWord {
    @Id
    @Column(name = "stop_word_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stopWordId;

    @Column(name = "stop_word_text", nullable = false, unique = true)
    private String text;
}
