package ru.altercom.spb.longman.subarticle;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SubarticleForm {

    private final Long id;

    private final SubarticleForm parent;

    private final int level;

    private final String text;

    private final List<SubarticleForm> children = new ArrayList<>();

    public SubarticleForm(Long id, SubarticleForm parent, int level, String text) {
        this.id = id;
        this.parent = parent;
        this.level = level;
        this.text = text;
    }
}
