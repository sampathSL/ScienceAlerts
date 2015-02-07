/*
 * Author : Sampath Thennakoon
 * Creation Date : 2012-04-30 8.30 PM
 */
package com.appzone.sciencealerts.properties;

import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * @author SAMPATH
 */
public class PropertyFileReader
{

    private static final Properties properties;
    private static final Logger logger;

    /**
     * Get property file content using the key
     * @param key
     * @return value
     */
    public static String getValue(String key)
    {
        String value = properties.getProperty(key);
        return value;
    }

    static 
    {
        logger = Logger.getLogger(PropertyFileReader.class);
        try
        {
            properties = new Properties();
            properties.load(PropertyFileReader.class.getResourceAsStream("/ScienceAlerts.properties")); // read the property file from resource path
        }
        catch(Exception ex)
        {
            logger.error(ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
