package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class TopJavaTestWatcher extends TestWatcher {
    private LocalTime startlocalTime;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    protected void starting(Description description) {
        this.startlocalTime = LocalTime.now();
    }

    @Override
    protected void finished(Description description) {
        LocalTime endlocalTime = LocalTime.now();
        this.log.info(String.format("Method %s total processing time: %s(millis)", description.getMethodName(),
                ChronoUnit.MILLIS.between(this.startlocalTime, endlocalTime)));
    }

}
