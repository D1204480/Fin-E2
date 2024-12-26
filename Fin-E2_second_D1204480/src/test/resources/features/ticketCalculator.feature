Feature: 健身房票價計算
  為了計算健身房票價
  作為一個使用者
  我想要根據不同條件計算票價

  Scenario: 測試一般票價計算
    Given 我進入健身房票價計算網頁
    When 我選擇 "Friday" 作為星期
    And 我選擇 "after7" 作為時段
    And 我輸入年齡 "67"
    And 我點選計算按鈕
    Then 我應該看到票價為 "$160.00"

  Scenario: 測試會員優惠票價
    Given 我進入健身房票價計算網頁
    When 我選擇 "Friday" 作為星期
    And 我選擇 "after7" 作為時段
    And 我輸入年齡 "67"
    And 我勾選會員選項
    And 我輸入會員編號 "IECS-1"
    And 我點選計算按鈕
    Then 我應該看到票價為 "$100.00"

  Scenario: 測試表單重置功能
    Given 我進入健身房票價計算網頁
    When 我選擇 "Friday" 作為星期
    And 我選擇 "after7" 作為時段
    And 我輸入年齡 "67"
    And 我點選重置按鈕
    Then 星期應該重置為 "Monday"
    And 時段應該重置為 "before7"
    And 年齡欄位應該為空
    And 會員選項應該為未勾選
    And 結果應該隱藏