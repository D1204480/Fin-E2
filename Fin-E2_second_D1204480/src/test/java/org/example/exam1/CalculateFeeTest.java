package org.example.exam1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalculateFeeTest {
  private CalculateFee calculator;
  private GitHubService gitHubService;

  @BeforeEach
  public void setUp() {

    // 建立 mock 物件
    gitHubService = mock(GitHubService.class);

    // 建立 calculator 物件
    calculator = new CalculateFee(gitHubService);

    // mockito (stub) 佈樁
    when(gitHubService.getLines("tom1")).thenReturn(1000);
    when(gitHubService.getLines("tom2")).thenReturn(2000);
    when(gitHubService.getLines("tom3")).thenReturn(3000);
    when(gitHubService.getLines("tom4")).thenReturn(4000);
    when(gitHubService.getLines("tom5")).thenReturn(5000);

    // 使用 "no-discount" 這個 repo 名稱，確保在 setUp 中設置它不會產生額外折扣
    when(gitHubService.getLines("no-discount")).thenReturn(0);
    when(gitHubService.getWMC("no-discount")).thenReturn(0);

    // WMC 的 stub
    when(gitHubService.getWMC("john")).thenReturn(50);
  }


  // 測試基本案例
  @Test
  @DisplayName("測試沒有折扣案例")
  public void testBasicFee() {
    assertEquals(500, calculator.calculateFee(2,5,1,1,""));
  }

  // 測試一年級
  @Test
  @DisplayName("一年級會拋出Exception")
  public void testFirstGrade() {
    assertThrows(IllegalArgumentException.class, () -> calculator.calculateFee(1,5,1,1,""));

    // 異常有特定的錯誤訊息，也可以驗證它
    // assertEquals("預期的錯誤訊息", exception.getMessage());
  }

  // 計算曠課折扣
  @Test
  @DisplayName("曠課折扣")
  public void testAbsenceDiscount() {
    // 使用 "no-discount" 這個 repo 名稱，確保在 setUp 中設置它不會產生額外折扣
    when(gitHubService.getLines("no-discount")).thenReturn(0);
    when(gitHubService.getWMC("no-discount")).thenReturn(0);

    assertEquals(450, calculator.calculateFee(2, 4, 1, 1, "no-discount"));  // 曠課<5次折50
    assertEquals(500, calculator.calculateFee(3, 5, 1, 1, "no-discount"));  // 曠課>=5次不折扣
  }

  // 測試wpm
  @Test@DisplayName("測試wpm")
  public void testWpm() {
    assertEquals(350, calculator.calculateFee(2, 4, 1, 101, "no-discount"));  // wpm>80折150
    assertEquals(400, calculator.calculateFee(3, 4, 1, 81, "no-discount"));  // wpm>80折100
    assertEquals(450, calculator.calculateFee(4, 4, 1, 61, "no-discount"));  // wpm>80折50
  }



  // 深蹲打字(二年級)
  @Test
  @DisplayName("typingMins,深蹲打字,2年級")
  public void testTypingDiscountGrade2() {
    assertEquals(400, calculator.calculateFee(2, 5, 5, 1, "no-discount")); // =5分鐘折100
    assertEquals(400, calculator.calculateFee(2, 5, 6, 1, "no-discount")); // >5分鐘折100
    assertEquals(300, calculator.calculateFee(2, 5, 10, 1, "no-discount")); // =10分鐘折200
    assertEquals(300, calculator.calculateFee(2, 5, 11, 1, "no-discount")); // >10分鐘折200
  }

  // 深蹲打字(三年級)
  @Test
  @DisplayName("typingMins,深蹲打字,3年級")
  public void testTypingDiscountGrade3() {
    assertEquals(400, calculator.calculateFee(3, 5, 15, 1, "no-discount")); // >=15分鐘折100
    assertEquals(300, calculator.calculateFee(3, 5, 20, 1, "no-discount")); // >=20分鐘折200
  }

  // 深蹲打字(四年級)
  @Test
  @DisplayName("typingMins,深蹲打字,4年級")
  public void testTypingDiscountGrade4() {
    assertEquals(400, calculator.calculateFee(4, 5, 15, 1, "no-discount")); // >=15分鐘折100
    assertEquals(300, calculator.calculateFee(4, 5, 20, 1, "no-discount")); // >=20分鐘折200
  }

  // 測試mockito
  @Test
  @DisplayName("測試mockito")
  public void testMockito() {
    assertEquals(450,calculator.calculateFee(2,5,1,1,"tom1")); // 每1000行,折扣50
    assertEquals(400,calculator.calculateFee(2,5,1,1,"tom2")); // 2000行,折扣100
    assertEquals(300,calculator.calculateFee(2,5,1,1,"tom5")); // 超過4000行,折扣200
    assertEquals(500,calculator.calculateFee(2,5,1,1,"no-discount")); // 未超過1000行,無折扣

  }

  @Test
  @DisplayName("測試三年級以上需要WMC>50才計算LOC折扣")
  public void testGrade3WithWMC() {
    when(gitHubService.getWMC("test")).thenReturn(51);
    when(gitHubService.getLines("test")).thenReturn(4000);

    assertEquals(300, calculator.calculateFee(3, 5, 1, 1, "test")); // WMC>50，4000行，折扣200
    assertEquals(300, calculator.calculateFee(3, 5, 1, 1, "test")); // WMC>50，4000行，折扣200

  }


  @Test
  @DisplayName("測試三年級以上WMC<=50不計算LOC折扣")
  public void testGrade3WithoutWMC() {
    when(gitHubService.getWMC("test")).thenReturn(50);
    when(gitHubService.getLines("test")).thenReturn(4000);

    assertEquals(500, calculator.calculateFee(3, 5, 1, 1, "test")); // WMC<=50，不折扣
  }

  // 測試多重折扣情況（應該只採用最大折扣）
  @Test
  @DisplayName("測試最大折扣")
  public void testMultipleDiscounts() {
    // 曠課<5次(50折扣)且打字速度>100(150折扣)，應該採用較大的折扣200
    assertEquals(300, calculator.calculateFee(2, 4, 10, 101, ""));
  }



}