import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.shouldNotContain
import models.createKeywordString
import models.sanitizeHeadLine


class WebPageHeadlineTest : StringSpec({
    "sanitize all white spaces" {
        val wrongHeadline = "Epic Software Developer Job"
        val result = sanitizeHeadLine(wrongHeadline)
        result shouldNotContain " "
    }
    "sanitize all semicolons to ensure csv safety" {
        val wrongHeadline = "This job is epic; use it"
        val result = sanitizeHeadLine(wrongHeadline)
        result shouldNotContain ";"
    }
    "check if keyword string creation works as expected" {
        val keywords = listOf("First", "Second", "Third")
        val result = createKeywordString(keywords)
        result shouldBe "First,Second,Third,"
    }
})