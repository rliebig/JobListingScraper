import net.bytebuddy.asm.Advice
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import  org.jsoup.select.Elements
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
//import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File
import java.io.IOException
import java.io.ObjectInputFilter
import java.lang.IndexOutOfBoundsException
import java.net.SocketException
import java.time.LocalDate
import java.time.LocalDateTime


fun getListWords(content : Elements) : List<String> {
    val listWords = mutableListOf<String>()
    try {
        content[0].getElementsByTag("li").forEach { it ->
            it  .html()
                .split(" ")
                .forEach{
                    listWords.add(it)
                }
        }
    } catch (e : IndexOutOfBoundsException) {
        println(e.toString())
    }

    return listWords.toList()
}



fun addWordToModel(word : String) {
    try {
        if(Model.items.containsKey(word)) {
            val newValue = Model.items.getValue(word) + 1
            Model.items[word] = newValue
        } else {
            Model.items[word] = 1
        }
    } catch (e : Exception) {
        println(e.toString())
    }
}

fun getPageContentByTag(url : String, tag : String) : Elements {
        val document  = Jsoup.connect(url).get()
        return document.getElementsByTag(tag)
}


/*
    This function returns an Elements object from JSoup.

    Parameters:
    @param url: The url of the request resource
    @param cssClass: Named this way, because class is a keyword in kotlin and will not compile.
 */
fun getPageContentByClass(url : String, cssClass : String ) : Elements {
    val document = Jsoup.connect(url).get()
    return document.getElementsByClass(cssClass)
}

fun getPagePublishedTime(url : String) : String {

    return ""
}

fun scrapList(url : String) {
    val content = getPageContentByTag(url, "a")

    content.filter{
        container ->
        container.attr("data-at") == "job-item-title"
    }.forEach {
        link ->
        println(link.attr("href"))
        try {
            val linkUrl = "http://www.stepstone.de/" + link.attr("href")
            val cssClass = "at-section-text-profile-content"
            val pageContent = getPageContentByClass(linkUrl, cssClass)

            println(getPagePublishedTime(url))
            parseList(pageContent)
            getListWords(pageContent).forEach {
                    word -> addWordToModel(filterWord(word))
            }
        } catch(e : SocketException) {
            println("Network Exception! Is the internet turned on?")
            println(e.stackTraceToString())
            } catch(e : HttpStatusException) {
            printException(e, "Fetching $link did not work")
        }

    }
}


//TODO("REFACTOR THIS PRETTY SOON")
fun acquireCurrentJobs(city : String, keywords : String) : List<String>{
    val returnList = mutableListOf<String>()
    val options = ChromeOptions()
    options.addArguments("--headless")
    val driver = ChromeDriver(options)

    val listUrls = mutableListOf<String>()


    driver.manage().window().size = Dimension(800, 800)
    driver.navigate().to("https://stepstone.de")

    driver.findElementByName("ke").sendKeys(keywords)
    driver.findElementByName("ws").sendKeys(city)
    driver.findElementByClassName("btn-primary").click()


    val foo = WebDriverWait(driver, 10)
        .until {
            driver.findElement(
                By.ByClassName(
                    "col-xs-12"
                )
            )
        }
    var nextLink = ""
    repeat(Configuration.listRestrictionNumber) {
        for (webElement in driver.findElementsByTagName("a")) {
            if (webElement.getAttribute("data-at") == "job-item-title") {
                println(webElement.text)
                val link = webElement.getAttribute("href")
                println(link)
                returnList.add(link.toString())
            }

            val result = webElement.getAttribute("data-at")
            if (result != null && result.contains("pagination-next")) {
                if (webElement.getAttribute("href") != null) {
                    nextLink = webElement.getAttribute("href")
                    println("Next Page Link found: $nextLink")
                } else {
                    nextLink = ""
                }
            }
        }

        if(nextLink != "")
            driver.navigate().to(nextLink)
        try {
            WebDriverWait(driver, 10)
                .until {
                    driver.findElement(
                        By.ByClassName(
                            "col-xs-12"
                        )
                    )
                }
        } catch (e: Exception) {
            File("error" + LocalDateTime.now().toString() + ".txt").writeText(driver.pageSource)
            printException(e, "For some reason, this did not work.")
        }
    }

    try {
        driver.close()
        driver.quit()
    } catch (e : Exception) {
        printException(e, "Expected error did happen")
    }

    return returnList
}

fun setCampaign(city : String, keywords: String) {
    val dateString = LocalDateTime.now().toString()
    val cityAndKeywordsString = city + "-" + keywords.replace(" ", "-")
    val campStr = dateString + cityAndKeywordsString + "-" + Configuration.listRestrictionNumber.toString()

    Configuration.SentenceDirectory = campStr + "/" + Configuration.SentenceDirectory
    Configuration.WebPageDirectory = campStr + "/" + Configuration.WebPageDirectory
    Configuration.databaseFile = campStr + "/" + Configuration.databaseFile
    Configuration.modelFile = campStr + "/" + Configuration.modelFile

    println("saving data under $campStr")
    File("$campStr/").mkdirs()
    File(Configuration.SentenceDirectory).mkdirs()
    File(Configuration.WebPageDirectory).mkdirs()
}

fun readCampaign(saved : String) {

}

fun main(args : Array<String>) {
    val city = "bundesweit"
    val keywords = "elektro ingenieur"
    setCampaign(city, keywords)

    val loneList = acquireCurrentJobs("bundesweit", "maschinenbau ingenieur")

//    if (args[0] != "--no-refresh-data") {
//        clearDirectory()
        loneList.forEach {link->
         //   scrapList(it)

            try {
                val linkUrl = link
                val cssClass = "at-section-text-profile-content"
                val pageContent = getPageContentByClass(linkUrl, cssClass)
                println("Scanning $linkUrl")
                parseList(pageContent)
                getListWords(pageContent).forEach {
                        word -> addWordToModel(filterWord(word))
                }
            } catch(e : SocketException) {
                println("Network Exception! Is the internet turned on?")
                println(e.stackTraceToString())
            } catch(e : HttpStatusException) {
                printException(e, "Fetching $link did not work")
            } catch(e : IOException) {
                printException(e, "Fetching $link did result in a network error")
            }
        }
//    }

    Model.saveModel("")
    //Model.readModel("")
    //Model.readModel("")
    println(Model.items)

    Model.filter()

//        val wordCloud = WorldCloud()
//        wordCloud.main()

    //val view = View()
    //view.main()
}

