package ru.altercom.spb.longman.subarticle;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("SUBARTICLES")
@Getter
public class Subarticle {

    @Id
    private final Long id;

    private final Long wordId;

    private final Long parentId;

    private final int level;

    private final String article;

    @PersistenceConstructor
    public Subarticle(Long id, Long wordId, Long parentId, int level, String article) {
        this.id = id;
        this.wordId = wordId;
        this.parentId = parentId;
        this.level = level;
        this.article = article;
    }
}
