package models

import filterWord

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.IllegalArgumentException


class Sentence(val keyword : String) {
    val list : ArrayList<String> = ArrayList<String>()
    val webPage : WebPage? = null
    val keywordSecure = filterWord(keyword)

    init {
        //if (keyword == "")
        //    throw IllegalArgumentException("Sentence class can not have empty keyword argument")

        try {
            val file = File("${Configuration.SentenceDirectory}/$keywordSecure.txt")
            if (!file.exists())
                file.createNewFile()

            val reader  = file.reader()
            reader.forEachLine {
                line -> list.add(line)
            }
            reader.close()
        } catch (e : Exception) {
            println("error with keyword $keywordSecure")
            println(e.toString())
            println(e.stackTraceToString())
        }
    }

    fun addSentence(sentence : String) {
        list.add(sentence)
        save()
    }

    fun getSentences() : List<String> {
        return list
    }

    private fun save() {


        try {
            val writer = FileWriter("${Configuration.SentenceDirectory}/$keywordSecure.txt")
            list.forEach { line ->
                writer.write("$line\n")
            }
            writer.close()
        } catch (e : Exception) {
            println("error saving keyword $keywordSecure")
            println(e.toString())
            println(e.stackTraceToString())
        }
    }
}