package org.example.branchCoverage;

public class A {

  public static String choose(String gender, int age, double height, double bmi ) {
    String result = "";
    if (gender.equals("男") && age < 30 && bmi < 24) { // LINE 03
      result = "籃球社";
    } else if (gender.equals("女") && age < 30) {      // LINE 05
      if (bmi > 24) {
        result = "健身社";
      } else if (height > 172) {                     // LINE 08
        result = "排球社";
      } else {
        if (age < 22 && bmi > 28) {                // LINE 11
          result = "減重社";
        } else {
          result = "桌球社";                      // LINE 14
        }
      }
    }

    return result;
  }
}
