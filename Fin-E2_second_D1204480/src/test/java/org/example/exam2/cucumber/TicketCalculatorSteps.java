package org.example.exam2.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketCalculatorSteps {
  private WebDriver driver;
  private final String BASE_URL = "https://nlhsueh.github.io/iecs-gym/";

  @Before
  public void setup() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
    options.addArguments("--headless");
    driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(4000));
  }

  @After
  public void teardown() throws InterruptedException {
    if (driver != null) {
      Thread.sleep(4000);
      driver.quit();
    }
  }

  @Given("我進入健身房票價計算網頁")
  public void i_visit_ticket_calculator_page() {
    driver.get(BASE_URL);
  }

  @When("我選擇 {string} 作為星期")
  public void i_select_day(String day) {
    Select daySelect = new Select(driver.findElement(By.id("day")));
    daySelect.selectByValue(day);
  }

  @When("我選擇 {string} 作為時段")
  public void i_select_time(String time) {
    Select timeSelect = new Select(driver.findElement(By.id("time")));
    timeSelect.selectByValue(time);
  }

  @When("我輸入年齡 {string}")
  public void i_enter_age(String age) {
    WebElement ageInput = driver.findElement(By.id("age"));
    ageInput.clear();
    ageInput.sendKeys(age);
  }

  @When("我勾選會員選項")
  public void i_check_member() {
    driver.findElement(By.id("member-yes")).click();
  }

  @When("我輸入會員編號 {string}")
  public void i_enter_member_id(String memberId) {
    WebElement memberIdInput = driver.findElement(By.id("member-id"));
    memberIdInput.clear();
    memberIdInput.sendKeys(memberId);
  }

  @When("我點選計算按鈕")
  public void i_click_calculate() throws InterruptedException {
    Thread.sleep(2000);
    driver.findElement(By.id("calculate")).click();
  }

  @When("我點選重置按鈕")
  public void i_click_reset() throws InterruptedException {
    Thread.sleep(2000);
    driver.findElement(By.id("reset")).click();
  }

  @Then("我應該看到票價為 {string}")
  public void i_should_see_price(String expectedPrice) {
    WebElement output = driver.findElement(By.id("output"));
    assertEquals("費用為 " + expectedPrice + ".", output.getText());
  }

  @Then("星期應該重置為 {string}")
  public void day_should_reset_to(String expectedDay) {
    Select daySelect = new Select(driver.findElement(By.id("day")));
    assertEquals(expectedDay, daySelect.getFirstSelectedOption().getAttribute("value"));
  }

  @Then("時段應該重置為 {string}")
  public void time_should_reset_to(String expectedTime) {
    Select timeSelect = new Select(driver.findElement(By.id("time")));
    assertEquals(expectedTime, timeSelect.getFirstSelectedOption().getAttribute("value"));
  }

  @Then("年齡欄位應該為空")
  public void age_should_be_empty() {
    WebElement ageInput = driver.findElement(By.id("age"));
    assertEquals("", ageInput.getAttribute("value"));
  }

  @Then("會員選項應該為未勾選")
  public void member_should_be_unchecked() {
    WebElement memberNoRadio = driver.findElement(By.id("member-no"));
    assertTrue(memberNoRadio.isSelected());
  }

  @Then("結果應該隱藏")
  public void result_should_be_hidden() {
    WebElement output = driver.findElement(By.id("output"));
    assertTrue(output.getCssValue("display").equals("none"));
  }
}