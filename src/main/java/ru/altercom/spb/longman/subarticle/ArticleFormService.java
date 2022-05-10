package ru.altercom.spb.longman.subarticle;

import org.springframework.stereotype.Service;
import ru.altercom.spb.longman.word.WordNotFoundException;
import ru.altercom.spb.longman.word.WordRepository;

import java.util.ArrayDeque;
import java.util.regex.Pattern;

@Service
public class ArticleFormService {

    private static final Pattern TEXT = Pattern.compile("▷\\[b] \\[trn] [^\\p{Cntrl}]+\\[/trn]\\[/b]");
    private static final Pattern TRANSCRIPTION = Pattern.compile("] /[^\\p{Cntrl}]+/\\[");
    private static final Pattern FEATURE = Pattern.compile("/[\\\\\\[][^\\p{Cntrl}]+[\\\\\\]]\\[/c]");
    private static final Pattern PART = Pattern.compile("\\[c \\w][^\\p{Cntrl}]+\\[/c]");

    private final WordRepository wordRepo;

    public ArticleFormService(WordRepository wordRepo) {
        this.wordRepo = wordRepo;
    }

    private String toHtml(String line) {
        line = line.replace("▷", "");
        line = line.replaceAll("\\[m4]\\[/m]", "");
        line = line.replaceAll("\\[/m]", "[/m]<br>");
        line = line.replaceAll("\\[b]", "<b>");
        line = line.replaceAll("\\[/b]", "</b>");
        line = line.replaceAll("\\[u]", "<b>");
        line = line.replaceAll("\\[/u]", "</b>");
        line = line.replaceAll("\\[ex]", "<i>");
        line = line.replaceAll("\\[/ex]", "</i>");
        line = line.replaceAll("\\[\\*]", "");
        line = line.replaceAll("\\[/\\*]", "");
        line = line.replaceAll("\\[i]", "");
        line = line.replaceAll("\\[/i]", "");
        line = line.replaceAll("\\[trn]", "");
        line = line.replaceAll("\\[/trn]", "");
        line = line.replaceAll("\\[c darkblue]", "<span style='color: darkblue'>");
        line = line.replaceAll("\\[c gray]", "<span style='color: gray'>");
        line = line.replaceAll("\\[c blue]", "<span style='color: darkblue'>");
        line = line.replaceAll("\\[c green]", "<span style='color: green'>");
        line = line.replaceAll("\\[c crimson]", "<span style='color: crimson'>");
        line = line.replaceAll("\\[c maroon]", "<span style='color: maroon'>");
        line = line.replaceAll("\\[c darkgoldenrod]", "<span style='color: darkgoldenrod'>");
        line = line.replaceAll("\\[/c]", "</span>");
        line = line.replaceAll("[\\\\\\[]", "");
        line = line.replaceAll("[\\\\\\]]", "");
        return line.trim();
    }

