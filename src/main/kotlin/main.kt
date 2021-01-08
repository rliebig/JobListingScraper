import cityvisualisation.showHeatmap
import driverManager.driver
import models.WebPage
import models.saveWebPage
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import  org.jsoup.select.Elements
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
//import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import java.io.File
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import java.net.SocketException


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


fun acquireCurrentJobs(city : String, keywords : String) : List<String>{
    if(System.getProperty("os.name").contains("ubuntu"))
        System.setProperty("webdriver.gecko.driver", "./")
    val returnList = ArrayList<String>()
    val options = FirefoxOptions()
//    options.addArguments("-headless")

    val driver = FirefoxDriver(options)

    val listUrls = ArrayList<String>()


    driver.manage().window().size = Dimension(800, 800)
    driver.navigate().to("https://stepstone.de")

    // DSGVO overwrite
    try {
        val foo = WebDriverWait(driver, 10)
            .until {
                driver.findElement(
                    By.ById(
                        "ccmgt_explicit_accept"
                    )
                )
            }
        driver.findElementById("ccmgt_explicit_accept").click()

    } catch (e : Exception) {

    }

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
        else
            throw IllegalStateException("lol")
        try
        {
            WebDriverWait(driver, 10)
                .until {
                    driver.findElement(
                        By.ByClassName(
                            "col-xs-12"
                        )
                    )
                }
        } catch (e: Exception) {
            File("error" + dateString() + ".txt").writeText(driver.pageSource)
            printException(e, "For some reason, this did not work.")
        }
    }

    try
    {
        driver.close()
    }
    catch (e : Exception)
    {
        printException(e, "Expected error did happen")
    }

    return returnList
}

fun setCampaign(city : String, keywords: String)
{
    val dateString = dateString()
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

fun useless()
{
    println("This is good")
}

fun readCampaign(campStr : String) {
    Configuration.SentenceDirectory = campStr + "/" + Configuration.SentenceDirectory
    Configuration.WebPageDirectory = campStr + "/" + Configuration.WebPageDirectory
    Configuration.databaseFile = campStr + "/" + Configuration.databaseFile
    Configuration.modelFile = campStr + "/" + Configuration.modelFile
    println("reading data under $campStr")
}

fun getPageTitle(linkUrl : String) : String {
    if(System.getProperty("os.name").contains("ubuntu")) {
        System.setProperty("webdriver.gecko.driver", "./")
    }
    val options = FirefoxOptions()
    options.addArguments("-headless")
    val driver = FirefoxDriver(options)
    driver.manage().window().size = Dimension(800, 800)

    driver.navigate().to(linkUrl)
    val returnString = driver.title

    driver.close()
    return returnString
}

object driverManager {
    var driver = FirefoxDriver()
    init{
        val options = FirefoxOptions()
        options.addArguments("-headless")

        driver = FirefoxDriver(options)
    }
}

fun getPageCity(url : String, driver : FirefoxDriver) : String {
    if(System.getProperty("os.name").contains("ubuntu")) {
        System.setProperty("webdriver.gecko.driver", "./")
    }

    driver.navigate().to(url)
    var returnString = ""

    /*for(webElement in driver.findElements(By.tagName("a"))) {
        if(webElement.getAttribute("href") == "#location") {
            println("Location + " + webElement.text)
            returnString = webElement.text
            break
        }
    }*/
    driver.findElements(By.className("at-listing__list-icons_location")).forEach {
        returnString = it.text
        return@forEach
    }


    return returnString
}

// Write a choosing dialog for selecting old campaigns or creating now ones
fun main(args : Array<String>) {
    val city = "bundesweit"
    val keywords = "Softwareentwickler"
    setCampaign(city = city, keywords = keywords)
    val lonelist = ArrayList<String>()

    val options = FirefoxOptions()
    options.addArguments("-headless")

    val driver = FirefoxDriver(options)

    try {
        for (acquireCurrentJob in acquireCurrentJobs(city, keywords)) {
            lonelist.add(acquireCurrentJob)
        }
    } catch(e : Exception) {
        printException(e)
    }

    val myList = ArrayList<cityvisualisation.toggle>()
    lonelist.forEach { link ->
        try {
            val cssClass = "at-section-text-profile-content"
            val pageContent = getPageContentByClass(link, cssClass)
            val title = getPageTitle(link)
            val city = getPageCity(link, driver)
                .replace("Großraum ", "")
                .split(",")[0]
                .split(" ")[0]
            println(city)
            myList.add(cityvisualisation.toggle(city = city, link = link, title = title))

            println("Scanning $link")
            parseList(pageContent)
            val listWords = getListWords(pageContent)
            listWords.forEach { word ->
                addWordToModel(filterWord(word))
            }
            val webPage = WebPage(link, title, listWords)
            saveWebPage(webPage)
        } catch (e: SocketException) {
            println("Network Exception! Is the internet turned on?")
            println(e.stackTraceToString())
        } catch (e: HttpStatusException) {
            printException(e, "Fetching $link did not work")
        } catch (e: IOException) {
            printException(e, "Fetching $link did result in a network error")
        }
    }

    cityvisualisation.toggleHeatmap(myList)
    print(myList)
    //Model.readModel()
    Model.saveModel()
    Model.filter()

    val wordCloud = WorldCloud()

    wordCloud.main()

}

