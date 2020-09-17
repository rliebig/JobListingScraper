package io.Kotest.provided


import io.kotest.core.config.AbstractProjectConfig
import java.io.File

object ProjectConfig : AbstractProjectConfig() {
    var started : Long = 0

    override fun beforeAll() {
        started = System.currentTimeMillis()
        File("sentenceTest/").mkdirs()
    }


    override  fun afterAll() {
        val time = System.currentTimeMillis() - started
        println("Took $time to finish all tests")
    }
}