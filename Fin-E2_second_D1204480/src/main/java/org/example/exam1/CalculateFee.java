package org.example.exam1;

public class CalculateFee {
  GitHubService gitHubService;

  public CalculateFee(GitHubService gitHubService) {
    this.gitHubService = gitHubService;
  }

  public int calculateFee(int grade, int absenceCount, int typingMinutes, int wpm, String gitHubRepo) throws IllegalArgumentException {
    int fee = 500;  // 社費
    int discount = 0;   // 折扣
    int WMC;
    int LOC;
    int[] maxDiscount = new int[4];   // 記錄各種折扣金額: absenceCount, wpm, typingMins, gitHubRepo

    // 1.大二下以後才可入團
    if (grade < 2) {
      throw new IllegalArgumentException();
    }

    // 2.曠課次數, 計算折扣: (少於５次, 折50)
    if (absenceCount < 5) {
      discount = 50;
    }
    maxDiscount[0] = discount;

    // 3.打字速度 (worPerMin, wpm): wpm 大於 60 折扣 50 元; wpm 大於 80 折扣 100; wpm 大於 100 折扣 150
    if (wpm > 100) {
      discount = 150;
    } else if (wpm > 80) {
      discount = 100;
    } else if (wpm > 60) {
      discount = 50;
    }
    maxDiscount[1] = discount;

    // 4.深蹲打字持續分鐘數 (typingMins):2, 3, 4 年級能夠持續 5, 10, 15 分鐘則折扣 100 元; 能夠持續 10, 15, 20 分鐘以上則折扣 200 元
    if (grade == 2) {
      if (typingMinutes >= 10) {
        discount = 200;
      } else if (typingMinutes >= 5) {
        discount = 100;
      }
    } else if (grade == 3) {
      if (typingMinutes >= 20) {
        discount = 200;
      } else if (typingMinutes >= 15) {
        discount = 100;
      }
    } else if (grade == 4) {
      if (typingMinutes >= 20) {
        discount = 200;
      } else if (typingMinutes >= 15) {
        discount = 100;
      }
    }
    maxDiscount[2] = discount;

    // 5.gitHubRepo 計算 WMC, LOC
    LOC = gitHubService.getLines(gitHubRepo);
    WMC = gitHubService.getWMC(gitHubRepo);

    if (grade == 2) {
      int discountLOC = 50 * (LOC / 1000);
      discount = discountLOC;

      if (discountLOC >= 200) {
        discount = 200;
      }
    } else if (grade > 2 && WMC > 50) {
      int discountLOC = 50 * (LOC / 1000);
      discount = discountLOC;

      if (discountLOC >= 200) {
        discount = 200;
      }
    }
    maxDiscount[3] = discount;

    // 取出陣列裡的最大值
    int max = 0;
    for (int num: maxDiscount) {
      if (num > max) {
        max = num;
      }
    }

    return fee - max;
  }
}
