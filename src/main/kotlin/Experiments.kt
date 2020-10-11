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


fun main() {
    val myList = listOf("Hurensohn", "Nutte", "Turret")
    myList.filter {
        it ->
        it !in setOf("a", "b", "c")
    }
    println(myList.joinToString(seperator = ", ", prefix = "HEY", postfix = "LOS"))
    processTheAnswer { number -> number * 2 }
}