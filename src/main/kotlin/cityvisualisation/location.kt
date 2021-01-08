package cityvisualisation

import java.awt.Desktop
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.random.Random

fun getCurrentTimestamp() : String {
    return DateTimeFormatter
        .ofPattern("yyyy-MM-dd-HH-mm-ss-SSSSSS")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())
}

fun getTemplate() : String {
    return File("/Users/etti1-mac1/Downloads/JobListingScraper/src/main/kotlin/cityvisualisation/template.html").readText()
}

fun writeTemplate(text : String) : String {
    val result = getCurrentTimestamp() + ".html"
    File(result).writeText(text)
    return result
}

fun convertCityNameToLatLog(name : String, hashable : String = "a") : String {
    var result = ""
    val file = BufferedReader(FileReader("/Users/etti1-mac1/Downloads/JobListingScraper/src/main/kotlin/cityvisualisation/cities.csv"))
    var line = file.readLine()
    while(line != null) {
        val splitted = line.split(";")

        val cityName = splitted[0].split(",")[0]
        val smallVariationX = 0.001 * Random(hashable.hashCode()).nextInt(-10,10).toDouble()
        val smallVariationY = 0.001 * Random(hashable.hashCode()).nextInt(-10,10).toDouble()

        val lat = (splitted[1].toDouble() + smallVariationX).toString()
        val long = (splitted[2].toDouble() + smallVariationY).toString()

        if(name == cityName) {
            result = "[$lat,$long]"
            break
        }
        line = file.readLine()
    }

    return result
}

fun addToggle(coordinates : String, link : String, title : String) : String {
    val scriptText = """
        try {
        L.marker($coordinates)
            .addTo(Karte)
            .bindPopup("<a href='$link'>$title</a>"); }
            catch(err) {
                console.log(err);
            }
    """.trimIndent()
    return scriptText
}

data class toggle (
    val city : String,
    val link : String,
    val title : String
)

fun toggleHeatmap(datapoints : List<toggle>) {
    val builder = StringBuilder()
    datapoints.forEach {
        val coordinates = convertCityNameToLatLog(it.city, it.title)
        builder.append(addToggle(coordinates, it.link, it.title))
    }

    val template = getTemplate()
        .replace("<<REPLACEKEY>>", "")
        .replace("//SECONDKEY", builder.toString())

    val writtenFile =writeTemplate(template)
    Desktop.getDesktop().browse(File(writtenFile).toURI())

}

fun showHeatmap(datapoints : List<String>){
    val template = getTemplate()
    val builder = StringBuilder()
    for(point in datapoints) {
        val name = convertCityNameToLatLog(point)
        if (name != "") {
            builder.append(convertCityNameToLatLog(point))
            builder.append(",")
        }
    }
    builder.removeSuffix(",")

    val newtext = template.replace("<<REPLACEKEY>>", builder.toString())
    val writtenFile = writeTemplate(newtext)
    Desktop.getDesktop().browse(File(writtenFile).toURI())
}

fun main() {
    toggleHeatmap(listOf(toggle("bundesweit", title = "(Senior) Microsoft Azure Solution Architect (m/w/d)",
    link = "https://www.stepstone.de/stellenangebote--Senior-Microsoft-Azure-Solution-Architect-m-w-d-bundesweit-Berlin-Frankfurt-am-Main-Hamburg-Koeln-Leipzig-Muenchen-Sopra-Steria--6660378-inline.html1")))
}
