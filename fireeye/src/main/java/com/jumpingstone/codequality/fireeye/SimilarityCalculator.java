package com.jumpingstone.codequality.fireeye;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by chenwei on 2018/10/28.
 */
public interface SimilarityCalculator {
    float calculate(Path file, Path file2Compare) throws IOException;
}
