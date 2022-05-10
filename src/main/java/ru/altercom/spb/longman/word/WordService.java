package ru.altercom.spb.longman.word;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.altercom.spb.longman.system.TransactionManager;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class WordService {

    private final TransactionManager transactionManager;
    private final WordRepository wordRepo;

    public WordService(TransactionManager transactionManager, WordRepository wordRepo) {
        this.transactionManager = transactionManager;
        this.wordRepo = wordRepo;
    }

    public Long save(Word word) {
        Objects.requireNonNull(word);
        return transactionManager.doInTransaction(() -> {
            var entity = wordRepo.save(word);
            return entity.getId();
        });
    }

    public List<WordListForm> findAll() {
        return wordRepo.findAll(Sort.by("name"));
    }

    public List<WordListForm> searchByName(String name) {
        return wordRepo.searchByName(name);
    }

    public Word findById(Long id) {
        var word = wordRepo.findById(id)
            .orElseThrow(() -> new WordNotFoundException(id));
        return new Word(word.getId(), word.getName(), parseArticle(word));
    }

    private Long findIdByName(String name, Long defaultId) {
        var word = wordRepo.findByName(name);
        if (word.isPresent()) {
            return word.get().getId();
        }
        return defaultId;
    }

    private String parseArticle(Word word) {
        var article = word.getArticle();

        article = addStructure(article);

        var pattern = Pattern.compile("<<[A-Z /]+>>");
        var matcher = pattern.matcher(article);
        while(matcher.find()) {
            var nameBefore = matcher.group();
            var name = nameBefore.replaceAll("<<", "");
            name = name.replaceAll(">>", "");
            var id = findIdByName(name, word.getId());
            article = article.replaceAll(nameBefore, "<a href='/dictionary/" + id + "'>" + name + "</a>");
        }

        article = article.replaceAll("\\[m4]\\[/m]", "");
        article = article.replaceAll("\\[/m]", "[/m]<br>");
        article = article.replaceAll("\\[b]", "<b>");
        article = article.replaceAll("\\[/b]", "</b>");
        article = article.replaceAll("\\[u]", "<b>");
        article = article.replaceAll("\\[/u]", "</b>");
        article = article.replaceAll("\\[ex]", "<i>");
        article = article.replaceAll("\\[/ex]", "</i>");
        article = article.replaceAll("\\[\\*]", "");
        article = article.replaceAll("\\[/\\*]", "");
        article = article.replaceAll("\\[/m]", "");
        article = article.replaceAll("\\[m1]", "");
        article = article.replaceAll("\\[m2]", "&emsp;");
        article = article.replaceAll("\\[m3]", "&emsp;&emsp;");
        article = article.replaceAll("\\[m4]", "&emsp;&emsp;&emsp;");
        article = article.replaceAll("\\[m5]", "&emsp;&emsp;&emsp;&emsp;");
        article = article.replaceAll("\\[m6]", "");
        article = article.replaceAll("\\[i]", "<b><i>");
        article = article.replaceAll("\\[/i]", "</i></b>");
        article = article.replaceAll("\\[trn]", "");
        article = article.replaceAll("\\[/trn]", "");
        article = article.replaceAll("\\[c darkblue]", "<span style='color: darkblue'>");
        article = article.replaceAll("\\[c gray]", "<span style='color: gray'>");
        article = article.replaceAll("\\[c blue]", "<span style='color: blue'>");
        article = article.replaceAll("\\[c green]", "<span style='color: green'>");
        article = article.replaceAll("\\[c crimson]", "<span style='color: crimson'>");
        article = article.replaceAll("\\[c maroon]", "<span style='color: maroon'>");
        article = article.replaceAll("\\[c darkgoldenrod]", "<span style='color: darkgoldenrod'>");
        article = article.replaceAll("\\[/c]", "</span>");
        article = article.replaceAll("[\\\\\\[]", "");
        article = article.replaceAll("[\\\\\\]]", "");
        return article;
    }

    private String addStructure(String article) {

        var stack = new ArrayDeque<Integer>();
        stack.push(0);

        var result = new StringBuilder();

        var pattern = Pattern.compile("\\[m[1-6]][^\\p{Cntrl}]*\\[/m]");
        var matcher = pattern.matcher(article);
        while(matcher.find()) {
            var line = matcher.group();
            var level = Integer.valueOf(String.valueOf(line.charAt(2)));
            while (stack.peek() >= level) {
                stack.pop();
                result.append("</div>").append("\n");
            }
            stack.push(level);
            line = line.replaceAll("(\\[m[1-6]]|\\[/m])", "");
            result.append("<div class='m").append(level)
                    .append(" ms-").append(level)
                    .append(" mt-").append(6-level)
                    .append(level == 1 ? " fs-5": " fs-6").append("'>")
                    .append(line).append("\n");
        }

        while (stack.size() > 1) {
            stack.pop();
            result.append("</div>").append("\n");
        }

        return result.toString();
    }

}
