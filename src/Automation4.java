import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Automation4 {

  static public WebDriver driver;

  public static void main(String[] args) throws InterruptedException {
    System.setProperty("webdriver.chrome.driver", "/Users/ebu/Desktop/SEL/chromedriver");

    String website = "https://www.orbitz.com";
    String expectedDestination = "Orlando";
    int resultListSize = 50;
    int pricePoint = 270;
    double minRating = 4.5;
    String[] urlTexts = {"Stays", "Flights", "Packages", "Cars", "Cruises", "Things to do", "Deals", "Groups & meetings", "Travel Blog"};

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-blink-features=AutomationControlled");

    driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    driver.manage().window().maximize();
    driver.get(website);

    String currentWindowHandle = driver.getWindowHandle();

    Actions sliderAction = new Actions(driver);

    jsClickElement("//button[@data-stid='location-field-destination-menu-trigger']");
    Thread.sleep(250);
    sendKeysElementById("location-field-destination", expectedDestination);
    Thread.sleep(250);
    clickElementByXPath("//ul[@data-stid='location-field-destination-results']//li//button//strong[.='Orlando']");
    Thread.sleep(250);

    jsClickElement("//button[@id='d1-btn']");
    Thread.sleep(250);
    clickElementByXPath("//td[@class='uitk-date-picker-day-number']//button[@data-day='20']");
    Thread.sleep(250);
    clickElementByXPath("//td[@class='uitk-date-picker-day-number']//button[@data-day='24']");
    Thread.sleep(250);
    clickElementByXPath("//button[@data-stid='apply-date-picker']");
    Thread.sleep(250);

    jsClickElement("//button[@data-testid='travelers-field-trigger']");
    Thread.sleep(250);

    String adultValue = getElementByXPath("//input[@id='adult-input-0']").getAttribute("value");
    WebElement adultValueDecrease = getElementByXPath("//input[@id='adult-input-0']//preceding-sibling::button");
    WebElement adultValueIncrease = getElementByXPath("//input[@id='adult-input-0']//following-sibling::button");

    while(!adultValue.equals("1")){// check if the adult's value is other than 1, make it 1

      if(Integer.valueOf(adultValue) > 1){
        adultValueDecrease.click();
      }
      else if(Integer.valueOf(adultValue) < 1){
        adultValueIncrease.click();
      }
      else{
        break;
      }
      adultValue = getElementByXPath("//input[@id='adult-input-0']").getAttribute("value");
    }

    String childValue = getElementByXPath("//input[@id='child-input-0']").getAttribute("value");
    WebElement childValueDecrease = getElementByXPath("//input[@id='child-input-0']//preceding-sibling::button");
    WebElement childValueIncrease = getElementByXPath("//input[@id='child-input-0']//following-sibling::button");

    while(!childValue.equals("2")){// check if the child's value is other than 2, make it 2

      if(Integer.valueOf(childValue) > 2){
        childValueDecrease.click();
      }
      else if(Integer.valueOf(childValue) < 2){
        childValueIncrease.click();
      }
      else{
        break;
      }
      childValue = getElementByXPath("//input[@id='child-input-0']").getAttribute("value");
    }

    Select firstChildSelect = new Select(getElementByXPath("//select[@id='child-age-input-0-0']"));
    firstChildSelect.selectByValue("4");

    Select secondChildSelect = new Select(getElementByXPath("//select[@id='child-age-input-0-1']"));
    secondChildSelect.selectByValue("8");

    clickElementByXPath("//button[@data-testid='guests-done-button']");

    clickElementByXPath("//button[@data-testid='submit-button']");
    Thread.sleep(1500);

    WebElement breakfastIncludedCheckbox = getElementByXPath("//input[contains(@id, 'FREE_BREAKFAST')]");
    breakfastIncludedCheckbox.click();
    Thread.sleep(2500);

    WebElement breakfastIncludedChip = getElementByXPath("//button[@id='playback-filter-pill-mealPlan-FREE_BREAKFAST']");
    Assert.assertTrue(breakfastIncludedChip.isDisplayed());

    breakfastIncludedCheckbox.click();
    Thread.sleep(2500);

    List<WebElement> freeBreakfastCheck = getElementsByXPath("playback-filter-pill-mealPlan-FREE_BREAKFAST");

    Assert.assertEquals(freeBreakfastCheck.size(), 0);

    WebElement priceSlider = getElementByXPath("//input[@id='price-slider-secondary']");
    sliderAction(priceSlider, "aria-valuemax", sliderAction, pricePoint);
    Thread.sleep(1500);

    Assert.assertTrue(getElementByXPath("//button[contains(@aria-label, 'Less than $270')]").isDisplayed());

    Thread.sleep(1500);

    jsScrollBy(0, 500);

    Thread.sleep(1500);

    List<WebElement> resultsList = getElementsByXPath("//li[contains(@class, 'uitk-spacing-margin-blockstart-three')][@tabindex=\"-1\"]");

    Thread.sleep(500);

    Assert.assertEquals(resultsList.size(), resultListSize);

    for(WebElement results : resultsList){
      int price = Integer.valueOf(results.findElement(By.xpath(".//div[starts-with(text(), 'The price is')]")).getText().split("\\$")[1]);
      Assert.assertTrue(price <= pricePoint);
    }

    clickElementByXPath("//span[contains(text(), 'Wonderful 4.5+')]");
    Thread.sleep(7500);

    jsScrollBy(0, 500);

    Thread.sleep(7500);

    resultsList = getElementsByXPath("//li[contains(@class, 'uitk-spacing-margin-blockstart-three')][@tabindex=\"-1\"]");

    for(WebElement results : resultsList){
      double rating = Double.valueOf(results.findElement(By.xpath(".//span[contains(text(), 'out of 5')]//preceding-sibling::span")).getText().split("/")[0]);
      Assert.assertTrue(rating >= minRating);
    }

    WebElement lastHotel = resultsList.get(resultsList.size() - 1);
    String lastHotelName = lastHotel.findElement(By.xpath(".//child::*[1]")).getText();
    double lastHotelRating = Double.valueOf(lastHotel.findElement(By.xpath(".//span[contains(text(), 'out of 5')]//preceding-sibling::span")).getText().split("/")[0]);

    lastHotel.click();

    changeWindow(driver.getWindowHandles(), currentWindowHandle, false);

    Thread.sleep(250);

    Assert.assertEquals(lastHotelName, driver.getTitle());

    String currentWindowHotelName = getElementByXPath("//div[@data-stid='content-hotel-title']//h1").getText();
    double currentWindowHotelRating = Double.valueOf(getElementByXPath("//div[@data-stid='content-hotel-reviewsummary']//h3").getText().split("/")[0]);

    Assert.assertEquals(currentWindowHotelName, lastHotelName);
    Assert.assertEquals(currentWindowHotelRating, lastHotelRating);

    driver.close();

    changeWindow(driver.getWindowHandles(), currentWindowHandle, true);

    clickElementByXPath("//a[@href='/']");

    Thread.sleep(7500);

    WebElement chatBoxIframe = getElementByXPath("//iframe[starts-with(@src, 'https://vac.vap.expedia.com/')]");

    chatBoxIframe.click();

    Thread.sleep(7500);

    driver.switchTo().frame(chatBoxIframe);

    String expectedMessage = "Hi, I'm your Virtual Agent";
    String shownMessage = getElementByXPath("(//div[@data-test-id='chat-text-message'])[1]").getText().split(" \uD83D\uDC4B")[0];

    Assert.assertEquals(expectedMessage, shownMessage);
    clickElementById("vac-close-button");

    driver.switchTo().defaultContent();

    jsClickElement("//div[@id='gc-custom-header-tool-bar-shop-menu']//button");

    Thread.sleep(750);

    List<WebElement> urlItems = getElementsByXPath("(//div[@class='uitk-list'])[1]//a");

    for(int i = 0; i < urlItems.size(); i++){
      Assert.assertEquals(urlItems.get(i).getText(), urlTexts[i]);
    }

    driver.quit();
  }

  public static void clickElementById(String id){
    driver.findElement(By.id(id)).click();
  }

  public static void clickElementByXPath(String xPath){
    driver.findElement(By.xpath(xPath)).click();
  }

  public static void clearElementById(String element){
    driver.findElement(By.id(element)).clear();
  }

  public static void sendKeysElementById(String element, String keys){
    driver.findElement(By.id(element)).sendKeys(keys);
  }

  public static WebElement getElementByXPath(String element){
    return driver.findElement(By.xpath(element));
  }

  public static List<WebElement> getElementsByXPath(String element){
    return driver.findElements(By.xpath(element));
  }

  public static void jsClickElement(String element){
    JavascriptExecutor js = (JavascriptExecutor) driver;
    WebElement downloadableDropdown = getElementByXPath(element);
    js.executeScript("arguments[0].click()", downloadableDropdown);
  }

  public static void jsScrollBy(int xOffset, int yOffset){
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("window.scrollBy("+xOffset+", "+yOffset+")");
  }

  public static void sliderAction(WebElement slider, String valueMax, Actions sliderAction, int targetValue){
    String sliderMaxValue = slider.getAttribute(valueMax);
    int origin = (Integer.valueOf(sliderMaxValue) / 2);
    int totalSteps = (Integer.valueOf(sliderMaxValue) - origin) / (Integer.valueOf(sliderMaxValue) / 10);
    int differenceFromOrigin = targetValue - origin;
    int targetSteps = (int)((differenceFromOrigin / (double)origin) * totalSteps);
    int percentage = (int)((targetSteps / (double)totalSteps) * 100);

    sliderAction.dragAndDropBy(slider, percentage, 0).build().perform();
  }

  public static void changeWindow(Set<String> currentWindowHandles, String changeToWindow, boolean changeToGivenWindow){
    for(String windowHandle : currentWindowHandles){
      if(changeToGivenWindow && windowHandle.equals(changeToWindow)){
        driver.switchTo().window(windowHandle);
        break;
      }
      else if(!changeToGivenWindow && !windowHandle.equals(changeToWindow)){
        driver.switchTo().window(windowHandle);
        break;
      }
    }
  }
}
