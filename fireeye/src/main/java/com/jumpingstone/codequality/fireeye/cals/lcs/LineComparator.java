package com.jumpingstone.codequality.fireeye.cals.lcs;

/*******************************************************************************
 * Copyright (c) 2004, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * This implementation of IRangeComparator breaks an input stream into lines.
 */
class LineComparator implements IRangeComparator {

    private String[] fLines;

    public LineComparator(final String[] lines) throws IOException {
        fLines = lines;
    }

    String getLine(int ix) {
        return fLines[ix];
    }

    @Override
    public int getRangeCount() {
        return fLines.length;
    }

    public int getRangeTextLength(int startLine, int endLine) {
        int count = 0;
        endLine = endLine >= fLines.length? fLines.length -1 : endLine;
        for(int i = startLine; i <= endLine; i++) {
            count += fLines[i].length();
        }
        return count;
    }

    @Override
    public boolean rangesEqual(int thisIndex, IRangeComparator other,
                               int otherIndex) {
        String s1 = fLines[thisIndex];
        String s2 = ((LineComparator) other).fLines[otherIndex];
        return s1.equals(s2);
    }

    @Override
    public boolean skipRangeComparison(int length, int maxLength, IRangeComparator other) {
        return false;
    }

    public int getTotalLineCount() {
        return fLines.length;
    }

    public int getTotalCount() {
        Optional<Integer> count = Arrays.stream(fLines).map(s -> s.length()).reduce( (a, b)-> a+b);
        return count.isPresent()?count.get() : 0;
    }
}