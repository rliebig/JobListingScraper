import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.be
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldNotContainKey
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.endWith
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.string.startWith
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import org.jsoup.Jsoup
import org.jsoup.select.Elements

//TODO("")
class SanitzeHtmlSpec : StringSpec({
    "No soft hypen in replaced characters" {
        val arb = Arb.string()
        arb.checkAll(99000) {
            a ->
            val result = sanitizeHtml(a)
            result shouldNotContain  "\u00AD"
        }
    }

    "Specifically trigger soft hypen in previous result" {
        val result = sanitizeHtml("&nbsptestword")
        result shouldNotContain "\u00AD"
        result shouldBe "testword"
    }

    "Strong should be filtered from software" {
        var arb = Arb.string()
        arb.checkAll { a ->
            val word = a + "<strong>" + arb.next()
            val result = sanitizeHtml(word)
            result shouldNotContain "<strong>"
        }
    }

    "Trigger the test in another way" {
        val result = sanitizeHtml("<strong>")
        result shouldNot be("\u00AD")
        result shouldBe ""
    }
})

class ParserTest : StringSpec({
    "C" {
        Model.items["in"] = 17
        Model.filter()

        for(banWord in Model.bansWord) {
            Model.items shouldNotContainKey banWord
        }
    }
})