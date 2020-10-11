package models

import filterWord
import java.io.File
import java.io.FileWriter

data class Date (
        val year : Int,
        val month : Int,
        val day : Int
)

//TODO("this probably needs more testing.")
fun sanitizeHeadLine(headline : String) : String {
    return headline
            .filter { it != ' ' }
            .filter { it != ';' }
            .toString()
}

fun sanitizeSemicolon(str : String) : String {
    return str.filter {it != ';'}.toString()
}
class WebPage(var url: String,
              var headline : String,
              var keywords: List<String>,
) {
}

fun createKeywordString(list : List<String>) : String {
    var endString = ""
    for (keyword in list ) {
        endString = "$endString$keyword,"
    }
    return endString
}


fun saveWebPage(webPage : WebPage) {
    val fileName = Configuration.WebPageDirectory + "/" + "webpage.txt"
    if(!File(fileName).exists()) {
        File(fileName).createNewFile()
    }
    val writer = FileWriter(fileName, true)
    val url = sanitizeSemicolon(webPage.url)
    val headline = sanitizeHeadLine(webPage.headline)
    val keywords = createKeywordString(webPage.keywords.map { filterWord(it) })

    writer.write("\n$url;$headline;$keywords")
    writer.flush()
    writer.close()
}