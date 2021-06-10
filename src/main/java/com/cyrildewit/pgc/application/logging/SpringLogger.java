package com.cyrildewit.pgc.appplication.logging;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyrildewit.pgc.support.logging.LoggerInterface;

@Service
public class SpringLogger implements LoggerInterface {

    Logger logger;

    public SpringLogger() {
        logger = LoggerFactory.getLogger(SpringLogger.class);
    }

    public void trace(String message) {
        logger.trace(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message) {
        logger.error(message);
    }
}