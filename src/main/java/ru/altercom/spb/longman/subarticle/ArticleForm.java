package ru.altercom.spb.longman.subarticle;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArticleForm {

    @Setter
    private Long id;

    private final int level;

    private final String text;

    private final String transcription;

    private final List<ArticleForm> children = new ArrayList<>();

    private final String feature;

    @Setter
    private int x;

    @Setter
    private int y;

    public ArticleForm(Long id, int level, String text, String transcription, String feature) {
        this.id = id;
        this.level = level;
        this.text = text;
        this.transcription = transcription;
        this.feature = feature;
    }

    public boolean isIndex() {
        return getLevel() == 1 && getText().contains("INDEX:");
    }

    public boolean isRelatedWords() {
        return getLevel() == 1 && getText().contains("RELATED WORDS");
    }

    public boolean isSimple() {
        return getLevel() == 1 && !isIndex() && !isRelatedWords();
    }

    public String getHtml() {
        var result = new StringBuilder();

        if (!getTranscription().isEmpty()) {
            result.append(getTranscription());//.append("<br>");
        }
        if (!getFeature().isEmpty()) {
            result.append(getFeature());//.append("<br>");
        }

        for (var child: getChildren()) {
            if (child.getText().isEmpty()) {
                continue;
            }
            result.append(child.getText())
                //.append("<br>")
                .append(child.getHtml());
        }

        return result.toString();
    }

}
