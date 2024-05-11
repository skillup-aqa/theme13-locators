package ua.skillup;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import ua.skillup.utils.LocatorsReader;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class InventoryPageTest {
    public WebDriver driver;

    public Map<String, Map<String, String>> locatorsToTest;

    public final Map<String, By> expectedLocators = Map.of(
            "byTagName", By.tagName("a"),
            "elementWithDescendant", By.cssSelector("a > img"),
            "byTagNameWithOr", By.cssSelector("a, button"),
            "byTagNameFollowedBy", By.cssSelector("div+button"),
            "byId" , By.id("shopping_cart_container"),
            "byClassName", By.cssSelector(".inventory_item_name"),
            "firstElementWithDataTest", By.cssSelector("*[data-test^=\"add-to-cart\"]"),
            "everySecondElementWithClassName", By.cssSelector(".inventory_item:nth-child(2n) .inventory_item_name"),
            "byText", By.xpath("//*[text()=\"Sauce Labs Bolt T-Shirt\"]/ancestor::div[@class=\"inventory_item\"]//button")
    );

    @BeforeClass
    public void readLocators() throws FileNotFoundException {
        locatorsToTest = LocatorsReader.getSelectors();
    }

    @BeforeClass
    public void setUpDriver() {
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.get("https://www.saucedemo.com");

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                webDriver -> webDriver.findElement(By.id("shopping_cart_container"))
        );
    }

    @DataProvider(name="xpathLocators")
    public Object[][] xpathLocators() {
        return locatorsOfType("xpath");
    }

    @DataProvider(name="cssLocators")
    public Object[][] cssLocators() {
        return locatorsOfType("css");
    }

    @Test(dataProvider = "xpathLocators")
    public void testXPathLocators(String locatorToTest, By expectedLocator) {
        List<WebElement> foundByLocatorToTest = driver.findElements(By.xpath(locatorToTest));
        List<WebElement> foundByExpectedLocator = driver.findElements(expectedLocator);
        assertEquals(foundByLocatorToTest, foundByExpectedLocator);
    }

    @Test(dataProvider = "cssLocators")
    public void testCssLocators(String locatorToTest, By expectedLocator) {
        List<WebElement> foundByLocatorToTest = driver.findElements(By.cssSelector(locatorToTest));
        List<WebElement> foundByExpectedLocator = driver.findElements(expectedLocator);
        assertEquals(foundByLocatorToTest, foundByExpectedLocator);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    public Object[][] locatorsOfType(String type) {
        return locatorsToTest.get(type).entrySet().stream()
                .map(entry -> new Object[] {entry.getValue(), expectedLocators.get(entry.getKey())})
                .toArray(Object[][]::new);
    }
}
