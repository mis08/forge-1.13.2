package com.underground.undergroundmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Debug {

    public static final Logger Debag= LogManager.getLogger();
    
    public static void text(String str) {
    	Debag.info(str);
    }
}
