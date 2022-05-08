package ru.altercom.spb.longman.word;

import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends CrudRepository<Word, Long> {

    @Transactional(readOnly = true)
    List<Word> findAll();

    @Query("SELECT words.id, words.name FROM words")
    @Transactional(readOnly = true)
    List<WordListForm> findAll(Sort sort);

    @Transactional(readOnly = true)
    Optional<Word> findByName(String name);

    @Query("SELECT words.id, words.name FROM words WHERE words.name LIKE concat('%', :name, '%') ORDER BY words.name")
    @Transactional(readOnly = true)
    List<WordListForm> searchByName(@Param("name") String name);

}
