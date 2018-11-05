package com.jumpingstone.codequality.fireeye.cals;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class PackageCheck extends BaseMatcher<String> {
    public boolean isPackage(String line) {
        line = line.trim();
        return line.startsWith("package ");
    }

    @Override
    public boolean matches(Object item) {
        return isPackage((String) item);
    }

    @Override
    public void describeTo(Description description) {

    }
}
