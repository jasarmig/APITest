package org.utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Listener class
 */
public class Listener implements ITestListener {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Interface to log the test result changing the color for the text of the passed test.
     * Receives the result of a test.
     *
     * @param result
     */
    @Override
    public void onTestSuccess(ITestResult result){
        Reporter.info("Test: " + result.getName() + ANSI_GREEN + " [PASSED]" + ANSI_RESET);
    }

    /**
     * Interface to log the test result changing the color for the text of the failed test.
     * Receives the result of a test.
     *
     * @param result
     */
    @Override
    public void onTestFailure(ITestResult result){
        Reporter.info("Test: " + result.getName() + ANSI_RED + " [FAILED]" + ANSI_RESET);
    }

}
