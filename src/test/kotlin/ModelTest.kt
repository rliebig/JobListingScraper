import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.data.forAll
import io.kotest.engine.config.ConfigManager.init
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveValues
import io.kotest.matchers.maps.shouldNotContainKey
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Gen
import io.kotest.property.arbitrary.arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.forAll
import models.Sentence
import models.WebPage
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.lang.IllegalArgumentException


class ModelSpec : StringSpec() {
    override fun testCaseOrder(): TestCaseOrder?  = TestCaseOrder.Sequential

    init {
        "beforeAll set database configuration" {
            Configuration.databaseFile = "ModelSpecTest.db"
            Configuration.SentenceDirectory = "sentenceTest/"
            clearDirectory()
            Model.items = HashMap<String, Int>()
            startExposed()
        }

        "Insert value into Model and save" {
            Model.items["test"] = 2
            Model.saveModel("")
        }

        "Read value from saved Model" {
            Model.readModel("")
            Model.items.keys shouldContain "test"
            Model.items.keys.size shouldBe 1
            Model.items.shouldHaveValues(2)
        }

        "Property testing with random words" {
        }
    }
}

class WebPageSpec : StringSpec() {
    init {
        "Before all change webPage directory" {
            //Configuration.WebPageDirectory = "webPageTest"
            //clearDirectory(Configuration.WebPageDirectory)
        }

        "Instantiate sample test class" {
            //val webPage = WebPage("http://test.com")
        }
    }
}

class SentenceSpec : StringSpec() {

    override fun testCaseOrder(): TestCaseOrder? = TestCaseOrder.Sequential

    init {
        "beforeAll change configuration for test purposes" {
            Configuration.databaseFile = "sentenceTest.db"
            Configuration.SentenceDirectory = "sentenceTest"
            clearDirectory()
            Model.items = HashMap<String, Int>()
            startExposed()
        }

        "Add test sentence to keyword test" {
            val set  = Sentence("test")
            set.addSentence("This is a test sentence.")
        }

        "Retrieve test sentence using constructor" {
            val set = Sentence("test")
            set.getSentences() shouldHaveSingleElement  "This is a test sentence."
        }

        "Add another sentence to same keyword" {
            val set = Sentence("test")
            set.addSentence("Another test is not in vain.")
        }

        "Retrieve all sentences" {
            val set = Sentence("test")
            set.getSentences() shouldContain "Another test is not in vain."
            set.getSentences() shouldContain "This is a test sentence."
        }

        "Insert random string and check if it is correctly added" {
            //this is currenlty meaningless
            val sillyArb = Arb.string()
            val secondArb = Arb.string()

            sillyArb.checkAll { a ->
                val set = Sentence(a)
                val b = secondArb.next()
                set.addSentence(b)
                set.getSentences() shouldContain b

                val setTwo = Sentence(a)
                setTwo.getSentences() shouldContain b
            }
        }

        "Insert very long random sentences" {
            val sillyArb = Arb.string()
            val secondArb = Arb.string()

            sillyArb.checkAll { a ->
                val set = Sentence(a)
                var b = ""
                repeat(100) {
                    b += secondArb.next()
                }
                set.addSentence(b)
                set.getSentences() shouldContain b

                val setTwo = Sentence(a)
                setTwo.getSentences() shouldContain b
            }
        }

        "Very hardcore file I/O test that will take a really long time" {
            val sillyArb = Arb.string()
            val secondArb = Arb.string()

            sillyArb.checkAll(100) { a ->
                secondArb.checkAll(100) { b ->
                    val set = Sentence(a)
                    set.addSentence(b)
                    set.getSentences() shouldContain b

                    val setTwo = Sentence(a)
                    setTwo.getSentences() shouldContain b
                }
            }
        }

        "Replaced character should not be replaced with a soft hypen unicode character" {
            val sillyArb = Arb.string()

            sillyArb.checkAll {
            }
        }
    }

}

//TODO("write more tests for establishing debugging")
class FilterWordsSpec : StringSpec() {

    override fun testCaseOrder(): TestCaseOrder? = TestCaseOrder.Sequential

    init {
        "beforeAll reset model" {
            Model.items = HashMap<String, Int>()
        }


        "No filtered world should appear" {
            Model.items["in"] = 17
            Model.filter()

            for (banWord in Model.bansWord) {
                Model.items shouldNotContainKey banWord
            }
        }

        "Allowed Words should not disapper" {
            Model.items["c++"] = 9
            Model.filter()

            Model.items.keys.size shouldBe 1
            Model.items.keys shouldContain "c++"
        }

        "insert very long word into Model" {
            val longStr : String = "thisContainsManyLargeWordsAndShouldNotBreakTheCurrentImplementation"

            Model.items[longStr] = 12

            Model.items[longStr] shouldBe 12
        }
    }


}