/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.properties;

import org.apache.log4j.Logger;

public class AppZoneConfig
{

    private static final String URL;
    private static final String APP_ID;
    private static final String PASS;
    private static final Logger logger;

    public static String getURL()
    {
        return URL;
    }

    public static String getApp_Id()
    {
        return APP_ID;
    }

    public static String getPass()
    {
        return PASS;
    }

    static 
    {
        logger = Logger.getLogger(AppZoneConfig.class);
        try
        {
            URL = PropertyFileReader.getValue("URL");
            APP_ID = PropertyFileReader.getValue("APP_ID");
            PASS = PropertyFileReader.getValue("PASS");
        }
        catch(Exception e)
        {
            logger.error(e);
            throw new ExceptionInInitializerError(e);
        }
    }
}
