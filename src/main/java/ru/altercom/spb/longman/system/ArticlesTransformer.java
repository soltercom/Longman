package ru.altercom.spb.longman.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.altercom.spb.longman.subarticle.Subarticle;
import ru.altercom.spb.longman.subarticle.SubarticleRepository;
import ru.altercom.spb.longman.word.Word;
import ru.altercom.spb.longman.word.WordRepository;

import java.util.ArrayDeque;
import java.util.regex.Pattern;

//@Component
public class ArticlesTransformer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        transform();
    }

    private final WordRepository wordRepo;
    private final SubarticleRepository subarticleRepo;

    @Autowired
    public ArticlesTransformer(WordRepository wordRepo, SubarticleRepository subarticleRepo) {
        this.wordRepo = wordRepo;
        this.subarticleRepo = subarticleRepo;
    }

    public void transform() {
        var words = wordRepo.findAll();

        for (var word: words) {
            saveStructure(word);
        }
    }

    private void saveStructure(Word word) {

        record Sub(Long id, int level) {}

        var stack = new ArrayDeque<Sub>();
        stack.push(new Sub(null, 0));

        var pattern = Pattern.compile("\\[m[1-6]][^\\p{Cntrl}]*\\[/m]");
        var matcher = pattern.matcher(word.getArticle());
        while(matcher.find()) {
            var line = matcher.group();
            int level = Integer.parseInt(String.valueOf(line.charAt(2)));
            while (stack.peek() != null && stack.peek().level >= level) {
                stack.pop();
            }
            var article = line.replaceAll("(\\[m[1-6]]|\\[/m])", "");

            var item = new Subarticle(null, word.getId(), stack.peek().id(), level, article);
            var savedItem = subarticleRepo.save(item);

            stack.push(new Sub(savedItem.getId(), level));

        }
    }

}
