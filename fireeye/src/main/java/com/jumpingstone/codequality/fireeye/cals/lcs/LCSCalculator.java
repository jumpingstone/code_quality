package com.jumpingstone.codequality.fireeye.cals.lcs;

import com.jumpingstone.codequality.fireeye.SimilarityCalculator;
import com.jumpingstone.codequality.fireeye.cals.CommentCheck;
import com.jumpingstone.codequality.fireeye.cals.PackageCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chenwei on 2018/10/28.
 */
public class LCSCalculator implements SimilarityCalculator {
    final static Logger logger = LoggerFactory.getLogger(LCSCalculator.class);

    final CommentCheck cchecker = new CommentCheck();
    final PackageCheck pchecker = new PackageCheck();

    @Override
    public float calculate(Path file, Path file2Compare) throws IOException {
        logger.debug(String.format("calculate similarity of two files :\n %s \n %s", file, file2Compare));

        String[] lines1 = trim(Files.readAllLines(file)).toArray(new String[0]);
        String[] lines2 = trim(Files.readAllLines(file2Compare)).toArray(new String[0]);

        LineComparator left = new LineComparator(lines1);
        LineComparator right = new LineComparator(lines2);

        RangeDifference[] differences = RangeDifferencer.findDifferences(left, right);

        int leftTotal = left.getTotalCount();
        int rightTotal = right.getTotalCount();

        int leftDiffLen = 0;
        int rightDiffLen = 0;

        for(RangeDifference d : differences) {
            leftDiffLen += left.getRangeTextLength(d.leftStart(), d.leftEnd());
            rightDiffLen += right.getRangeTextLength(d.rightStart(), d.rightEnd());
        }

        float leftCommonRange = (leftTotal - leftDiffLen) * 1.0f/leftTotal;
        float rightCommonRange = (rightTotal - rightDiffLen) * 1.0f/rightTotal;

        if (lines1.length <= 5 && lines2.length >= 50) {
            return 0;
        }  else if (lines2.length <= 5 && lines1.length >= 50) {
            return 0;
        }  else {
            return Math.max(leftCommonRange, rightCommonRange);
        }
    }

    private List<String> trim(List<String> lines) {
        return lines.stream().filter(l -> !cchecker.isComment(l))
                .filter(l -> !pchecker.isPackage(l)).collect(Collectors.toList());
    }

}
