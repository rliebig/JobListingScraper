import java.io.File

fun printException(e : Exception, s : String = "") {
    if (s != "") {
        println(s)
    }
    println(e.toString())
    println(e.stackTraceToString())
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
