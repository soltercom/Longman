package ru.altercom.spb.longman.subarticle;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubarticleRepository extends CrudRepository<Subarticle, Long> {

    List<Subarticle> findAllByParentId(Long id);

    @Query("SELECT id FROM subarticles WHERE word_id = :word_id AND level = 2")
    List<Long> findAllCards(@Param("word_id") Long wordId);

}
