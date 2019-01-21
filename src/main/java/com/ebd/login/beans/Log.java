package com.ebd.login.beans;

import java.util.logging.Logger;

public class Log {
    //private Logger logger = Logger.getLogger("PGE-WEB");
    //protected Logger logger = Logger.getLogger("PawNewsExample");
    private static Logger logger = Logger.getLogger("EBD");

    public enum Level {fine, info, warning, severe}

    public static void fine(String msg) {
        logger.fine(msg);
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void warning(String msg) {
        logger.warning(msg);
    }

    public void severe(String msg) {
        logger.severe(msg);
    }

    public void fine(String user, String msg) {
        logger.fine(user + "::" +msg);
    }

    public void info(String user, String msg) {
        logger.info(user + "::" +msg);
    }

    public void warning(String user, String msg) {
        logger.warning(user + "::" +msg);
    }

    public void severe(String user, String msg) {
        logger.severe(user + "::" +msg);
    }

}
