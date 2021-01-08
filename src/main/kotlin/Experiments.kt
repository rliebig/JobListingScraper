fun <T> Collection<T>.joinToString(
    seperator : String = ", ",
    prefix : String = "",
    postfix : String = ""
) : String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(seperator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

fun processTheAnswer(f : (Int) -> Int) {
    println(f(42))
}

fun clozeEveryWords(str : String) : List<String> {
    val result = ArrayList<String>()
    val splittedStr = str.split(" ")
    var i : Int = 0

    splittedStr.forEachIndexed { index, string ->
        val previous = splittedStr.subList(0, index).joinToString(seperator = " ")
        val posterior = if (index != splittedStr.size - 1) {
            splittedStr.subList(index + 1, splittedStr.size - 1).joinToString(seperator =  " ")
        } else {
            ""
        }
        result.add("$previous {{$string}} $posterior")
    }

    return result
}

fun Collection<String>.clozeEverything() : String {
    val result = StringBuilder()
    for ((index, element) in this.withIndex()) {
        result.append("{{c${index+1}::$element}} ")
    }

    return result.toString()
}

fun wordsToList(str : String) : List<String> {
    val result = ArrayList<String>()
    str.split(" ", "\n").forEach {
        result.add(it)
    }

    return result
}

fun main() {

    val words = wordsToList("""
        Principle of Virtual Rewards
        Computer Simulations that reward target behaviours in a virtual world such as giving virtual rewards
        for exercising can influence people to perform the target behaviour more frequently and effectively
        in the real world
    """.trimIndent())
    println(words.clozeEverything())
}

fun other() {
    val myList = listOf("Hurensohn", "Nutte", "Turret")
    myList.filter {
        it ->
        it !in setOf("a", "b", "c")
    }
    println(myList.joinToString(seperator = ", ", prefix = "HEY", postfix = "LOS"))
    processTheAnswer { number -> number * 2 }
}

