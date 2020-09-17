import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver

import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration


fun main() {
    System.setProperty("webdriver.chrome.driver", "/Users/etti1-mac1/Downloads/chromedriver")
    val driver = SafariDriver()

    driver.manage().window().size = Dimension(800, 800)
    driver.navigate().to("https://stepstone.de")

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


    }

    driver.close()
    driver.quit()
}