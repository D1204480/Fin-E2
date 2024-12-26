package org.example.exam2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class webCrawlerTest {
  private WebDriver driver;
  String testedURL = "https://nlhsueh.github.io/iecs-gym/";

  @BeforeEach
  void setUp() {
    // 創建 Chrome 瀏覽器選項物件
    ChromeOptions options = new ChromeOptions();

    // 設置允許遠端跨來源的請求
    options.addArguments("--remote-allow-origins=*");
    // 啟用無頭模式 (headless mode)，不顯示瀏覽器 UI（適合自動化測試或無需互動的場景）
    options.addArguments("--headless");
    // 使用帶有設定選項的 ChromeDriver 實例來初始化 driver
    driver = new ChromeDriver(options);
    // 設置隱式等待時間，等待元素加載最多 4000 毫秒
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(4000));
    // 設置瀏覽器視窗的大小為 1200 x 800 像素; headless 則不需要設置
    driver.manage().window().setSize(new Dimension(1200, 800));
  }

  @AfterEach
  void tearDown() throws InterruptedException {
    if (driver != null) {
      Thread.sleep(4000); // 延遲 4 秒
      driver.quit();
    }
  }

  @Test
  @DisplayName("沒有優惠的測試案例")
  void testTicketCalculator() throws InterruptedException {
    driver.get(testedURL);

    // 獲取表單元件
    WebElement weekDayInput = driver.findElement(By.id("day"));
    WebElement timeInput = driver.findElement(By.id("time"));
    WebElement ageInput = driver.findElement(By.id("age"));
    WebElement memberYesRadio = driver.findElement(By.id("member-yes"));
    WebElement memberNoRadio = driver.findElement(By.id("member-no"));
    WebElement memberIdSection = driver.findElement(By.id("member-id-section"));
    WebElement memberIdError = driver.findElement(By.id("member-id-error"));
    WebElement submitButton = driver.findElement(By.id("calculate"));

    // 填寫表單
    Select weekDaySelect = new Select(weekDayInput);
    weekDaySelect.selectByValue("Friday");

    Select timeSelect = new Select(timeInput);
    timeSelect.selectByValue("after7");

    ageInput.clear();
    ageInput.sendKeys("67");

    // 提交
    Thread.sleep(2000);
    submitButton.click();

    // 驗證結果
    WebElement output = driver.findElement(By.id("output"));
    assertEquals("費用為 $160.00.", output.getText());
  }

  @Test
  @DisplayName("會員優惠的測試案例")
  void testTicketMemberCalculator() throws InterruptedException {
    driver.get(testedURL);

    // 獲取表單元件
    WebElement weekDayInput = driver.findElement(By.id("day"));
    WebElement timeInput = driver.findElement(By.id("time"));
    WebElement ageInput = driver.findElement(By.id("age"));
    WebElement memberYesRadio = driver.findElement(By.id("member-yes"));
    WebElement memberNoRadio = driver.findElement(By.id("member-no"));
    WebElement memberIdInput = driver.findElement(By.id("member-id"));
    WebElement memberIdSection = driver.findElement(By.id("member-id-section"));
    WebElement memberIdError = driver.findElement(By.id("member-id-error"));
    WebElement submitButton = driver.findElement(By.id("calculate"));

    // 填寫表單
    Select weekDaySelect = new Select(weekDayInput);
    weekDaySelect.selectByValue("Friday");

    Select timeSelect = new Select(timeInput);
    timeSelect.selectByValue("after7");

    ageInput.clear();
    ageInput.sendKeys("67");

    // 選擇會員並輸入會員編號
    memberYesRadio.click();
    memberIdInput.clear();
    memberIdInput.sendKeys("IECS-1");   //必須是 IECS- 開頭

    // 提交
    Thread.sleep(2000);
    submitButton.click();

    // 驗證結果
    WebElement output = driver.findElement(By.id("output"));
    assertEquals("費用為 $100.00.", output.getText());  // 200*0.5
  }

  @Test
  @DisplayName("清除表格欄位")
  void testClearForm() throws InterruptedException {
    driver.get(testedURL);

    WebElement weekDayInput = driver.findElement(By.id("day"));
    WebElement timeInput = driver.findElement(By.id("time"));
    WebElement ageInput = driver.findElement(By.id("age"));
    WebElement memberYesRadio = driver.findElement(By.id("member-yes"));
    WebElement memberNoRadio = driver.findElement(By.id("member-no"));
    WebElement memberIdSection = driver.findElement(By.id("member-id-section"));
    WebElement memberIdError = driver.findElement(By.id("member-id-error"));
    WebElement submitButton = driver.findElement(By.id("calculate"));
    WebElement clearButton = driver.findElement(By.id("reset"));


    // 填寫表單
    Select weekDaySelect = new Select(weekDayInput);
    weekDaySelect.selectByValue("Friday");

    Select timeSelect = new Select(timeInput);
    timeSelect.selectByValue("after7");

    ageInput.clear();
    ageInput.sendKeys("67");

    // 按下清除
    Thread.sleep(2000);
    clearButton.click();

    // 驗證欄位清空
    assertEquals("Monday", weekDaySelect.getFirstSelectedOption().getAttribute("value"));
    assertEquals("before7", timeSelect.getFirstSelectedOption().getAttribute("value"));
    assertEquals("", ageInput.getAttribute("value"));
    assertTrue(memberNoRadio.isSelected());
    assertFalse(memberYesRadio.isSelected());

    // 驗證結果隱藏
    WebElement result = driver.findElement(By.id("output"));
    assertTrue(result.getCssValue("display").equals("none"));
  }
}
