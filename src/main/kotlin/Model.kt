import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class ModelInstance {
    var items  = HashMap<String,Int>()
                                                        object Model {
    fun filter() {    var items  = HashMap<String,Int>()
        val newItems = HashMap<String, Int>()
        for (item in items) {    fun filter() {
            if(!bansWord.contains(item.key)) {        val newItems = HashMap<String, Int>()
                newItems[item.key] = item.value        for (item in items) {
            }            if(!bansWord.contains(item.key)) {
        }                newItems[item.key] = item.value
                                                                    }
        items = newItems        }
    }
                                                                items = newItems
    fun saveModel() {    }
        val writer = FileWriter(Configuration.modelFile)
        items.forEach {    fun saveModel() {
            (t,u) ->        val writer = FileWriter(Configuration.modelFile)
                writer.write("$t=$u\n")        items.forEach {
        }            (t,u) ->
        writer.close()                writer.write("$t=$u\n")
    }        }
                                                                writer.close()
    fun readModel() {    }
        val newItems = HashMap<String, Int>()
                                                            fun readModel() {
        val reader = FileReader(Configuration.modelFile)        val newItems = HashMap<String, Int>()
        reader.forEachLine {
            line ->        val reader = FileReader(Configuration.modelFile)
            println(line)        reader.forEachLine {
            try {            line ->
                val key = line.split("=")[0]            println(line)
                val value = line.split("=")[1]            try {
                newItems[key] = value.toInt()                val key = line.split("=")[0]
            } catch (e : Exception) {                val value = line.split("=")[1]
                println(e.toString())                newItems[key] = value.toInt()
            }            } catch (e : Exception) {
        }                println(e.toString())
        reader.close()            }
        items = newItems        }
    }        reader.close()
                                                                items = newItems
                                                            }
    val bansWord = listOf(
        "den",
        "der",    val bansWord = listOf(
        "an",        "den",
        "ein",        "der",
        "die",        "an",
        "bei",        "ein",
        "ist",        "die",
        "du",        "bei",
        "bzw.",        "ist",
        "im",        "du",
        "in",        "bzw.",
        "",        "im",
        "z.b.",        "in",
        "gerne",        "",
        "sowie",        "z.b.",
        "und",        "gerne",
        "eine",        "sowie",
        "kenntnisse",        "und",
        "oder",        "eine",
        "hast",        "kenntnisse",
        "dein",        "oder",
        "von",        "hast",
        "sie",        "dein",
        "vorzugsweise",        "von",
        "bspw.",        "sie",
            "dich",        "vorzugsweise",
        "zur",        "bspw.",
        "mit",            "dich",
        "beim",        "zur",
        "zu",        "mit",
        "über",        "beim",
        "wie",        "zu",
        "haben",        "über",
        "-",        "wie",
        "kentnisse",        "haben",
        "zum",        "-",
        "and",        "kentnisse",
        "auf"        "zum",
    )        "and",
}        "auf"
    )
}