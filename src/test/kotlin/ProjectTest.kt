package io.Kotest.provided


import io.kotest.core.config.AbstractProjectConfig

object ProjectConfig : AbstractProjectConfig() {
    var started : Long = 0

    override fun beforeAll() {
        started = System.currentTimeMillis()
    }


    override  fun afterAll() {
        val time = System.currentTimeMillis() - started
        println("Took $time to finish all tests")
    }
}