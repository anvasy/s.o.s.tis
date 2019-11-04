package bsuir.ai.sostis.repository;

import bsuir.ai.sostis.model.StopWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopWordRepository extends JpaRepository<StopWord, Integer> {
    StopWord findByText(String text);
}
