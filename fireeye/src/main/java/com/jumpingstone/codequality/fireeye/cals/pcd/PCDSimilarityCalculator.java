package com.jumpingstone.codequality.fireeye.cals.pcd;

import com.jumpingstone.codequality.fireeye.SimilarityCalculator;
import com.jumpingstone.codequality.fireeye.cals.CommentCheck;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.cpd.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chenwei on 2018/10/28.
 */
public class PCDSimilarityCalculator implements SimilarityCalculator {
    @Override
    public float calculate(Path file, Path file2compare) throws IOException {
        CPDConfiguration configuration = new CPDConfiguration() {

            public SourceCode sourceCodeFor(File file) {
                return new SourceCode(new FileSourceCodeLoader(file, getSourceEncoding().name()));
            }

        };
        configuration.setMinimumTileSize(100);
        configuration.setLanguage(createLanguage(true, false, true, true));
        CPD cpd = new CPD(configuration);
        cpd.add(file.toFile());
        cpd.add(file2compare.toFile());

        cpd.go();

        List<LineRange> leftRanges = new ArrayList<>();
//        List<LineRange> rightRanges = new ArrayList();

        int commonLength = 0;
        for (Iterator<Match> it = cpd.getMatches(); it.hasNext(); ) {
            Match match = it.next();
            int matchAllFiles = 0;
            LineRange leftRange = null;
//            LineRange rightRange = null;
            for(Mark mark : match.getMarkSet()) {
                if (mark.getFilename().equals(file.toAbsolutePath().toString())) {
                    leftRange = new LineRange(mark.getBeginLine(), mark.getEndLine());
                    matchAllFiles |= 1;
                } else  if (mark.getFilename().equals(file2compare.toAbsolutePath().toString())) {
//                    rightRange = new LineRange(mark.getBeginLine(), mark.getEndLine());
                    matchAllFiles |= 2;

                }
            }

            if (matchAllFiles == 3) {
//                String source = match.getSourceCodeSlice();
//                commonLength += source.length();
                merge(leftRanges, leftRange);
//                merge(rightRanges, leftRange);
            }
        }

        FileSourceCodeLoader loader = new FileSourceCodeLoader(file.toFile(), configuration.getSourceEncoding().name());
        for(LineRange r : leftRanges) {
            List<String> lines = loader.getCodeSlice(r.startLine, r.endLine);
            for (String line : lines) {
                commonLength += line.length() + 1;
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

    private void merge(List<LineRange> ranges, LineRange newRange) {
        Iterator<LineRange> iterator = ranges.iterator();
        LineRange mergeRange = null;
        while(iterator.hasNext()) {
            LineRange r = iterator.next();
            if (r.intersect(newRange)) {
                r.merge(newRange);
                mergeRange = r;
                iterator.remove();;
                break;
            }
        }
        if (mergeRange == null) {
            ranges.add(newRange);
        } else {
            merge(ranges, mergeRange);
        }
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


    public static class FileSourceCodeLoader extends SourceCode.FileCodeLoader {

        public FileSourceCodeLoader(File file, String encoding) {
            super(file, encoding);
        }


        public List<String> getCodeSlice(int startLine, int endLine) {
            List<String> lines = getCode();
            return lines.subList(startLine - 1, endLine);
        }

        protected List<String> load() {
            final CommentCheck checker = new CommentCheck();
            return super.load().stream()
                    .filter(l -> !checker.isComment(l))
                    .filter(l -> !l.trim().startsWith("import ") && !l.trim().startsWith("package"))
                    .collect(Collectors.toList());
        }

    }

    public static class LineRange {
        int startLine;
        int endLine;

        public LineRange(int beginLine, int endLine) {
            this.startLine = beginLine;
            this.endLine = endLine;
        }

        public boolean intersect(LineRange newRange) {
            return (startLine <= newRange.startLine && newRange.startLine <= endLine)
                    || (startLine <= newRange.endLine  && newRange.endLine  <= endLine)
                    || (newRange.startLine <=  startLine && startLine <= newRange.endLine)
                    || (newRange.startLine <= endLine  && endLine  <= newRange.endLine);
        }

        public void merge(LineRange newRange) {
            if (intersect(newRange)) {
                if (newRange.startLine < startLine) {
                    startLine = newRange.startLine;
                }
                if (newRange.endLine > endLine) {
                    endLine = newRange.endLine;
                }
            }
        }
    }
}
