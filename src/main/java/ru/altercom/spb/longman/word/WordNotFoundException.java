package ru.altercom.spb.longman.word;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WordNotFoundException extends RuntimeException {

    public WordNotFoundException(Long id) {
        super(String.format("Word with id %d not found", id));
    }

}
