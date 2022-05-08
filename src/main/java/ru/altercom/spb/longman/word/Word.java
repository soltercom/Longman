package ru.altercom.spb.longman.word;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("WORDS")
@Getter
public class Word {

    @Id
    private final Long id;

    private final String name;

    private final String article;

    @PersistenceConstructor
    public Word(Long id, String name, String article) {
        this.id = id;
        this.name = name;
        this.article = article;
    }
}
