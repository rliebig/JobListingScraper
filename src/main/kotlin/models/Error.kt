package models

import java.io.FileWriter

object Error {
    fun add(url : String, errorMessage : String) {
        val fileName = Configuration.SentenceDirectory + "/" + Configuration.errorFileName
        val writer = FileWriter(fileName)
        writer.appendLine("$url:::$errorMessage")
    }

    @Deprecated("This has no reason to be used.")
    fun getErrors() : List<String> {
        return mutableListOf<String>()
    }
}