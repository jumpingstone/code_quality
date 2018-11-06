package com.jumpingstone.codequality.fireeye.cals.pcd;


import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.assertTrue;

/**
 * Created by chenwei on 2018/11/1.
 */
public class PCDSimilarityCalculatorTest {

    @Test
    public void givenFilesWithSameContent_compare_similarityEquals100() throws IOException {
        PCDSimilarityCalculator calculator = new PCDSimilarityCalculator();
        Path file1 = Paths.get("./src/test/resources/files/a.java.1");
        Path file2 = Paths.get("./src/test/resources/files/a.java.2");

        float value = calculator.calculate(file1, file2);
        assertTrue(value > 0.95);
    }


    @Test
    public void givenFilesWithCompleteDiffContent_compare_similarityLessThan10() throws IOException {
        PCDSimilarityCalculator calculator = new PCDSimilarityCalculator();
        Path file1 = Paths.get("./src/test/resources/files/a.java.1");
        Path file2 = Paths.get("./src/test/resources/files/b.java.1");

        float value = calculator.calculate(file1, file2);
        assertTrue(value < 0.1);
    }

    @Test()
    public void givenFiles_compare_similarityLessThan100() throws IOException {
        PCDSimilarityCalculator calculator = new PCDSimilarityCalculator();
        Path file1 = Paths.get("./src/test/resources/files/CodeNode.java");
        Path file2 = Paths.get("./src/test/resources/files/RuntimeRuleCacheHandler.java");

        float value = calculator.calculate(file1, file2);
        assertTrue(value < 1.0);
    }


    @Test()
    public void givenVerySimilarFiles_compare_similarityLessThan100() throws IOException {
        PCDSimilarityCalculator calculator = new PCDSimilarityCalculator();
        Path file1 = Paths.get("./src/test/resources/files/FloatCH.java");
        Path file2 = Paths.get("./src/test/resources/files/DoubleCH.java");
        float value = calculator.calculate(file1, file2);
        assertTrue(value < 0.95);
    }


    @Test()
    public void givenFilesWithConstantLiteralOnly_compare_similarityLessThan100() throws IOException {
        PCDSimilarityCalculator calculator = new PCDSimilarityCalculator();
        Path file1 = Paths.get("./src/test/resources/files/SFConfig.java");
        Path file2 = Paths.get("./src/test/resources/files/SystemBean.java");

        float value = calculator.calculate(file1, file2);
        assertTrue(value < 0.5);
    }
}