    private String findText(String str, Pattern pattern) {
        var matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    private ArticleForm parseLine1(String line) {
        var html = "<span style='color: darkblue'><b>" + toHtml(line) + "</b></span>";
        return new ArticleForm(null, 1, html, "", "");
        /*if (line.contains("INDEX:") || line.contains("RELATED WORDS")) {
            var html = toHtml(line);
            return new ArticleForm(null, 1, html, "", "");
        } else {
            var arr = line.split(" ", 2);
            var num = arr[0].replaceAll("(\\[b]|\\[/b]|\\.)", "");
            var html = toHtml(arr[1]);
            return new ArticleForm(Long.valueOf(num), 1, html, "", "");
        }*/
    }

    private ArticleForm parseLine2(String line) {
        var html = toHtml(line);
        return new ArticleForm(null, 2, html, "", "");
        /*if (line.startsWith("▷")) {
            var text = toHtml(findText(line, TEXT).replace("▷", ""));
            var transcription = toHtml(findText(line, TRANSCRIPTION));
            var feature = toHtml(findText(line, FEATURE));
            if (feature.length() > 0) {
                feature = feature.substring(1);
            }
            return new ArticleForm(null, 2, text, transcription, feature);
        } else {
            var html = toHtml(line);
            return new ArticleForm(null, 2, html, "", "");
        }*/
    }

    private ArticleForm parseLine3(String line) {
        var html = toHtml(line);
        return new ArticleForm(null, 3, html, "", "");
        /*if (line.startsWith("[b]")) {
            var arr = line.split(" ", 2);
            var num = arr[0].replaceAll("(\\[b]|\\[/b]|\\.)", "");
            var html = toHtml(arr[1]);
            return new ArticleForm(Long.valueOf(num), 3, html, "", "");
        } else if (line.startsWith("↑<<")) {
            var html = toHtml(line);
            return new ArticleForm(null, 3, html, "", "");
        } else {
            // [c darkgoldenrod]nudity[/c] /ˈnjuːdɪti, ˈnjuːdətiǁˈnuː-/[c green] \[uncountable noun\][/c]
            // [c darkgoldenrod]nude[/c][c green] \[countable noun\][/c]
            // [c crimson][trn]strip somebody naked[/trn][/c]

            var html = toHtml(line);
            return new ArticleForm(null, 3, html, "", "");
        }*/
    }

    private ArticleForm parseLine4(String line) {
        var html = toHtml(line);
        return new ArticleForm(null, 4, html, "", "");
    }

    private ArticleForm parseLine6(String line) {
        var html = toHtml(line);
        return new ArticleForm(null, 6, html, "", "");
    }

    private ArticleForm parseLine(int level, String line) {
        return switch (level) {
            case 1 -> parseLine1(line);
            case 2 -> parseLine2(line);
            case 3 -> parseLine3(line);
            case 4 -> parseLine4(line);
            case 6 -> parseLine6(line);
            default -> throw new IllegalArgumentException("Illegal level " + level);
        };
    }

    public ArticleForm parseWord(Long id) {
        var word = wordRepo.findById(id)
            .orElseThrow(() -> new WordNotFoundException(id));
        var article = word.getArticle();

        var root = new ArticleForm(word.getId(), 0, word.getName(), "", "");
        var stack = new ArrayDeque<ArticleForm>();
        stack.push(root);

        var pattern = Pattern.compile("\\[m[1-6]][^\\p{Cntrl}]*\\[/m]");
        var matcher = pattern.matcher(article);
        while(matcher.find()) {
            var line = matcher.group();
            var level = Integer.parseInt(String.valueOf(line.charAt(2)));
            //line = line.replaceAll("(\\[m[1-6]]|\\[/m])", "");
            line = line.replaceAll("(\\[m[1-6]])", "<div>");
            line = line.replaceAll("(\\[/m])", "</div>");
            while (stack.peek() != null && stack.peek().getLevel() >= level) {
                stack.pop();
            }
            var parent = stack.peek();
            var child = parseLine(level, line);
            assert parent != null;
            parent.getChildren().add(child);
            stack.push(child);
        }

        while (stack.size() > 1) {
            stack.pop();
        }

        return root;
    }

    public void calculatePositions(ArticleForm articleForm) {
        int i1 = 0;
        for (var l1 : articleForm.getChildren()) {
            if (l1.isSimple()) {
                i1++;
                var x1 = 50;
                var y1 = i1 * 300;
                l1.setX(x1);
                l1.setY(y1);

                int i2 = 0;
                int s2 = l1.getChildren().size();
                for (var l2: l1.getChildren()) {
                    i2++;
                    var x2 = (int)(200 * Math.cos(i2 * 2 * Math.PI/s2));
                    var y2 = (int)(100 * Math.sin(i2 * 2 * Math.PI/s2));
                    l2.setX(x2);
                    l2.setY(y1 + y2);
                    l2.setId(1000L * i1 + i2);
                }
            }
        }

    }

}
