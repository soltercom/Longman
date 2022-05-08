package ru.altercom.spb.longman.subarticle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubarticleNotFoundException extends RuntimeException {

    public SubarticleNotFoundException(Long id) {
        super(String.format("Subarticle with id %d not found", id));
    }

}
