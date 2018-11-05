package com.jumpingstone.codequality.fireeye.cals;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class CommentCheck extends BaseMatcher<String> {
    boolean inComment = false;
    public boolean isComment(String line) {
        line = line.trim();
        if (!inComment) {
            if (line.startsWith("/*")) {
                inComment = true;
                return true;
            } else if (line.startsWith("//")) {
                return true;
            } else {
                return false;
            }
        } else if (line.endsWith("*/")) {
            inComment = false;
            return true;
        } else {
            return true;
        }
    }

    @Override
    public boolean matches(Object item) {
        return isComment((String) item);
    }

    @Override
    public void describeTo(Description description) {

    }
}
