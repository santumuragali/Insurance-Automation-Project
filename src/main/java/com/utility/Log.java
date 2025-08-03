package com.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private Log() {}

    public static Logger getInstance(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}