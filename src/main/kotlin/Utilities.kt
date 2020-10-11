import java.io.File
import java.time.LocalDateTime
import models.Error

fun printException(e : Exception, s : String = "") {
    if (s != "") {
        println(s)
    }
    println(e.toString())
    println(e.stackTraceToString())
    Error.add("", e.toString())
}

fun campaignIsCorrupted(path : String) : Boolean {
    return !File("$path/model.txt").exists()
}

fun findAllCampaigns() : List<String> {
    val returnList = ArrayList<String>()
    val currentDir = File(".")
    currentDir.walk().filter {
        file ->
        file.name.contains("2020") }.forEach {
        returnList.add(it.name)
    }

    return returnList
}

fun clearDirectory(dirName  : String = Configuration.SentenceDirectory) {
    val dir = File(dirName)

    if (!dir.isDirectory) {
        println("Not a directory. Do nothing")
    }

    val listFiles = dir.listFiles()
    for (file in listFiles!!) {
        println("Deleting " + file.name)
        file.delete()
    }
}

fun dateString() : String = LocalDateTime.now().toString().replace(":", "-")