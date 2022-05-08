package ru.altercom.spb.longman.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.altercom.spb.longman.word.Word;
import ru.altercom.spb.longman.word.WordService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

//@Component
public class DictionaryLoader implements CommandLineRunner {

    private final WordService wordService;

    private final Pattern WORD_PATTERN = Pattern.compile("[A-Z/ ,']+");

    public DictionaryLoader(WordService wordService) {
        this.wordService = wordService;
    }

    @Override
    public void run(String... args) throws Exception {
        readFile();
    }

    private void readFile() throws IOException {
        var resource = new ClassPathResource("Longman.dsl");

        try (var br = new BufferedReader(new FileReader(resource.getFile()))) {
            String line;
            int state = 0;
            String name = "";
            StringBuilder article = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (state == 0 && checkWord(line)) {
                    name = line;
                    state = 1;
                } else if (state == 1) {
                    if (checkArticle(line)) {
                        article = new StringBuilder(line);
                        state = 2;
                    }
                } else if (state == 2) {
                    if (checkArticle(line)) {
                        article.append(line);
                    } else {
                        var word = new Word(null, name, article.toString());
                        var id = wordService.save(word);
                        state = 0;
                        if (checkWord(line)) {
                            name = line;
                            state = 1;
                        }
                    }
                }
            }
        }
    }

    private boolean checkWord(String line) {
        // return WORD_PATTERN.matcher(line).matches();
        for (int i = 0; i < line.length(); i++) {
            if (Character.isLowerCase(line.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean checkArticle(String line) {
        return ((line.charAt(0)) == 9) && (line.endsWith("[/m]"));
    }
}
