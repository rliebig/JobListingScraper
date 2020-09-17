import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.endWith
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.string.startWith
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import org.jsoup.Jsoup
import org.jsoup.select.Elements


class GetListWordsSpec : StringSpec({
    "list should be found" {
        val html : String = """
            <html>
                <div>
                    <ul>
                        <li>Coffee</li>
                        <li>Tea</li>
                        <li>IceTea</li>
                    </ul>
                </div>
            </html>
        """.trimIndent()

        val elements = Jsoup.parse(html).allElements
        val values = getListWords(elements)
        values shouldContain "Coffee"
        values shouldContain "Tea"
        values shouldContain "IceTea"
        println(values)

        values.size shouldBe 3
    }

    "No list within html should return zero words" {
        val html : String = """
            <html>
                <div>
                    <ul>
                        <blah>Coffee</blah>
                        <blah>Tea</blah>
                        <blah>IceTea</blah>
                    </ul>
                </div>
            </html>
        """.trimIndent()

        val elements = Jsoup.parse(html).allElements
        val values = getListWords(elements)

        values.size shouldBe 0
    }

    "empty hmtl string should return zero words" {
        val html : String = ""

        val elements = Jsoup.parse(html).allElements
        val values = getListWords(elements)
        println(values)

        values.size shouldBe 0
    }

    "serve no correct element by Jsoup"  {
        val html : String = ""

        val elements = Jsoup.parse(html).getElementsByClass("beautiful")
        val values = getListWords(elements)
        println(values)

        values.size shouldBe 0
    }
})

class WordFilterSpec : StringSpec({
    "<li> at the beginning should be removed" {
        val filteredWord = filterWord("<li>hello")
        filteredWord shouldNot startWith("<li>")
    }

    "</li> at the end should be remove" {
        val filteredWord = filterWord("hello</li>")
        filteredWord shouldNot endWith("</li>")
    }

    "- should be removed at every position" {
        val filteredWord = filterWord("a-t-a-t")
        filteredWord shouldBe "atat"
    }

    "soft hypen should be removed from word" {
        val filteredWord = filterWord("test\u00ADword")
        filteredWord shouldNotContain "\u00AD"
        filteredWord shouldBe "testword"
    }

    "< should be removed at every position" {
        val filteredWord = filterWord("<abp")
        filteredWord shouldBe "abp"
    }

    "> should be removed at every position" {
        val filteredWord = filterWord("abp>")
        filteredWord shouldBe "abp"
    }

    "<strong> should be removed at the beginning" {
        val filteredWord = filterWord("<strong>Eigenschaften")
        filteredWord shouldNot startWith("<strong>")
        filteredWord shouldBe "eigenschaften"
    }

    "Words should be parsed lowercase" {
        val filterWord = filterWord("AaAaAaAaBbBbBZz")
        filterWord shouldNotContain "A"
        filterWord shouldNotContain "B"
        filterWord shouldNotContain "Z"
    }

    "Should parse all letters lowercase" {
        val arb = Arb.string()
        arb.checkAll { a ->
            val filterWord = filterWord(a)
            for (c in 'A'..'Z') {
                filterWord shouldNotContain c.toString()
            }
        }
    }
})

class AddToModelSpec : AnnotationSpec() {
    fun `Word should be sucessfully added to model`() {
        addWordToModel("test")
        Model.items.keys shouldContain "test"
    }

    fun `Increment should work`() {
        addWordToModel("increment")
        addWordToModel("increment")
        Model.items.keys shouldContain "increment"
        Model.items["increment"] shouldBe 2
    }

    fun `Many Increment should work`() {
        val number = 20
        repeat(number) {
            addWordToModel("many")
        }
        Model.items.keys shouldContain "many"
        Model.items["many"] shouldBe number
    }

    suspend fun `randomized insertion test should work`() {
        val number = 20

        val arb = Arb.string()
        arb.checkAll { a ->
            repeat(number) {
                addWordToModel(a)
            }

            Model.items.keys shouldContain filterWord(a)
            Model.items[filterWord(a)] shouldBe number
        }
    }

    @BeforeEach
    fun clearModel() {
        Model.items = HashMap<String, Int>()
    }
}


