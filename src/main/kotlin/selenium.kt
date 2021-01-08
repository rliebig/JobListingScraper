import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver

import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration


fun main() {
    val driver = SafariDriver()

    driver.manage().window().size = Dimension(800, 800)
    driver.navigate().to("https://www.stepstone.de/stellenangebote--Software-Entwickler-Java-C-Javascript-Frameworks-Angular-2-node-js-vue-js-react-js-Muenchen-Nuernberg-Stuttgart-Frankfurt-Koeln-Hannover-Hamburg-Berlin-Leipzig-proQrent-GmbH--4723470-inline.html")


    var returnString = ""

    for(webElement in driver.findElements(By.tagName("a"))) {
        if(webElement.getAttribute("href") == "#location") {
            println("Location + " + webElement.text)
            returnString = webElement.text
            break
        }
    }
    driver.findElements(By.className("at-listing__list-icons_location")).forEach {
        returnString = it.text
        return@forEach
    }

    print("result: "+ returnString)

    driver.close()
}

fun roth() {
    System.setProperty("webdriver.chrome.driver", "/Users/etti1-mac1/Downloads/chromedriver")
    val driver = SafariDriver()

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

    driver.findElementByName("ke").sendKeys("Softwareentwickler")
    driver.findElementByName("ws").sendKeys("München")
    driver.findElementByClassName("btn-primary").click()


    val foo = WebDriverWait(driver, 10)
        .until {
            driver.findElement(
                By.ByClassName(
                    "col-lg-3"
                )
            )
        }

    for (webElement in driver.findElementsByTagName("a")) {
        if(webElement.getAttribute("data-at") == "job-item-title") {
            println(webElement.text)
            println(webElement.getAttribute("href"))
        }

        val result = webElement.getAttribute("data-at")
        if(result != null && result.contains("pagination-next"))  {
                println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                println(webElement.getAttribute("href"))
                println("find me!!!")
        }

        if(webElement.getAttribute("href") == "#location") {
            println("Location + " + webElement.text)
        }

    }

    driver.close()
    driver.quit()
}