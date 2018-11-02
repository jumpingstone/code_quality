package com.jumpingstone.codequality.fireeye.cals.lcs;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.assertTrue;

public class LCSCalculatorTest {

    @Test
    public void givenFilesWithSameContent_compare_similarityEquals100() throws IOException {
        LCSCalculator calculator = new LCSCalculator();
        Path file1 = Paths.get("./src/test/resources/files/a.java.1");
        Path file2 = Paths.get("./src/test/resources/files/a.java.2");

        float value = calculator.calculate(file1, file2);
        assertTrue(value > 0.99);
    }

    @Test
    public void givenFilesWithCompleteDiffContent_compare_similarityLessThan10() throws IOException {
        LCSCalculator calculator = new LCSCalculator();
        Path file1 = Paths.get("./src/test/resources/files/a.java.1");
        Path file2 = Paths.get("./src/test/resources/files/b.java.1");

        float value = calculator.calculate(file1, file2);
        assertTrue(value < 0.1);
    }


}