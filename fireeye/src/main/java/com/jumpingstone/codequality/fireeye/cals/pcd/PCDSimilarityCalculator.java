package com.jumpingstone.codequality.fireeye.cals.pcd;

import com.jumpingstone.codequality.fireeye.SimilarityCalculator;
import com.jumpingstone.codequality.fireeye.cals.CommentCheck;
import net.sourceforge.pmd.cpd.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by chenwei on 2018/10/28.
 */
public class PCDSimilarityCalculator implements SimilarityCalculator {
    @Override
    public float calculate(Path file, Path file2compare) throws IOException {
        CPDConfiguration configuration = new CPDConfiguration();
        configuration.setMinimumTileSize(100);
        configuration.setLanguage(createLanguage(true, true, true, true));
        CPD cpd = new CPD(configuration);
        cpd.add(file.toFile());
        cpd.add(file2compare.toFile());

        cpd.go();

        int commonLength = 0;
        for (Iterator<Match> it = cpd.getMatches(); it.hasNext(); ) {
            Match match = it.next();
            int matchAllFiles = 0;
            for(Mark mark : match.getMarkSet()) {
                if (mark.getFilename().equals(file.toAbsolutePath().toString())) {
                    matchAllFiles |= 1;
                } else  if (mark.getFilename().equals(file2compare.toAbsolutePath().toString())) {
                    matchAllFiles |= 2;
                }
            }

            if (matchAllFiles == 3) {
                String source = match.getSourceCodeSlice();
                commonLength += source.length();
            }
        }

        final CommentCheck checker = new CommentCheck();


        int leftLength = Files.lines(file)
                .filter(l -> !checker.isComment(l))
                .filter(l -> !l.trim().startsWith("import ") && !l.trim().startsWith("package"))
                .map(l -> l.length() + 1).reduce((a,b)-> a+ b).get();
        int rightLength = Files.lines(file2compare)
                .filter(l -> !checker.isComment(l))
                .filter(l -> !l.trim().startsWith("import ") && !l.trim().startsWith("package"))
                .map(l -> l.length() + 1).reduce((a,b)-> a+ b).get();

        if (Math.min(leftLength, rightLength) / Math.max(leftLength, rightLength) > 10
                && Math.min(leftLength, rightLength) < 100) {
            return 0;
        }
        return commonLength * 1.0f / Math.min(leftLength, rightLength);
    }

    private Language createLanguage(boolean ignoreLiterals,
                                    boolean ignoreIdentifiers,
                                    boolean ignoreAnnotations,
                                    boolean ignoreUsings) {
        Properties p = new Properties();
        if (ignoreLiterals) {
            p.setProperty(Tokenizer.IGNORE_LITERALS, "true");
        }
        if (ignoreIdentifiers) {
            p.setProperty(Tokenizer.IGNORE_IDENTIFIERS, "true");
        }
        if (ignoreAnnotations) {
            p.setProperty(Tokenizer.IGNORE_ANNOTATIONS, "true");
        }
        if (ignoreUsings) {
            p.setProperty(Tokenizer.IGNORE_USINGS, "true");
        }
//        p.setProperty(Tokenizer.OPTION_SKIP_BLOCKS, skipBlocks);
//        p.setProperty(Tokenizer.OPTION_SKIP_BLOCKS_PATTERN, skipBlocksPattern);
        return LanguageFactory.createLanguage("Java", p);
    }


}
