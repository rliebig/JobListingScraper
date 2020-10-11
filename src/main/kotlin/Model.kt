import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.FileReader
import java.io.FileWriter



object Model {
    var items  = HashMap<String,Int>()

    fun filter() {
        val newItems = HashMap<String, Int>()
        for (item in items) {
            if(!bansWord.contains(item.key)) {
                newItems[item.key] = item.value
            }
        }

        items = newItems
    }

    fun saveModel() {
        val writer = FileWriter(Configuration.modelFile)
        items.forEach {
            (t,u) ->
                writer.write("$t=$u\n")
        }
        writer.close()
    }

    fun readModel() {
        val newItems = HashMap<String, Int>()

        val reader = FileReader(Configuration.modelFile)
        reader.forEachLine {
            line ->
            println(line)
            try {
                val key = line.split("=")[0]
                val value = line.split("=")[1]
                newItems[key] = value.toInt()
            } catch (e : Exception) {
                println(e.toString())
            }
        }
        reader.close()
        items = newItems
    }


    val bansWord = listOf(
        "den",
        "der",
        "an",
        "ein",
        "die",
        "bei",
        "ist",
        "du",
        "bzw.",
        "im",
        "in",
        "",
        "z.b.",
        "gerne",
        "sowie",
        "und",
        "eine",
        "kenntnisse",
        "oder",
        "hast",
        "dein",
        "von",
        "sie",
        "vorzugsweise",
        "bspw.",
            "dich",
        "zur",
        "mit",
        "beim",
        "zu",
        "über",
        "wie",
        "haben",
        "-",
        "kentnisse",
        "zum",
        "and",
        "auf"
    )
}

class ModelInstance {
    var items  = HashMap<String,Int>()

    fun filter() {
        val newItems = HashMap<String, Int>()
        for (item in items) {
            if(!bansWord.contains(item.key)) {
                newItems[item.key] = item.value
            }
        }

        items = newItems
    }

    fun saveModel() {
        val writer = FileWriter(Configuration.modelFile)
        items.forEach {
            (t,u) ->
                writer.write("$t=$u\n")
        }
        writer.close()
    }

    fun readModel(fileName : String) {
        val newItems = HashMap<String, Int>()

        val reader = FileReader(fileName)
        reader.forEachLine {
            line ->
            println(line)
            try {
                val key = line.split("=")[0]
                val value = line.split("=")[1]
                newItems[key] = value.toInt()
            } catch (e : Exception) {
                println(e.toString())
            }
        }
        reader.close()
        items = newItems
    }


    val bansWord = listOf(
        "den",
        "der",
        "an",
        "ein",
        "die",
        "bei",
        "ist",
        "du",
        "bzw.",
        "im",
        "in",
        "",
        "z.b.",
        "gerne",
        "sowie",
        "und",
        "eine",
        "kenntnisse",
        "oder",
        "hast",
        "dein",
        "von",
        "sie",
        "vorzugsweise",
        "bspw.",
            "dich",
        "zur",
        "mit",
        "beim",
        "zu",
        "über",
        "wie",
        "haben",
        "-",
        "kentnisse",
        "zum",
        "and",
        "auf"
    )
}