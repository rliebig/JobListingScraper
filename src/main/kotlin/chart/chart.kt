package chart

import cityvisualisation.getCurrentTimestamp
import java.io.File

fun writeTemplate(text : String) : String {
    val result = getCurrentTimestamp() + "chart,html"
    File(result).writeText(text)
    return result
}

fun getTemplate() : String {
    return File("/Users/etti1-mac1/Downloads/JobListingScraper/src/main/kotlin/chart").readText()
}