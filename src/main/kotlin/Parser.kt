import models.Sentence
import org.jsoup.select.Elements
import java.lang.IndexOutOfBoundsException




fun parseList(content : Elements) {
    val keyWords = mutableListOf<String>()
    try {
        content[0].getElementsByTag("li").forEach { it ->
            parse(it.html())

        }
    } catch (e: IndexOutOfBoundsException) {
        println(e.toString())
    }
}

fun filterWord(word : String) : String {
    return word
            .removePrefix("<strong>")
            .removePrefix("<li>")
            .removeSuffix("</li>")
            .removePrefix("<p>")
            .removePrefix("<b>")
            .removeSuffix("</b>")
            .removeSuffix("</p>")
            .filter { it != '-' }
            .filter { it != ',' }
            .filter { it != '(' }
            .filter { it != ')' }
            .filter { it != '<' }
            .filter { it != '>' }
            .filter { it != '&' }
            .filter { it != ';' }
            .filter { it != '\n' }
            .filter { it != '/' }
            .filter { it != '\u00AD' }
            .toLowerCase()
}

fun sanitizeHtml(text : String) : String {
    return text.replace("</li>", "")
        .replace("&nbsp", "")
        .replace("z.B.", "zB")
        .replace("<strong>","")
        .replace("</strong>","")
}

fun parse(text : String) : List<Sentence>{
    val returnList = mutableListOf<Sentence>()
    sanitizeHtml(text).split(".").forEach { sentenceText ->
        sentenceText.split(" ").forEach { word ->
            val set = Sentence(filterWord(word))
            val correctedSentenceText = sentenceText.split(" ").map {
                it -> filterWord(it)
            }.joinToString(" ")
            set.addSentence(correctedSentenceText)
        }
    }

    return returnList
}