package org.example.branchCoverage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATest {

  @Test
  void choose() {
    String result = A.choose("男", 29, 190, 23);
    assertEquals("籃球社", result);

    String result2 = A.choose("女", 29, 172, 25);
    assertEquals("健身社", result2);
  }
}