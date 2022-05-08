package ru.altercom.spb.longman.subarticle;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubarticleService {

    private final SubarticleRepository subarticleRepo;

    public SubarticleService(SubarticleRepository subarticleRepo) {
        this.subarticleRepo = subarticleRepo;
    }

    private SubarticleForm fetchCardLevel2(Long id) {
        var subarticle = subarticleRepo.findById(id)
                .orElseThrow(() -> new SubarticleNotFoundException(id));

        var root = new SubarticleForm(subarticle.getId(), null, subarticle.getLevel(), prepareHtml(subarticle.getArticle()));

        var list = subarticleRepo.findAllByParentId(id);
        for (var item: list) {
            var child = new SubarticleForm(item.getId(), root, item.getLevel(), prepareHtml(item.getArticle()));
            root.getChildren().add(child);

            var list2 = subarticleRepo.findAllByParentId(item.getId());
            for (var item2: list2) {
                var child2 = new SubarticleForm(item2.getId(), child, item2.getLevel(), prepareHtml(item2.getArticle()));
                child.getChildren().add(child2);
            }
        }

        return root;
    }

    private String prepareHtml(String article) {
        article = article.replaceAll("\\[b]", "<b>");
        article = article.replaceAll("\\[/b]", "</b>");
        article = article.replaceAll("\\[u]", "<b>");
        article = article.replaceAll("\\[/u]", "</b>");
        article = article.replaceAll("\\[ex]", "<i>");
        article = article.replaceAll("\\[/ex]", "</i>");
        article = article.replaceAll("\\[\\*]", "");
        article = article.replaceAll("\\[/\\*]", "");
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

    public List<SubarticleForm> getCards(Long wordId) {
        return subarticleRepo.findAllCards(wordId)
            .stream()
            .map(this::fetchCardLevel2)
            .toList();
    }
}
