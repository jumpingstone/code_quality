package com.jumpingstone.codequality.fireeye.cals.pcd;

import com.jumpingstone.codequality.fireeye.SimilarityCalculator;
import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.Mark;
import net.sourceforge.pmd.cpd.Match;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Created by chenwei on 2018/10/28.
 */
public class PCDSimilarityCalculator implements SimilarityCalculator {
    @Override
    public float calculate(Path file, Path file2compare) throws IOException {
        CPDConfiguration configuration = new CPDConfiguration();
        configuration.setMinimumTileSize(100);
        CPD cpd = new CPD(configuration);

        cpd.add(file.toFile());
        cpd.add(file2compare.toFile());

        cpd.go();

        int commonLength = 0;
        for (Iterator<Match> it = cpd.getMatches(); it.hasNext(); ) {
            Match match = it.next();
            String source = match.getSourceCodeSlice();
            commonLength += source.length();
        }

        int leftLength = Files.lines(file).map(l -> l.length()).reduce((a,b)-> a+ b).get();
        int rightLength = Files.lines(file2compare).map(l -> l.length()).reduce((a,b)-> a+ b).get();

        return commonLength * 1.0f / Math.min(leftLength, rightLength);
    }
}
