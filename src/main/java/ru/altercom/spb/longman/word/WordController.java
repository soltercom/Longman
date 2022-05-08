package ru.altercom.spb.longman.word;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.altercom.spb.longman.subarticle.ArticleFormService;

@Controller
@RequestMapping("/dictionary")
public class WordController {

    private static final String LIST = "/dictionary/list";
    private static final String LIST_DATA = "/dictionary/list-fragment :: list-data";
    private static final String FORM = "/dictionary/form";
    private static final String SCHEMA = "/dictionary/schema";

    private final WordService wordService;
    private final ArticleFormService articleFormService;

    public WordController(WordService wordService, ArticleFormService articleFormService) {
        this.wordService = wordService;
        this.articleFormService = articleFormService;
    }

    @GetMapping
    public String list(@RequestParam(value = "search", required = false) String search,
                       ModelMap model) {
        if (search == null) {
            model.put("wordList", wordService.findAll());
            return LIST;
        } else if (search.isEmpty()) {
            model.put("wordList", wordService.findAll());
            return LIST_DATA;
        } else {
            model.put("wordList", wordService.searchByName(search));
            return LIST_DATA;
        }
    }

    @GetMapping("/{id}")
    public String processForm(@PathVariable("id") Long id, ModelMap model) {
        model.put("word", wordService.findById(id));
        return FORM;
    }

    @GetMapping("/{id}/schema")
    public String processSchema(@PathVariable("id") Long id, ModelMap model) {
        model.put("word", wordService.findById(id));
        var articleForm = articleFormService.parseWord(id);
        articleFormService.calculatePositions(articleForm);
        model.put("articleForm", articleForm);
        return SCHEMA;
    }

}
