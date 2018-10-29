package com.jumpingstone.codequality.fireeye.cals.lcs;

import com.jumpingstone.codequality.fireeye.SimilarityCalculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by chenwei on 2018/10/28.
 */
public class LCSCalculator implements SimilarityCalculator {

    @Override
    public float calculate(Path file, Path file2Compare) throws IOException {
        List<TextLineLCS.TextLine> contents1=toTextLines(file);
        List<TextLineLCS.TextLine> contents2=toTextLines(file2Compare);

        TextLineLCS lcs = new TextLineLCS(contents1.toArray(new TextLineLCS.TextLine[0]),
                contents2.toArray(new TextLineLCS.TextLine[0]));
        lcs.getResult();
        return 0;
    }

    private List<TextLineLCS.TextLine> toTextLines(Path file) throws IOException {
        AtomicInteger linenumber = new AtomicInteger(0);
        List<TextLineLCS.TextLine> textLines = Files.lines(file).map(l -> new TextLineLCS.TextLine(linenumber.getAndIncrement(), l)).
                collect(Collectors.toList());
        return textLines;
    }
}
