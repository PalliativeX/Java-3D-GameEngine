package com.base.engine.core;

public class Time
{
    private static final long SECOND = 1000000000L;

    // return time in seconds
    public static double getTime()
    {
        return (double)System.nanoTime()/(double)SECOND;
    }

}
